package org.xpdojo.bank;

import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xpdojo.bank.Account.accountWithBalance;
import static org.xpdojo.bank.Money.ZERO;
import static org.xpdojo.bank.Money.amountOf;

class BalanceStatementTest {

	@Test
	void aBalanceStatementIncludesTheAccountsBalance() {
		Clock clock = () -> Instant.ofEpochMilli(0);
		BalanceStatement statement = new BalanceStatement(accountWithBalance(amountOf(250)), clock);
		StringWriter writer = new StringWriter();
		statement.writeTo(writer);
		assertThat(writer.toString(), containsString("250"));
	}

	@Test
	void aBalanceStatementIncludesTheCurrentDateNicelyFormatted() {
		Clock now = () -> Instant.parse("2019-02-03T10:15:30Z");
		BalanceStatement statement = new BalanceStatement(accountWithBalance(ZERO), now);
		StringWriter writer = new StringWriter();
		statement.writeTo(writer);
		assertThat(writer.toString(), containsString("03/02/19 10:15"));
	}
	
}