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
        //https://ru.stackoverflow.com/questions/1314103/%D0%97%D0%B0%D1%87%D0%B5%D0%BC-%D0%BD%D1%83%D0%B6%D0%BD%D1%8B-%D0%B2%D0%BB%D0%BE%D0%B6%D0%B5%D0%BD%D0%BD%D1%8B%D0%B5-%D0%B1%D0%BB%D0%BE%D0%BA%D0%B8-%D1%81%D0%B8%D0%BD%D1%85%D1%80%D0%BE%D0%BD%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D0%B8
        Object firstLock, secondLock;
        if (fromAccount.getAccountNumber().compareTo(toAccount.getAccountNumber()) < 0) {
            firstLock = fromAccount;
            secondLock = toAccount;
        } else {
            firstLock = toAccount;
            secondLock = fromAccount;
        }
        synchronized (firstLock) {
            synchronized (secondLock) {
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