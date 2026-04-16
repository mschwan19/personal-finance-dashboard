<script setup>
import { t, currentLocale } from '../utils/i18n'
import { Wallet, LogIn, UserPlus } from 'lucide-vue-next'
import keycloak from '../utils/keycloak'

const handleLogin = () => {
  keycloak.login({ redirectUri: window.location.origin + '/' });
};

const handleRegister = () => {
  keycloak.register({ redirectUri: window.location.origin + '/' });
};

const toggleLanguage = () => {
  currentLocale.value = currentLocale.value === 'de' ? 'en' : 'de'
}
</script>

<template>
  <div class="login-wrapper">
    <button class="lang-toggle" @click="toggleLanguage">
      {{ currentLocale === 'de' ? '🇬🇧 EN' : '🇩🇪 DE' }}
    </button>

    <div class="login-card">
      <div class="brand">
        <div class="brand-row">
          <div class="logo-circle">
            <Wallet :size="32" color="white" />
          </div>
          <h2>Finvo</h2>
        </div>
        <p class="subtitle">{{ t('login.title') || 'Willkommen bei Finvo' }}</p>
        <p class="security-badge">🔒 Sicherer Login über Keycloak</p>
      </div>

      <div class="action-buttons">
        <button @click="handleLogin" class="submit-btn login">
          <LogIn :size="20" /> {{ t('login.submit') || 'Anmelden' }}
        </button>

        <button @click="handleRegister" class="submit-btn register">
          <UserPlus :size="20" /> {{ t('register.submit') || 'Konto erstellen' }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-wrapper { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background-color: var(--bg-gray); display: flex; justify-content: center; align-items: center; z-index: 9999; }
.lang-toggle { position: absolute; top: 20px; right: 20px; padding: 8px 16px; background-color: var(--white); border: 2px solid #e2e8f0; border-radius: 20px; cursor: pointer; font-weight: bold; color: var(--text-main); transition: all 0.2s; display: flex; align-items: center; gap: 5px; box-shadow: var(--shadow-soft); }
:root.dark-mode .lang-toggle { background-color: var(--bg-gray); border-color: #334155; }
.lang-toggle:hover { border-color: var(--primary); color: var(--primary); transform: translateY(-2px); }

.login-card { background-color: var(--white); width: 100%; max-width: 400px; padding: 50px 40px; border-radius: var(--radius-lg); box-shadow: var(--shadow-soft); text-align: center; }

.brand { margin-bottom: 40px; }
.brand-row { display: flex; align-items: center; justify-content: center; gap: 12px; margin-bottom: 15px; }
.logo-circle { width: 56px; height: 56px; background: linear-gradient(135deg, var(--primary), var(--primary-dark)); border-radius: 14px; display: flex; align-items: center; justify-content: center; box-shadow: 0 4px 12px rgba(16, 185, 129, 0.2); }
.brand h2 { margin: 0; color: var(--text-main); font-size: 2.2rem; font-weight: 800; }
.subtitle { margin: 0 0 15px 0; color: var(--text-muted); font-size: 1rem; }
.security-badge { display: inline-block; background-color: rgba(16, 185, 129, 0.1); color: var(--primary-dark); padding: 6px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: 600; margin: 0; }

.action-buttons { display: flex; flex-direction: column; gap: 15px; }

.submit-btn { display: flex; align-items: center; justify-content: center; gap: 10px; width: 100%; padding: 15px; border-radius: var(--radius-md); font-size: 1.05rem; font-weight: 600; cursor: pointer; transition: transform 0.2s, background-color 0.2s; border: none; }
.submit-btn.login { background-color: var(--primary); color: white; box-shadow: 0 4px 6px rgba(16, 185, 129, 0.2); }
.submit-btn.login:hover { background-color: var(--primary-dark); transform: translateY(-2px); }

.submit-btn.register { background-color: transparent; border: 2px solid #e2e8f0; color: var(--text-main); }
:root.dark-mode .submit-btn.register { border-color: #334155; }
.submit-btn.register:hover { border-color: var(--primary); color: var(--primary); transform: translateY(-2px); }
</style>