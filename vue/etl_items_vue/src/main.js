import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

// router 가져오기
import router from './router/router'

// .use(router)추가
createApp(App).use(router).mount('#app')