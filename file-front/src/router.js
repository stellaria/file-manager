import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import File from "@/components/file.vue"
import Login from "@/components/login.vue"
import Home from "@/components/home.vue"
import Admin from "@/components/admin.vue"

export default new Router({
  mode: 'history',
  // base: process.env.BASE_URL,
  routes: [
    {
      name: 'root',
      path: '/',
      component: Home,
      children: [
        {
          name: 'file',
          path: 'file',
          component: File
        },
        {
          name: 'admin',
          path: 'admin',
          component: Admin
        }
      ]
    },
    {
      name: 'login',
      path: '/login',
      component: Login
    },
    {
      name: 'signup',
      path: '/sign',
      component: ()=> import("@/components/signup.vue") 
    },
    {
      name: 'preview',
      path: '/preview',
      component: ()=> import("@/components/preview.vue")
    },
    {
      name: 'image',
      path: '/image',
      component: ()=> import("@/components/image.vue")
    }
  ]
})
