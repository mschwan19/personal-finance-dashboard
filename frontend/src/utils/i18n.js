import { ref } from 'vue'

export const currentLocale = ref('de')

const dictionaries = {
    en: {
        dashboard: {
            welcomeTitle: "Welcome back, {name}!",
            welcomeSubtitle: "Here is your financial overview.",
            newTransaction: "New Transaction",
            currentBalance: "Current Balance",
            income: "Income",
            expenses: "Expenses",
            recentMovements: "Recent Movements",
            noTransactions: "No transactions yet.",
            expensesByCategory: "Expenses by Category",
            noExpenses: "No expenses yet."
        },
        filters: {
            all: "All Time",
            month: "This Month",
            quarter: "Last 90 Days",
            year: "This Year"
        },
        categories: {
            SALARY: "Salary", GIFTS: "Gifts", REFUNDS: "Refunds",
            HOUSING: "Housing & Utilities", GROCERIES: "Groceries",
            LEISURE: "Leisure", MOBILITY: "Mobility",
            SHOPPING: "Shopping", INVESTMENTS: "Investments",
            EDUCATION: "School & Uni",
            HEALTH: "Health",
        },
        types: {
            INCOME: "Income",
            EXPENSE: "Expense"
        },
        modal: {
            title: "New Transaction",
            amount: "Amount (€)",
            date: "Date",
            description: "Description",
            descPlaceholder: "Where did you shop?",
            category: "Category",
            select: "Please select...",
            save: "Save",
            saving: "Saving..."
        },
        transactions: {
            title: "Transactions",
            subtitle: "Manage your income and expenses.",
            search: "Search description...",
            allTypes: "All Types",
            allCategories: "All Categories",
            allMonths: "All Months",
            allYears: "All Years",
            edit: "Edit",
            delete: "Delete",
            confirmDelete: "Are you sure you want to delete this transaction?"
        },
        settings: {
            title: "Settings",
            subtitle: "Personalize your app.",
            language: "Language",
            langDesc: "Choose your preferred display language:",
            appearance: "Appearance",
            themeDesc: "Choose between light and dark mode:",
            lightMode: "Light Mode",
            darkMode: "Dark Mode",
            startingBalance: "Starting Balance",
            balanceDesc: "Enter your current bank account balance.",
            save: "Save",
            saving: "Saving...",
            balanceSaved: "Starting balance saved successfully!"
        },
        sidebar: {
            profile: "Profile",
            logout: "Logout"
        },
        login: {
            title: "Please log in to continue.",
            email: "Email Address",
            password: "Password",
            forgotPassword: "Forgot Password?",
            submit: "Login",
            loading: "Logging in...",
            noAccount: "Don't have an account?",
            registerLink: "Register now"
        },
        register: {
            title: "Create Account",
            subtitle: "Start your financial tracking today.",
            name: "Full Name",
            namePlaceholder: "John Doe",
            confirmPassword: "Confirm Password",
            confirmPlaceholder: "••••••••",
            submit: "Register",
            alreadyHaveAccount: "Already have an account?",
            loginLink: "Log in here"
        }
    },
    de: {
        dashboard: {
            welcomeTitle: "Willkommen zurück, {name}!",
            welcomeSubtitle: "Hier ist dein finanzieller Überblick.",
            newTransaction: "Neue Transaktion",
            currentBalance: "Übersicht",
            income: "Einnahmen",
            expenses: "Ausgaben",
            recentMovements: "Letzte Bewegungen",
            noTransactions: "Noch keine Transaktionen vorhanden.",
            expensesByCategory: "Ausgaben nach Kategorie",
            noExpenses: "Keine Ausgaben vorhanden."
        },
        filters: {
            all: "Gesamter Zeitraum",
            month: "Dieser Monat",
            quarter: "Letzte 90 Tage",
            year: "Dieses Jahr"
        },
        categories: {
            SALARY: "Gehalt", GIFTS: "Geschenke", REFUNDS: "Rückerstattungen",
            HOUSING: "Wohnen & Fixkosten", GROCERIES: "Lebensmittel",
            LEISURE: "Freizeit", MOBILITY: "Mobilität",
            SHOPPING: "Shopping", INVESTMENTS: "Sparen & Investments",
            EDUCATION: "Schule & Studium",
            HEALTH: "Gesundheit",
        },
        types: {
            INCOME: "Einnahme",
            EXPENSE: "Ausgabe"
        },
        modal: {
            title: "Neue Transaktion",
            amount: "Betrag (€)",
            date: "Datum",
            description: "Beschreibung",
            descPlaceholder: "Wo warst du einkaufen?",
            category: "Kategorie",
            select: "Bitte wählen...",
            save: "Speichern",
            saving: "Speichert..."
        },
        transactions: {
            title: "Transaktionen",
            subtitle: "Verwalte deine Einnahmen und Ausgaben.",
            search: "Beschreibung suchen...",
            allTypes: "Alle Typen",
            allCategories: "Alle Kategorien",
            allMonths: "Alle Monate",
            allYears: "Alles Jahre",
            edit: "Bearbeiten",
            delete: "Löschen",
            confirmDelete: "Möchtest du diese Transaktion wirklich löschen?"
        },
        settings: {
            title: "Einstellungen",
            subtitle: "Personalisiere deine App.",
            language: "Sprache",
            langDesc: "Wähle deine bevorzugte Anzeigesprache:",
            appearance: "Erscheinungsbild",
            themeDesc: "Wähle zwischen hellem und dunklem Design:",
            lightMode: "Heller Modus",
            darkMode: "Dunkler Modus",
            startingBalance: "Startguthaben",
            balanceDesc: "Trage hier den aktuellen Stand deines Bankkontos ein.",
            save: "Speichern",
            saving: "Speichert...",
            balanceSaved: "Startguthaben erfolgreich gespeichert!"
        },
        sidebar: {
            profile: "Profil",
            logout: "Abmelden"
        },
        login: {
            title: "Bitte melde dich an, um fortzufahren.",
            email: "E-Mail Adresse",
            password: "Passwort",
            forgotPassword: "Passwort vergessen?",
            submit: "Anmelden",
            loading: "Anmelden...",
            noAccount: "Noch kein Konto?",
            registerLink: "Jetzt registrieren"
        },
        register: {
            title: "Konto erstellen",
            subtitle: "Starte heute mit deinem Finanz-Tracking.",
            name: "Vollständiger Name",
            namePlaceholder: "Max Mustermann",
            confirmPassword: "Passwort bestätigen",
            confirmPlaceholder: "••••••••",
            submit: "Registrieren",
            alreadyHaveAccount: "Bereits ein Konto?",
            loginLink: "Jetzt anmelden"
        }
    }
}

export const t = (path, variables = {}) => {
    const keys = path.split('.')
    let result = dictionaries[currentLocale.value]

    for (const key of keys) {
        if (!result || result[key] === undefined) {
            return path
        }
        result = result[key]
    }

    Object.keys(variables).forEach(key => {
        result = result.replace(`{${key}}`, variables[key])
    })

    return result
}