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
        this.$message.warning('Please enter email first')
        return
      }
      
      // Validate email format
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!emailRegex.test(this.forgotForm.email)) {
        this.$message.warning('Please enter a valid email address')
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
        this.$message.success('Verification code sent successfully')
      })
      .catch(error => {
        console.error('Failed to send verification code:', error)
        this.$message.error(error.response?.data?.message || 'Failed to send verification code')
        this.sending = false
      })
    },
    
    handleResetPassword() {
      this.$refs.forgotFormRef.validate((valid) => {
        if (valid) {
          // 使用authAPI进行密码重置
          authAPI.resetPassword({
            email: this.forgotForm.email,
            verificationCode: this.forgotForm.verificationCode,
            newPassword: this.forgotForm.newPassword
          })
          .then(response => {
            this.$message.success('Password reset successful')
            console.log('Password reset info:', this.forgotForm.email)
            
            // 自动登录逻辑
            if (response.data && response.data.token) {
              // 存储token
              localStorage.setItem('token', response.data.token)
              // 设置登录状态
              localStorage.setItem('isLoggedIn', 'true')
              // 设置用户名/邮箱（使用邮箱）
              localStorage.setItem('usernameOrEmail', this.forgotForm.email)
              
              // 重置成功后自动跳转到首页
              this.$router.push('/')
            } else {
              // 如果没有返回token，仍然跳转到登录页
              this.$router.push('/login')
            }
          })
          .catch(error => {
            console.error('Password reset failed:', error)
            this.$message.error(error.response?.data?.message || 'Password reset failed')
          })
        }
      })
    }
  }
}
</script>

<style>
/* 导入共享的认证页面样式 */
@import '../../assets/styles/auth.css';
/* light-button.css已通过auth.css导入，此处不再重复导入 */
</style>