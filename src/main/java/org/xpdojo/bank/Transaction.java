package org.xpdojo.bank;

public abstract class Transaction {

	private final Money amount;

	Transaction(Money amount) {
		this.amount = amount;
	}

	abstract Transaction against(Transaction other);

	public Money amount() {
		return amount;
	}

	public static class Deposit extends Transaction {
		
		private Deposit(Money amount) {
			super(amount);
		}

		public static Deposit depositOf(Money amount) {
			return new Deposit(amount);
		}

		public static Deposit deposit(Money amount) {
			return depositOf(amount);
		}

		public Transaction against(Transaction other) {
			return Tally.tally(other.amount.plus(super.amount));
		}
	}

	public static class Withdraw extends Transaction {

		public static Withdraw withdrawalOf(Money amount) {
			return new Withdraw(amount);
		}

		public static Withdraw withdraw(Money amount) {
			return withdrawalOf(amount);
		}

		private Withdraw(Money amount) {
			super(amount);
		}

		public Transaction against(Transaction other) {
			return Tally.tally(other.amount.minus(super.amount));
		}
	}

	public static class Tally extends Transaction {

		private Tally(Money amount) {
			super(amount);
		}

		public static Tally tally(Money amount) {
			return new Tally(amount);
		}

		@Override
		Transaction against(Transaction other) {
			return this;
		}
	}
}