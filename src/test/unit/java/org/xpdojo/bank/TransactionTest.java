package org.xpdojo.bank;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xpdojo.bank.Money.*;
import static org.xpdojo.bank.Transaction.*;
import static org.xpdojo.bank.Transaction.Deposit.*;
import static org.xpdojo.bank.Transaction.Withdraw.*;

class TransactionTest {

	@Test
	void addDepositToZeroAmount() {
		Deposit initialDeposit = deposit(Money.ZERO);
		Transaction result = deposit(amountOf(10)).against(initialDeposit);
		assertThat(result.getAmount(), is(amountOf(10)));
	}

	@Test
	void addWithdrawToCauseAnAmountToBeZeroed() {
		Deposit initialDeposit = deposit(amountOf(10));
		Transaction result = withdraw(amountOf(10)).against(initialDeposit);
		assertThat(result.getAmount(), is(Money.ZERO));
	}
	
	@Test
	void applyAMixOfTransactions() {
		Transaction result = deposit(amountOf(5)).against(
								withdraw(amountOf(10)).against(
									deposit(amountOf(10))));
		assertThat(result.getAmount(), is(amountOf(5)));
	}
	
	@Test
	void canSumDeposits() {
		assertThat(deposit(amountOf(10)).against(deposit(amountOf(20))).getAmount(), is(amountOf(30)));
		assertThat(deposit(amountOf(20)).against(deposit(amountOf(10))).getAmount(), is(amountOf(30)));
	}

	@Test
	void withdrawMoreThanTheInitialDeposit() {
		assertThat(withdraw(amountOf(5)).against(deposit(amountOf(2))).getAmount(), is(amountOf(-3)));
	}
	
	@Test
	void canSumMoreComplexSeriesOfDeposits() {
		Deposit initialDeposit = deposit(amountOf(2));
		Transaction balance =
			withdraw(amountOf(5)).against(
				deposit(amountOf(20)).against(
					deposit(amountOf(10)).against(
						initialDeposit
					)
				));
		assertThat(balance.getAmount(), is(amountOf(27)));
	}

	@Test
	void canFoldOverReducingToCurrentBalance() {
		List<Transaction> transactions = asList(
			deposit(amountOf(10)),
			deposit(amountOf(20)),
			withdraw(amountOf(5))
		);
		Optional<Transaction> reduce = transactions.stream().reduce((a, b) -> b.against(a));
		assertThat(reduce.get().getAmount(), is(amountOf(25)));
	}

}