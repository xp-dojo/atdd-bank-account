package org.xpdojo.bank;

import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static java.lang.Long.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.xpdojo.bank.Money.amountOf;

class MoneyTest {

	@Test
	void addingMonies() {
		assertThat(amountOf(10).plus(amountOf(14)), is(amountOf(24)));
	}

	@Test
	void addingNegativeMonies() {
		assertThat(amountOf(10).plus(amountOf(-14)), is(amountOf(-4)));
	}

	@Test
	@Disabled
	void addingBigMoneySafely() {
		assertThat(amountOf(MAX_VALUE / 2).plus(amountOf(MAX_VALUE / 2)), is(amountOf(MAX_VALUE)));
	}

	@Test
	@Disabled
	void addingBigMoney() {
		assertThrows(IllegalArgumentException.class, () -> amountOf(MAX_VALUE).plus(amountOf(1)));
		assertThrows(IllegalArgumentException.class, () -> amountOf(MAX_VALUE / 2).plus(amountOf((MAX_VALUE / 2) + 1)));
	}

	@Test
	void takingMoniesAway() {
		assertThat(amountOf(14).minus(amountOf(10)), is(amountOf(4)));
	}

	@Test
	void takingMoniesAwayWhenSubtractionIsGreaterThanInitialValue() {
		assertThat(amountOf(10).minus(amountOf(14)), is(amountOf(-4)));
	}

	@Test
	void takingNegativeMoniesAway() {
		assertThat(amountOf(10).minus(amountOf(-14)), is(amountOf(24)));
	}

	@Test
	void lessThan() {
		assertThat(amountOf(0).isLessThan(amountOf(0)), is(false));
		assertThat(amountOf(10).isLessThan(amountOf(10)), is(false));
		assertThat(amountOf(10).isLessThan(amountOf(11)), is(true));
		assertThat(amountOf(11).isLessThan(amountOf(10)), is(false));
		assertThat(amountOf(-10).isLessThan(amountOf(10)), is(true));
		assertThat(amountOf(10).isLessThan(amountOf(-10)), is(false));
	}
	
	@Test
	public void stringRepresentation() {
		assertThat(amountOf(100).toString(), containsString("100"));
	}
	
}