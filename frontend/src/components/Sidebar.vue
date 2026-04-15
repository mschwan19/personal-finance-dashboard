<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const logout = () => {
  // Später: Login-Logik/Token löschen
  router.push('/login')
}
</script>

<template>
  <aside class="sidebar">
    <div class="logo">
      <h2>FinanceApp</h2>
    </div>

    <nav class="menu">
      <router-link to="/" class="nav-item">📊 Dashboard</router-link>
      <router-link to="/transactions" class="nav-item">💸 Transaktionen</router-link>
      <router-link to="/settings" class="nav-item">⚙️ Einstellungen</router-link>

      <div class="divider"></div>

      <button @click="logout" class="nav-item logout-btn">
        🚪 Abmelden
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

.nav-item:hover {
  background-color: #f8fafc;
  color: var(--text-main);
  transform: translateX(4px);
}

.router-link-active {
  background-color: #e2e8f0;
  color: #0f172a;
}

.divider {
  height: 1px;
  background-color: #f1f5f9;
  margin: 15px 10px;
}

.logout-btn {
  color: #ef4444;
}

.logout-btn:hover {
  background-color: #fef2f2;
  color: #dc2626;
}
</style>