package org.xpdojo.bank;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xpdojo.bank.Account.accountWithBalance;
import static org.xpdojo.bank.Account.emptyAccount;
import static org.xpdojo.bank.Money.ZERO;
import static org.xpdojo.bank.Money.amountOf;

class AccountTest {

	@Test
	void newAccountShouldHaveZeroBalance() {
		Account account = emptyAccount();
		assertThat(account.balance(), is(ZERO));
	}

	@Test
	void depositToEmptyAccountShouldIncreaseTheBalance() {
		Account account = emptyAccount();
		account.deposit(amountOf(10));
		assertThat(account.balance(), is(amountOf(10)));
	}

	@Test
	void depositToNonEmptyAccountShouldIncreaseTheBalance() {
		Account account = accountWithBalance(amountOf(10));
		account.deposit(amountOf(20));
		assertThat(account.balance(), is(amountOf(30)));
	}

	@Test
	void withdrawalShouldDecreaseTheBalance() {
		Account account = accountWithBalance(amountOf(10));
		account.withdraw(amountOf(10));
		assertThat(account.balance(), is(ZERO));
	}

	@Test
	void withdrawalShouldNotBeAppliedWhenItTakesAccountOverdrawn() {
		Account account = emptyAccount();
		account.withdraw(amountOf(1));
		assertThat(account.balance(), is(ZERO));
	}

	@Test
	void transferShouldMoveMoneyFromOneAccountToAnother() {
		Account sender = accountWithBalance(amountOf(10));
		Account receiver = emptyAccount();

		sender.transfer(amountOf(10), receiver);

		assertThat(sender.balance(), is(ZERO));
		assertThat(receiver.balance(), is(amountOf(10)));
	}

	@Test
	void transferShouldNotBeAppliedWhenItTakesSendingAccountOverdrawn() {
		Account sender = emptyAccount();
		Account receiver = emptyAccount();

		sender.transfer(amountOf(1), receiver);

		assertThat(sender.balance(), is(ZERO));
		assertThat(receiver.balance(), is(ZERO));
	}

	@Test
	void statementShouldBeWrittenToSuppliedWriter() throws IOException {
		Statement statement = (account, writer) -> writer.append("STATEMENT GENERATED ").append(account.balance().toString());
		assertThat(accountWithBalance(amountOf(2314)).writeStatement(statement, new StringWriter()), is("STATEMENT GENERATED 2,314.00"));
	}
}
