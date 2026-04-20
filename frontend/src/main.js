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
        if (error === undefined || error === 'login_required' || (error.error === 'login_required')) {
            createApp(App).use(router).mount('#app')
        } else {
            console.error("Kritischer Keycloak Fehler:", error)
            alert("Sicherheitsfehler. Bitte Browser-Cache/Cookies löschen und Seite neu laden.")
        }
    })