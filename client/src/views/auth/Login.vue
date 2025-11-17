<template>
  <back-div>
    <h1 class="auth-title">Intelligent Information Assistant</h1>
    <el-form :model="loginForm" :rules="rules" ref="loginFormRef" class="auth-form">
      <div class="form-group">
        <label class="form-label">Email</label>
        <el-form-item prop="email" style="margin: 0;">
          <light-input v-model="loginForm.email" placeholder="Email"></light-input>
        </el-form-item>
      </div>
      <div class="form-group">
        <div class="password-container">
          <label class="form-label">Password</label>
          <router-link to="/forgot-password" class="forgot-password-link">Forgot password?</router-link>
        </div>
        <el-form-item prop="password" style="margin: 0;">
          <light-input v-model="loginForm.password" type="password" placeholder="Password"></light-input>
        </el-form-item>
      </div>
      <auth-button @click="handleLogin" submit>Sign in</auth-button>
      <div class="bottom-text">
        Don't have an account? <router-link to="/register">Create an account</router-link>
      </div>
    </el-form>
    <light-toast
      :visible="toastVisible"
      :message="toastMessage"
      :type="toastType"
      @close="closeToast"
    ></light-toast>
  </back-div>
</template>

<script>
import { authAPI } from '@/api/auth';
import AuthButton from './components/AuthButton.vue';
import LightToast from '@/components/LightToast.vue';
import LightInput from '@/components/LightInput.vue';
import BackDiv from '@/components/BackDiv.vue';
export default {
  name: 'Login',
  components: {
    AuthButton,
    LightToast,
    LightInput,
    BackDiv
  },
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
      },
      // LightToast control properties
      toastVisible: false,
      toastMessage: '',
      toastType: 'success' // success, error, warning, info
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginFormRef.validate((valid) => {
        if (valid) {
          // Prepare login data
          const loginData = {
            email: this.loginForm.email,
            password: this.loginForm.password
          }
          
          // Use authAPI to login
          authAPI.login(loginData)
            .then(response => {
              console.log('Login successful:', response)
              
              // Get data from backend response structure
              const responseData = response.data || response;
              const result = responseData.result || responseData.data || responseData;
              
              // Check if token exists
              const token = result.token;
              console.log('Token returned from server:', token);
              
              if (token) {
                // Set login status and token
                localStorage.setItem('isLoggedIn', 'true')
                localStorage.setItem('usernameOrEmail', this.loginForm.email)
                localStorage.setItem('token', token)
                
                // Show success toast with LightToast
                this.showToast('success', 'Login successful, redirecting to home page...');
                
                // Delay navigation to let user see success toast
                const self = this;
                setTimeout(() => {
                  self.$router.push('/home')
                }, 1000);
              } else {
                this.showToast('error', 'Login failed, token not returned');
              }
            })
            .catch(error => {
              console.error('Login failed:', error)
              // Error handling: try to get error message from different structures
              let errorMsg = 'Login failed, please try again later';
              if (error.response) {
                errorMsg = error.response.data?.message || 
                           error.response.data?.error || 
                           error.response.data?.msg || 
                           errorMsg;
              } else if (error.message) {
                errorMsg = error.message;
              }
              this.showToast('error', errorMsg);
            })
        }
      })
    },
    
    showToast(type, message) {
      this.toastType = type;
      this.toastMessage = message;
      this.toastVisible = true;
    },
    closeToast() {
      this.toastVisible = false;
    }
  }
}
</script>

<style scoped>
@import '@/assets/styles/page/auth.css';
</style>