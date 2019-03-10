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

    private final Account account;
    private final Clock clock;

	public BalanceStatement(Account account, Clock clock) {
		this.account = account;
		this.clock = clock;
	}

	@Override
	public void writeTo(Writer writer) throws IOException  {
		writer.append(header());
		writer.append(preamble(clock.now()));
		writer.append(balance());
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
	
	private String balance() {
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
