<template>
  <div class="auth-container">
    <h1 class="auth-title">Intelligent Information Assistant</h1>
    <el-form :model="forgotForm" :rules="rules" ref="forgotFormRef" class="auth-form">
      <div class="form-group">
        <label class="form-label">Email</label>
        <el-form-item prop="email" style="margin: 0;">
          <el-input v-model="forgotForm.email" placeholder="" class="form-input"></el-input>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">Verification Code</label>
        <el-form-item prop="verificationCode" style="margin: 0;">
          <div class="verification-code-container">
            <el-input v-model="forgotForm.verificationCode" placeholder="" class="verification-code-input"></el-input>
            <el-button @click="sendVerificationCode" :disabled="sending" class="light-button">
              {{ sending ? 'Sending...' : 'Send Code' }}
            </el-button>
          </div>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">New Password</label>
        <el-form-item prop="newPassword" style="margin: 0;">
          <el-input v-model="forgotForm.newPassword" type="password" placeholder="" show-password class="form-input"></el-input>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">Confirm Password</label>
        <el-form-item prop="confirmPassword" style="margin: 0;">
          <el-input v-model="forgotForm.confirmPassword" type="password" placeholder="" show-password class="form-input"></el-input>
        </el-form-item>
      </div>
      <el-button type="primary" @click="handleResetPassword" class="submit-button">Reset Password</el-button>
      <div class="bottom-text">
        <router-link to="/login">Back to Login</router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import { authAPI } from '../../api/auth';
import '../../assets/styles/glass-toast.css';
export default {
  name: 'ForgotPassword',
  data() {
    return {
      forgotForm: {
        email: '',
        verificationCode: '',
        newPassword: '',
        confirmPassword: ''
      },
      sending: false,
      rules: {
        email: [
          { required: true, message: 'Please enter email', trigger: 'blur' },
          { type: 'email', message: 'Please enter a valid email address', trigger: 'blur' }
        ],
        verificationCode: [
          { required: true, message: 'Please enter verification code', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: 'Please enter new password', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: 'Please confirm new password', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.forgotForm.newPassword) {
                callback(new Error('Passwords do not match'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    sendVerificationCode() {
      if (!this.forgotForm.email) {
        this.createGlassToast('warning', 'Please enter email first');
        return
      }
      
      // Validate email format
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!emailRegex.test(this.forgotForm.email)) {
        this.createGlassToast('warning', 'Please enter a valid email address');
        return
      }
      
      this.sending = true
      console.log('Sending verification code to:', this.forgotForm.email)
      
      // 使用authAPI发送验证码
      authAPI.sendVerificationCode({
        email: this.forgotForm.email,
        type: 'reset'
      })
      .then(() => {
        this.sending = false
        this.createGlassToast('success', 'Verification code sent successfully');
      })
      .catch(error => {
        console.error('Failed to send verification code:', error)
        this.createGlassToast('error', error.response?.data?.message || 'Failed to send verification code');
        this.sending = false
      })
    },
    
    handleResetPassword() {
      this.$refs.forgotFormRef.validate((valid) => {
        if (valid) {
          // 准备密码重置数据
          const resetData = {
            email: this.forgotForm.email,
            verificationCode: this.forgotForm.verificationCode,
            newPassword: this.forgotForm.newPassword
          };
          
          // 使用authAPI进行密码重置
          authAPI.resetPassword(resetData)
          .then(response => {
            this.createGlassToast('success', 'Password reset successful');
            console.log('Password reset info:', this.forgotForm.email);
            
            // 自动登录逻辑 - 从不同的响应结构中尝试获取token
            const token = response.data?.token || response.token;
            
            if (token) {
              // 存储token
              localStorage.setItem('token', token);
              // 设置登录状态
              localStorage.setItem('isLoggedIn', 'true');
              // 设置用户名/邮箱（使用邮箱）
              localStorage.setItem('usernameOrEmail', this.forgotForm.email);
              
              // 重置成功后延迟跳转到首页，确保用户能看到成功提示
              setTimeout(() => {
                this.$router.push('/');
              }, 1500);
            } else {
              // 如果没有返回token，延迟跳转到登录页
              setTimeout(() => {
                this.$router.push('/login');
              }, 1500);
            }
          })
          .catch(error => {
            console.error('Password reset failed:', error);
            // 尝试从不同结构中获取错误信息
            let errorMsg = 'Password reset failed';
            if (error.response) {
              errorMsg = error.response.data?.message || error.response.data || errorMsg;
            } else if (error.message) {
              errorMsg = error.message;
            }
            this.createGlassToast('error', errorMsg);
          })
        }
      })
    },
    
    // 毛玻璃效果提示函数
    createGlassToast(type, message) {
      // 创建提示元素
      const toast = document.createElement('div');
      toast.className = `glass-toast glass-toast-${type}`;
      toast.textContent = message;
      
      // 添加到body
      document.body.appendChild(toast);
      
      // 自动移除
      setTimeout(() => {
        // 添加淡出动画
        toast.style.opacity = '0';
        setTimeout(() => {
          if (document.body.contains(toast)) {
            document.body.removeChild(toast);
          }
        }, 300);
      }, 3000);
      
      // 点击移除
      toast.addEventListener('click', () => {
        toast.style.opacity = '0';
        setTimeout(() => {
          if (document.body.contains(toast)) {
            document.body.removeChild(toast);
          }
        }, 300);
      });
    }
  }
}
</script>

<style>
/* 导入共享的认证页面样式 */
@import '../../assets/styles/auth.css';
/* light-button.css已通过auth.css导入，此处不再重复导入 */
</style>