import { ref, watch } from 'vue'

// Schauen, ob im Browser-Speicher schon ein Theme liegt, sonst 'light'
const savedTheme = localStorage.getItem('finance-theme') || 'light'

// Das ist unser reaktiver Schalter
export const isDarkMode = ref(savedTheme === 'dark')

// Vue beobachtet diesen Schalter. Wenn er sich ändert, wird das Design angepasst.
watch(isDarkMode, (newVal) => {
    if (newVal) {
        document.documentElement.classList.add('dark-mode')
        localStorage.setItem('finance-theme', 'dark')
    } else {
        document.documentElement.classList.remove('dark-mode')
        localStorage.setItem('finance-theme', 'light')
    }
}, { immediate: true }) // immediate: true führt das direkt beim Starten der App aus!