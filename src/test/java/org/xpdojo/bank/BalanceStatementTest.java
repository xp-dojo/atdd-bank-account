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
		assertThat(writer.toString(), containsString("03/02/19 10:15"));
	}
	
	@Test
	void exceptionsArePropogatedFromTheWriter() {
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