<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import './assets/main.css'
import Sidebar from './components/Sidebar.vue'

const route = useRoute()

const showSidebar = computed(() => route.name !== 'login')
</script>

<template>
  <div class="app-layout" :class="{ 'auth-mode': !showSidebar }">

    <Sidebar v-if="showSidebar" />

    <main class="main-content">
      <div class="content-card">
        <router-view />
      </div>
    </main>

  </div>
</template>

<style scoped>
.app-layout {
  display: flex;
  height: 100vh;
}

.main-content {
  flex-grow: 1;
  padding: 30px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.content-card {
  background-color: var(--white);
  flex-grow: 1;
  border-radius: var(--radius-lg);
  padding: 40px;
  box-shadow: var(--shadow-soft);
}

.auth-mode .main-content {
  padding: 0;
  align-items: center;
  justify-content: center;
  background-color: var(--bg-gray);
}

.auth-mode .content-card {
  flex-grow: 0;
  width: 100%;
  max-width: 400px;
  min-height: auto;
}

@media (max-width: 768px) {
  .main-content {
    padding: 10px;
  }

  .content-card {
    padding: 15px;
    border-radius: 12px;
    overflow-x: hidden;
  }
}
</style>