package org.xpdojo.bank;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.security.SecureRandom;

import static org.xpdojo.bank.Account.*;
import static org.xpdojo.bank.Money.*;

@RunWith(ConcordionRunner.class)
public class ViewBalanceSlipFixture {

	private Account account;

	public void setCurrentBalance(long balance) {
		account = accountWithBalance(amountOf(balance));
	}
	
	public BalanceSlip checkCurrentBalance() {
		return new BalanceSlip(account.balance());
	}
	
	// Note this is a test fixture only class, it is abstract from the production code
	class BalanceSlip {

		public Money balance;

		public BalanceSlip(Money balance) {
			this.balance = balance;
		}
		
		public String isBalanceEqualTo(long balance) {
			return this.balance.equals(Money.amountOf(balance)) ? Long.toString(balance) : this.balance.toString();
		}

		public String dateAndTime() {
			if (randomChoice())
				return "current date and time";
			return "12 Feb 2019";
		}

		private boolean randomChoice() {
			return new SecureRandom().nextBoolean();
		}
	}
}