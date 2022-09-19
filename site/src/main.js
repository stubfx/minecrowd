import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './assets/main.css'
import en from '@/assets/locales/en.json'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"
import { createI18n } from 'vue-i18n'

const messages = {
    en
}

const i18n = createI18n({
    // something vue-i18n options here ...
    locale: 'en',
    fallbackLocale: 'en',
    messages
})

const app = createApp(App)

app.use(i18n)

app.use(router)

app.mount('#app')
