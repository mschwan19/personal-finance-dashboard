<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { t } from '../utils/i18n'
import { LayoutDashboard, ArrowRightLeft, Settings, LogOut, Wallet, User, Menu } from 'lucide-vue-next'
import keycloak from '../utils/keycloak'

const isCollapsed = ref(false)

const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}

const handleResize = () => {
  if (window.innerWidth <= 1040) {
    isCollapsed.value = true
  } else {
    isCollapsed.value = false
  }
}
onMounted(() => {
  handleResize()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})

const openProfile = () => {
  if (keycloak.authenticated) {
    keycloak.accountManagement()
  }
}

const logout = () => {
  keycloak.logout({ redirectUri: window.location.origin })
}
</script>

<template>
  <aside :class="['sidebar', { 'collapsed': isCollapsed }]">
    <div class="header-section">
      <div class="logo">
        <Wallet v-if="!isCollapsed" class="logo-icon icon-fixed" :size="24" />
        <h2 class="brand-name">Finvo</h2>
      </div>
      <button class="toggle-btn" @click="toggleSidebar">
        <Menu class="icon-fixed" :size="20" />
      </button>
    </div>

    <nav class="menu">
      <router-link to="/" class="nav-item" :title="isCollapsed ? t('dashboard.currentBalance') : ''">
        <LayoutDashboard class="icon-fixed" :size="20" />
        <span v-if="!isCollapsed">{{ t('dashboard.currentBalance') }}</span>
      </router-link>

      <router-link to="/transactions" class="nav-item" :title="isCollapsed ? t('dashboard.recentMovements') : ''">
        <ArrowRightLeft class="icon-fixed" :size="20" />
        <span v-if="!isCollapsed">{{ t('dashboard.recentMovements') }}</span>
      </router-link>

      <router-link to="/settings" class="nav-item" :title="isCollapsed ? t('settings.title') : ''">
        <Settings class="icon-fixed" :size="20" />
        <span v-if="!isCollapsed">{{ t('settings.title') }}</span>
      </router-link>

      <button @click="openProfile" class="nav-item" :title="isCollapsed ? t('sidebar.profile') : ''">
        <User class="icon-fixed" :size="20" />
        <span v-if="!isCollapsed">{{ t('sidebar.profile') }}</span>
      </button>

      <div class="divider"></div>

      <button @click="logout" class="nav-item logout-btn" :title="isCollapsed ? t('sidebar.logout') : ''">
        <LogOut class="icon-fixed" :size="20" />
        <span v-if="!isCollapsed">{{ t('sidebar.logout') }}</span>
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
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: sticky;
  top: 0;
  overflow: hidden;
}

.sidebar.collapsed {
  width: 85px;
  padding: 30px 10px;
}

.icon-fixed {
  flex-shrink: 0;
  min-width: 20px;
  display: flex;
  justify-content: center;
}

.header-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 40px;
  padding: 0 10px;
}

.collapsed .header-section {
  flex-direction: column;
  gap: 15px;
  padding: 0;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  color: var(--text-main);
}

.brand-name {
  margin: 0;
  font-size: 1.4rem;
  font-weight: 800;
  white-space: nowrap;
  transition: font-size 0.2s;
}

.collapsed .brand-name {
  font-size: 1.1rem;
}

.logo-icon {
  color: var(--primary);
}

.toggle-btn {
  background: var(--bg-hover);
  border: none;
  padding: 8px;
  border-radius: 8px;
  cursor: pointer;
  color: var(--text-muted);
  display: flex;
  align-items: center;
  justify-content: center;
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
  gap: 15px;
  white-space: nowrap;
}

.collapsed .nav-item {
  padding: 12px;
  justify-content: center;
  gap: 0;
}

.nav-item:hover {
  background-color: var(--bg-hover);
  color: var(--text-main);
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

.logout-btn {
  color: #ef4444;
}

.logout-btn:hover {
  background-color: #fef2f2;
}
</style>