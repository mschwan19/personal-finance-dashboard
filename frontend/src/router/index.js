import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '../views/DashboardView.vue'
import keycloak from '../utils/keycloak' // WICHTIG: Keycloak importieren!

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'dashboard',
            component: DashboardView,
            alias: '/dashboard',
            meta: { requiresAuth: true } // Geschützt!
        },
        {
            path: '/transactions',
            name: 'transactions',
            component: () => import('../views/TransactionsView.vue'),
            meta: { requiresAuth: true } // Geschützt!
        },
        {
            path: '/settings',
            name: 'settings',
            component: () => import('../views/SettingsView.vue'),
            meta: { requiresAuth: true } // Geschützt!
        },
        // Diese beiden können theoretisch gelöscht werden,
        // oder du nutzt sie später als öffentliche Startseiten.
        {
            path: '/login',
            name: 'login',
            component: () => import('../views/LoginView.vue')
        },
        {
            path: '/register',
            name: 'register',
            component: () => import('../views/RegisterView.vue')
        },
        {
            path: '/:pathMatch(.*)*',
            name: 'NotFound',
            redirect: '/'
        }
    ]
})

// --- DER TÜRSTEHER (Navigation Guard) ---
router.beforeEach((to, from, next) => {
    // 1. Prüfen, ob die Ziel-Route geschützt ist
    if (to.meta.requiresAuth) {

        // 2. Ist der User NICHT eingeloggt?
        if (!keycloak.authenticated) {
            // Abweisen und direkt zu unserer neuen Keycloak-Seite schicken!
            // Das coole: Er merkt sich, wo er hinwollte (to.fullPath)
            keycloak.login({ redirectUri: window.location.origin + to.fullPath })
        } else {
            // User hat ein Token? Darf passieren!
            next()
        }
    } else {
        // Öffentliche Seiten dürfen immer besucht werden
        next()
    }
})

export default router