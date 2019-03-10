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
			return new Identity(other.amount.plus(super.amount));
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
			return new Identity(other.amount.minus(super.amount));
		}
	}

	public static class Identity extends Transaction {

		public Identity(Money amount) {
			super(amount);
		}

		@Override
		Transaction against(Transaction other) {
			return this;
		}
	}
}

//public class Transaction {
//
//	enum Direction {
//		CREDIT {
//			@Override
//			public BiFunction<Money, Money, Money> apply(Money amount) {
//				return (a, b) -> a.plus(b);
//			}
//		},
//
//		DEBIT {
//			@Override
//			public BiFunction<Money, Money, Money> apply(Money amount) {
//				return (a, b) -> a.minus(b);
//			}
//		};
//
//		public abstract BiFunction<Money, Money, Money> apply(Money amount);
//	}
//
//	private final Money amount;
//	private final Direction direction;
//
//	public Transaction(Money amount, Direction direction) {
//		this.amount = amount;
//		this.direction = direction;
//	}
//
//	public Money amount() {
//		return amount;
//	}
//
//	public void apply(Money money) {
//		direction.apply(money);
//	}
//	
//	public Direction getDirection() {
//		return direction;
//	}
//}
//

