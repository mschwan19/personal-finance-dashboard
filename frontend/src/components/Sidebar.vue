<script setup>
import { useRouter } from 'vue-router'
import { t } from '../utils/i18n'
import { LayoutDashboard, ArrowRightLeft, Settings, LogOut, Wallet } from 'lucide-vue-next'

const router = useRouter()

const logout = () => {
  router.push('/login')
}
</script>

<template>
  <aside class="sidebar">
    <div class="logo">
      <h2><Wallet class="logo-icon" :size="24" /> FinanceApp</h2>
    </div>

    <nav class="menu">
      <router-link to="/" class="nav-item">
        <LayoutDashboard :size="20" />
        <span>{{ t('dashboard.currentBalance') }}</span>
      </router-link>

      <router-link to="/transactions" class="nav-item">
        <ArrowRightLeft :size="20" />
        <span>{{ t('dashboard.recentMovements') }}</span>
      </router-link>

      <router-link to="/settings" class="nav-item">
        <Settings :size="20" />
        <span>{{ t('settings.title') }}</span> </router-link>

      <div class="divider"></div>

      <button @click="logout" class="nav-item logout-btn">
        <LogOut :size="20" />
        <span>Logout</span>
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
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 0 0 40px 10px;
  color: var(--text-main);
  font-size: 1.4rem;
}

.logo-icon {
  color: var(--primary);
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
  gap: 12px;
}

.nav-item:hover {
  background-color: var(--bg-hover);
  color: var(--text-main);
  transform: translateX(4px);
}

.router-link-active {
  background-color: var(--bg-hover);
  color: var(--primary);
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