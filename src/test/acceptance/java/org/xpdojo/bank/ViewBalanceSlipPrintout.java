package org.xpdojo.bank;

import org.concordion.api.ConcordionResources;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Instant;

import static org.xpdojo.bank.Account.accountWithBalance;
import static org.xpdojo.bank.Money.*;
import static org.xpdojo.bank.Money.amountOf;

@RunWith(ConcordionRunner.class)
@ConcordionResources(value = {"../../../concordion.css"})
public class ViewBalanceSlipPrintout {
	
	private Account account = accountWithBalance(ZERO);

	public void setCurrentBalance(long balance) {
		account = accountWithBalance(amountOf(balance));
	}

	public BalanceStatementFixture checkCurrentBalance(String isoUtcDateTime) throws IOException {
		return new BalanceStatementFixture(account, isoUtcDateTime);
	}

}