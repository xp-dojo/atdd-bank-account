package org.xpdojo.bank;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.xpdojo.bank.Account.accountWithBalance;
import static org.xpdojo.bank.Account.emptyAccount;
import static org.xpdojo.bank.Money.ZERO;
import static org.xpdojo.bank.Money.amountOf;

class AccountTest {

	@Test
	void newAccountShouldHaveZeroBalance() {
		Account account = emptyAccount();
		assertThat(account.balance()).isEqualTo(ZERO);
	}

	@Test
	void depositToEmptyAccountShouldIncreaseTheBalance() {
		Account account = emptyAccount();
		account.deposit(amountOf(10));
		assertThat(account.balance()).isEqualTo(amountOf(10));
	}

	@Test
	void depositToNonEmptyAccountShouldIncreaseTheBalance() {
		Account account = accountWithBalance(amountOf(10));
		account.deposit(amountOf(20));
		assertThat(account.balance()).isEqualTo(amountOf(30));
	}

	@Test
	void withdrawalShouldDecreaseTheBalance() {
		Account account = accountWithBalance(amountOf(10));
		account.withdraw(amountOf(10));
		assertThat(account.balance()).isEqualTo(ZERO);
	}

	@Test
	void withdrawalShouldNotBeAppliedWhenItTakesAccountOverdrawn() {
		Account account = emptyAccount();
		account.withdraw(amountOf(1));
		assertThat(account.balance()).isEqualTo(ZERO);
	}

	@Test
	void moneyCanBeTransferredBetweenAccounts() {
		Account sender = accountWithBalance(amountOf(10));
		Account receiver = emptyAccount();

		sender.transfer(amountOf(10), receiver);

		assertThat(sender.balance()).isEqualTo(ZERO);
		assertThat(receiver.balance()).isEqualTo(amountOf(10));
	}

	@Test
	void moneyShouldNotBeTransferredWhenItTakesSendingAccountOverdrawn() {
		Account sender = emptyAccount();
		Account receiver = emptyAccount();

		sender.transfer(amountOf(1), receiver);

		assertThat(sender.balance()).isEqualTo(ZERO);
		assertThat(receiver.balance()).isEqualTo(ZERO);
	}

	@Test
	public void canRetrieveABalanceSlip() {
		Clock now = () -> Instant.ofEpochSecond(0);
		BalanceSlip slip = emptyAccount().getBalanceSlip(now);
		assertThat(slip).isEqualTo(new BalanceSlip(ZERO, now));
	}

}
