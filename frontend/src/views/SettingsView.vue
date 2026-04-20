<script setup>
import { currentLocale, t } from '../utils/i18n'
import { isDarkMode } from '../utils/theme'
import { Settings, Globe, Palette, Sun, Moon } from 'lucide-vue-next'

const setLanguage = (lang) => {
  currentLocale.value = lang
}

const setTheme = (isDark) => {
  isDarkMode.value = isDark
}
</script>

<template>
  <div class="settings-page">
    <header class="settings-header">
      <div class="header-titles">
        <h2><Settings class="icon-title" :size="28" /> {{ t('settings.title') }}</h2>
        <p class="subtitle">{{ t('settings.subtitle') }}</p>
      </div>
    </header>

    <div class="settings-grid">
      <section class="settings-card">
        <h3><Globe class="section-icon" :size="20" /> {{ t('settings.language') }}</h3>
        <p>{{ t('settings.langDesc') }}</p>

        <div class="button-group">
          <button
              @click="setLanguage('de')"
              :class="['action-btn', { active: currentLocale === 'de' }]"
          >
            <span class="emoji-flag">🇩🇪</span> Deutsch
          </button>
          <button
              @click="setLanguage('en')"
              :class="['action-btn', { active: currentLocale === 'en' }]"
          >
            <span class="emoji-flag">🇬🇧</span> English
          </button>
        </div>
      </section>

      <section class="settings-card">
        <h3><Palette class="section-icon" :size="20" /> {{ t('settings.appearance') }}</h3>
        <p>{{ t('settings.themeDesc') }}</p>

        <div class="button-group">
          <button
              @click="setTheme(false)"
              :class="['action-btn', { active: !isDarkMode }]"
          >
            <Sun :size="18" /> {{ t('settings.lightMode') }}
          </button>
          <button
              @click="setTheme(true)"
              :class="['action-btn', { active: isDarkMode }]"
          >
            <Moon :size="18" /> {{ t('settings.darkMode') }}
          </button>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped>
.settings-header { margin-bottom: 30px; }
.header-titles h2 { display: flex; align-items: center; gap: 10px; margin: 0 0 5px 0; color: var(--text-main); font-size: 2rem; }
.icon-title { color: var(--primary); }
.subtitle { margin: 0; color: var(--text-muted); }

.settings-grid {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.settings-card {
  background-color: var(--white);
  padding: 30px;
  border-radius: var(--radius-lg);
  max-width: 500px;
  box-shadow: var(--shadow-soft);
}

.settings-card h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 0;
  color: var(--text-main);
}
.section-icon { color: var(--text-muted); }
.settings-card p { color: var(--text-muted); }

.button-group {
  display: flex;
  gap: 15px;
  margin-top: 20px;
}

.action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 24px;
  border: 2px solid #e2e8f0;
  background-color: transparent;
  border-radius: var(--radius-md);
  cursor: pointer;
  font-weight: bold;
  color: var(--text-muted);
  transition: all 0.2s;
  flex: 1;
}

.emoji-flag {
  font-size: 1.2rem;
  line-height: 1;
}

:root.dark-mode .action-btn {
  border-color: #334155;
  color: #cbd5e1;
}

.action-btn:hover { border-color: var(--primary); color: var(--primary); }

.action-btn.active {
  border-color: var(--primary);
  background-color: var(--primary);
  color: white;
}
</style>