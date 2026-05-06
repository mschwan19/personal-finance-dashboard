<script setup>
import { ref, onMounted } from 'vue'
import { currentLocale, t } from '../utils/i18n'
import { isDarkMode } from '../utils/theme'
import { Settings, Globe, Palette, Sun, Moon, Wallet, Save, Tags, Plus, Trash2, Circle } from 'lucide-vue-next'
import api from '../utils/axios'

const setLanguage = (lang) => { currentLocale.value = lang }
const setTheme = (isDark) => { isDarkMode.value = isDark }

const initialBalance = ref(0.00)
const isSaving = ref(false)

const customCategories = ref([])
const newCategory = ref({ name: '', color: '#3b82f6', type: 'EXPENSE' })
const isSavingCategory = ref(false)

const loadSettings = async () => {
  try {
    const response = await api.get('/settings/balance')
    initialBalance.value = response.data
  } catch (error) {
    console.error(error)
  }
}

const saveBalance = async () => {
  isSaving.value = true
  try {
    await api.put('/settings/balance', { initialBalance: initialBalance.value })
    alert(t('settings.balanceSaved') || 'Startguthaben erfolgreich gespeichert!')
  } catch (error) {
    console.error(error)
  } finally {
    isSaving.value = false
  }
}

const loadCustomCategories = async () => {
  try {
    const response = await api.get('/categories/custom')
    customCategories.value = response.data
  } catch (error) {
    console.error(error)
  }
}

const addCategory = async () => {
  if (!newCategory.value.name.trim()) return
  isSavingCategory.value = true

  try {
    const response = await api.post('/categories/custom', newCategory.value)
    customCategories.value.push(response.data)
    newCategory.value = { name: '', color: '#3b82f6', type: 'EXPENSE' }
  } catch (error) {
    console.error(error)
  } finally {
    isSavingCategory.value = false
  }
}

const deleteCategory = async (id) => {
  if (!confirm(t('settings.confirmDeleteCategory') || 'Bist du sicher, dass du diese Kategorie löschen möchtest?')) return

  try {
    await api.delete(`/categories/custom/${id}`)
    customCategories.value = customCategories.value.filter(cat => cat.id !== id)
  } catch (error) {
    console.error(error)
    alert(t('settings.categoryInUse') || 'Kategorie konnte nicht gelöscht werden. Sind noch Transaktionen damit verknüpft?')
  }
}

onMounted(() => {
  loadSettings()
  loadCustomCategories()
})
</script>

<template>
  <div class="settings-page">
    <header class="settings-header">
      <div class="header-titles">
        <h2><Settings class="icon-title" :size="28" /> {{ t('settings.title') || 'Einstellungen' }}</h2>
        <p class="subtitle">{{ t('settings.subtitle') || 'Verwalte dein Konto und deine Präferenzen' }}</p>
      </div>
    </header>

    <div class="settings-grid">

      <section class="settings-card">
        <h3><Wallet class="section-icon" :size="20" /> {{ t('settings.startingBalance') || 'Startguthaben' }}</h3>
        <p>{{ t('settings.balanceDesc') || 'Trage hier den aktuellen Stand deines Bankkontos ein, damit deine Gesamtbilanz im Dashboard stimmt.' }}</p>

        <form @submit.prevent="saveBalance" class="balance-form">
          <div class="input-wrapper">
            <input type="number" step="0.01" v-model="initialBalance" class="balance-input" />
            <span class="currency-symbol">€</span>
          </div>
          <button type="submit" class="save-btn" :disabled="isSaving">
            <Save :size="18" /> {{ isSaving ? (t('settings.saving') || 'Speichert...') : (t('settings.save') || 'Speichern') }}
          </button>
        </form>
      </section>

      <section class="settings-card categories-card">
        <h3><Tags class="section-icon" :size="20" /> {{ t('settings.customCategories') || 'Eigene Kategorien' }}</h3>
        <p>{{ t('settings.customCategoriesDesc') || 'Erstelle eigene Kategorien für deine Einnahmen und Ausgaben. Diese sind nur für dich sichtbar.' }}</p>

        <div class="category-list" v-if="customCategories.length > 0">
          <div v-for="cat in customCategories" :key="cat.id" class="category-item">
            <div class="category-info">
              <Circle :size="14" :fill="cat.color" :color="cat.color" />
              <span class="category-name">{{ cat.name }}</span>
              <span class="category-type-badge">{{ cat.type === 'INCOME' ? '+' : '-' }}</span>
            </div>
            <button @click="deleteCategory(cat.id)" class="icon-btn delete-btn" :title="t('settings.delete') || 'Löschen'">
              <Trash2 :size="16" />
            </button>
          </div>
        </div>
        <p v-else class="no-categories">{{ t('settings.noCustomCategories') || 'Du hast noch keine eigenen Kategorien erstellt.' }}</p>

        <form @submit.prevent="addCategory" class="add-category-form">
          <input
              type="text"
              v-model="newCategory.name"
              :placeholder="t('settings.categoryName') || 'Name der Kategorie'"
              required
              class="cat-input text-input"
          />
          <div class="bottom-controls">
            <input
                type="color"
                v-model="newCategory.color"
                class="color-input"
                title="Farbe wählen"
            />
            <select v-model="newCategory.type" class="cat-input select-input">
              <option value="EXPENSE">{{ t('transaction.expense') || 'Ausgabe' }}</option>
              <option value="INCOME">{{ t('transaction.income') || 'Einnahme' }}</option>
            </select>
            <button type="submit" class="icon-btn add-btn" :disabled="isSavingCategory || !newCategory.name">
              <Plus :size="22" />
            </button>
          </div>
        </form>
      </section>

      <section class="settings-card">
        <h3><Globe class="section-icon" :size="20" /> {{ t('settings.language') || 'Sprache' }}</h3>
        <p>{{ t('settings.langDesc') || 'Wähle deine bevorzugte Anzeigesprache.' }}</p>
        <div class="button-group">
          <button @click="setLanguage('de')" :class="['action-btn', { active: currentLocale === 'de' }]">{{ t('settings.german') || 'Deutsch' }}</button>
          <button @click="setLanguage('en')" :class="['action-btn', { active: currentLocale === 'en' }]">{{ t('settings.english') || 'Englisch' }}</button>
        </div>
      </section>

      <section class="settings-card">
        <h3><Palette class="section-icon" :size="20" /> {{ t('settings.appearance') || 'Erscheinungsbild' }}</h3>
        <p>{{ t('settings.themeDesc') || 'Wechsle zwischen hellem und dunklem Design.' }}</p>
        <div class="button-group">
          <button @click="setTheme(false)" :class="['action-btn', { active: !isDarkMode }]"><Sun :size="18" /> {{ t('settings.lightMode') || 'Hell' }}</button>
          <button @click="setTheme(true)" :class="['action-btn', { active: isDarkMode }]"><Moon :size="18" /> {{ t('settings.darkMode') || 'Dunkel' }}</button>
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

.settings-grid { display: flex; flex-direction: column; gap: 20px; }
.settings-card { background-color: var(--white); padding: 30px; border-radius: var(--radius-lg); max-width: 500px; box-shadow: var(--shadow-soft); }
.categories-card { max-width: 600px; }
.settings-card h3 { display: flex; align-items: center; gap: 8px; margin-top: 0; color: var(--text-main); }
.section-icon { color: var(--text-muted); }
.settings-card p { color: var(--text-muted); font-size: 0.95rem; margin-bottom: 20px; }

.balance-form { display: flex; gap: 15px; align-items: center; }
.input-wrapper { position: relative; flex-grow: 1; display: flex; align-items: center; }
.balance-input { width: 100%; padding: 12px 35px 12px 15px; border: 2px solid #e2e8f0; border-radius: var(--radius-md); font-size: 1.1rem; font-weight: bold; color: var(--text-main); background: transparent; outline: none; transition: border-color 0.2s; }
:root.dark-mode .balance-input { border-color: #334155; }
.balance-input:focus { border-color: var(--primary); }
.currency-symbol { position: absolute; right: 15px; color: var(--text-muted); font-weight: bold; }

.save-btn { display: flex; align-items: center; gap: 8px; padding: 12px 20px; background-color: var(--primary); color: white; border: none; border-radius: var(--radius-md); font-weight: bold; cursor: pointer; transition: background-color 0.2s; white-space: nowrap; }
.save-btn:hover:not(:disabled) { background-color: var(--primary-dark); }
.save-btn:disabled { opacity: 0.7; cursor: not-allowed; }

.category-list { display: flex; flex-direction: column; gap: 10px; margin-bottom: 20px; max-height: 250px; overflow-y: auto; padding-right: 5px; }
.category-item { display: flex; align-items: center; justify-content: space-between; padding: 10px 15px; background-color: #f8fafc; border: 1px solid #e2e8f0; border-radius: var(--radius-md); }
:root.dark-mode .category-item { background-color: #1e293b; border-color: #334155; }
.category-info { display: flex; align-items: center; gap: 10px; font-weight: 500; color: var(--text-main); }
.category-type-badge { font-size: 0.8rem; padding: 2px 6px; background-color: #e2e8f0; border-radius: 10px; color: var(--text-muted); font-weight: bold; }
:root.dark-mode .category-type-badge { background-color: #334155; color: #cbd5e1; }
.no-categories { font-style: italic; text-align: center; margin-bottom: 20px; }

.add-category-form { display: flex; gap: 12px; align-items: center; margin-top: 15px; padding-top: 20px; border-top: 1px solid #e2e8f0; }
:root.dark-mode .add-category-form { border-color: #334155; }
.bottom-controls { display: flex; gap: 12px; align-items: center; flex-grow: 1; }

.cat-input { padding: 12px; border: 1px solid #cbd5e1; border-radius: var(--radius-md); font-family: inherit; font-size: 0.95rem; background: var(--white); color: var(--text-main); outline: none; }
:root.dark-mode .cat-input { border-color: #475569; }
.text-input { flex-grow: 1; }
.select-input { cursor: pointer; flex-grow: 1; }

.color-input { width: 44px; height: 44px; padding: 0; border: 1px solid #cbd5e1; cursor: pointer; border-radius: var(--radius-md); overflow: hidden; flex-shrink: 0; background: transparent; }
:root.dark-mode .color-input { border-color: #475569; }
.color-input::-webkit-color-swatch-wrapper { padding: 0; }
.color-input::-webkit-color-swatch { border: none; border-radius: var(--radius-md); }

.icon-btn { display: flex; align-items: center; justify-content: center; background: none; border: none; cursor: pointer; transition: all 0.2s; }
.delete-btn { color: #ef4444; border-radius: var(--radius-md); padding: 8px; }
.delete-btn:hover { background-color: #fee2e2; }
:root.dark-mode .delete-btn:hover { background-color: rgba(239, 68, 68, 0.2); }

.add-btn { width: 44px; height: 44px; border-radius: var(--radius-md); background-color: var(--primary); color: white; flex-shrink: 0; }
.add-btn:hover:not(:disabled) { background-color: var(--primary-dark); transform: scale(1.05); }
.add-btn:disabled { opacity: 0.5; cursor: not-allowed; transform: none; }

.button-group { display: flex; gap: 15px; margin-top: 10px; }
.action-btn { display: flex; align-items: center; justify-content: center; gap: 8px; padding: 12px 24px; border: 2px solid #e2e8f0; background-color: transparent; border-radius: var(--radius-md); cursor: pointer; font-weight: bold; color: var(--text-muted); transition: all 0.2s; flex: 1; }
:root.dark-mode .action-btn { border-color: #334155; color: #cbd5e1; }
.action-btn:hover { border-color: var(--primary); color: var(--primary); }
.action-btn.active { border-color: var(--primary); background-color: var(--primary); color: white; }

@media (max-width: 768px) {
  .settings-header { margin-bottom: 20px; }
  .header-titles h2 { font-size: 1.5rem; }
  .button-group { flex-direction: column; gap: 10px; }
  .action-btn { width: 100%; }
  .balance-form { flex-direction: column; align-items: stretch; }

  .add-category-form { flex-direction: column; align-items: stretch; gap: 15px; }
  .bottom-controls { width: 100%; justify-content: space-between; }
}
</style>