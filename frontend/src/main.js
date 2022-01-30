import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import VueNaverMap from 'vue-naver-map'

Vue.config.productionTip = false

Vue.use(VueNaverMap, {
  key: 'b10j8213jn',
  libraries: ['geocoder']
})

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')

window.Kakao.init("7d51d1ded9775ea0bc4ced86238a9f19");
