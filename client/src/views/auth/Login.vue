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
          // 准备登录数据
          const loginData = {
            email: this.loginForm.email,
            password: this.loginForm.password
          }
          
          // 使用authAPI进行登录
          authAPI.login(loginData)
            .then(response => {
              console.log('Login successful:', response)
              
              // 根据后端响应结构获取数据
              const responseData = response.data || response;
              const result = responseData.result || responseData.data || responseData;
              
              // 检查token是否存在
              const token = result.token;
              
              if (token) {
                // 设置登录状态和token
                localStorage.setItem('isLoggedIn', 'true')
                localStorage.setItem('usernameOrEmail', this.loginForm.email)
                localStorage.setItem('token', token)
                
                // 使用毛玻璃效果显示成功提示
                this.createGlassToast('success', 'Login successful, redirecting to home page...');
                
                // 延迟跳转，让用户看到成功提示
                setTimeout(() => {
                  this.$router.push('/home')
                }, 1000);
              } else {
                this.createGlassToast('error', 'Login failed, token not returned');
              }
            })
            .catch(error => {
              console.error('Login failed:', error)
              // 错误处理，尝试从不同结构中获取错误信息
              let errorMsg = 'Login failed, please try again later';
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
    
    // 毛玻璃提示方法
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
/* 导入共享的认证页面样式 */
@import '../../assets/styles/page/auth.css';
</style>