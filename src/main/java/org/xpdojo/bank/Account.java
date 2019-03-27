package org.xpdojo.bank;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.xpdojo.bank.Money.ZERO;
import static org.xpdojo.bank.Result.failure;
import static org.xpdojo.bank.Result.success;
import static org.xpdojo.bank.Transaction.Deposit.depositOf;
import static org.xpdojo.bank.Transaction.Withdraw.withdrawalOf;

public class Account {

    private final List<Transaction> transactions = new ArrayList<>();

    public static Account accountWithBalance(Money balance) {
        return new Account(balance);
    }

    public static Account emptyAccount() {
        return accountWithBalance(ZERO);
    }

    private Account(Money balance) {
        deposit(balance);
    }

    public Money balance() {
        Money total = ZERO;
        for (Transaction transaction : transactions) {
            total = transaction.against(total);
        }
        return total;
    }

	public void deposit(Money amount) {
        transactions.add(depositOf(amount));
    }

    public Result withdraw(Money amount) {
        if (balance().isLessThan(amount))
            return failure();

        transactions.add(withdrawalOf(amount));
        return success();
    }

    public void transfer(Money amount, Account receiver) {
        Result withdrawal = withdraw(amount);
        if (withdrawal.succeeded()) {
            receiver.deposit(amount);
        }
    }

    public String writeStatement(Statement statement, Writer writer) throws IOException {
        statement.write(this, writer);
        return writer.toString();
    }

    public Stream<Transaction> transactions() {
    	return transactions.stream();
	}

}
