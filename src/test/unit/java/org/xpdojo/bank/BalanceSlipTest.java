package org.xpdojo.bank;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xpdojo.bank.Account.*;
import static org.xpdojo.bank.Money.amountOf;

class BalanceSlipTest {
	
	@Test
	void getBalanceSlip() {
		Clock now = () -> Instant.parse("2019-02-03T10:15:30Z");
		BalanceSlip actual = accountWithBalance(amountOf(100)).balanceSlip(now);
		BalanceSlip expected = new BalanceSlip(amountOf(100), now);
		assertThat(actual, is(expected));
	}
}
