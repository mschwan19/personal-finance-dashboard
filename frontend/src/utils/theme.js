import { ref, watch } from 'vue'

const savedTheme = localStorage.getItem('finance-theme') || 'light'

export const isDarkMode = ref(savedTheme === 'dark')

watch(isDarkMode, (newVal) => {
    if (newVal) {
        document.documentElement.classList.add('dark-mode')
        localStorage.setItem('finance-theme', 'dark')
    } else {
        document.documentElement.classList.remove('dark-mode')
        localStorage.setItem('finance-theme', 'light')
    }
}, { immediate: true })