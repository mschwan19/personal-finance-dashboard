<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { t, currentLocale } from '../utils/i18n'
import { Wallet, Mail, Lock, User, UserPlus, Loader2 } from 'lucide-vue-next'

const router = useRouter()
const name = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const isLoading = ref(false)

const handleRegister = () => {
  if (password.value !== confirmPassword.value) {
    alert("Passwörter stimmen nicht überein!")
    return
  }

  isLoading.value = true
  setTimeout(() => {
    isLoading.value = false
    router.push('/login')
  }, 1500)
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
        <div class="logo-circle">
          <Wallet :size="32" color="white" />
        </div>
        <h2>{{ t('register.title') }}</h2>
        <p>{{ t('register.subtitle') }}</p>
      </div>

      <form @submit.prevent="handleRegister" class="login-form">

        <div class="form-group">
          <label>{{ t('register.name') }}</label>
          <div class="input-wrapper">
            <User class="input-icon" :size="18" />
            <input type="text" v-model="name" :placeholder="t('register.namePlaceholder')" required />
          </div>
        </div>

        <div class="form-group">
          <label>{{ t('login.email') }}</label>
          <div class="input-wrapper">
            <Mail class="input-icon" :size="18" />
            <input type="email" v-model="email" placeholder="name@beispiel.de" required />
          </div>
        </div>

        <div class="form-group">
          <label>{{ t('login.password') }}</label>
          <div class="input-wrapper">
            <Lock class="input-icon" :size="18" />
            <input type="password" v-model="password" placeholder="••••••••" required />
          </div>
        </div>

        <div class="form-group">
          <label>{{ t('register.confirmPassword') }}</label>
          <div class="input-wrapper">
            <Lock class="input-icon" :size="18" />
            <input type="password" v-model="confirmPassword" :placeholder="t('register.confirmPlaceholder')" required />
          </div>
        </div>

        <button type="submit" class="submit-btn" :disabled="isLoading">
          <template v-if="isLoading">
            <Loader2 class="spinner" :size="20" /> {{ t('modal.saving') }}
          </template>
          <template v-else>
            <UserPlus :size="20" /> {{ t('register.submit') }}
          </template>
        </button>

        <div class="footer-link">
          {{ t('register.alreadyHaveAccount') }}
          <router-link to="/login">{{ t('register.loginLink') }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.login-wrapper { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background-color: var(--bg-gray); display: flex; justify-content: center; align-items: center; z-index: 9999; }

/* NEU: Styling für den Sprachwechsler */
.lang-toggle { position: absolute; top: 20px; right: 20px; padding: 8px 16px; background-color: var(--white); border: 2px solid #e2e8f0; border-radius: 20px; cursor: pointer; font-weight: bold; color: var(--text-main); transition: all 0.2s; display: flex; align-items: center; gap: 5px; box-shadow: var(--shadow-soft); }
:root.dark-mode .lang-toggle { background-color: var(--bg-gray); border-color: #334155; }
.lang-toggle:hover { border-color: var(--primary); color: var(--primary); transform: translateY(-2px); }

.login-card { background-color: var(--white); width: 100%; max-width: 450px; padding: 40px; border-radius: var(--radius-lg); box-shadow: var(--shadow-soft); animation: fadeIn 0.4s ease-out; }
.brand { text-align: center; margin-bottom: 30px; }
.logo-circle { width: 64px; height: 64px; background: linear-gradient(135deg, var(--primary), var(--primary-dark)); border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 15px auto; }
.brand h2 { margin: 0; color: var(--text-main); font-size: 1.6rem; }
.brand p { color: var(--text-muted); font-size: 0.95rem; }
.login-form { display: flex; flex-direction: column; gap: 18px; }
.form-group { display: flex; flex-direction: column; gap: 8px; }
.form-group label { font-size: 0.85rem; font-weight: 600; color: var(--text-muted); text-transform: uppercase; }
.input-wrapper { position: relative; display: flex; align-items: center; }
.input-icon { position: absolute; left: 14px; color: var(--text-muted); }
.input-wrapper input { width: 100%; padding: 12px 12px 12px 42px; border: 2px solid #e2e8f0; border-radius: var(--radius-md); background-color: var(--white); color: var(--text-main); font-family: inherit; font-size: 1rem; outline: none; }
:root.dark-mode .input-wrapper input { border-color: #334155; background-color: #1e293b; }
.input-wrapper input:focus { border-color: var(--primary); }
.submit-btn { display: flex; align-items: center; justify-content: center; gap: 10px; width: 100%; padding: 14px; background-color: var(--primary); color: white; border: none; border-radius: var(--radius-md); font-weight: 600; cursor: pointer; transition: transform 0.2s; }
.submit-btn:hover:not(:disabled) { transform: translateY(-2px); background-color: var(--primary-dark); }
.footer-link { text-align: center; margin-top: 10px; font-size: 0.9rem; color: var(--text-muted); }
.footer-link a { color: var(--primary); font-weight: 600; text-decoration: none; }
.spinner { animation: spin 1s linear infinite; }
@keyframes spin { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
</style>