<template>
  <div class="auth-container">
    <h1 class="auth-title">Intelligent Information Assistant</h1>
    <div class="auth-form">
      <div class="form-group">
        <label class="form-label">Email</label>
        <el-input v-model="loginForm.email" placeholder="" class="form-input"></el-input>
      </div>
      <div class="form-group">
        <div class="password-container">
          <label class="form-label">Password</label>
          <router-link to="/forgot-password" class="forgot-password-link">Forgot password?</router-link>
        </div>
        <el-input v-model="loginForm.password" type="password" placeholder="" class="form-input"></el-input>
      </div>
      <button class="submit-button" @click="handleLogin">Sign in</button>
      <div class="bottom-text">
        Don't have an account? <router-link to="/register">Create an account</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        email: '',
        password: ''
      },
      rules: {
        email: [
          { required: true, message: 'Please enter email', trigger: 'blur' },
          { type: 'email', message: 'Please enter a valid email address', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Please enter password', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginFormRef.validate((valid) => {
        if (valid) {
          // Login logic can be added here
          console.log('Login info:', this.loginForm)
          
          // 设置登录状态
          localStorage.setItem('isLoggedIn', 'true')
          localStorage.setItem('email', this.loginForm.email)
          
          // Simulate login success and redirect
          this.$message.success('Login successful')
          this.$router.push('/home')
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