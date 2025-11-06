<template>
  <div class="register-container">
    <h1 class="main-title">个人信息助手</h1>
    <el-card class="register-form">
      <template #header>
        <div class="register-title">注册</div>
      </template>
      <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" style="width: 100%">注册</el-button>
        </el-form-item>
        <div class="register-footer">
          <router-link to="/login" class="login-link">已有账号？去登录</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Register',
  data() {
    return {
      registerForm: {
        username: '',
        password: '',
        confirmPassword: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.registerForm.password) {
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
    handleRegister() {
      this.$refs.registerFormRef.validate((valid) => {
        if (valid) {
          // 这里可以添加注册逻辑
          this.$message.success('注册成功')
          console.log('注册信息:', this.registerForm)
          // 注册成功后跳转到登录页
          this.$router.push('/login')
        }
      })
    }
  }
}
</script>

<style scoped>
.register-container {
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
.register-form {
  width: 100%;
  max-width: 400px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.register-form:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
}
.register-form >>> .el-card__header {
  background-color: #f8f9ff;
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;
}
.register-title {
  text-align: center;
  font-size: 22px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}
.register-form >>> .el-card__body {
  padding: 30px;
}
.register-form >>> .el-form {
  margin-bottom: 0;
}
.register-form >>> .el-form-item {
  margin-bottom: 24px;
}
.register-form >>> .el-input__wrapper {
  border-radius: 8px;
  transition: all 0.3s ease;
}
.register-form >>> .el-input__wrapper:focus-within {
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}
.register-form >>> .el-button {
  border-radius: 8px;
  padding: 12px;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
}
.register-form >>> .el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}
.register-form >>> .el-button--primary {
  background-color: #409eff;
  border-color: #409eff;
}
.register-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  padding: 0 2px;
}
.login-link {
  color: #409eff;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s ease;
}
.login-link:hover {
  color: #66b1ff;
}
</style>