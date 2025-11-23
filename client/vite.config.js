import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    allowedHosts: true,  // 允许所有IP访问
    host: '0.0.0.0',  // 绑定到所有网络接口，允许通过IP访问
    port: 80,  // 保持为80端口，用于内网映射
    // 配置代理解决CORS问题
    proxy: {
      '/api': {
        target: 'https://10kb241io2970.vicp.fun',
        changeOrigin: true,
        secure: true,
        rewrite: (path) => path
      }
    }
  }
})
