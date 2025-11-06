<template>
  <div class="forgot-password-container">
    <h1 class="main-title">个人信息助手</h1>
    <el-card class="forgot-password-form">
      <template #header>
        <div class="forgot-password-title">找回密码</div>
      </template>
      <el-form :model="forgotForm" :rules="rules" ref="forgotFormRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="forgotForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="forgotForm.newPassword" type="password" placeholder="请输入新密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="forgotForm.confirmPassword" type="password" placeholder="请确认新密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleResetPassword" style="width: 100%">重置密码</el-button>
        </el-form-item>
        <router-link to="/login" class="login-link">返回登录</router-link>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ForgotPassword',
  data() {
    return {
      forgotForm: {
        username: '',
        newPassword: '',
        confirmPassword: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.forgotForm.newPassword) {
                callback(new Error('两次输入的密码不一致'))
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
    handleResetPassword() {
      this.$refs.forgotFormRef.validate((valid) => {
        if (valid) {
          // 这里可以添加找回密码逻辑
          this.$message.success('密码重置成功')
          console.log('重置密码信息:', this.forgotForm)
          // 重置成功后跳转到登录页
          this.$router.push('/login')
        }
      })
    }
  }
}
</script>

<style scoped>
.forgot-password-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}
.main-title {
  text-align: center;
  font-size: 32px;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 40px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  letter-spacing: 1px;
}
.forgot-password-form {
  width: 100%;
  max-width: 400px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.forgot-password-form:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
}
.forgot-password-form >>> .el-card__header {
  background-color: #f8f9ff;
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;
}
.forgot-password-title {
  text-align: center;
  font-size: 22px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}
.forgot-password-form >>> .el-card__body {
  padding: 30px;
}
.forgot-password-form >>> .el-form {
  margin-bottom: 0;
}
.forgot-password-form >>> .el-form-item {
  margin-bottom: 24px;
}
.forgot-password-form >>> .el-input__wrapper {
  border-radius: 8px;
  transition: all 0.3s ease;
}
.forgot-password-form >>> .el-input__wrapper:focus-within {
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}
.forgot-password-form >>> .el-button {
  border-radius: 8px;
  padding: 12px;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
}
.forgot-password-form >>> .el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}
.forgot-password-form >>> .el-button--primary {
  background-color: #409eff;
  border-color: #409eff;
}
.login-link {
  color: #409eff;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s ease;
  display: block;
  text-align: center;
  margin-top: 20px;
}
.login-link:hover {
  color: #66b1ff;
}
</style>