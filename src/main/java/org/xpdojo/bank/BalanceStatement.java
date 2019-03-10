package org.xpdojo.bank;

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.io.Writer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.*;
import static java.time.ZoneId.systemDefault;
import static java.time.ZoneOffset.*;
import static java.time.format.DateTimeFormatter.*;
import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;
import static java.time.format.FormatStyle.SHORT;
import static java.util.Locale.UK;

public class BalanceStatement implements Statement {

	private final int MAX_WIDTH = 30
		;
	private final Account account;
	private final Clock clock;
	private final String newLine = System.getProperty("line.separator");

	public BalanceStatement(Account account, Clock clock) {
		this.account = account;
		this.clock = clock;
	}

	@Override
	public void writeTo(Writer writer) throws IOException  {
		writer.append(header());
		writer.append(preamble(clock.now()));
		writer.append(balance());
		writer.append(newLine);
		writer.append(footer());
	}
	
	private String header() {
		return "-----------------------------" + newLine +
			   "         XP DOJO BANK        " + newLine +
			   "-----------------------------" + newLine +
			   newLine;
	}
	
	private String preamble(Instant instant) {
		return "Terminal #            1003423" + newLine +
			   "Date                 " + getDate(instant) + newLine +
			   "Time 24H                " + getTime(instant) + newLine + newLine +
			   "Account           XXXXXXXXXXX" + newLine;
	}
	
	private String getDate(Instant instant) {
		return ofPattern("dd/MM/yy").format(ofInstant(instant, UTC));
	}
	
	private String getTime(Instant instant) {
		return ofPattern("HH:mm").format(ofInstant(instant, UTC));
	}
	
	private String balance() {
		String balance = account.balance().toString();
		return "Your current balance is:     " + newLine + padLeft(balance, MAX_WIDTH - 1) + newLine;
	}

	private static String padLeft(String value, int amount) {
		return String.format("%" + amount + "s", value);
	}
	
	private String footer() {
		return newLine +
			   "  for great deals on loans   " + newLine +
			   "  visit https://xpdojo.org   " + newLine +
			   "-----------------------------";
	}
}
