<template>
  <div class="auth-container">
    <h1 class="auth-title">Intelligent Information Assistant</h1>
    <div class="auth-form">
      <div class="form-group">
          <label class="form-label">Email</label>
          <el-input v-model="forgotForm.email" placeholder="" class="form-input"></el-input>
        </div>
        <div class="form-group">
          <label class="form-label">Verification Code</label>
          <div class="verification-code-container">
            <el-input v-model="forgotForm.verificationCode" placeholder="" class="verification-code-input"></el-input>
            <el-button type="primary" @click="sendVerificationCode" :disabled="sending" class="send-code-button">
              {{ sending ? 'Sending...' : 'Send Code' }}
            </el-button>
          </div>
        </div>
      <div class="form-group">
        <label class="form-label">New Password</label>
        <el-input v-model="forgotForm.newPassword" type="password" placeholder="" show-password class="form-input"></el-input>
      </div>
      <div class="form-group">
        <label class="form-label">Confirm Password</label>
        <el-input v-model="forgotForm.confirmPassword" type="password" placeholder="" show-password class="form-input"></el-input>
      </div>
      <button class="submit-button" @click="handleResetPassword">Reset Password</button>
      <div class="bottom-text">
        <router-link to="/login">Back to Login</router-link>
      </div>
    </div>
  </div>
</template>

<script>
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
      
      // Simulate API call with timeout
      setTimeout(() => {
        this.sending = false
        this.$message.success('Verification code sent successfully')
      }, 1500)
    },
    
    handleResetPassword() {
      this.$refs.forgotFormRef.validate((valid) => {
        if (valid) {
          // Password reset logic can be added here
          this.$message.success('Password reset successful')
          console.log('Password reset info:', this.forgotForm.email, this.forgotForm.verificationCode, this.forgotForm.newPassword)
          // 重置成功后跳转到登录页
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