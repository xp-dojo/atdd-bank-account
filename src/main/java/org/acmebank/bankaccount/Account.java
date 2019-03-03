package org.acmebank.bankaccount;

import static org.acmebank.bankaccount.Money.ZERO;
import static org.acmebank.bankaccount.Result.failure;
import static org.acmebank.bankaccount.Result.success;

public class Account {

    private Money balance;

    public static Account emptyAccount() {
        return accountWithBalance(ZERO);
    }

    public Money balance() {
        return balance;
    }

    public void deposit(Money amount) {
        balance = balance.plus(amount);
    }

    public Result withdraw(Money amount) {
        if (balance.isLessThan(amount)) {
            return failure();
        }
        balance = balance.minus(amount);
        return success();
    }

    public void transfer(Money amount, Account receiver) {
        Result withdrawal = withdraw(amount);
        if (withdrawal.succeeded()) {
            receiver.deposit(amount);
        }
    }

    private Account(Money balance) {
        this.balance = balance;
    }

    static Account accountWithBalance(Money balance) {
        return new Account(balance);
    }
}
