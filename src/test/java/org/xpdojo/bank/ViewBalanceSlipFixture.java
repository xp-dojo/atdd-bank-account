package org.xpdojo.bank;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class ViewBalanceSlipFixture {

	private long balance;

	public void setCurrentBalance(long balance) {
		// instead of setting up the system under to test with an account and balance, 
		// we store the balance here (as we haven't implemented the former yet)
		this.balance = balance;
	}
	
	public BalanceSlip checkCurrentBalance() {
		// connect to system under test and retrieve the balance (again, "faking" it 
		// with a local variable
		return new BalanceSlip(balance);
	}
	
	// Note this is a test fixture only class, it is abstract from the production code
	class BalanceSlip {

		public long balance;

		public BalanceSlip(long balance) {
			this.balance = balance;
		}
	}
}