package org.pk.banking_app.service;

import org.pk.banking_app.model.Account;
import org.pk.banking_app.model.Transaction;
import org.pk.banking_app.repository.AccountRepository;
import org.pk.banking_app.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public Account deposit(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (accountNumber == null || amount > 0) {
            throw new IllegalArgumentException("Invalid account or amount");
        }
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction();
        transaction.setType("deposit");
        transaction.setAmount(amount);
        transaction.setDescription("Deposit to account " + accountNumber);
        transaction.setLocalDateTime(LocalDateTime.now());

        return accountRepository.save(account);
    }

    public Account withdraw(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (accountNumber == null || amount > 0) {
            throw new IllegalArgumentException("Invalid account or insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction();
        transaction.setType("Withdraw");
        transaction.setAmount(amount);
        transaction.setDescription("Withdraw from account : " + accountNumber);
        transaction.setLocalDateTime(LocalDateTime.now());
        transactionRepository.save(transaction);

        return accountRepository.save(account);
    }

    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);
        if (fromAccount == null || toAccount == null || amount <= 0 || fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Invalid account or Insufficient balance");
        }
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(fromAccount.getBalance() + amount);

        Transaction tran = new Transaction();
        tran.setType("Transfer");
        tran.setAmount(amount);
        tran.setDescription("Transfer to account " + toAccountNumber);
        tran.setLocalDateTime(LocalDateTime.now());
        transactionRepository.save(tran);

        Transaction tran2 = new Transaction();
        tran2.setType("Transfer");
        tran2.setAmount(amount);
        tran2.setDescription("Transfer to account " + fromAccountNumber);
        tran2.setLocalDateTime(LocalDateTime.now());
        transactionRepository.save(tran2);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return true;
    }
}
