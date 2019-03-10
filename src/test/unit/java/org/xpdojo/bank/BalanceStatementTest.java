package org.xpdojo.bank;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.xpdojo.bank.Account.accountWithBalance;
import static org.xpdojo.bank.Money.ZERO;
import static org.xpdojo.bank.Money.amountOf;

class BalanceStatementTest {

	@Test
	void aBalanceStatementShouldIncludeTheAccountsBalance() throws IOException {
		BalanceStatement statement = new BalanceStatement(accountWithBalance(amountOf(250)), () -> Instant.ofEpochMilli(0));
		String writtenStatement = writeBalanceStatement(statement);
		assertThat(writtenStatement, containsString("250"));
	}

	@Test
	void aBalanceStatementShouldIncludeTheCurrentDateNicelyFormatted() throws IOException {
		Clock now = () -> Instant.parse("2019-02-03T10:15:30Z");
		BalanceStatement statement = new BalanceStatement(accountWithBalance(ZERO), now);

		String writtenStatement = writeBalanceStatement(statement);

		assertThat(writtenStatement, containsString("03/02/19"));
		assertThat(writtenStatement, containsString("10:15"));
	}
	
	@Test
	/* NB this is intentionally loose to assert against the main elements of a balance slip. 
	* More literal, full text like comparisons are done in the acceptance tests layer */
	void aBalanceStatementShouldIncludePreambleAndAdditionalText() throws IOException {
		Clock now = () -> Instant.parse("2019-02-03T10:15:30Z");
		BalanceStatement statement = new BalanceStatement(accountWithBalance(amountOf(1000)), now);

		String writtenStatement = writeBalanceStatement(statement);

		assertThat(writtenStatement, containsString("XP DOJO BANK"));
		assertThat(writtenStatement, containsString("Terminal #            1003423"));
		assertThat(writtenStatement, containsString("Date                 03/02/19"));
		assertThat(writtenStatement, containsString("Time 24H                10:15"));
		assertThat(writtenStatement, containsString("Account           XXXXXXXXXXX"));
		assertThat(writtenStatement, containsString("Your current balance is:     "));
		assertThat(writtenStatement, containsString("                     1,000.00"));
		assertThat(writtenStatement, containsString("for great deals on loans"));
		assertThat(writtenStatement, containsString("visit https://xpdojo.org"));
	}

	private String writeBalanceStatement(BalanceStatement statement) throws IOException {
		Writer writer = new StringWriter();
		statement.writeTo(writer);
		return writer.toString();
	}

	@Test
	void exceptionsShouldBePropagatedFromTheWriter() {
		Writer erroringWriter = new ErroringWriter();
		BalanceStatement statement = new BalanceStatement(accountWithBalance(ZERO), () -> Instant.ofEpochMilli(0));
		assertThrows(IOException.class, () -> statement.writeTo(erroringWriter));
	}

	private static class ErroringWriter extends Writer {
		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			throw new IOException("writing");
		}

		@Override
		public void flush() throws IOException {
			throw new IOException("flush");

		}

		@Override
		public void close() throws IOException {
			throw new IOException("close");
		}
	}
}