import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { initKeycloak } from './utils/keycloak'

initKeycloak()
    .then((authenticated) => {
        console.log("Keycloak ist bereit! Authentifiziert:", authenticated)

        createApp(App).use(router).mount('#app')
    })
    .catch((error) => {
        console.error("Keycloak Initialisierung fehlgeschlagen!", error)
    })