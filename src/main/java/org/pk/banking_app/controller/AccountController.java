package org.pk.banking_app.controller;

import org.pk.banking_app.model.Account;
import org.pk.banking_app.service.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountNumber}/deposit")
    public Account deposit(@PathVariable String accountNumber, @RequestParam double amount){
        return accountService.deposit(accountNumber, amount);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public Account withdraw(@PathVariable String accountNumber, @RequestParam double amount){
        return accountService.withdraw(accountNumber, amount);
    }

    @PostMapping("/transfer")
    public boolean transfer(@RequestParam String fromAccount, @RequestParam String toAccount, @RequestParam double amount){
        return accountService.transfer(fromAccount,toAccount,amount);
    }
}
