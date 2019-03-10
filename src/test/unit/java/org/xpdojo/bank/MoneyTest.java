package org.xpdojo.bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xpdojo.bank.Money.amountOf;

class MoneyTest {

	@Test
	void addingAnAmountShouldGiveTheSumOfTheTwoAmounts() {
		assertThat(amountOf(10).plus(amountOf(14)), is(amountOf(24)));
	}

	@Test
	void addingANegativeAmountShouldSubtractTheEquivalentPositiveAmount() {
		assertThat(amountOf(10).plus(amountOf(-14)), is(amountOf(-4)));
	}

	@Test
	void subtractingAnAmountShouldGiveTheSubtractedSum() {
		assertThat(amountOf(14).minus(amountOf(10)), is(amountOf(4)));
	}

	@Test
	void subtractingMoreThanTheCurrentAmountShouldGiveANegativeResult() {
		assertThat(amountOf(10).minus(amountOf(14)), is(amountOf(-4)));
	}

	@Test
	void subtractingANegativeAmountShouldAddTheEquivalentPositiveAmount() {
		assertThat(amountOf(10).minus(amountOf(-14)), is(amountOf(24)));
	}

	@ParameterizedTest(name = "{0} is less than {1} should be {2}")
	@CsvSource({
			"0, 0, false",
			"10, 10, false",
			"10, 11, true",
			"11, 10, false",
			"-10, 10, true",
			"10, -10, false"
	})
	void lessThanShouldGiveCorrectResult(long amount1, long amount2, boolean correctResult) {
		assertThat(amountOf(amount1).isLessThan(amountOf(amount2)), is(correctResult));
	}
	
	@Test
	void stringRepresentationShouldBeFormattedTo2DecimalPlacesWithCommas() {
		assertThat(amountOf(1000103).toString(), is("1,000,103.00"));
	}
}