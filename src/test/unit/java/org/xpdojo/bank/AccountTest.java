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
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.xpdojo.bank.Account.accountWithBalance;
import static org.xpdojo.bank.Account.emptyAccount;
import static org.xpdojo.bank.Money.ZERO;
import static org.xpdojo.bank.Money.amountOf;
import static org.xpdojo.bank.Transaction.Deposit.deposit;
import static org.xpdojo.bank.Transaction.Withdraw.withdraw;

class AccountTest {

	private static final Clock FIXED_CLOCK = () -> Instant.parse("2019-02-03T10:15:30Z");
	
	@Test
	void newAccountShouldHaveZeroBalance() {
		Account account = emptyAccount(FIXED_CLOCK);
		assertThat(account.balance(), is(ZERO));
	}

	@Test
	void depositToEmptyAccountShouldIncreaseTheBalance() {
		Account account = emptyAccount(FIXED_CLOCK);
		account.deposit(amountOf(10));
		assertThat(account.balance(), is(amountOf(10)));
	}

	@Test
	void depositToNonEmptyAccountShouldIncreaseTheBalance() {
		Account account = accountWithBalance(amountOf(10), FIXED_CLOCK);
		account.deposit(amountOf(20));
		assertThat(account.balance(), is(amountOf(30)));
	}

	@Test
	void withdrawalShouldDecreaseTheBalance() {
		Account account = accountWithBalance(amountOf(10), FIXED_CLOCK);
		account.withdraw(amountOf(10));
		assertThat(account.balance(), is(ZERO));
	}

	@Test
	void withdrawalShouldNotBeAppliedWhenItTakesAccountOverdrawn() {
		Account account = emptyAccount(FIXED_CLOCK);
		account.withdraw(amountOf(1));
		assertThat(account.balance(), is(ZERO));
	}

	@Test
	void transferShouldMoveMoneyFromOneAccountToAnother() {
		Account sender = accountWithBalance(amountOf(10), FIXED_CLOCK);
		Account receiver = emptyAccount(FIXED_CLOCK);

		sender.transfer(amountOf(10), receiver);

		assertThat(sender.balance(), is(ZERO));
		assertThat(receiver.balance(), is(amountOf(10)));
	}

	@Test
	void transferShouldNotBeAppliedWhenItTakesSendingAccountOverdrawn() {
		Account sender = emptyAccount(FIXED_CLOCK);
		Account receiver = emptyAccount(FIXED_CLOCK);

		sender.transfer(amountOf(1), receiver);

		assertThat(sender.balance(), is(ZERO));
		assertThat(receiver.balance(), is(ZERO));
	}

	@Test
	void statementShouldBeWrittenToSuppliedWriter() throws IOException {
		Statement statement = (account, writer) -> writer.append("STATEMENT GENERATED ").append(account.balance().toString());
		String actual = accountWithBalance(amountOf(2123), FIXED_CLOCK).writeStatement(statement, new StringWriter());
		assertThat(actual, is("STATEMENT GENERATED 2,123.00"));
	}
	
	@Test
	void emptyAccountIncludesAnInitialDeposit() {
		Account account = emptyAccount(FIXED_CLOCK);
		List<Transaction> transactions = account.transactions().collect(Collectors.toList());
		
		assertThat(transactions, contains(deposit(ZERO, FIXED_CLOCK.now())));
	}	
	
	@Test
	void transactionsShouldRecordTimeOfTransaction() {
		Account account = emptyAccount(FIXED_CLOCK);
		account.deposit(amountOf(100));
		account.withdraw(amountOf(60));
		List<Transaction> transactions = account.transactions().collect(Collectors.toList());
		
		assertThat(transactions, contains(deposit(ZERO, FIXED_CLOCK.now()), deposit(amountOf(100), FIXED_CLOCK.now()), withdraw(amountOf(60), FIXED_CLOCK.now())));
	}
}
