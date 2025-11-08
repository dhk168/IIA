<template>
  <div class="auth-container">
    <h1 class="auth-title">Intelligent Information Assistant</h1>
    <el-form :model="loginForm" :rules="rules" ref="loginFormRef" class="auth-form">
      <div class="form-group">
        <label class="form-label">Email</label>
        <el-form-item prop="email" style="margin: 0;">
          <el-input v-model="loginForm.email" placeholder="" class="form-input"></el-input>
        </el-form-item>
      </div>
      <div class="form-group">
        <div class="password-container">
          <label class="form-label">Password</label>
          <router-link to="/forgot-password" class="forgot-password-link">Forgot password?</router-link>
        </div>
        <el-form-item prop="password" style="margin: 0;">
          <el-input v-model="loginForm.password" type="password" placeholder="" class="form-input"></el-input>
        </el-form-item>
      </div>
      <el-button type="primary" @click="handleLogin" class="submit-button">Sign in</el-button>
      <div class="bottom-text">
        Don't have an account? <router-link to="/register">Create an account</router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import { authAPI } from '../../api/auth';
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
          // 使用authAPI进行登录
          authAPI.login(this.loginForm)
            .then(response => {
              console.log('Login successful:', response)
              
              // 设置登录状态和token
              localStorage.setItem('isLoggedIn', 'true')
              localStorage.setItem('usernameOrEmail', this.loginForm.email)
              localStorage.setItem('token', response.token)
              
              this.$message.success('Login successful')
              this.$router.push('/home')
            })
            .catch(error => {
              console.error('Login failed:', error)
              this.$message.error(error.response?.data?.message || 'Login failed')
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