# Midnight Expense Vault

A privacy-focused Android DApp for tracking personal expenses on the Midnight network.

Midnight Expense Vault allows users to record expenses across three categories — Food, Travel, and Education — using a Compact smart contract deployed on Midnight PreProd.

The application was built as an original Android DApp using the Kuira Android SDK and performs Compact contract deployment and circuit execution directly from the Android device.

---

## Built on Midnight

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

## Features

### Expense Tracking

The application provides three expense categories:

- 🍔 Food
- ✈️ Travel
- 🎓 Education

Each category is backed by its own ledger value in the Compact smart contract.

### Midnight Integration

- Midnight PreProd network
- Embedded wallet
- Dust registration
- On-device proving
- Compact smart contract deployment
- On-chain circuit execution
- Persistent contract address storage

---

## How It Works

The Compact contract maintains three expense counters:

```text
foodExpenses
travelExpenses
educationExpenses