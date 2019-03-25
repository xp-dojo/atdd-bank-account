/*
 *
 * Copyright (c) 2019 xp-dojo organisation and committers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xpdojo.bank;

import java.util.Objects;

public abstract class Transaction {

	private final Money amount;

	private Transaction(Money amount) {
		this.amount = amount;
	}

	Money getAmount() {
		return amount;
	}

	abstract Money against(Money money);

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Transaction that = (Transaction) o;
		return Objects.equals(amount, that.amount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount);
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

		@Override
		public Money against(Money other) {
			return other.plus(this.getAmount());
		}
	}

	public static class Withdraw extends Transaction {

		private Withdraw(Money amount) {
			super(amount);
		}

		public static Withdraw withdrawalOf(Money amount) {
			return new Withdraw(amount);
		}

		public static Withdraw withdraw(Money amount) {
			return withdrawalOf(amount);
		}

		@Override
		public Money against(Money other) {
			return other.minus(this.getAmount());
		}
	}
}