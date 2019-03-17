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

import org.concordion.api.ConcordionResources;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.xpdojo.bank.Money.amountOf;

@RunWith(ConcordionRunner.class)
@ConcordionResources(value = {"../../../concordion.css"})
public class ViewStatement {

	private final Account account = Account.emptyAccount();
	private final List<Transaction> executed = new ArrayList<>();

	public List<Transaction> transaction(String dateTime, String direction, String amount) {
		executed.add(new Transaction(direction, amount));
		if (direction.equals("Deposit"))
			account.deposit(amountOf(Long.parseLong(amount)));
		else if (direction.equals("Withdraw"))
			account.withdraw(amountOf(Long.parseLong(amount)));
		else
			throw new RuntimeException(direction + " not recognised");
		return executed;
	}

	public String statementIncludes(List<Transaction> transactions) throws IOException {
		String statement = account.writeStatement(new FullStatement(), new StringWriter());
		List<Boolean> found = transactions.stream().map(transactionFound(statement)).collect(toList());

		if (found.contains(false))
			return "doesn't include all transactions";
		else
			return "includes all transactions";
	}

	private Function<Transaction, Boolean> transactionFound(String line) {
		return transaction -> line.contains(transaction.direction + " " + transaction.amount);
	}

	static class Transaction {

		private final String direction;
		private final String amount;

		public Transaction(String direction, String amount) {
			this.direction = direction;
			this.amount = amount;
		}
	}
}
