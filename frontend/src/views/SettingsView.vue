<script setup>
import { currentLocale, t } from '../utils/i18n'

const setLanguage = (lang) => {
  currentLocale.value = lang
}
</script>

<template>
  <div class="settings-page">
    <header class="settings-header">
      <h2>⚙Settings / Einstellungen</h2>
      <p class="subtitle">Personalisiere dein Erlebnis.</p>
    </header>

    <section class="settings-card">
      <h3>Sprache / Language</h3>
      <p>Wähle deine bevorzugte Anzeigesprache:</p>

      <div class="button-group">
        <button
            @click="setLanguage('de')"
            :class="['lang-btn', { active: currentLocale === 'de' }]"
        >
          🇩🇪 Deutsch
        </button>
        <button
            @click="setLanguage('en')"
            :class="['lang-btn', { active: currentLocale === 'en' }]"
        >
          🇬🇧 English
        </button>
      </div>
    </section>
  </div>
</template>

<style scoped>
.settings-header { margin-bottom: 30px; }
.settings-header h2 { margin: 0 0 5px 0; color: var(--text-main); font-size: 2rem; }
.subtitle { margin: 0; color: var(--text-muted); }

.settings-card {
  background-color: var(--bg-gray);
  padding: 30px;
  border-radius: var(--radius-lg);
  max-width: 500px;
}

.settings-card h3 { margin-top: 0; color: var(--text-main); }

.button-group {
  display: flex;
  gap: 15px;
  margin-top: 20px;
}

.lang-btn {
  padding: 12px 24px;
  border: 2px solid #e2e8f0;
  background-color: var(--white);
  border-radius: var(--radius-md);
  cursor: pointer;
  font-weight: bold;
  color: var(--text-muted);
  transition: all 0.2s;
}

.lang-btn:hover { border-color: var(--primary); }

.lang-btn.active {
  border-color: var(--primary);
  background-color: var(--primary);
  color: white;
}
</style>