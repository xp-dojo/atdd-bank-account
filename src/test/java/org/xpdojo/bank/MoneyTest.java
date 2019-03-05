package org.xpdojo.bank;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import static com.sun.jmx.snmp.ThreadContext.contains;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.xpdojo.bank.Money.*;

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
	public void stringRepresentation() {
		assertThat(amountOf(100).toString(), containsString("100"));
	}


}