# Midnight Expense Vault

A privacy-focused Android DApp for tracking personal expenses on the Midnight network.

Midnight Expense Vault allows users to record expenses across three categories — Food, Travel, and Education — using a Compact smart contract deployed on Midnight PreProd.

The application was built as an original Android DApp using the Kuira Android SDK and performs Compact contract deployment and circuit execution directly from the Android device.

---

## 📱 Application Demo

<p align="center">
  <img
    src="docs/screenshots/expense-vault-deployed.jpg"
    alt="Midnight Expense Vault Android DApp"
    width="320"
  />
</p>

<p align="center">
  <b>Midnight Expense Vault running on Midnight PreProd</b>
</p>

---

## 🌙 Built on Midnight

This project was built on the **Midnight network** using the **Kuira Android SDK** and **Compact smart contracts**.

The application demonstrates:

- Embedded Midnight wallet integration
- Midnight PreProd network connectivity
- On-device Compact contract deployment
- On-device circuit execution
- Dust registration and wallet synchronization
- Reading on-chain ledger state
- Android-native DApp development with Kotlin and Jetpack Compose

---

## ✨ Features

### Expense Tracking

The application provides three expense categories:

- 🍔 Food
- ✈️ Travel
- 🎓 Education

Each category is backed by its own ledger value in the Compact smart contract.

### Midnight Integration

- Midnight PreProd network
- Embedded Midnight wallet
- Dust registration
- On-device proving
- Compact smart contract deployment
- On-chain circuit execution
- Persistent contract address storage

---

## ⚙️ How It Works

The Compact contract maintains three expense counters:

```text
foodExpenses
travelExpenses
educationExpenses
```

The Android application exposes three circuits:

```text
addFoodExpense()
addTravelExpense()
addEducationExpense()
```

When a user adds an expense, the corresponding circuit is executed on Midnight and the application's ledger state is refreshed.

For example:

```text
Food expenses
0 → 1

Total expenses
0 → 1
```

This demonstrates a real contract interaction after deployment.

---

## 🧩 Smart Contract

The Compact contract is located at:

```text
contract/src/expense_vault.compact
```

### Ledger State

```text
foodExpenses
travelExpenses
educationExpenses
```

### Circuits

```text
addFoodExpense()
addTravelExpense()
addEducationExpense()
```

---

## 🏗️ Architecture

```text
┌──────────────────────────────┐
│      Android Application     │
│      Kotlin + Compose        │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│       Kuira Android SDK      │
│   Embedded Midnight Wallet   │
└──────────────┬───────────────┘
               │
               │ Deploy / Call
               ▼
┌──────────────────────────────┐
│       Midnight PreProd       │
│                              │
│   Expense Vault Contract     │
│                              │
│  foodExpenses                │
│  travelExpenses              │
│  educationExpenses           │
└──────────────────────────────┘
```

---

## 🌐 Network

**Network:** Midnight PreProd

**Proving:** On-device

The application was deployed and tested on the Midnight PreProd network.

---

## 📜 Deployed Contract

The Expense Vault Compact contract was deployed directly from the Android application.

**Network:** Midnight PreProd

**Contract Address:**

```text
ec36717cf002dfe44d61253da7e102128ca91c64d60eba3ccdf017c7fbf70a92
```

---

## ⛓️ On-Chain Interaction

After deploying the contract, the application successfully executed the:

```text
addFoodExpense()
```

circuit from the Android device.

The resulting ledger state was:

```text
Total Expenses: 1

Food:       1
Travel:     0
Education:  0
```

The change from `0` to `1` demonstrates a successful on-chain circuit interaction beyond contract deployment.

---

## 📸 Proof of Deployment and On-Chain Interaction

### Wallet and Dust Registration

The embedded wallet was successfully synchronized on Midnight PreProd and Dust was registered before deploying the contract.

The wallet panel shows:

- Network: Midnight PreProd
- Proving: On-device
- NIGHT balance: 5,000
- Dust balance: 8,981.72
- Dust registration completed successfully

<p align="center">
  <img
    src="docs/screenshots/wallet-dust-registered.jpg"
    alt="Midnight wallet synced and Dust registered"
    width="320"
  />
</p>

---

### Contract Deployment and Circuit Execution

The Expense Vault Compact contract was deployed from the Android DApp on Midnight PreProd.

The application then executed the `addFoodExpense()` circuit successfully.

The resulting application state shows:

- Total Expenses: 1
- Food: 1
- Travel: 0
- Education: 0

<p align="center">
  <img
    src="docs/screenshots/expense-vault-deployed.jpg"
    alt="Midnight Expense Vault deployed contract and successful circuit call"
    width="320"
  />
</p>

---

## 🛠️ Technology Stack

### Android

- Kotlin
- Jetpack Compose
- Android SDK
- Hilt
- Gradle

### Midnight / Web3

- Midnight
- Compact
- Kuira Android SDK
- Midnight PreProd
- On-device proving
- Embedded Midnight wallet
- Dust

---

## 📁 Project Structure

```text
midnight-expense-vault-dapp/
│
├── app/
│   └── src/
│       └── main/
│           └── java/
│               └── ...
│
├── contract/
│   └── src/
│       ├── expense_vault.compact
│       └── managed/
│           └── expense_vault/
│
├── docs/
│   └── screenshots/
│       ├── wallet-dust-registered.jpg
│       └── expense-vault-deployed.jpg
│
├── gradle/
│
├── README.md
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🚀 Building the Project

### Requirements

- Android Studio
- JDK 17
- Android SDK
- Android device or emulator
- Internet connection
- Midnight PreProd connectivity

### Build

Clone the repository and run:

```powershell
.\gradlew.bat :app:assembleDebug
```

The generated debug APK will be located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

---

## 📱 Running the DApp

1. Build the Android application.
2. Install the APK on an Android device.
3. Open Midnight Expense Vault.
4. Select the **Midnight PreProd** network.
5. Configure the embedded wallet.
6. Fund the wallet with NIGHT on PreProd.
7. Register Dust.
8. Wait for wallet synchronization to complete.
9. Deploy the Expense Vault Compact contract.
10. Execute an expense circuit such as `addFoodExpense()`.
11. Observe the updated ledger state in the application.

---

## 🔄 Demonstration Flow

```text
Android DApp
     │
     ▼
Embedded Midnight Wallet
     │
     ▼
Midnight PreProd
     │
     ▼
Register Dust
     │
     ▼
Deploy Compact Contract
     │
     ▼
Expense Vault Contract
     │
     ▼
addFoodExpense()
     │
     ▼
On-chain Ledger Update
     │
     ▼
Food: 0 → 1
Total: 0 → 1
```

---

## 🏆 Proof of Work

This project demonstrates:

- An original Android DApp
- Integration with the Kuira Android SDK
- A Compact smart contract
- Deployment to Midnight PreProd
- Dust registration
- On-device proving
- A real post-deployment circuit call
- Reading the resulting ledger state from the Android application

The screenshots above provide visual evidence of the wallet synchronization, Dust registration, contract deployment, and successful expense circuit execution.

---

## 🙏 Attribution

This project was built on the **Midnight network** using the **Kuira Android SDK** and **Compact**.

Midnight provides privacy-focused blockchain infrastructure for building applications with programmable privacy and zero-knowledge technology.

---

## 📄 License

This project is provided for educational and demonstration purposes.
