import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
// 引入Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// 导入所有Element Plus图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 导入项目对话框毛玻璃效果样式

// 导入背景动画样式
/* Removed background.css import */
// 引入路由
import router from './router'

const app = createApp(App)
// 注册所有Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.use(ElementPlus)
app.use(router)
app.mount('#app')
