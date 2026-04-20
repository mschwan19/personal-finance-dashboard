import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '../views/DashboardView.vue'
import keycloak from '../utils/keycloak'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'dashboard',
            component: DashboardView,
            alias: '/dashboard',
            meta: { requiresAuth: true }
        },
        {
            path: '/transactions',
            name: 'transactions',
            component: () => import('../views/TransactionsView.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: '/settings',
            name: 'settings',
            component: () => import('../views/SettingsView.vue'),
            meta: { requiresAuth: true }
        },
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


router.beforeEach((to, from, next) => {
    if (to.meta.requiresAuth) {

        if (!keycloak.authenticated) {
            keycloak.login()
        } else {
            next()
        }
    } else {
        next()
    }
})

export default router