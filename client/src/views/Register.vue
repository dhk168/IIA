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
            <el-button type="primary" @click="sendVerificationCode" :disabled="sending" class="send-code-button">
              {{ sending ? 'Sending...' : 'Send Code' }}
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
      <el-button type="primary" @click="handleRegister" class="submit-button">Sign up</el-button>
      <div class="bottom-text">
        Already have an account? <router-link to="/login">Sign in</router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import { authAPI } from '../api/auth';
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
          if (!this.registerForm.email) {
            this.$message.error('Please enter your email first')
            return
          }
          
          // Validate email format
          const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
          if (!emailRegex.test(this.registerForm.email)) {
            this.$message.error('Please enter a valid email address')
            return
          }
          
          this.sending = true
          
          // 使用authAPI发送验证码
          authAPI.sendVerificationCode({
            email: this.registerForm.email,
            type: 'register'
          })
          .then(() => {
            this.$message.success('Verification code sent to ' + this.registerForm.email)
            this.sending = false
          })
          .catch(error => {
            console.error('Failed to send verification code:', error)
            this.$message.error(error.response?.data?.message || 'Failed to send verification code')
            this.sending = false
          })
        },
        
        handleRegister() {
          this.$refs.registerFormRef.validate((valid) => {
            if (valid) {
              // 使用authAPI进行注册
              authAPI.register({
                username: this.registerForm.username,
                email: this.registerForm.email,
                password: this.registerForm.password,
                verificationCode: this.registerForm.verificationCode
              })
              .then(response => {
                console.log('Registration successful:', response)
                
                // 设置登录状态和token
                localStorage.setItem('isLoggedIn', 'true')
                localStorage.setItem('usernameOrEmail', this.registerForm.username)
                localStorage.setItem('token', response.token)
                
                // 显示成功消息并跳转到首页
                this.$message.success('Registration successful and automatically logged in')
                this.$router.push('/home')
              })
              .catch(error => {
                console.error('Registration failed:', error)
                this.$message.error(error.response?.data?.message || 'Registration failed')
              })
            }
          })
        }
      }
}
</script>

<style>
/* 导入共享的认证页面样式 */
@import '../assets/styles/auth.css';

/* 确保auth-container正确应用样式 */
.auth-container {
  position: relative;
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>