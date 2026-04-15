<script setup>
import { useRouter } from 'vue-router'
import { t } from '../utils/i18n'

const router = useRouter()

const logout = () => {
  router.push('/login')
}
</script>

<template>
  <aside class="sidebar">
    <div class="logo">
      <h2>💳 FinanceApp</h2>
    </div>

    <nav class="menu">
      <router-link to="/" class="nav-item">📊 {{ t('dashboard.currentBalance') }}</router-link>
      <router-link to="/transactions" class="nav-item">💸 {{ t('dashboard.recentMovements') }}</router-link>
      <router-link to="/settings" class="nav-item">⚙️ Settings</router-link>

      <div class="divider"></div>

      <button @click="logout" class="nav-item logout-btn">
        🚪 Logout
      </button>
    </nav>
  </aside>
</template>

<style scoped>
.sidebar {
  width: 260px;
  background-color: var(--white);
  padding: 30px 20px;
  display: flex;
  flex-direction: column;
  box-shadow: 4px 0 15px rgba(0,0,0,0.03);
  height: 100vh;
}

.logo h2 {
  margin: 0 0 40px 10px;
  color: var(--text-main);
  font-size: 1.4rem;
}

.menu {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.nav-item {
  text-decoration: none;
  color: var(--text-muted);
  font-weight: 600;
  padding: 12px 18px;
  border-radius: var(--radius-md);
  transition: all 0.2s ease;
  border: none;
  background: none;
  text-align: left;
  cursor: pointer;
  font-size: 0.95rem;
  display: flex;
  align-items: center;
}

/* GEÄNDERT: Nutzt jetzt die CSS-Variable für den Hintergrund */
.nav-item:hover {
  background-color: var(--bg-hover);
  color: var(--text-main);
  transform: translateX(4px);
}

.router-link-active {
  background-color: var(--bg-hover); /* Auch hier die Variable nutzen */
  color: var(--primary); /* Highlight-Farbe für den aktiven Link */
}

.divider {
  height: 1px;
  background-color: rgba(0, 0, 0, 0.05);
  margin: 15px 10px;
}

:root.dark-mode .divider {
  background-color: rgba(255, 255, 255, 0.05);
}

.logout-btn {
  color: #ef4444;
}

.logout-btn:hover {
  background-color: #fef2f2;
  color: #dc2626;
}

:root.dark-mode .logout-btn:hover {
  background-color: rgba(239, 68, 68, 0.1);
}
</style>