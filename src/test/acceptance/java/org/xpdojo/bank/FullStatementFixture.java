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

import java.io.IOException;
import java.io.StringWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toList;
import static org.xpdojo.bank.Account.emptyAccount;
import static org.xpdojo.bank.Money.amountOf;

public class FullStatementFixture {

	private final List<Transaction> executed = new ArrayList<>();

	public List<Transaction> addTransaction(Instant dateTime, String direction, String amount) {
		executed.add(new Transaction(dateTime, direction, amount));
		return executed;
	}

	public Account applyTransactionsToAccount(List<Transaction> transactions) {
		Account account = emptyAccount(new TickingClock(timesOf(transactions)));

		transactions.forEach(transaction -> {
			if (transaction.direction.equals("Deposit"))
				account.deposit(amountOf(Long.parseLong(transaction.amount)));
			else if (transaction.direction.equals("Withdraw"))
				account.withdraw(amountOf(Long.parseLong(transaction.amount)));
			else
				throw new RuntimeException(transaction.direction + " not recognised");
		});
		return account;
	}

	public String statementIncludes(List<Transaction> transactions, Account account) throws IOException {
		String statement = getActualStatement(account);
		List<Boolean> found = transactions.stream().map(isTransactionFoundIn(statement)).collect(toList());

		if (found.contains(false))
			return "doesn't include all transaction details";
		else
			return "includes all transactions";
	}

	public String statementIncludesBalance(Account account) throws IOException {
		String statement = getActualStatement(account);
		if (statement.contains(account.balance().toString()))
			return "shows the current balance";
		return "does not include the current balance";
	}

	public String getActualStatement(Account account) throws IOException {
		return account.writeStatement(new FullStatement(), new StringWriter());
	}

	private static List<Instant> timesOf(List<Transaction> transactions) {
		return transactions.stream().map(transaction -> transaction.dateTime).collect(toList());
	}

	private static Function<Transaction, Boolean> isTransactionFoundIn(String line) {
		return transaction -> {
			String formattedDate = ofPattern("dd/MM/yyyy").format(ofInstant(transaction.dateTime, UTC));
			String formattedTime = ofPattern("HH:mm").format(ofInstant(transaction.dateTime, UTC));
			return line.contains(formattedDate + " " + formattedTime + " " + transaction.direction + " " + transaction.amount);
		};
	}

	static class Transaction {

		private final Instant dateTime;
		private final String direction;
		private final String amount;

		public Transaction(Instant dateTime, String direction, String amount) {
			this.dateTime = dateTime;
			this.direction = direction;
			this.amount = amount;
		}
	}

	static class TickingClock implements Clock {

		private final List<Instant> instants;
		private int index = 0;

		TickingClock(List<Instant> instants) {
			this.instants = instants;
			this.instants.add(0, Instant.now()); // to take care of the initial deposit on account creation
		}

		@Override
		public Instant now() {
			if (index == instants.size())
				return Instant.now();
			return instants.get(index++);
		}
	}
}
