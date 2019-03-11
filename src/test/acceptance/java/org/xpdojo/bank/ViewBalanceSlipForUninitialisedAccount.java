package org.xpdojo.bank;

import org.concordion.api.ConcordionResources;
import org.concordion.api.ExpectedToFail;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@ConcordionResources(value = { "../../../concordion.css" })
@ExpectedToFail
public class ViewBalanceSlipForUninitialisedAccount {
}
