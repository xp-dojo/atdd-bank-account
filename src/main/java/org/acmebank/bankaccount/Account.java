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

    public Transfer transfer(Money amount) {
        return new Transfer(amount, this);
    }

    public static class Transfer {
        private Money amount;
        private Account sender;

        private Transfer(Money amount, Account sender) {
            this.amount = amount;
            this.sender = sender;
        }

        public void to(Account receiver) {
            Result withdrawal = sender.withdraw(amount);
            if (withdrawal.succeeded()) {
                receiver.deposit(amount);
            }
        }
    }

    private Account(Money balance) {
        this.balance = balance;
    }

    static Account accountWithBalance(Money balance) {
        return new Account(balance);
    }
}
