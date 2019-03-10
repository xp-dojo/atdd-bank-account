package org.xpdojo.bank;

import java.io.StringWriter;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import static java.time.ZoneId.*;
import static java.time.format.DateTimeFormatter.*;
import static java.time.format.FormatStyle.*;
import static java.util.Locale.*;

public class BalanceStatement implements Statement {

	private final Account account;
	private final Clock clock;

	public BalanceStatement(Account account, Clock clock) {
		this.account = account;
		this.clock = clock;
	}

	@Override
	public void writeTo(StringWriter writer) {
		DateTimeFormatter formatter = ofLocalizedDateTime(SHORT).withLocale(UK).withZone(systemDefault());
		writer.append(account.balance().toString());
		writer.append(formatter.format(clock.now()));
	}
}
