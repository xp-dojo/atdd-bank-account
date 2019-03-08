package org.xpdojo.bank;

import org.concordion.api.ConcordionResources;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.time.Instant;

import static org.xpdojo.bank.Account.accountWithBalance;
import static org.xpdojo.bank.Money.amountOf;

@RunWith(ConcordionRunner.class)
@ConcordionResources(value = { "../../../../resources/concordion.css" })
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
			return slip.getBalance().equals(amountOf(balance)) ? "correct" : "incorrect";
		}

		public boolean isDateAndTimeEqualTo(String isoUtcDateTime) {
			return slip.getAsAt().equals(Instant.parse(isoUtcDateTime));
		}
		
		public String toString() {
			return slip.toString();
		}
	}
}