package com.ivanalimin.concurrent_bank;

import java.math.BigDecimal;

public class ConcurrentBankExample {
    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();

        // Создание счетов
        BankAccount account1 = bank.createAccount(BigDecimal.valueOf(1000.25));
        BankAccount account2 = bank.createAccount(BigDecimal.valueOf(500.75));

        // Перевод между счетами
        Thread transferThread1 = new Thread(() -> bank.transfer(account1.getAccountNumber(),
                account2.getAccountNumber(), BigDecimal.valueOf(200)));
        Thread transferThread2 = new Thread(() -> bank.transfer(account2.getAccountNumber(),
                account1.getAccountNumber(), BigDecimal.valueOf(100)));

        transferThread1.start();
        transferThread2.start();

        try {
            transferThread1.join();
            transferThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Вывод общего баланса
        System.out.println("Account1 balance = " + account1.getBalance());
        System.out.println("Account2 balance = " + account2.getBalance());
        System.out.println("Total balance: " + bank.getTotalBalance());
    }
}
