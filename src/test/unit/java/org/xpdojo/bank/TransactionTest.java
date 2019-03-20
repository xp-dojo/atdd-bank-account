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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.ofEpochSecond;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.xpdojo.bank.Money.ZERO;
import static org.xpdojo.bank.Money.amountOf;
import static org.xpdojo.bank.Transaction.Deposit.deposit;
import static org.xpdojo.bank.Transaction.Withdraw.withdraw;

class TransactionTest {

	private final Instant now = ofEpochSecond(1);

	@Test
	void depositShouldAddDepositAmount() {
		assertThat(deposit(amountOf(10), now).against(ZERO), is(amountOf(10)));
	}

	@Test
	void withdrawalShouldSubtractWithdrawalAmount() {
		assertThat(withdraw(amountOf(10), now).against(amountOf(10)), is(ZERO));
	}

	@ParameterizedTest(name = "deposit of {0} and {1} should sum to {2}")
	@CsvSource({
		"10, 20, 30",
		"20, 10, 30"
	})
	void twoDepositsSumToTotal(long amount1, long amount2, long expectedSum) {
		assertThat(deposit(amountOf(amount1), now).against(amountOf(amount2)), is(amountOf(expectedSum)));
	}

	@Test
	void withdrawalOfMoreThanInitialDepositShouldGiveNegativeTotal() {
		assertThat(withdraw(amountOf(5), now).against(amountOf(2)), is(amountOf(-3)));
	}

	@Test
	void reducingAStreamOfTransactionsShouldGiveCurrentBalance() {
		List<Transaction> transactions = asList(
			deposit(amountOf(10), ofEpochSecond(1)),
			deposit(amountOf(20), ofEpochSecond(2)),
			withdraw(amountOf(5), ofEpochSecond(3)),
			withdraw(amountOf(5), ofEpochSecond(4)),
			deposit(amountOf(3), ofEpochSecond(5))
		);

		Money total = transactions.stream().reduce(ZERO, (money, transaction) -> transaction.against(money), Money::plus);
		assertThat(total, is(amountOf(23)));
	}

	@Test
	void reducingAStreamOfTransactionsShouldGiveCurrentBalanceWhenStartingFromNegativeBalance() {
		List<Transaction> transactions = asList(
			deposit(amountOf(10), ofEpochSecond(1)),
			deposit(amountOf(20), ofEpochSecond(2)),
			withdraw(amountOf(5), ofEpochSecond(3)),
			withdraw(amountOf(5), ofEpochSecond(4)),
			deposit(amountOf(3), ofEpochSecond(5))
		);

		Money total = transactions.stream().reduce(amountOf(-10), (money, transaction) -> transaction.against(money), Money::plus);
		assertThat(total, is(amountOf(13)));
	}

	@Test
	void basicEqualityChecks() {
		Instant now = Instant.now();
		assertThat(deposit(amountOf(100), now), is(not(withdraw(amountOf(100), now))));
		assertThat(deposit(amountOf(100), now), is(deposit(amountOf(100), now)));
		assertThat(withdraw(amountOf(100), now), is(withdraw(amountOf(100), now)));
	}

	@Test
	void runningBalance() {
		List<Transaction> transactions = asList(
			deposit(amountOf(10), ofEpochSecond(1)),
			deposit(amountOf(20), ofEpochSecond(2)),
			withdraw(amountOf(5), ofEpochSecond(3)),
			withdraw(amountOf(5), ofEpochSecond(4)),
			deposit(amountOf(3), ofEpochSecond(5))
		);

		assertThat(getRunningBalance(transactions), contains(
			amountOf(10),
			amountOf(30),
			amountOf(25),
			amountOf(20),
			amountOf(23)
		));
	}

	private List<Money> getRunningBalance(List<Transaction> transactions) {
		return transactions.stream().reduce(new ArrayList<>(), (accumulator, transaction) -> {
					if (accumulator.isEmpty()) {
						accumulator.add(transaction.getAmount());
					} else {
						accumulator.add(transaction.against(accumulator.get(accumulator.size() - 1)));
					}
					return accumulator;
				}, (a, b) -> a);
	}

}