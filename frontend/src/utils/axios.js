import axios from 'axios'
import keycloak from './keycloak'

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json'
    }
})

api.interceptors.request.use(
    async (config) => {
        if (keycloak.authenticated) {
            try {
                await keycloak.updateToken(30)

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