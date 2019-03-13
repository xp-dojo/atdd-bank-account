/*
 *
 * Copyright (c) 2019 xp-dojo organisation and committers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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