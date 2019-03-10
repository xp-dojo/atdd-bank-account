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
	void aBalanceStatementIncludesTheAccountsBalance() throws IOException {
		BalanceStatement statement = new BalanceStatement(accountWithBalance(amountOf(250)), () -> Instant.ofEpochMilli(0));
		Writer writer = new StringWriter();
		statement.writeTo(writer);
		assertThat(writer.toString(), containsString("250"));
	}

	@Test
	void aBalanceStatementIncludesTheCurrentDateNicelyFormatted() throws IOException {
		Clock now = () -> Instant.parse("2019-02-03T10:15:30Z");
		BalanceStatement statement = new BalanceStatement(accountWithBalance(ZERO), now);
		Writer writer = new StringWriter();
		statement.writeTo(writer);
		assertThat(writer.toString(), containsString("03/02/19"));
		assertThat(writer.toString(), containsString("10:15"));
	}
	
	@Test
	/* NB this is intentionally loose to assert against the main elements of a balance slip. 
	* More literal, full text like comparisons are done in the acceptance tests layer */
	void aBalanceStatementIncludesPreambleAndAdditionalText() throws IOException {
		Clock now = () -> Instant.parse("2019-02-03T10:15:30Z");
		BalanceStatement statement = new BalanceStatement(accountWithBalance(amountOf(1000)), now);
		Writer writer = new StringWriter();
		statement.writeTo(writer);
		
		String balanceSlip = writer.toString();
		assertThat(balanceSlip, containsString("XP DOJO BANK"));
		assertThat(balanceSlip, containsString("Terminal #            1003423"));
		assertThat(balanceSlip, containsString("Date                 03/02/19"));
		assertThat(balanceSlip, containsString("Time 24H                10:15"));
		assertThat(balanceSlip, containsString("Account           XXXXXXXXXXX"));
		assertThat(balanceSlip, containsString("Your current balance is:     " + '\n' + 
			                                   "                     1,000.00"));
		assertThat(balanceSlip, containsString("for great deals on loans"));
		assertThat(balanceSlip, containsString("visit https://xpdojo.org"));
	}
	
	@Test
	void exceptionsArePropagatedFromTheWriter() {
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