package org.xpdojo.bank;

import org.concordion.api.ConcordionResources;
import org.concordion.api.ExpectedToFail;
import org.concordion.api.Unimplemented;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.time.Instant;

import static org.xpdojo.bank.Account.accountWithBalance;
import static org.xpdojo.bank.Money.amountOf;

@RunWith(ConcordionRunner.class)
@ConcordionResources(value = { "../../../../resources/concordion.css" })
@ExpectedToFail
public class ViewBalanceSlipPrintoutFixture {
}