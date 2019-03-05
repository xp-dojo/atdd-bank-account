package org.xpdojo.bank;

import java.time.Instant;
import java.util.Objects;

public class BalanceSlip {
	private final Money balance;
	private final Instant asAt;

	public BalanceSlip(Money balance, Clock clock) {
		this.balance = balance;
		this.asAt = clock.now();
	}

	public Money getBalance() {
		return balance;
	}

	public Instant getAsAt() {
		return asAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BalanceSlip)) return false;
		BalanceSlip that = (BalanceSlip) o;
		return Objects.equals(balance, that.balance) &&
			Objects.equals(asAt, that.asAt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(balance, asAt);
	}

	@Override
	public String toString() {
		return String.format("BalanceSlip{balance=%s, asAt=%s}", balance, asAt);
	}
}
