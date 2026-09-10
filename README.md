# IMS – Offline Inventory Management System (Android)

IMS is a **complete, offline-first Inventory Management System** Android application designed for **retailers, wholesalers, and small-to-medium businesses**.  
It manages **stock, purchases, sales, customers, suppliers, billing, reports, and backups**, all without requiring internet connectivity.

The app is **production-ready**, built with a **clean, scalable architecture**, and supports **Excel export/import, printing, and PDF billing**.

---

## ✨ Key Features

### 📊 Dashboard
- Business overview at a glance
- Stock summary
- Purchase & sales insights
- Quick navigation to core modules

**Screen:** `Dashboard.kt`

---

### 🧾 Billing System
- Sales billing with item-wise calculation
- Purchase billing for stock entry
- Automatic total, tax, and balance calculation
- Professional bill generation
- Print bills or share as PDF

**Screens:**
- `Billing.kt`
- `PurchaseBilling.kt`

---

### 👥 Customer Management
- Add, edit, delete customers
- View complete customer transaction history
- Customer-wise sales reports
- Balance tracking (credit / debit)

**Screens:**
- `CustomerManagementConsole.kt`
- `SellHistory.kt`

---

### 🚚 Supplier Management
- Manage suppliers and vendors
- Supplier-wise purchase history
- Track outstanding balances

**Screen:** `SuppliersManagementConsole.kt`

---

### 📦 Stock Management
- Add, update, and delete items
- Real-time stock updates after purchase/sale
- Low stock tracking
- Item-wise stock history

**Screen:** `StockManagementConsole.kt`

---

### 🛒 Purchase Management
- Record purchase bills
- Automatic stock increment
- Purchase history and reports

**Screens:**
- `PurchaseBilling.kt`
- `PurchaseHistory.kt`

---

### 💰 Sales History
- Complete sales records
- Date-wise, customer-wise filtering
- Reprint or share past bills

**Screen:** `SellHistory.kt`

---

### 📁 Excel Export & Restore
- Export full database to **Excel**
- Restore app data from Excel backup
- Safe offline backup & recovery

---

### 🖨 Printing & Sharing
- Print bills using supported printers
- Share bills and reports as PDF
- Reprint old invoices anytime

---

### 👤 User & Business Profile
- Store shop/business information
- User profile management
- Display business info on bills

**Screens:**
- `UserInfo.kt`
- `LandingScreen.kt`

---

### 🌙 UI & Experience
- Light Mode & Dark Mode
- Clean, professional UI
- Optimized for daily business operations

---

## 🧱 Architecture

IMS follows **Clean Architecture**, ensuring long-term maintainability and scalability.

presentation/
domain/
data/

### Architecture Benefits:
- Clear separation of concerns
- Easy testing and feature expansion
- Stable and production-safe codebase

---

## 🛠 Tech Stack

- **Language:** Kotlin
- **Architecture:** Clean Architecture (MVVM)
- **Database:** Room (SQLite)
- **Offline First:** Yes
- **Excel Export/Import:** Supported
- **PDF Generation:** Supported
- **Printing:** Thermal & standard printers
- **UI:** Material Design
- **Themes:** Light & Dark

---

## 📦 Core Modules Summary

| Module | Description |
|------|------------|
| Dashboard | Business overview |
| Billing | Sales billing |
| Purchase Billing | Stock purchase |
| Customers | Customer management |
| Suppliers | Supplier management |
| Stock | Inventory control |
| Sales History | Sales reports |
| Purchase History | Purchase tracking |
| Backup | Excel export & restore |
| Printing | Bills & reports |

---

## 🔐 Data Safety & Reliability

- Fully offline
- No server dependency
- Local Room database
- Manual backup & restore
- Safe for long-term business usage

---

## 🚀 Production Ready

- Real-world tested flows
- Clean, readable code
- Optimized performance
- Suitable for Play Store deployment
- Designed for daily commercial use

---

## 👨‍💻 Author
**Madan Raj Upadhyay**  
AI and Android Developer  

---

## ⭐ Support
If this project helped you, please consider giving it a ⭐ on GitHub.
