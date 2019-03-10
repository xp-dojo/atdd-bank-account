package org.xpdojo.bank;

import java.io.IOException;
import java.io.Writer;
import java.time.format.DateTimeFormatter;

import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;
import static java.time.format.FormatStyle.SHORT;
import static java.util.Locale.UK;

public class BalanceStatement implements Statement {

	private final Account account;
	private final Clock clock;

	public BalanceStatement(Account account, Clock clock) {
		this.account = account;
		this.clock = clock;
	}

	@Override
	public void writeTo(Writer writer) throws IOException  {
		DateTimeFormatter formatter = ofLocalizedDateTime(SHORT).withLocale(UK).withZone(systemDefault());
		writer.append(account.balance().toString());
		writer.append(System.getProperty("line.separator"));
		writer.append(formatter.format(clock.now()));
	}
}
