<script setup>
import { ref, onMounted, watch } from 'vue'
import { t } from '../utils/i18n'
import api from '../utils/axios'

const props = defineProps({
  editData: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['close', 'saved'])

const categories = ref([])
const isLoading = ref(false)
const todayStr = new Date().toISOString().split('T')[0]
const isRecurring = ref(false)
const recurrenceInterval = ref('MONTHLY')

const formData = ref({
  amount: null,
  date: new Date().toISOString().split('T')[0],
  description: '',
  categoryId: ''
})

watch(isRecurring, (newValue) => {
  if (newValue === true && formData.value.date < todayStr) {
    formData.value.date = todayStr
  }
})

watch(() => formData.value.date, (newDate) => {
  if (isRecurring.value && newDate < todayStr) {
    formData.value.date = todayStr
  }
})

onMounted(async () => {
  try {
    const response = await api.get('/categories')
    categories.value = response.data
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
  try {
    const isEdit = !!props.editData

    if (isEdit) {
      await api.put(`/transactions/${props.editData.id}`, formData.value)
    } else {
      if (isRecurring.value) {
        const selectedCat = categories.value.find(c => c.id === formData.value.categoryId)
        const catType = selectedCat ? selectedCat.type : 'EXPENSE'

        const recurringPayload = {
          amount: formData.value.amount,
          description: formData.value.description,
          categoryId: formData.value.categoryId,
          type: catType,
          recurrenceInterval: recurrenceInterval.value,
          nextExecutionDate: formData.value.date
        }
        await api.post('/recurring', recurringPayload)
      } else {
        await api.post('/transactions', formData.value)
      }
    }

    emit('saved')
    emit('close')
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

      <div class="modal-body">
        <form @submit.prevent="submitTransaction" class="transaction-form">
          <div class="form-group">
            <label>{{ t('modal.amount') }}</label>
            <input type="number" step="0.01" v-model="formData.amount" required />
          </div>

          <div class="form-group">
            <label>{{ t('modal.date') }}</label>
            <input
                type="date"
                v-model="formData.date"
                :min="isRecurring ? todayStr : undefined"
                required
            />
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

          <template v-if="!props.editData">
            <div class="form-group checkbox-group">
              <label class="checkbox-label">
                <input type="checkbox" v-model="isRecurring" />
                {{ t('modal.isRecurring') || 'Als Dauerauftrag anlegen' }}
              </label>
            </div>

            <div v-if="isRecurring" class="form-group fade-in">
              <label>{{ t('modal.interval') || 'Wiederholung' }}</label>
              <select v-model="recurrenceInterval" required>
                <option value="MONTHLY">{{ t('modal.monthly') || 'Monatlich' }}</option>
                <option value="YEARLY">{{ t('modal.yearly') || 'Jährlich' }}</option>
              </select>
            </div>
          </template>

          <button type="submit" class="submit-btn" :disabled="isLoading">
            {{ isLoading ? t('modal.saving') : t('modal.save') }}
          </button>
        </form>
      </div>

    </div>
  </div>
</template>

<style scoped>
.modal-backdrop { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background-color: rgba(0, 0, 0, 0.4); backdrop-filter: blur(4px); display: flex; align-items: center; justify-content: center; z-index: 100; }

.modal-card {
  background: var(--white);
  border-radius: var(--radius-lg);
  width: 100%;
  max-width: 450px;
  box-shadow: 0 20px 40px rgba(0,0,0,0.1);
  animation: slideUp 0.3s ease-out;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

@keyframes slideUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30px 30px 15px 30px;
  margin-bottom: 10px;
  border-bottom: 1px solid rgba(0,0,0,0.05);
}
:root.dark-mode .modal-header { border-bottom-color: rgba(255,255,255,0.05); }

.modal-header h3 { margin: 0; color: var(--text-main); }
.close-btn { background: none; border: none; font-size: 1.2rem; cursor: pointer; color: var(--text-muted); transition: color 0.2s; }
.close-btn:hover { color: var(--text-main); }

.modal-body {
  flex-grow: 1;
  overflow-y: auto;
  padding: 0 30px 30px 30px;
}

.form-group { margin-bottom: 15px; display: flex; flex-direction: column; }
.form-group label { font-size: 0.9rem; font-weight: 600; color: var(--text-muted); margin-bottom: 5px; }
.form-group input, .form-group select { padding: 12px; border: 1px solid #e2e8f0; border-radius: var(--radius-md); font-size: 1rem; font-family: inherit; background-color: var(--white); color: var(--text-main); transition: border-color 0.2s, background-color 0.3s; width: 100%; }
:root.dark-mode .form-group input, :root.dark-mode .form-group select { border-color: #334155; }
.form-group input:focus, .form-group select:focus { outline: none; border-color: var(--primary); }

.checkbox-group { flex-direction: row; align-items: center; margin-top: 5px; margin-bottom: 20px;}
.checkbox-label { display: flex; align-items: center; gap: 10px; cursor: pointer; color: var(--text-main) !important; font-weight: normal !important; margin-bottom: 0 !important; }
.checkbox-label input[type="checkbox"] { width: 18px; height: 18px; cursor: pointer; accent-color: var(--primary); }

.fade-in { animation: fadeIn 0.3s ease-in-out; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(-10px); } to { opacity: 1; transform: translateY(0); } }

.submit-btn { width: 100%; padding: 14px; background-color: var(--primary); color: white; border: none; border-radius: var(--radius-md); font-size: 1rem; font-weight: 600; cursor: pointer; margin-top: 10px; transition: background-color 0.2s; }
.submit-btn:hover { background-color: var(--primary-dark); }
.submit-btn:disabled { opacity: 0.7; cursor: not-allowed; }

@media (max-width: 768px) {
  .modal-backdrop { padding: 15px; align-items: center; }
  .modal-card { margin-bottom: 0px; border-radius: 20px; max-height: 85vh; }
  .modal-header { padding: 20px 20px 10px 20px; }
  .modal-body { padding: 0 20px 20px 20px; }
}
</style>