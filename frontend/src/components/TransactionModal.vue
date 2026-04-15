<script setup>
import { ref, onMounted } from 'vue'
import { t } from '../utils/i18n'
import keycloak from '../utils/keycloak' // WICHTIG: Keycloak importiert

const props = defineProps({
  editData: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['close', 'saved'])

const categories = ref([])
const isLoading = ref(false)

const formData = ref({
  amount: null,
  date: new Date().toISOString().split('T')[0],
  description: '',
  categoryId: ''
})

onMounted(async () => {
  if (keycloak.token) {
    try {
      await keycloak.updateToken(30);
    } catch (err) {
      keycloak.login();
      return;
    }
  } else {
    keycloak.login();
    return;
  }

  try {
    const response = await fetch('http://localhost:8080/api/categories', {
      headers: {
        'Authorization': `Bearer ${keycloak.token}`
      }
    })

    if (response.ok) {
      categories.value = await response.json()
    } else if (response.status === 401) {
      keycloak.login()
    }
  } catch (error) {
    console.error("Kategorien konnten nicht geladen werden:", error)
  }

  if (props.editData) {
    formData.value = {
      amount: props.editData.amount,
      date: props.editData.date.split('T')[0],
      description: props.editData.description,
      categoryId: props.editData.categoryId
    }
  }
})

const submitTransaction = async () => {
  isLoading.value = true

  if (keycloak.token) {
    try {
      await keycloak.updateToken(30);
    } catch (err) {
      keycloak.login();
      return;
    }
  } else {
    keycloak.login();
    return;
  }

  try {
    const isEdit = !!props.editData
    const url = isEdit
        ? `http://localhost:8080/api/transactions/${props.editData.id}`
        : 'http://localhost:8080/api/transactions'

    const response = await fetch(url, {
      method: isEdit ? 'PUT' : 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${keycloak.token}` // Hier ist der Türöffner!
      },
      body: JSON.stringify(formData.value)
    })

    if (response.ok) {
      emit('saved')
      emit('close')
    } else if (response.status === 401) {
      keycloak.login()
    }
  } catch (error) {
    console.error("Fehler beim Speichern:", error)
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="modal-backdrop" @click.self="$emit('close')">
    <div class="modal-card">

      <header class="modal-header">
        <h3>{{ props.editData ? t('modal.editTitle') : t('modal.title') }}</h3>
        <button class="close-btn" @click="$emit('close')">✕</button>
      </header>

      <form @submit.prevent="submitTransaction" class="transaction-form">
        <div class="form-group">
          <label>{{ t('modal.amount') }}</label>
          <input type="number" step="0.01" v-model="formData.amount" required />
        </div>

        <div class="form-group">
          <label>{{ t('modal.date') }}</label>
          <input type="date" v-model="formData.date" required />
        </div>

        <div class="form-group">
          <label>{{ t('modal.description') }}</label>
          <input type="text" v-model="formData.description" required :placeholder="t('modal.descPlaceholder')" />
        </div>

        <div class="form-group">
          <label>{{ t('modal.category') }}</label>
          <select v-model="formData.categoryId" required>
            <option value="" disabled>{{ t('modal.select') }}</option>
            <optgroup :label="t('types.INCOME')">
              <option v-for="cat in categories.filter(c => c.type === 'INCOME')" :key="cat.id" :value="cat.id">
                {{ t(`categories.${cat.name}`) }}
              </option>
            </optgroup>
            <optgroup :label="t('types.EXPENSE')">
              <option v-for="cat in categories.filter(c => c.type === 'EXPENSE')" :key="cat.id" :value="cat.id">
                {{ t(`categories.${cat.name}`) }}
              </option>
            </optgroup>
          </select>
        </div>

        <button type="submit" class="submit-btn" :disabled="isLoading">
          {{ isLoading ? t('modal.saving') : t('modal.save') }}
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.modal-backdrop { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background-color: rgba(0, 0, 0, 0.4); backdrop-filter: blur(4px); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal-card { background: var(--white); padding: 30px; border-radius: var(--radius-lg); width: 100%; max-width: 450px; box-shadow: 0 20px 40px rgba(0,0,0,0.1); animation: slideUp 0.3s ease-out; }
@keyframes slideUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }
.modal-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; }
.modal-header h3 { margin: 0; color: var(--text-main); }
.close-btn { background: none; border: none; font-size: 1.2rem; cursor: pointer; color: var(--text-muted); transition: color 0.2s; }
.close-btn:hover { color: var(--text-main); }
.form-group { margin-bottom: 15px; display: flex; flex-direction: column; }
.form-group label { font-size: 0.9rem; font-weight: 600; color: var(--text-muted); margin-bottom: 5px; }
.form-group input, .form-group select { padding: 12px; border: 1px solid #e2e8f0; border-radius: var(--radius-md); font-size: 1rem; font-family: inherit; background-color: var(--white); color: var(--text-main); transition: border-color 0.2s, background-color 0.3s; }
:root.dark-mode .form-group input, :root.dark-mode .form-group select { border-color: #334155; }
.form-group input:focus, .form-group select:focus { outline: none; border-color: var(--primary); }
.submit-btn { width: 100%; padding: 14px; background-color: var(--primary); color: white; border: none; border-radius: var(--radius-md); font-size: 1rem; font-weight: 600; cursor: pointer; margin-top: 10px; transition: background-color 0.2s; }
.submit-btn:hover { background-color: var(--primary-dark); }
.submit-btn:disabled { opacity: 0.7; cursor: not-allowed; }
</style>