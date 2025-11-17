<template>
  <back-div>
    <h1 class="auth-title">Intelligent Information Assistant</h1>
    <el-form :model="forgotForm" :rules="rules" ref="forgotFormRef" class="auth-form">
      <div class="form-group">
        <label class="form-label">Email</label>
        <el-form-item prop="email" style="margin: 0;">
          <light-input v-model="forgotForm.email" placeholder="Email"></light-input>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">Verification Code</label>
        <el-form-item prop="verificationCode" style="margin: 0;">
          <div class="verification-code-container">
            <light-input v-model="forgotForm.verificationCode" placeholder="Verification Code"></light-input>
            <LightButton @click="sendVerificationCode" :disabled="sending">
              {{ sending ? 'Sending...' : 'Send Code' }}
            </LightButton>
          </div>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">New Password</label>
        <el-form-item prop="newPassword" style="margin: 0;">
          <light-input v-model="forgotForm.newPassword" type="password" placeholder="New Password"></light-input>
        </el-form-item>
      </div>
      <div class="form-group">
        <label class="form-label">Confirm Password</label>
        <el-form-item prop="confirmPassword" style="margin: 0;">
          <light-input v-model="forgotForm.confirmPassword" type="password" placeholder="Confirm Password"></light-input>
        </el-form-item>
      </div>
      <auth-button @click="handleResetPassword" submit>Reset Password</auth-button>
      <div class="bottom-text">
        Remember your password? <router-link to="/login">Back to Login</router-link>
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
  name: 'ForgotPassword',
  components: {
    LightButton,
    AuthButton,
    LightToast,
    LightInput,
    BackDiv
  },
  data() {
    return {
      forgotForm: {
        email: '',
        verificationCode: '',
        newPassword: '',
        confirmPassword: ''
      },
      sending: false,
      // LightToast control properties
      toastVisible: false,
      toastMessage: '',
      toastType: 'success', // success, error, warning, info
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
        this.showToast('warning', 'Please enter email first');
        return
      }
      
      // Validate email format
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(this.forgotForm.email)) {
        this.showToast('warning', 'Please enter a valid email address');
        return
      }
      
      this.sending = true
      console.log('Sending verification code to:', this.forgotForm.email)
      
      // Use authAPI to send verification code
      authAPI.sendVerificationCode({
        email: this.forgotForm.email,
        type: 'reset'
      })
      .then(() => {
        this.sending = false
        this.showToast('success', 'Verification code sent successfully');
      })
      .catch(error => {
        console.error('Failed to send verification code:', error)
        this.showToast('error', error.response?.data?.message || 'Failed to send verification code');
        this.sending = false
      })
    },
    
    handleResetPassword() {
      this.$refs.forgotFormRef.validate((valid) => {
        if (valid) {
          // Prepare password reset data
          const resetData = {
            email: this.forgotForm.email,
            verificationCode: this.forgotForm.verificationCode,
            newPassword: this.forgotForm.newPassword
          };
          
          // Use authAPI to reset password
          authAPI.resetPassword(resetData)
          .then(response => {
            this.showToast('success', 'Password reset successful');
            console.log('Password reset info:', this.forgotForm.email);
            
            // Auto-login logic - try to get token from different response structures
            const token = response.data?.token || response.token;
            
            if (token) {
              // Store token
              localStorage.setItem('token', token);
              // Set login status
              localStorage.setItem('isLoggedIn', 'true');
              // Set username/email (using email)
              localStorage.setItem('usernameOrEmail', this.forgotForm.email);
              
              // Delay redirect to home after successful reset to ensure user sees success prompt
              setTimeout(() => {
                this.$router.push('/');
              }, 1500);
            } else {
              // If no token returned, delay redirect to login page
              setTimeout(() => {
                this.$router.push('/login');
              }, 1500);
            }
          })
          .catch(error => {
            console.error('Password reset failed:', error);
            // Error handling: try to get message from different structures
            let errorMsg = 'Password reset failed';
            if (error.response) {
              errorMsg = error.response.data?.message || error.response.data || errorMsg;
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
  }
}
</script>

<style scoped>
@import '@/assets/styles/page/auth.css';
</style>