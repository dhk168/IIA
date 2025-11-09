<template>
  <div class="auth-container">
    <h1 class="auth-title">Intelligent Information Assistant</h1>
    <el-form :model="registerForm" :rules="rules" ref="registerFormRef" class="auth-form">
      <div class="form-group">
        <label class="form-label">Username</label>
        <el-form-item prop="username" style="margin: 0;">
          <el-input v-model="registerForm.username" placeholder="" class="form-input"></el-input>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">Email</label>
        <el-form-item prop="email" style="margin: 0;">
          <el-input v-model="registerForm.email" placeholder="" class="form-input"></el-input>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">Verification Code</label>
        <el-form-item prop="verificationCode" style="margin: 0;">
          <div class="verification-code-container">
            <el-input v-model="registerForm.verificationCode" placeholder="" class="verification-code-input"></el-input>
            <el-button @click="sendVerificationCode" :disabled="countDownSeconds > 0 || sending" class="light-button">
              {{ countDownSeconds > 0 ? `${countDownSeconds}s to resend` : 'Send Code' }}
            </el-button>
          </div>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">Password</label>
        <el-form-item prop="password" style="margin: 0;">
          <el-input v-model="registerForm.password" type="password" placeholder="" class="form-input"></el-input>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">Confirm Password</label>
        <el-form-item prop="confirmPassword" style="margin: 0;">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="" class="form-input"></el-input>
        </el-form-item>
      </div>
      <!-- 调试按钮 -->

      <el-button type="primary" @click="handleRegister" class="submit-button">Sign up</el-button>
      <div class="bottom-text">
        Already have an account? <router-link to="/login">Sign in</router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import { authAPI } from '../../api/auth';
import '../../assets/styles/glass-toast.css';
export default {
  name: 'Register',
  data() {
    return {
      registerForm: {
        username: '',
        email: '',
        verificationCode: '',
        password: '',
        confirmPassword: ''
      },
      sending: false,
      countDownSeconds: 0,
      countDownTimer: null,
      rules: {
        username: [
          { required: true, message: 'Please enter username', trigger: 'blur' },
          { min: 3, max: 20, message: 'Username must be between 3 and 20 characters', trigger: 'blur' }
        ],
        email: [
          { required: true, message: 'Please enter email', trigger: 'blur' },
          { type: 'email', message: 'Please enter a valid email address', trigger: 'blur' }
        ],
        verificationCode: [
          { required: true, message: 'Please enter verification code', trigger: 'blur' },
          { min: 6, max: 6, message: 'Verification code must be 6 digits', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
          { min: 6, message: 'Password must be at least 6 characters', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: 'Please confirm password', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.registerForm.password) {
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
          console.log('Send Code button clicked')
          // Prevent clicking during countdown
          if (this.countDownSeconds > 0) {
            console.log('Countdown active, ignoring click')
            return
          }
          
          // 邮箱格式验证
          if (!this.registerForm.email) {
            this.createGlassToast('error', 'Please enter email')
            return
          }
          
          // 使用正则表达式验证邮箱格式
          const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
          if (!emailRegex.test(this.registerForm.email)) {
            this.createGlassToast('error', 'Please enter a valid email address')
            return
          }
          
          console.log('Email validated successfully:', this.registerForm.email)
          this.sending = true
          
          // 使用authAPI发送验证码
          console.log('Calling API to send verification code...')
          authAPI.sendVerificationCode({
            email: this.registerForm.email
          })
          .then((response) => {
            // 检查响应数据结构
            console.log('Verification code response:', response)
            
            // 使用提取出来的方法显示毛玻璃提示
            this.createGlassToast('success', 'Verification code sent to your email')
            
            // 添加倒计时功能
            this.countDown()
          })
          .catch(error => {
            console.error('Failed to send verification code:', error)
            console.error('Error details:', JSON.stringify(error, null, 2))
            let errorMsg = error?.message || 'Failed to send verification code, please try again later'
            this.createGlassToast('error', errorMsg)
          })
          .finally(() => {
            console.log('API call completed')
            this.sending = false
          })
        },
        
        // Verification code countdown function
        countDown() {
          this.countDownSeconds = 60
          this.sending = true
          
          // 清除可能存在的旧计时器
          if (this.countDownTimer) {
            clearInterval(this.countDownTimer)
          }
          
          this.countDownTimer = setInterval(() => {
            this.countDownSeconds--
            if (this.countDownSeconds <= 0) {
              clearInterval(this.countDownTimer)
              this.sending = false
              this.countDownTimer = null
            }
          }, 1000)
        },
        
        // Clear timer when component is destroyed
        beforeDestroy() {
          if (this.countDownTimer) {
            clearInterval(this.countDownTimer)
          }
        },
        
        handleRegister() {
          this.$refs.registerFormRef.validate((valid) => {
            if (valid) {
              // 准备注册数据，注意后端字段名要求
              const registerData = {
                username: this.registerForm.username,
                email: this.registerForm.email,
                password: this.registerForm.password,
                code: this.registerForm.verificationCode // 后端使用code字段
              }
              
              // 使用authAPI进行注册
              authAPI.register(registerData)
              .then(response => {
                console.log('Registration successful:', response)
                
                // 根据后端响应结构获取数据
                const responseData = response.data || response;
                const result = responseData.result || responseData.data || responseData;
                
                // 检查token是否存在
                const token = result.token;
                
                if (token) {
                  // 设置登录状态和token
                  localStorage.setItem('isLoggedIn', 'true')
                  localStorage.setItem('usernameOrEmail', this.registerForm.username)
                  localStorage.setItem('token', token)
                  
                  // 使用毛玻璃效果显示成功提示
                  this.createGlassToast('success', 'Registration successful, logging in...');
                  
                  // 延迟跳转，让用户看到成功提示
                  setTimeout(() => {
                    this.$router.push('/home')
                  }, 1500);
                } else {
                  // 即使没有token，注册成功后也跳转到登录页
                  this.createGlassToast('success', 'Registration successful, please log in');
                  setTimeout(() => {
                    this.$router.push('/login')
                  }, 1500);
                }
              })
              .catch(error => {
                console.error('Registration failed:', error)
                // 错误处理，尝试从不同结构中获取错误信息
                let errorMsg = 'Registration failed, please try again later';
                if (error.response) {
                  errorMsg = error.response.data?.message || 
                             error.response.data?.error || 
                             error.response.data?.msg || 
                             errorMsg;
                } else if (error.message) {
                  errorMsg = error.message;
                }
                this.createGlassToast('error', errorMsg);
              })
            }
          })
        },
        
        // 提取毛玻璃提示为独立方法
        createGlassToast(type, message) {
          const toast = document.createElement('div');
          toast.className = `glass-toast glass-toast-${type}`;
          toast.textContent = message;
          document.body.appendChild(toast);
          
          // 自动移除
          setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transform = 'translateX(100%)';
          }, 4000);
          
          setTimeout(() => {
            if (document.body.contains(toast)) {
              document.body.removeChild(toast);
            }
          }, 4500);
          
          toast.addEventListener('click', () => {
            if (document.body.contains(toast)) {
              document.body.removeChild(toast);
            }
          });
        }
      }
}
</script>

<style>
@import '../../assets/styles/page/auth.css';


</style>