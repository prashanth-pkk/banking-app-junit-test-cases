package org.pk.banking_app.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.pk.banking_app.model.Account;
import org.pk.banking_app.service.AccountService;
import org.pk.banking_app.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private String getUsernameFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return jwtTokenUtil.getUsernameFromToken(token.replace("Bearer", ""));
    }

    @PostMapping("/{accountNumber}/deposit")
    public Account deposit(@PathVariable String accountNumber, @RequestParam double amount, HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        return accountService.deposit(accountNumber, amount);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public Account withdraw(@PathVariable String accountNumber, @RequestParam double amount, HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        return accountService.withdraw(accountNumber, amount);
    }

    @PostMapping("/transfer")
    public boolean transfer(@RequestParam String fromAccount, @RequestParam String toAccount, @RequestParam double amount, HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        return accountService.transfer(fromAccount, toAccount, amount);
    }
}
