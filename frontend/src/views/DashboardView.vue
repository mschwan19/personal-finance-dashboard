<script setup>
import { ref, computed, onMounted } from 'vue'
import { t, currentLocale } from '../utils/i18n'
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import { Pie } from 'vue-chartjs'
import TransactionModal from '../components/TransactionModal.vue'
import { LayoutDashboard, Plus, TrendingUp, TrendingDown, Euro } from 'lucide-vue-next'
import keycloak from '../utils/keycloak'
import api from '../utils/axios'

ChartJS.register(ArcElement, Tooltip, Legend)

const transactions = ref([])
const categories = ref([])
const isLoading = ref(true)
const showModal = ref(false)
const initialBalance = ref(0)

const userName = computed(() => {
  if (!keycloak.tokenParsed) return ''
  const firstName = keycloak.tokenParsed.given_name || ''
  const lastName = keycloak.tokenParsed.family_name || ''
  const preferredName = keycloak.tokenParsed.preferred_username || ''
  const fullRealName = `${firstName} ${lastName}`.trim()
  return fullRealName || preferredName
})

const loadData = async () => {
  isLoading.value = true
  try {
    const [transRes, catRes, balanceRes] = await Promise.all([
      api.get('/transactions'),
      api.get('/categories'),
      api.get('/settings/balance')
    ])
    transactions.value = transRes.data
    categories.value = catRes.data
    initialBalance.value = balanceRes.data
  } catch (error) {
    console.error("Netzwerk- oder Serverfehler:", error)
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

const selectedRange = ref('all')

const filterOptions = computed(() => [
  { label: t('filters.all') || 'Gesamter Zeitraum', value: 'all' },
  { label: t('filters.year') || 'Dieses Jahr', value: 'year' },
  { label: t('filters.quarter') || 'Letzte 90 Tage', value: 'quarter' },
  { label: t('filters.month') || 'Dieser Monat', value: 'month' }
])

const filteredTransactions = computed(() => {
  const now = new Date()
  return transactions.value.filter(t => {
    if (selectedRange.value === 'all') return true
    const tDate = new Date(t.date)
    if (selectedRange.value === 'year') {
      return tDate.getFullYear() === now.getFullYear()
    }
    if (selectedRange.value === 'month') {
      return tDate.getMonth() === now.getMonth() && tDate.getFullYear() === now.getFullYear()
    }
    if (selectedRange.value === 'quarter') {
      const ninetyDaysAgo = new Date()
      ninetyDaysAgo.setDate(now.getDate() - 90)
      return tDate >= ninetyDaysAgo
    }
    return true
  })
})

const totalIncome = computed(() => {
  return filteredTransactions.value
      .filter(t => getCategoryType(t.categoryId) === 'INCOME')
      .reduce((sum, t) => sum + t.amount, 0)
})

const totalExpense = computed(() => {
  return filteredTransactions.value
      .filter(t => getCategoryType(t.categoryId) === 'EXPENSE')
      .reduce((sum, t) => sum + t.amount, 0)
})

const balance = computed(() => {
  return initialBalance.value + totalIncome.value - totalExpense.value
})

const recentTransactions = computed(() => {
  return [...filteredTransactions.value]
      .sort((a, b) => new Date(b.date) - new Date(a.date) || b.id - a.id)
      .slice(0, 5)
})

const formatCurrency = (value) => {
  return new Intl.NumberFormat('de-DE', { minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(value)
}

const chartData = computed(() => {
  const expenses = filteredTransactions.value.filter(t => getCategoryType(t.categoryId) === 'EXPENSE')
  const groupedData = {}
  const colors = {}

  expenses.forEach(transaction => {
    const catName = t(`categories.${transaction.categoryName}`)
    if (!groupedData[catName]) {
      groupedData[catName] = 0
      colors[catName] = transaction.categoryColorHex
    }
    groupedData[catName] += transaction.amount
  })

  return {
    labels: Object.keys(groupedData),
    datasets: [{
      data: Object.values(groupedData),
      backgroundColor: Object.values(colors),
      borderWidth: 2,
      borderColor: '#ffffff'
    }]
  }
})

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: { legend: { position: 'bottom' }, tooltip: { callbacks: { label: (context) => ` ${formatCurrency(context.raw)}` } } }
}
</script>

<template>
  <div class="dashboard">
    <header class="dashboard-header">
      <div class="header-titles">
        <h2><LayoutDashboard class="icon-title" :size="28" /> Dashboard</h2>
        <p class="subtitle">{{ t('dashboard.welcomeTitle', { name: userName }) }} {{ t('dashboard.welcomeSubtitle') || 'Hier ist dein Überblick.' }}</p>
      </div>

      <div class="header-actions">
        <select v-model="selectedRange" class="filter-select">
          <option v-for="opt in filterOptions" :key="opt.value" :value="opt.value">
            {{ opt.label }}
          </option>
        </select>
        <button class="add-btn" @click="showModal = true">
          <Plus :size="18" /> {{ t('dashboard.newTransaction') || 'Neu' }}
        </button>
      </div>
    </header>

    <div v-if="isLoading" class="loading-state">...</div>

    <div v-else>
      <section class="kpi-grid">
        <div :class="['kpi-card', 'balance', { 'is-negative': balance < 0 }]">
          <h3>{{ t('dashboard.currentBalance') }}</h3>
          <div class="amount flex-amount">
            {{ formatCurrency(balance) }} <Euro :size="28" />
          </div>
        </div>
        <div class="kpi-card income">
          <div class="icon-wrapper"><TrendingUp :size="28" color="var(--primary)" /></div>
          <div>
            <h3>{{ t('dashboard.income') }}</h3>
            <div class="amount success flex-amount">
              {{ formatCurrency(totalIncome) }} <Euro :size="22" />
            </div>
          </div>
        </div>
        <div class="kpi-card expense">
          <div class="icon-wrapper"><TrendingDown :size="28" color="#ef4444" /></div>
          <div>
            <h3>{{ t('dashboard.expenses') }}</h3>
            <div class="amount danger flex-amount">
              {{ formatCurrency(totalExpense) }} <Euro :size="22" />
            </div>
          </div>
        </div>
      </section>

      <div class="content-grid">
        <section class="recent-section content-box">
          <h3>{{ t('dashboard.recentMovements') }}</h3>
          <div v-if="recentTransactions.length === 0" class="empty-state">{{ t('dashboard.noTransactions') }}</div>
          <ul v-else class="transaction-list">
            <li v-for="transaction in recentTransactions" :key="transaction.id" class="transaction-item">
              <div class="t-left">
                <span class="color-dot" :style="{ backgroundColor: transaction.categoryColorHex }"></span>
                <div class="t-info">
                  <strong>{{ transaction.description }}</strong>
                  <span class="t-category">{{ t(`categories.${transaction.categoryName}`) }}</span>
                </div>
              </div>
              <div class="t-right">
                <span :class="['t-amount flex-amount-small', getCategoryType(transaction.categoryId) === 'INCOME' ? 'success' : 'danger']">
                  {{ getCategoryType(transaction.categoryId) === 'INCOME' ? '+' : '-' }} {{ formatCurrency(transaction.amount) }} <Euro :size="16" />
                </span>
                <span class="t-date">{{ new Date(transaction.date).toLocaleDateString(currentLocale === 'de' ? 'de-DE' : 'en-US') }}</span>
              </div>
            </li>
          </ul>
        </section>

        <section class="chart-section content-box">
          <h3>{{ t('dashboard.expensesByCategory') }}</h3>
          <div v-if="chartData.labels.length === 0" class="empty-state">{{ t('dashboard.noExpenses') }}</div>
          <div v-else class="chart-container">
            <Pie :data="chartData" :options="chartOptions" />
          </div>
        </section>
      </div>
    </div>

    <TransactionModal v-if="showModal" @close="showModal = false" @saved="loadData" />
  </div>
</template>

<style scoped>
.dashboard-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
.header-titles h2 { display: flex; align-items: center; gap: 10px; margin: 0 0 5px 0; color: var(--text-main); font-size: 2rem; }
.icon-title { color: var(--primary); }
.subtitle { margin: 0; color: var(--text-muted); }
.header-actions { display: flex; gap: 15px; align-items: center; }

.filter-select { appearance: none; -webkit-appearance: none; padding: 10px 35px 10px 15px; border: 1px solid #e2e8f0; border-radius: 20px; background-color: var(--white); background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='%2364748b'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M19 9l-7 7-7-7'%3E%3C/path%3E%3C/svg%3E"); background-repeat: no-repeat; background-position: right 12px center; background-size: 14px; font-family: inherit; font-size: 0.9rem; font-weight: 600; color: var(--text-main); cursor: pointer; outline: none; }
:root.dark-mode .filter-select { border-color: #334155; }
.filter-select:hover, .filter-select:focus { border-color: var(--primary); }
.add-btn { display: flex; align-items: center; justify-content: center; gap: 8px; background-color: var(--primary); color: white; border: none; padding: 12px 20px; border-radius: var(--radius-md); font-weight: 600; cursor: pointer; transition: background-color 0.2s; }

.kpi-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-bottom: 30px; }
.kpi-card { background-color: var(--bg-gray); padding: 25px; border-radius: var(--radius-lg); display: flex; align-items: center; gap: 15px; }
.kpi-card.balance { background: linear-gradient(135deg, var(--primary), var(--primary-dark)); color: white; flex-direction: column; align-items: flex-start; gap: 10px; }
.kpi-card.balance.is-negative { background: linear-gradient(135deg, #ef4444, #b91c1c); }
.kpi-card.balance h3 { color: rgba(255, 255, 255, 0.8); margin:0;}
.kpi-card.balance .amount { color: white; font-size: 2.2rem; }
.kpi-card h3 { margin: 0 0 5px 0; font-size: 0.9rem; color: var(--text-muted); text-transform: uppercase; }
.amount { font-size: 1.8rem; font-weight: bold; color: var(--text-main); }
.amount.success { color: var(--primary); }
.amount.danger { color: #ef4444; }
.icon-wrapper { font-size: 2rem; background: var(--bg-gray); width: 60px; height: 60px; display: flex; align-items: center; justify-content: center; border-radius: 50%; }

.content-grid { display: grid; gap: 30px; grid-template-columns: 1fr; }
@media (min-width: 1024px) { .content-grid { grid-template-columns: 1.5fr 1fr; } }
.content-box { background-color: var(--bg-gray); padding: 25px; border-radius: var(--radius-lg); overflow: hidden; }
.content-box h3 { margin: 0 0 20px 0; color: var(--text-main); }
.transaction-list { list-style: none; padding: 0; margin: 0; }
.transaction-item { display: flex; justify-content: space-between; align-items: center; padding: 12px 0; border-bottom: 1px solid rgba(0,0,0,0.05); }
:root.dark-mode .transaction-item { border-bottom-color: rgba(255, 255, 255, 0.05); }
.transaction-item:last-child { border-bottom: none; }
.t-left { display: flex; align-items: center; gap: 15px; }
.color-dot { width: 12px; height: 12px; border-radius: 50%; min-width: 12px; }
.t-info { display: flex; flex-direction: column; gap: 4px; }
.t-info strong { color: var(--text-main); font-size: 1.05rem; }
.t-category { color: var(--text-muted); font-size: 0.85rem; }
.t-right { display: flex; flex-direction: column; align-items: flex-end; gap: 4px; }
.t-amount { font-weight: bold; font-size: 1.1rem; }
.t-amount.success { color: var(--primary); }
.t-amount.danger { color: #ef4444; }
.t-date { color: var(--text-muted); font-size: 0.85rem; }
.chart-container { position: relative; height: 250px; width: 100%; max-width: 100%; }
.empty-state { padding: 30px; text-align: center; color: var(--text-muted); }
.flex-amount { display: flex; align-items: center; gap: 4px; }
.flex-amount-small { display: flex; align-items: center; gap: 2px; }


@media (max-width: 768px) {
  .dashboard-header { flex-direction: column; align-items: flex-start; gap: 15px; margin-bottom: 20px; }
  .header-titles h2 { font-size: 1.5rem; }

  .header-actions { width: 100%; flex-direction: column; gap: 10px; }
  .filter-select, .add-btn { width: 100%; }

  .kpi-grid { grid-template-columns: 1fr; gap: 15px; }
  .content-box { padding: 15px; }

  .chart-container { height: 220px; }
}
</style>