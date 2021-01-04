import Vue from 'vue'
import Router from 'vue-router'
import store from 'vuex'

Vue.use(Router)

import File from "@/components/file.vue"
import Login from "@/components/login.vue"
import Home from "@/components/home.vue"
import Admin from "@/components/admin.vue"
import Preview from "@/components/preview.vue"
import Image from "@/components/image.vue"

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
      name: 'preview',
      path: '/preview',
      component: Preview
    },
    {
      name: 'image',
      path: '/image',
      component: Image
    }
  ]
})
