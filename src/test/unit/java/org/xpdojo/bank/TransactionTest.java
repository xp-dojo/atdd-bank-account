package org.xpdojo.bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.xpdojo.bank.Money.ZERO;
import static org.xpdojo.bank.Money.amountOf;
import static org.xpdojo.bank.Transaction.Deposit.deposit;
import static org.xpdojo.bank.Transaction.Withdraw.withdraw;

class TransactionTest {

	@Test
	void depositShouldAddDepositAmount() {
		assertThat(deposit(amountOf(10)).against(ZERO), is(amountOf(10)));
	}

	@Test
	void withdrawalShouldSubtractWithdrawalAmount() {
		assertThat(withdraw(amountOf(10)).against(amountOf(10)), is(ZERO));
	}

	@ParameterizedTest(name = "deposit of {0} and {1} should sum to {2}")
	@CsvSource({
		"10, 20, 30",
		"20, 10, 30"
	})
	void twoDepositsSumToTotal(long amount1, long amount2, long expectedSum) {
		assertThat(deposit(amountOf(amount1)).against(amountOf(amount2)), is(amountOf(expectedSum)));
	}

	@Test
	void withdrawalOfMoreThanInitialDepositShouldGiveNegativeTotal() {
		assertThat(withdraw(amountOf(5)).against(amountOf(2)), is(amountOf(-3)));
	}

	@Test
	void reducingAStreamOfTransactionsShouldGiveCurrentBalance() {
		List<Transaction> transactions = asList(
			deposit(amountOf(10)),
			deposit(amountOf(20)),
			withdraw(amountOf(5)),
			withdraw(amountOf(5)),
			deposit(amountOf(3))
		);

		Money total = transactions.stream().reduce(ZERO, (money, transaction) -> transaction.against(money), Money::plus);
		assertThat(total, is(amountOf(23)));
	}

	@Test
	void reducingAStreamOfTransactionsShouldGiveCurrentBalanceWhenStartingFromNegativeBalance() {
		List<Transaction> transactions = asList(
			deposit(amountOf(10)),
			deposit(amountOf(20)),
			withdraw(amountOf(5)),
			withdraw(amountOf(5)),
			deposit(amountOf(3))
		);

		Money total = transactions.stream().reduce(amountOf(-10), (money, transaction) -> transaction.against(money), Money::plus);
		assertThat(total, is(amountOf(13)));
	}

	@Test
	void basicEqualityChecks() {
		assertThat(deposit(amountOf(100)), is(not(withdraw(amountOf(100)))));
		assertThat(deposit(amountOf(100)), is(deposit(amountOf(100))));
		assertThat(withdraw(amountOf(100)), is(withdraw(amountOf(100))));
	}

}