<template>
  <back-div>
    <h1 class="auth-title">Intelligent Information Assistant</h1>
    <el-form :model="registerForm" :rules="rules" ref="registerFormRef" class="auth-form">
      <div class="form-group">
        <label class="form-label">Username</label>
        <el-form-item prop="username" style="margin: 0;">
          <light-input v-model="registerForm.username" placeholder="Username"></light-input>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">Email</label>
        <el-form-item prop="email" style="margin: 0;">
          <light-input v-model="registerForm.email" placeholder="Email"></light-input>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">Verification Code</label>
        <el-form-item prop="verificationCode" style="margin: 0;">
          <div class="verification-code-container">
            <light-input v-model="registerForm.verificationCode" placeholder="Verification Code"></light-input>
            <LightButton @click="sendVerificationCode" :disabled="countDownSeconds > 0 || sending">
              {{ countDownSeconds > 0 ? `${countDownSeconds}s to resend` : 'Send Code' }}
            </LightButton>
          </div>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">Password</label>
        <el-form-item prop="password" style="margin: 0;">
          <light-input v-model="registerForm.password" type="password" placeholder="Password"></light-input>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">Confirm Password</label>
        <el-form-item prop="confirmPassword" style="margin: 0;">
          <light-input v-model="registerForm.confirmPassword" type="password" placeholder="Confirm Password"></light-input>
        </el-form-item>
      </div>
      <auth-button @click="handleRegister" submit>Sign up</auth-button>
      <div class="bottom-text">
        Already have an account? <router-link to="/login">Sign in</router-link>
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
import LightButton from '@/components/LightButton.vue';
import AuthButton from './components/AuthButton.vue';
import LightToast from '@/components/LightToast.vue';
import LightInput from '@/components/LightInput.vue';
import BackDiv from '@/components/BackDiv.vue';
export default {
  name: 'Register',
  components: {
    LightButton,
    AuthButton,
    LightToast,
    LightInput,
    BackDiv
  },
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
      toastVisible: false,
      toastMessage: '',
      toastType: 'success', // success, error, warning, info
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
          { validator: (rule, value, callback) => {
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
          
          // Validate email format
          if (!this.registerForm.email) {
            this.showToast('error', 'Please enter email')
            return
          }
          
          // Validate email format using regex
          const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
          if (!emailRegex.test(this.registerForm.email)) {
            this.showToast('error', 'Please enter a valid email address')
            return
          }
          
          console.log('Email validated successfully:', this.registerForm.email)
          this.sending = true
          
          // Use authAPI to send verification code
          console.log('Calling API to send verification code...')
          authAPI.sendVerificationCode({
            email: this.registerForm.email
          })
          .then((response) => {
            console.log('Verification code response:', response)
            
            this.showToast('success', 'Verification code sent to your email')
            
            this.countDown()
          })
          .catch(error => {
            console.error('Failed to send verification code:', error)
            console.error('Error details:', JSON.stringify(error, null, 2))
            let errorMsg = error?.message || 'Failed to send verification code, please try again later'
            this.showToast('error', errorMsg)
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
          
          // Clear existing timer if any
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
        

        handleRegister() {
          this.$refs.registerFormRef.validate((valid) => {
            if (valid) {
              // Prepare registration data (note backend field name requirements)
              const registerData = {
                username: this.registerForm.username,
                email: this.registerForm.email,
                password: this.registerForm.password,
                code: this.registerForm.verificationCode // backend uses code field
              }
              
              // Use authAPI to register
              authAPI.register(registerData)
              .then(response => {
                console.log('Registration successful:', response)
                
                // Get data from backend response structure
                const responseData = response.data || response;
                const result = responseData.result || responseData.data || responseData;
                
                // Check if token exists
                const token = result.token;
                
                if (token) {
                  // Set login status and token
                  localStorage.setItem('isLoggedIn', 'true')
                  localStorage.setItem('usernameOrEmail', this.registerForm.username)
                  localStorage.setItem('token', token)
                  
                  // Show success toast with frosted glass effect
                  this.showToast('success', 'Registration successful, logging in...');
                  
                  // Delay navigation to let user see success toast
                  setTimeout(() => {
                    this.$router.push('/home')
                  }, 1500);
                } else {
                  // Even without token, redirect to login after registration
                  this.showToast('success', 'Registration successful, please log in');
                  setTimeout(() => {
                    this.$router.push('/login')
                  }, 1500);
                }
              })
              .catch(error => {
                console.error('Registration failed:', error)
                // Error handling: try to get error message from different structures
                let errorMsg = 'Registration failed, please try again later';
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
        
        // LightToast control methods
        showToast(type, message) {
          this.toastType = type;
          this.toastMessage = message;
          this.toastVisible = true;
        },
        closeToast() {
          this.toastVisible = false;
        }
      },
      // Clear timer when component is destroyed
      beforeDestroy() {
        if (this.countDownTimer) {
          clearInterval(this.countDownTimer)
        }
      }
}

</script>

<style scoped>
@import '@/assets/styles/page/auth.css';
</style>