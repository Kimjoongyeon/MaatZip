import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

//회원가입 + 로그인
import MemberLoginPage from '../views/member/MemberLoginPage.vue'
import MemberJoinPage from '../views/member/MemberJoinPage.vue'

import DinerMap from '../views/map/DinerMap.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'MemberLoginPage',
    component: MemberLoginPage
  },
  {
    path: '/join',
    name: 'MemberJoinPage',
    component: MemberJoinPage
  },
  {
    path: '/map',
    name: 'DinerMap',
    component: DinerMap
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
