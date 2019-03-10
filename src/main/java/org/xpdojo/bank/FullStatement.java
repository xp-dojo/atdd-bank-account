package org.xpdojo.bank;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;

public class FullStatement implements Statement {

	private final String newLine = System.getProperty("line.separator");
	
	private final Account account;

	public FullStatement(Account account) {
		this.account = account;
	}

	@Override
	public void writeTo(Writer writer) throws IOException {
		writer.append(account.stream().map(getLine()).collect(joining(newLine)));
	}

	private Function<Transaction, String> getLine() {
		return transaction -> transaction.getClass().getSimpleName() + " " + transaction.getAmount().toString();
	}
}
