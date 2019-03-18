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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xpdojo.bank.Account.emptyAccount;
import static org.xpdojo.bank.Money.amountOf;

class FullStatementTest {

	private static final String NEW_LINE = System.getProperty("line.separator");
	
	@Test
	void aFullStatementShouldIncludeAllTransactionsInOrderWithEachOnItsOwnRow() throws IOException {
		Account account = emptyAccount(new IncrementingClock(Instant.parse("2019-02-23T10:15:00Z")));
		account.deposit(amountOf(10));
		account.deposit(amountOf(20));
		account.withdraw(amountOf(15));

		FullStatement statement = new FullStatement();
		Writer writer = new StringWriter();
		statement.write(account, writer);

		String expected =
			"23/02/2019 10:15 Deposit .00"   + NEW_LINE +
			"23/02/2019 11:15 Deposit 10.00" + NEW_LINE +
			"23/02/2019 12:15 Deposit 20.00" + NEW_LINE +
			"23/02/2019 13:15 Withdraw 15.00";
		assertThat(writer.toString(), is(expected));
	}
	
	static class IncrementingClock implements Clock {

		private final Instant initial;
		private final int anHourInSeconds = 3600;
		
		private long increment = 0;

		IncrementingClock(Instant initial) {
			this.initial = initial;
		}

		@Override
		public Instant now() {
			Instant now = Instant.ofEpochSecond(initial.getEpochSecond() + increment);
			increment += anHourInSeconds;
			return now;
		}
	}
}