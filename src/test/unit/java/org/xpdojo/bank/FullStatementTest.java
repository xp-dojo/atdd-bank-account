package org.xpdojo.bank;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class FullStatementTest {

	private final String newLine = System.getProperty("line.separator");

	@Test
	void fullStatement() throws IOException {
		Account account = Account.emptyAccount();
		account.deposit(Money.amountOf(10));
		account.deposit(Money.amountOf(20));
		account.withdraw(Money.amountOf(15));

		FullStatement statement = new FullStatement(account);
		Writer writer = new StringWriter();
		statement.writeTo(writer);

		String expected =
			"Deposit .00"   + newLine +
			"Deposit 10.00" + newLine +
			"Deposit 20.00" + newLine +
			"Withdraw 15.00";
		assertThat(writer.toString(), is(expected));
	}
}