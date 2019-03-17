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
import java.io.Writer;
import java.time.Instant;

import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;

public class BalanceStatement implements Statement {

	private static final int MAX_WIDTH = 30;
    private static final String NEW_LINE = System.getProperty("line.separator");

    private final Clock clock;

	public BalanceStatement(Clock clock) {
		this.clock = clock;
	}

	@Override
	public void write(Account account, Writer writer) throws IOException  {
		writer.append(header());
		writer.append(preamble(clock.now()));
		writer.append(balance(account));
		writer.append(NEW_LINE);
		writer.append(footer());
	}
	
	private String header() {
		return "-----------------------------" + NEW_LINE +
			   "         XP DOJO BANK        " + NEW_LINE +
			   "-----------------------------" + NEW_LINE +
                NEW_LINE;
	}
	
	private String preamble(Instant instant) {
		return "Terminal #            1003423" + NEW_LINE +
			   "Date                 " + getDate(instant) + NEW_LINE +
			   "Time 24H                " + getTime(instant) + NEW_LINE + NEW_LINE +
			   "Account           XXXXXXXXXXX" + NEW_LINE;
	}
	
	private String getDate(Instant instant) {
		return ofPattern("dd/MM/yy").format(ofInstant(instant, UTC));
	}
	
	private String getTime(Instant instant) {
		return ofPattern("HH:mm").format(ofInstant(instant, UTC));
	}
	
	private String balance(Account account) {
		String balance = account.balance().toString();
		return "Your current balance is:     " + NEW_LINE + NEW_LINE + padLeft(balance, MAX_WIDTH - 1) + NEW_LINE;
	}

	private static String padLeft(String value, int amount) {
		return String.format("%" + amount + "s", value);
	}
	
	private String footer() {
		return NEW_LINE +
			   "  for great deals on loans   " + NEW_LINE +
			   "  visit https://xpdojo.org   " + NEW_LINE +
			   "-----------------------------";
	}
}
