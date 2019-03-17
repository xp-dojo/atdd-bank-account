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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.time.Instant;

import static java.time.Instant.parse;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;

class BalanceStatementFixture {

	private final String balanceSlip;

	public BalanceStatementFixture(Account account, String isoUtcDateTime) throws IOException {
		BalanceStatement statementOfAccount = new BalanceStatement(() -> parse(isoUtcDateTime));
		Writer writer = new StringWriter();
		statementOfAccount.write(account, writer);
		balanceSlip = writer.toString();
	}

	public String isBalanceEqualTo(long balance) {
		String formattedBalance = new DecimalFormat("#,###.00").format(balance);
		return balanceSlip.contains(formattedBalance) ? "correctly" : "incorrectly";
	}

	public boolean isDateAndTimeEqualTo(String isoUtcDateTime) {
		Instant dateTime = parse(isoUtcDateTime);
		String formattedDate = ofPattern("dd/MM/yy").format(ofInstant(dateTime, UTC));
		String formattedTime = ofPattern("HH:mm").format(ofInstant(dateTime, UTC));
		return balanceSlip.contains(formattedDate) && balanceSlip.contains(formattedTime);
	}

	public String getActualBalanceSlip() {
		return balanceSlip;
	}
}
