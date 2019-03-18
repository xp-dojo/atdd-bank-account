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

import org.concordion.api.ConcordionResources;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static java.time.Instant.parse;

@RunWith(ConcordionRunner.class)
@ConcordionResources(value = {"../../../concordion.css"})
public class ViewStatement {

	private final FullStatementFixture fixture = new FullStatementFixture();

	public List<FullStatementFixture.Transaction> addTransaction(String isoUtcDateTime, String direction, String amount) {
		return fixture.addTransaction(parse(isoUtcDateTime), direction, amount);
	}

	public Account applyTransactionsToAccount(List<FullStatementFixture.Transaction> transactions) {
		return fixture.applyTransactionsToAccount(transactions);
	}
	
	public String statementIncludes(List<FullStatementFixture.Transaction> transactions, Account account) throws IOException {
		return fixture.statementIncludes(transactions, account);
	}

	public String includesBalance(Account account) throws IOException {
		return fixture.statementIncludesBalance(account);
	}

	public String getActualStatement(Account account) throws IOException {
		return fixture.getActualStatement(account);
	}
}
