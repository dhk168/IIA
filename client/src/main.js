import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
// 引入Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// 导入下拉菜单毛玻璃效果样式，确保在Element Plus样式之后加载
import './assets/styles/components/dropdown-glass.css'
// 引入路由
import router from './router'

const app = createApp(App)
app.use(ElementPlus)
app.use(router)
app.mount('#app')
