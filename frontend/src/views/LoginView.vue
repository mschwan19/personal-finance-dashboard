<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { t, currentLocale } from '../utils/i18n'
import { Wallet, Mail, Lock, LogIn, Loader2 } from 'lucide-vue-next'

const router = useRouter()
const email = ref('')
const password = ref('')
const isLoading = ref(false)

const handleLogin = () => {
  isLoading.value = true

  setTimeout(() => {
    isLoading.value = false
    router.push('/')
  }, 1000)
}

// Funktion zum Sprache wechseln
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
            <Wallet :size="28" color="white" />
          </div>
          <h2>Finvo</h2>
        </div>
        <p>{{ t('login.title') }}</p>
      </div>

      <form @submit.prevent="handleLogin" class="login-form">

        <div class="form-group">
          <label>{{ t('login.email') }}</label>
          <div class="input-wrapper">
            <Mail class="input-icon" :size="18" />
            <input
                type="email"
                v-model="email"
                placeholder="name@beispiel.de"
                required
                :disabled="isLoading"
            />
          </div>
        </div>

        <div class="form-group">
          <label>{{ t('login.password') }}</label>
          <div class="input-wrapper">
            <Lock class="input-icon" :size="18" />
            <input
                type="password"
                v-model="password"
                placeholder="••••••••"
                required
                :disabled="isLoading"
            />
          </div>
        </div>

        <div class="form-actions">
          <a href="#" class="forgot-password">{{ t('login.forgotPassword') }}</a>
        </div>

        <button type="submit" class="submit-btn" :disabled="isLoading">
          <template v-if="isLoading">
            <Loader2 class="spinner" :size="20" /> {{ t('login.loading') }}
          </template>
          <template v-else>
            <LogIn :size="20" /> {{ t('login.submit') }}
          </template>
        </button>

        <div class="footer-link">
          {{ t('login.noAccount') }}
          <router-link to="/register">{{ t('login.registerLink') }}</router-link>
        </div>

      </form>
    </div>
  </div>
</template>

<style scoped>
.login-wrapper { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background-color: var(--bg-gray); display: flex; justify-content: center; align-items: center; z-index: 9999; transition: background-color 0.3s ease; }

.lang-toggle { position: absolute; top: 20px; right: 20px; padding: 8px 16px; background-color: var(--white); border: 2px solid #e2e8f0; border-radius: 20px; cursor: pointer; font-weight: bold; color: var(--text-main); transition: all 0.2s; display: flex; align-items: center; gap: 5px; box-shadow: var(--shadow-soft); }
:root.dark-mode .lang-toggle { background-color: var(--bg-gray); border-color: #334155; }
.lang-toggle:hover { border-color: var(--primary); color: var(--primary); transform: translateY(-2px); }

.login-card { background-color: var(--white); width: 100%; max-width: 400px; padding: 40px; border-radius: var(--radius-lg); box-shadow: var(--shadow-soft); animation: fadeIn 0.4s ease-out; transition: background-color 0.3s ease, box-shadow 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }

/* NEU: Styles für die Brand-Zeile */
.brand { text-align: center; margin-bottom: 30px; }
.brand-row { display: flex; align-items: center; justify-content: center; gap: 12px; margin-bottom: 10px; }
.logo-circle { width: 48px; height: 48px; background: linear-gradient(135deg, var(--primary), var(--primary-dark)); border-radius: 12px; display: flex; align-items: center; justify-content: center; box-shadow: 0 4px 12px rgba(16, 185, 129, 0.2); }
.brand h2 { margin: 0; color: var(--text-main); font-size: 1.8rem; font-weight: 800; }
.brand p { margin: 0; color: var(--text-muted); font-size: 0.95rem; }

.login-form { display: flex; flex-direction: column; gap: 20px; }
.form-group { display: flex; flex-direction: column; gap: 8px; }
.form-group label { font-size: 0.85rem; font-weight: 600; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.5px; }
.input-wrapper { position: relative; display: flex; align-items: center; }
.input-icon { position: absolute; left: 14px; color: var(--text-muted); transition: color 0.3s; }
.input-wrapper input { width: 100%; padding: 14px 14px 14px 42px; border: 2px solid #e2e8f0; border-radius: var(--radius-md); background-color: var(--white); color: var(--text-main); font-family: inherit; font-size: 1rem; transition: all 0.3s; outline: none; }
:root.dark-mode .input-wrapper input { border-color: #334155; background-color: #1e293b; }
.input-wrapper input:focus { border-color: var(--primary); box-shadow: 0 0 0 4px rgba(16, 185, 129, 0.1); }
.input-wrapper input:focus + .input-icon, .input-wrapper input:focus ~ .input-icon { color: var(--primary); }
.form-actions { display: flex; justify-content: flex-end; }
.forgot-password { font-size: 0.85rem; color: var(--primary); text-decoration: none; font-weight: 600; }
.forgot-password:hover { text-decoration: underline; }
.submit-btn { display: flex; align-items: center; justify-content: center; gap: 10px; width: 100%; padding: 14px; background-color: var(--primary); color: white; border: none; border-radius: var(--radius-md); font-size: 1.05rem; font-weight: 600; cursor: pointer; box-shadow: 0 4px 6px rgba(16, 185, 129, 0.2); transition: transform 0.2s, background-color 0.2s; margin-top: 5px; }
.submit-btn:hover:not(:disabled) { background-color: var(--primary-dark); transform: translateY(-2px); }
.submit-btn:disabled { opacity: 0.8; cursor: not-allowed; }
.spinner { animation: spin 1s linear infinite; }
@keyframes spin { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }

.footer-link { text-align: center; margin-top: 10px; font-size: 0.9rem; color: var(--text-muted); }
.footer-link a { color: var(--primary); font-weight: 600; text-decoration: none; }
.footer-link a:hover { text-decoration: underline; }
</style>