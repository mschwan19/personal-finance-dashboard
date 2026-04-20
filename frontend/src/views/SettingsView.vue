<script setup>
import { ref, onMounted } from 'vue'
import { currentLocale, t } from '../utils/i18n'
import { isDarkMode } from '../utils/theme'
import { Settings, Globe, Palette, Sun, Moon, Wallet, Save } from 'lucide-vue-next'
import api from '../utils/axios'

// --- Sprach- und Theme-Logik ---
const setLanguage = (lang) => { currentLocale.value = lang }
const setTheme = (isDark) => { isDarkMode.value = isDark }

// --- NEU: Startguthaben-Logik ---
const initialBalance = ref(0.00)
const isSaving = ref(false)

const loadSettings = async () => {
  try {
    const response = await api.get('/settings/balance')
    initialBalance.value = response.data
  } catch (error) {
    console.error("Fehler beim Laden des Startguthabens:", error)
  }
}

const saveBalance = async () => {
  isSaving.value = true
  try {
    await api.put('/settings/balance', { initialBalance: initialBalance.value })
    // Kleines visuelles Feedback (könnte man später durch ein Toast-Popup ersetzen)
    alert(t('settings.balanceSaved') || 'Startguthaben erfolgreich gespeichert!')
  } catch (error) {
    console.error("Fehler beim Speichern:", error)
  } finally {
    isSaving.value = false
  }
}

onMounted(() => {
  loadSettings()
})
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
        <h3><Wallet class="section-icon" :size="20" /> {{ t('settings.startingBalance') || 'Startguthaben' }}</h3>
        <p>{{ t('settings.balanceDesc') || 'Trage hier den aktuellen Stand deines Bankkontos ein, damit deine Gesamtbilanz im Dashboard stimmt.' }}</p>

        <form @submit.prevent="saveBalance" class="balance-form">
          <div class="input-wrapper">
            <input
                type="number"
                step="0.01"
                v-model="initialBalance"
                class="balance-input"
            />
            <span class="currency-symbol">€</span>
          </div>
          <button type="submit" class="save-btn" :disabled="isSaving">
            <Save :size="18" /> {{ isSaving ? (t('settings.saving') || 'Speichert...') : (t('settings.save') || 'Speichern') }}
          </button>
        </form>
      </section>

      <section class="settings-card">
        <h3><Globe class="section-icon" :size="20" /> {{ t('settings.language') }}</h3>
        <p>{{ t('settings.langDesc') }}</p>
        <div class="button-group">
          <button @click="setLanguage('de')" :class="['action-btn', { active: currentLocale === 'de' }]"><span class="emoji-flag">🇩🇪</span> Deutsch</button>
          <button @click="setLanguage('en')" :class="['action-btn', { active: currentLocale === 'en' }]"><span class="emoji-flag">🇬🇧</span> English</button>
        </div>
      </section>

      <section class="settings-card">
        <h3><Palette class="section-icon" :size="20" /> {{ t('settings.appearance') }}</h3>
        <p>{{ t('settings.themeDesc') }}</p>
        <div class="button-group">
          <button @click="setTheme(false)" :class="['action-btn', { active: !isDarkMode }]"><Sun :size="18" /> {{ t('settings.lightMode') }}</button>
          <button @click="setTheme(true)" :class="['action-btn', { active: isDarkMode }]"><Moon :size="18" /> {{ t('settings.darkMode') }}</button>
        </div>
      </section>

    </div>
  </div>
</template>

<style scoped>
/* Dein bestehendes CSS für Settings bleibt hier... füge nur das NEUE für das Guthaben hinzu: */
.settings-header { margin-bottom: 30px; }
.header-titles h2 { display: flex; align-items: center; gap: 10px; margin: 0 0 5px 0; color: var(--text-main); font-size: 2rem; }
.icon-title { color: var(--primary); }
.subtitle { margin: 0; color: var(--text-muted); }

.settings-grid { display: flex; flex-direction: column; gap: 20px; }
.settings-card { background-color: var(--white); padding: 30px; border-radius: var(--radius-lg); max-width: 500px; box-shadow: var(--shadow-soft); }
.settings-card h3 { display: flex; align-items: center; gap: 8px; margin-top: 0; color: var(--text-main); }
.section-icon { color: var(--text-muted); }
.settings-card p { color: var(--text-muted); font-size: 0.95rem; margin-bottom: 20px; }

/* --- NEU: Styling für das Startguthaben-Formular --- */
.balance-form { display: flex; gap: 15px; align-items: center; }
.input-wrapper { position: relative; flex-grow: 1; display: flex; align-items: center; }
.balance-input { width: 100%; padding: 12px 35px 12px 15px; border: 2px solid #e2e8f0; border-radius: var(--radius-md); font-size: 1.1rem; font-family: inherit; font-weight: bold; color: var(--text-main); background: transparent; outline: none; transition: border-color 0.2s; }
:root.dark-mode .balance-input { border-color: #334155; }
.balance-input:focus { border-color: var(--primary); }
.currency-symbol { position: absolute; right: 15px; color: var(--text-muted); font-weight: bold; }

.save-btn { display: flex; align-items: center; gap: 8px; padding: 12px 20px; background-color: var(--primary); color: white; border: none; border-radius: var(--radius-md); font-weight: bold; cursor: pointer; transition: background-color 0.2s; white-space: nowrap; }
.save-btn:hover:not(:disabled) { background-color: var(--primary-dark); }
.save-btn:disabled { opacity: 0.7; cursor: not-allowed; }

/* Bestehendes Styling für Theme/Language Buttons */
.button-group { display: flex; gap: 15px; margin-top: 10px; }
.action-btn { display: flex; align-items: center; justify-content: center; gap: 8px; padding: 12px 24px; border: 2px solid #e2e8f0; background-color: transparent; border-radius: var(--radius-md); cursor: pointer; font-weight: bold; color: var(--text-muted); transition: all 0.2s; flex: 1; }
.emoji-flag { font-size: 1.2rem; line-height: 1; }
:root.dark-mode .action-btn { border-color: #334155; color: #cbd5e1; }
.action-btn:hover { border-color: var(--primary); color: var(--primary); }
.action-btn.active { border-color: var(--primary); background-color: var(--primary); color: white; }

@media (max-width: 768px) {
  .settings-header { margin-bottom: 20px; }
  .header-titles h2 { font-size: 1.5rem; }
  .button-group { flex-direction: column; gap: 10px; }
  .action-btn { width: 100%; }
  /* NEU für Mobile */
  .balance-form { flex-direction: column; align-items: stretch; }
}
</style>