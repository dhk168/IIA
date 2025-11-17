import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/auth/Login.vue'),
    meta: { requiresGuest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/auth/Register.vue'),
    meta: { requiresGuest: true }
  },
  {
    path: '/forgot-password',
    name: 'ForgotPassword',
    component: () => import('../views/auth/ForgotPassword.vue'),
    meta: { requiresGuest: true }
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/home/Home.vue'),
    meta: { requiresAuth: true },
    redirect: '/home/projects',
    children: [
      {
        path: 'blank',
        name: 'Blank',
        component: () => import('../views/Blank.vue')
      },
      {
        path: 'projects',
        name: 'Projects',
        component: () => import('../views/projects/Projects.vue')
      },
      { 
        path: 'tasks', 
        name: 'Tasks', 
        component: () => import('../views/tasks/Tasks.vue') 
      },
      {        
        path: 'analytics',        
        name: 'Analytics',        
        component: () => import('../views/analytics/Analytics.vue')      
      },      
      {        
        path: 'debug',        
        name: 'Debug',        
        component: () => import('../views/debug/Debug.vue')      
      },
      {        
        path: 'demo',        
        name: 'Demo',        
        component: () => import('../views/demo/Demo.vue')      
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 导航守卫
router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'
  
  // 需要认证的页面
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isLoggedIn) {
      // 未登录，重定向到登录页
      next({ path: '/login' })
    } else {
      next()
    }
  }
  // 游客页面（已登录用户不能访问）
  else if (to.matched.some(record => record.meta.requiresGuest)) {
    if (isLoggedIn) {
      // 已登录，重定向到主页
      next({ path: '/home' })
    } else {
      next()
    }
  }
  else {
    next()
  }
})

export default router