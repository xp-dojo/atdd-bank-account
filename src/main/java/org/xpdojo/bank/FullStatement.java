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
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.joining;

public class FullStatement implements Statement {

	private static final String NEW_LINE = System.getProperty("line.separator");

	@Override
	public void write(Account account, Writer writer) throws IOException {
		List<StatementLine> lines = runningBalance(account.transactions());
		writer
			.append(lines.stream().map(toStatement()).collect(joining(NEW_LINE)))
			.append("\n\nbalance: ")
			.append(account.balance().toString());
	}

	private List<StatementLine> runningBalance(Stream<Transaction> transactions) {
		List<StatementLine> accumulator = new ArrayList<>();
		transactions.forEach(transaction -> {
			if (accumulator.isEmpty())
				accumulator.add(new StatementLine(transaction, transaction.getAmount()));
			else {
				StatementLine previous = accumulator.get(accumulator.size() - 1);
				accumulator.add(new StatementLine(transaction, transaction.against(previous.balance)));
			}
		});
		return accumulator;
	}

	private Function<StatementLine, String> toStatement() {
		return line -> {
			LocalDateTime dateTime = ofInstant(line.transaction.getDateTime(), UTC);
			String date = ofPattern("dd/MM/yyyy").format(dateTime);
			String time = ofPattern("HH:mm").format(dateTime);
			String direction = line.transaction.getClass().getSimpleName();
			String amount = line.transaction.getAmount().toString();
			String balance = line.balance.toString();
			return date + " " + time + " " + direction + " " + amount + " " + balance;
		};
	}

	private class StatementLine {
		private final Transaction transaction;
		private final Money balance;

		public StatementLine(Transaction transaction, Money balance) {
			this.transaction = transaction;
			this.balance = balance;
		}
	}
}
