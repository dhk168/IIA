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
            <el-button @click="sendVerificationCode" :disabled="sending" class="light-button">
              {{ countDownSeconds > 0 ? `${countDownSeconds}秒后重发` : '发送验证码' }}
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
import { authAPI } from '../../api/auth';
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
          // 使用表单验证来验证邮箱
          this.$refs.registerFormRef.validateField('email', (error) => {
            if (error) {
              return
            }
            
            this.sending = true
            
            // 使用authAPI发送验证码
            authAPI.sendVerificationCode({
              email: this.registerForm.email,
              type: 'register'
            })
            .then(() => {
              this.$message.success('验证码已发送至 ' + this.registerForm.email)
              // 添加倒计时功能
              this.countDown()
            })
            .catch(error => {
              console.error('发送验证码失败:', error)
              this.$message.error(error.response?.data?.message || '发送验证码失败')
              this.sending = false
            })
          })
        },
        
        // 验证码倒计时功能
        countDown() {
          this.countDownSeconds = 60
          this.sending = true
          
          const timer = setInterval(() => {
            this.countDownSeconds--
            if (this.countDownSeconds <= 0) {
              clearInterval(timer)
              this.sending = false
            }
          }, 1000)
        },
        
        handleRegister() {
          this.$refs.registerFormRef.validate((valid) => {
            if (valid) {
              // 准备注册数据
              const registerData = {
                username: this.registerForm.username,
                email: this.registerForm.email,
                password: this.registerForm.password,
                verificationCode: this.registerForm.verificationCode
              }
              
              // 使用authAPI进行注册
              authAPI.register(registerData)
              .then(response => {
                console.log('注册成功:', response)
                
                // 检查token是否在response.data中（根据axios拦截器配置）
                const token = response.token || (response.data && response.data.token)
                
                if (token) {
                  // 设置登录状态和token
                  localStorage.setItem('isLoggedIn', 'true')
                  localStorage.setItem('usernameOrEmail', this.registerForm.username)
                  localStorage.setItem('token', token)
                  
                  // 显示成功消息并跳转到首页
                  this.$message.success('注册成功并自动登录')
                  this.$router.push('/home')
                } else {
                  // 即使没有token，注册成功后也跳转到登录页
                  this.$message.success('注册成功，请登录')
                  this.$router.push('/login')
                }
              })
              .catch(error => {
                console.error('注册失败:', error)
                this.$message.error(error.response?.data?.message || '注册失败')
              })
            }
          })
        }
      }
}
</script>

<style>
@import '../../assets/styles/auth.css';
</style>