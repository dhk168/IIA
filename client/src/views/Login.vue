<template>
  <div class="login-container">
    <h1 class="main-title">个人信息助手</h1>
    <el-card class="login-form">
      <template #header>
        <div class="login-title">登录</div>
      </template>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%">登录</el-button>
        </el-form-item>
        <div class="login-footer">
          <router-link to="/register" class="register-link">注册账号</router-link>
          <router-link to="/forgot-password" class="forgot-link">忘记密码？</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginFormRef.validate((valid) => {
        if (valid) {
          // 这里可以添加登录逻辑
          console.log('登录信息:', this.loginForm)
          
          // 设置登录状态
          localStorage.setItem('isLoggedIn', 'true')
          localStorage.setItem('username', this.loginForm.username)
          
          // 模拟登录成功并跳转
          this.$message.success('登录成功')
          this.$router.push('/home')
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
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
.login-form {
  width: 100%;
  max-width: 400px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.login-form:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
}
.login-form >>> .el-card__header {
  background-color: #f8f9ff;
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;
}
.login-title {
  text-align: center;
  font-size: 22px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}
.login-form >>> .el-card__body {
  padding: 30px;
}
.login-form >>> .el-form {
  margin-bottom: 0;
}
.login-form >>> .el-form-item {
  margin-bottom: 24px;
}
.login-form >>> .el-input__wrapper {
  border-radius: 8px;
  transition: all 0.3s ease;
}
.login-form >>> .el-input__wrapper:focus-within {
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}
.login-form >>> .el-button {
  border-radius: 8px;
  padding: 12px;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
}
.login-form >>> .el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}
.login-form >>> .el-button--primary {
  background-color: #409eff;
  border-color: #409eff;
}
.login-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  padding: 0 2px;
}
.register-link,
.forgot-link {
  color: #409eff;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s ease;
}
.register-link:hover,
.forgot-link:hover {
  color: #66b1ff;
}
</style>