package org.xpdojo.bank;

import org.concordion.api.ConcordionResources;
import org.concordion.api.ExpectedToFail;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Instant;

import static org.xpdojo.bank.Account.accountWithBalance;
import static org.xpdojo.bank.Money.amountOf;

@RunWith(ConcordionRunner.class)
@ConcordionResources(value = {"../../../../resources/concordion.css"})
@ExpectedToFail
public class ViewBalanceSlipPrintoutFixture {
	
	private Account account;
	private Clock clock;

	public void setCurrentBalance(long balance) {
		account = accountWithBalance(amountOf(balance));
	}

	public void setCurrentDateTime(String isoUtcDateTime) {
		clock = () -> Instant.parse(isoUtcDateTime);
	}

	public String printBalanceSlip() throws IOException {
		Writer writer = new StringWriter();
		account.generate(new BalanceStatement(account, clock), writer);
		return writer.toString();
	}
}