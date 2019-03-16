package org.xpdojo.bank;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xpdojo.bank.Account.emptyAccount;
import static org.xpdojo.bank.Money.amountOf;

class FullStatementTest {

	private static final String NEW_LINE = System.getProperty("line.separator");

	@Test
	void aFullStatementShouldIncludeAllTransactionsInOrderWithEachOnItsOwnRow() throws IOException {
		Account account = emptyAccount();
		account.deposit(amountOf(10));
		account.deposit(amountOf(20));
		account.withdraw(amountOf(15));

		FullStatement statement = new FullStatement();
		Writer writer = new StringWriter();
		statement.write(account, writer);

		String expected =
			"Deposit .00"   + NEW_LINE +
			"Deposit 10.00" + NEW_LINE +
			"Deposit 20.00" + NEW_LINE +
			"Withdraw 15.00";
		assertThat(writer.toString(), is(expected));
	}
}