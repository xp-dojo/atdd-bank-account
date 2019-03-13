package org.xpdojo.bank;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;

public class FullStatement implements Statement {

	private static final String NEW_LINE = System.getProperty("line.separator");
	
	private final Account account;

	public FullStatement(Account account) {
		this.account = account;
	}

	@Override
	public void writeTo(Writer writer) throws IOException {
		writer.append(account.transactions().map(toStatementLine()).collect(joining(NEW_LINE)));
	}

	private Function<Transaction, String> toStatementLine() {
		return transaction -> transaction.getClass().getSimpleName() + " " + transaction.amount().toString();
	}
}
