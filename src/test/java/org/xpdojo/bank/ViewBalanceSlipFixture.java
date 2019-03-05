package org.xpdojo.bank;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.security.SecureRandom;
import java.time.Instant;

import static org.xpdojo.bank.Account.*;
import static org.xpdojo.bank.Money.*;

@RunWith(ConcordionRunner.class)
public class ViewBalanceSlipFixture {

	private Account account;

	public void setCurrentBalance(long balance) {
		account = accountWithBalance(amountOf(balance));
	}
	
	public BalanceSlip checkCurrentBalance(String isoUtcDateTime) {
		return new BalanceSlip(account, isoUtcDateTime);
	}
	
	// Note this is a test fixture only class, it is abstract from the production code
	class BalanceSlip {

		public final org.xpdojo.bank.BalanceSlip slip;
		
		public BalanceSlip(Account account, String isoUtcDateTime) {
			this.slip = account.balanceSlip(() -> Instant.parse(isoUtcDateTime));
		}
		
		public String isBalanceEqualTo(long balance) {
			return slip.getBalance().equals(amountOf(balance)) ? Long.toString(balance) : slip.getBalance().toString();
		}

		public boolean isDateAndTimeEqualTo(String isoUtcDateTime) {
			return slip.getAsAt().equals(Instant.parse(isoUtcDateTime));
		}
	}
}