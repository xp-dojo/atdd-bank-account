package org.xpdojo.bank;

import org.concordion
	.api.ConcordionResources;
import org.concordion
	.api.ExpectedToFail;
import org.concordion
	.api.Unimplemented;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.time.Instant;

import static org.xpdojo.bank.Account.accountWithBalance;
import static org.xpdojo.bank.Money.amountOf;

@RunWith(ConcordionRunner.class)
@ConcordionResources(value = {"../../../../resources/concordion.css"})
public class ViewBalanceSlipPrintoutFixture {

	public String printBalanceSlip() {
		return "-----------------------------" + '\n' +
			"         XP DOJO BANK        " + '\n' +
			"-----------------------------" + '\n' +
			'\n' +
			"Terminal #            1003423" + '\n' +
			"Date               2019-02-03" + '\n' +
			"Time 24H                10:15" + '\n' +
			'\n' +
			"Account           XXXXXXXXXXX" + '\n' +
			'\n' +
			"Your current                 " + '\n' +
			"balance is:         £1,000.00" + '\n' +
			'\n' +
			'\n' +
			'\n' +
			"  for a great deal on loans  " + '\n' +
			"  visit https://xpdojo.org   " + '\n' +
			"-----------------------------";
	}
}