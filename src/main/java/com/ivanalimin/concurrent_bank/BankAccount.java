package com.ivanalimin.concurrent_bank;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class BankAccount {
    private final UUID accountNumber;
    //https://stackoverflow.com/questions/8567596/how-to-make-updating-bigdecimal-within-concurrenthashmap-thread-safe
    private final AtomicReference<BigDecimal> balance;

    public BankAccount(BigDecimal initialBalance) {
        this.accountNumber = UUID.randomUUID();
        this.balance = new AtomicReference<>(initialBalance);
    }

    public void deposit(BigDecimal amount) {
        balance.updateAndGet(balance -> balance.add(amount));
    }

    public void withdraw(BigDecimal amount) {
        balance.updateAndGet(balance -> {
            if (amount.compareTo(balance) <= 0) {
                return balance.subtract(amount);
            } else {
                throw new IllegalArgumentException("Insufficient funds");
            }
        });
    }

    public BigDecimal getBalance() {
        return balance.get();
    }

    public UUID getAccountNumber() {
        return accountNumber;
    }
}
