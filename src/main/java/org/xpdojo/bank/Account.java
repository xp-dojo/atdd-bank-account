package org.xpdojo.bank;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import static org.xpdojo.bank.Money.*;
import static org.xpdojo.bank.Result.failure;
import static org.xpdojo.bank.Result.success;
import static org.xpdojo.bank.Transaction.*;
import static org.xpdojo.bank.Transaction.Deposit;
import static org.xpdojo.bank.Transaction.Withdraw;

public class Account {

    private final List<Transaction> transactions = new ArrayList<>();
    
    public static Account emptyAccount() {
        return accountWithBalance(ZERO);
    }

    public Money balance() {
	 	return transactions.stream().reduce(sum()).orElse(Deposit.deposit(ZERO)).getAmount();
    }

	public void deposit(Money amount) {
        transactions.add(Deposit.deposit(amount));
    }

    public Result withdraw(Money amount) {
        if (balance().isLessThan(amount)) 
            return failure();
        
        transactions.add(Withdraw.withdraw(amount));
        return success();
    }

    public void transfer(Money amount, Account receiver) {
        Result withdrawal = withdraw(amount);
        if (withdrawal.succeeded()) {
            receiver.deposit(amount);
        }
    }
    
    public String generate(Statement statement, Writer writer) throws IOException {
        statement.writeTo(writer);
        return writer.toString();
    }
    
    public Stream<Transaction> stream() {
    	return transactions.stream();
	}

    
    private Account(Money balance) {
        deposit(balance);
    }

    static Account accountWithBalance(Money balance) {
        return new Account(balance);
    }

    private BinaryOperator<Transaction> sum() {
        return (a, b) -> new Identity(b.against(a).getAmount());
    }
}
