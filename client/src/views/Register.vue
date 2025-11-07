<template>
  <div class="auth-container">
    <h1 class="auth-title">Intelligent Information Assistant</h1>
    <div class="auth-form">
      <div class="form-group">
        <label class="form-label">Email</label>
        <el-input v-model="registerForm.email" placeholder="" class="form-input"></el-input>
      </div>
      <div class="form-group">
          <label class="form-label">Verification Code</label>
          <div class="verification-code-container">
            <el-input v-model="registerForm.verificationCode" placeholder="" class="verification-code-input"></el-input>
            <el-button type="primary" @click="sendVerificationCode" :disabled="sending" class="send-code-button">
              {{ sending ? 'Sending...' : 'Send Code' }}
            </el-button>
          </div>
        </div>
      <div class="form-group">
        <label class="form-label">Password</label>
        <el-input v-model="registerForm.password" type="password" placeholder="" class="form-input"></el-input>
      </div>
      <div class="form-group">
        <label class="form-label">Confirm Password</label>
        <el-input v-model="registerForm.confirmPassword" type="password" placeholder="" class="form-input"></el-input>
      </div>
      <button class="submit-button" @click="handleRegister">Sign up</button>
      <div class="bottom-text">
        Already have an account? <router-link to="/login">Sign in</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Register',
  data() {
    return {
      registerForm: {
        email: '',
        verificationCode: '',
        password: '',
        confirmPassword: ''
      },
      sending: false,
      rules: {
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
          
          // Simulate sending verification code
          setTimeout(() => {
            this.$message.success('Verification code sent to ' + this.registerForm.email)
            this.sending = false
          }, 2000)
        },
        
        handleRegister() {
          this.$refs.registerFormRef.validate((valid) => {
            if (valid) {
              // Registration logic can be added here
              this.$message.success('Registration successful')
              console.log('Registration info:', this.registerForm)
              // Redirect to login page after successful registration
              this.$router.push('/login')
            }
          })
        }
      }
}
</script>

<style>
/* 导入共享的认证页面样式 */
@import '../assets/styles/auth.css';
</style>