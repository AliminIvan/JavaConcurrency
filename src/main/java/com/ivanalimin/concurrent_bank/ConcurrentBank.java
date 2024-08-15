package com.ivanalimin.concurrent_bank;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentBank {
    private final ConcurrentHashMap<UUID, BankAccount> accounts = new ConcurrentHashMap<>();

    public BankAccount createAccount(BigDecimal initialBalance) {
        BankAccount newAccount = new BankAccount(initialBalance);
        accounts.put(newAccount.getAccountNumber(), newAccount);
        return newAccount;
    }

    public void transfer(UUID from, UUID to, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        BankAccount fromAccount = accounts.get(from);
        BankAccount toAccount = accounts.get(to);
        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Invalid account number");
        }
        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.getBalance().compareTo(amount) >= 0) {
                    fromAccount.withdraw(amount);
                    toAccount.deposit(amount);
                } else {
                    throw new IllegalArgumentException("Insufficient funds");
                }
            }
        }
    }

    public BigDecimal getTotalBalance() {
        return accounts.values().stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}