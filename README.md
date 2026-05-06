# Personal Finance Dashboard 📊

**Website:** [https://personal-finance-tracker.de](https://personal-finance-tracker.de)

Ein moderner, selbstgehosteter Finanz-Tracker zur Verwaltung von Einnahmen, Ausgaben und Daueraufträgen. Entwickelt mit Fokus auf Datensicherheit, Benutzerfreundlichkeit und einer sauberen Full-Stack-Architektur.

## Kernfunktionen

* **Daueraufträge:** Automatisierte Erfassung wiederkehrender Zahlungen (monatlich/jährlich) inklusive Vorschau-Widget.
* **Sichere Authentifizierung:** Integration von **Keycloak** (OAuth2/OIDC) für professionelles Identity Management.
* **Personalisierung:** Unterstützung für Dark/Light Mode und Mehrsprachigkeit (Deutsch/Englisch).
* **Transparenz:** Interaktive Visualisierung der Cashflows und Kategorien-Verteilung.
* **Datenbank-Verschlüsselung (AES):** Datensicherheit durch serverseitige Verschlüsselung sensibler Finanzdaten (Beträge, Beschreibungen) auf Datenbankebene.
* **Individuelle Kategorien:** Neben globalen Standard-Kategorien kann jeder Nutzer eigene, private Kategorien mit individuellen Farben anlegen und verwalten.

## Technologie-Stack

* **Frontend:** Vue.js 3 (Composition API), Vite, Chart.js, Lucide Icons.
* **Backend:** Java 21, Spring Boot 3, Hibernate, Spring Security.
* **Datenbank:** PostgreSQL 16.
* **Infrastruktur:** Docker & Docker Compose, Nginx Proxy Manager.

## Installation & Setup

1. Repository klonen.
2. Umgebungsvariablen in einer `.env` Datei basierend auf der Konfiguration in `docker-compose.yml` definieren.
3. Container starten:
   ```bash
   docker compose up -d --build
