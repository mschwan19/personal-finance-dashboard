import axios from 'axios'
import keycloak from './keycloak' // Achte darauf, dass der Pfad zu deiner keycloak.js stimmt

// 1. Wir erstellen eine eigene Axios-Instanz ("Der Postbote")
const api = axios.create({
    // Hier kommt später die URL von deinem echten Spring Boot / Node Backend rein.
    // Für jetzt nehmen wir einen Platzhalter.
    baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json'
    }
})

// 2. Der "Interceptor" fängt JEDE Anfrage ab, kurz bevor sie den Browser verlässt
api.interceptors.request.use(
    async (config) => {
        if (keycloak.authenticated) {
            try {
                // Genialer Keycloak-Trick: Wir prüfen, ob das Token in den nächsten 30 Sekunden abläuft.
                // Wenn ja, holt Keycloak blitzschnell und unsichtbar ein frisches im Hintergrund!
                await keycloak.updateToken(30)

                // Jetzt kleben wir das VIP-Bändchen an die Anfrage
                config.headers.Authorization = `Bearer ${keycloak.token}`
            } catch (error) {
                console.error('Fehler beim Token-Refresh. Nutzer muss sich neu einloggen.', error)
                keycloak.login()
            }
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

export default api