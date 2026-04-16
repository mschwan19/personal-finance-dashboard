<script setup>
import { ref, computed, onMounted } from 'vue'
import { t, currentLocale } from '../utils/i18n'
import TransactionModal from '../components/TransactionModal.vue'
import { Search, Plus, Pencil, Trash2, ArrowRightLeft, Euro } from 'lucide-vue-next'
import api from '../utils/axios' // NEU: Unser Axios Postbote

const transactions = ref([])
const categories = ref([])
const isLoading = ref(true)

const showModal = ref(false)
const transactionToEdit = ref(null)

const searchQuery = ref('')
const selectedType = ref('ALL')
const selectedCategory = ref('ALL')
const selectedMonth = ref('ALL')
const selectedYear = ref('ALL')

const loadData = async () => {
  isLoading.value = true
  try {
    const [transRes, catRes] = await Promise.all([
      api.get('/transactions'),
      api.get('/categories')
    ])

    transactions.value = transRes.data
    categories.value = catRes.data
  } catch (error) {
    console.error("Fehler beim Laden:", error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  loadData()
})

const getCategoryType = (categoryId) => {
  const category = categories.value.find(c => c.id === categoryId)
  return category ? category.type : 'EXPENSE'
}

const deleteTransaction = async (id) => {
  if (!confirm(t('transactions.confirmDelete') || 'Wirklich löschen?')) return

  try {
    // MAGIE: Einfach api.delete aufrufen
    await api.delete(`/transactions/${id}`)
    loadData()
  } catch (error) {
    console.error("Fehler beim Löschen:", error)
  }
}

const openEditModal = (transaction) => {
  transactionToEdit.value = transaction
  showModal.value = true
}

const openAddModal = () => {
  transactionToEdit.value = null
  showModal.value = true
}

const availableYears = computed(() => {
  const years = new Set(transactions.value.map(t => new Date(t.date).getFullYear()))
  return Array.from(years).sort((a, b) => b - a)
})

const availableMonths = computed(() => {
  return Array.from({ length: 12 }, (_, i) => {
    return {
      value: i,
      label: new Date(2000, i, 1).toLocaleString(currentLocale.value === 'de' ? 'de-DE' : 'en-US', { month: 'long' })
    }
  })
})

const filteredTransactions = computed(() => {
  return [...transactions.value]
      .filter(t => {
        const date = new Date(t.date)
        const type = getCategoryType(t.categoryId)

        const matchesSearch = t.description.toLowerCase().includes(searchQuery.value.toLowerCase())
        const matchesType = selectedType.value === 'ALL' || selectedType.value === type
        const matchesCategory = selectedCategory.value === 'ALL' || t.categoryId === selectedCategory.value
        const matchesMonth = selectedMonth.value === 'ALL' || date.getMonth() === selectedMonth.value
        const matchesYear = selectedYear.value === 'ALL' || date.getFullYear() === selectedYear.value
        return matchesSearch && matchesType && matchesCategory && matchesMonth && matchesYear
      })
      .sort((a, b) => new Date(b.date) - new Date(a.date))
})

const formatCurrency = (value) => {
  return new Intl.NumberFormat('de-DE', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(value)
}
</script>

<template>
  <div class="transactions-page">

    <header class="page-header">
      <div class="header-titles">
        <h2><ArrowRightLeft class="icon-title" /> {{ t('transactions.title') || 'Transaktionen' }}</h2>
        <p class="subtitle">{{ t('transactions.subtitle') || 'Verwalte deine Finanzen.' }}</p>
      </div>
      <button class="add-btn" @click="openAddModal">
        <Plus :size="18" /> {{ t('dashboard.newTransaction') }}
      </button>
    </header>

    <section class="filter-section content-box">
      <div class="search-wrapper">
        <Search class="search-icon" :size="20" />
        <input
            type="text"
            v-model="searchQuery"
            :placeholder="t('transactions.search') || 'Suchen...'"
            class="search-input"
        />
      </div>

      <div class="detail-filters">
        <select v-model="selectedType" class="filter-select">
          <option value="ALL">{{ t('transactions.allTypes') || 'Alle Typen' }}</option>
          <option value="INCOME">{{ t('types.INCOME') }}</option>
          <option value="EXPENSE">{{ t('types.EXPENSE') }}</option>
        </select>

        <select v-model="selectedCategory" class="filter-select">
          <option value="ALL">{{ t('transactions.allCategories') || 'Alle Kategorien' }}</option>
          <option
              v-for="cat in categories"
              :key="cat.id"
              :value="cat.id"
              v-show="selectedType === 'ALL' || cat.type === selectedType"
          >
            {{ t(`categories.${cat.name}`) }}
          </option>
        </select>

        <select v-model="selectedMonth" class="filter-select">
          <option value="ALL">{{ t('transactions.allMonths') || 'Alle Monate' }}</option>
          <option v-for="month in availableMonths" :key="month.value" :value="month.value">
            {{ month.label }}
          </option>
        </select>

        <select v-model="selectedYear" class="filter-select">
          <option value="ALL">{{ t('transactions.allYears') || 'Alle Jahre' }}</option>
          <option v-for="year in availableYears" :key="year" :value="year">
            {{ year }}
          </option>
        </select>
      </div>
    </section>

    <div v-if="isLoading" class="loading-state">Lade Daten...</div>

    <section v-else class="list-section content-box">
      <div v-if="filteredTransactions.length === 0" class="empty-state">
        {{ t('dashboard.noTransactions') }}
      </div>

      <ul v-else class="transaction-list">
        <li v-for="transaction in filteredTransactions" :key="transaction.id" class="transaction-item">

          <div class="t-left">
            <span class="color-dot" :style="{ backgroundColor: transaction.categoryColorHex }"></span>
            <div class="t-info">
              <strong>{{ transaction.description }}</strong>
              <span class="t-category">
                {{ t(`categories.${transaction.categoryName}`) }} •
                {{ new Date(transaction.date).toLocaleDateString(currentLocale === 'de' ? 'de-DE' : 'en-US') }}
              </span>
            </div>
          </div>

          <div class="t-right">
            <span :class="['t-amount flex-amount-small', getCategoryType(transaction.categoryId) === 'INCOME' ? 'success' : 'danger']">
              {{ getCategoryType(transaction.categoryId) === 'INCOME' ? '+' : '-' }} {{ formatCurrency(transaction.amount) }} <Euro :size="16" />
            </span>

            <div class="t-actions">
              <button @click="openEditModal(transaction)" class="action-btn edit" :title="t('transactions.edit')">
                <Pencil :size="18" />
              </button>
              <button @click="deleteTransaction(transaction.id)" class="action-btn delete" :title="t('transactions.delete')">
                <Trash2 :size="18" />
              </button>
            </div>
          </div>

        </li>
      </ul>
    </section>

    <TransactionModal
        v-if="showModal"
        :edit-data="transactionToEdit"
        @close="showModal = false; transactionToEdit = null"
        @saved="loadData"
    />

  </div>
</template>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
.header-titles h2 { display: flex; align-items: center; gap: 10px; margin: 0 0 5px 0; color: var(--text-main); font-size: 2rem; }
.icon-title { color: var(--primary); }
.subtitle { margin: 0; color: var(--text-muted); }

.add-btn { display: flex; align-items: center; gap: 8px; background-color: var(--primary); color: white; border: none; padding: 12px 20px; border-radius: var(--radius-md); font-weight: 600; cursor: pointer; box-shadow: 0 4px 6px rgba(16, 185, 129, 0.2); transition: transform 0.2s, background-color 0.2s; }
.add-btn:hover { background-color: var(--primary-dark); transform: translateY(-2px); }

.content-box { background-color: var(--white); padding: 25px; border-radius: var(--radius-lg); box-shadow: var(--shadow-soft); margin-bottom: 20px; }

.filter-section { display: flex; flex-direction: column; gap: 15px; padding: 20px 25px; }

.search-wrapper { position: relative; display: flex; align-items: center; }
.search-icon { position: absolute; left: 15px; color: var(--text-muted); }
.search-input { width: 100%; padding: 14px 15px 14px 45px; border: 1px solid #e2e8f0; border-radius: var(--radius-md); font-family: inherit; font-size: 1rem; background-color: var(--bg-hover); color: var(--text-main); transition: all 0.3s; outline: none; }
:root.dark-mode .search-input { border-color: #334155; }
.search-input:focus { border-color: var(--primary); background-color: transparent; box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.1); }

.detail-filters { display: flex; gap: 10px; flex-wrap: wrap; }

.filter-select { flex: 1; min-width: 150px; appearance: none; -webkit-appearance: none; padding: 12px 35px 12px 15px; border: 1px solid #e2e8f0; border-radius: var(--radius-md); font-family: inherit; font-size: 0.95rem; background-color: transparent; color: var(--text-main); cursor: pointer; outline: none; transition: border-color 0.2s; background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='%2364748b'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M19 9l-7 7-7-7'%3E%3C/path%3E%3C/svg%3E"); background-repeat: no-repeat; background-position: right 12px center; background-size: 14px; }
:root.dark-mode .filter-select { border-color: #334155; }
.filter-select:focus { border-color: var(--primary); }

.transaction-list { list-style: none; padding: 0; margin: 0; }
.transaction-item { display: flex; justify-content: space-between; align-items: center; padding: 15px; border-radius: var(--radius-md); margin-bottom: 10px; transition: background-color 0.2s; }
.transaction-item:hover { background-color: var(--bg-hover); }

.t-left { display: flex; align-items: center; gap: 15px; }
.color-dot { width: 12px; height: 12px; border-radius: 50%; flex-shrink: 0; }
.t-info { display: flex; flex-direction: column; gap: 4px; }
.t-info strong { color: var(--text-main); font-size: 1.05rem; }
.t-category { color: var(--text-muted); font-size: 0.85rem; }

.t-right { display: flex; align-items: center; gap: 20px; }
.t-amount { font-weight: bold; font-size: 1.1rem; white-space: nowrap; }
.t-amount.success { color: var(--primary); }
.t-amount.danger { color: #ef4444; }

.t-actions { display: flex; gap: 5px; }
.action-btn { display: flex; align-items: center; justify-content: center; background: none; border: none; padding: 8px; border-radius: 8px; cursor: pointer; color: var(--text-muted); opacity: 0; transition: all 0.2s; }

.transaction-item:hover .action-btn { opacity: 1; }
@media (max-width: 768px) { .action-btn { opacity: 1; } }

.action-btn.edit:hover { background-color: rgba(59, 130, 246, 0.1); color: #3b82f6; }
.action-btn.delete:hover { background-color: rgba(239, 68, 68, 0.1); color: #ef4444; }

.empty-state { padding: 40px; text-align: center; color: var(--text-muted); }

.flex-amount-small {
  display: flex;
  align-items: center;
  gap: 2px;
}
</style>