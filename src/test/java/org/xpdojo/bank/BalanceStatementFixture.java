package org.xpdojo.bank;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.format.DateTimeFormatter;

import static java.time.Instant.parse;
import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;
import static java.time.format.FormatStyle.SHORT;
import static java.util.Locale.UK;

// Note this is a test fixture only class, it is abstract from the production code
class BalanceStatementFixture {

	private final String balanceSlip;

	public BalanceStatementFixture(Account account, String isoUtcDateTime) throws IOException {
		BalanceStatement statementOfAccount = new BalanceStatement(account, () -> parse(isoUtcDateTime));
		Writer writer = new StringWriter();
		statementOfAccount.writeTo(writer);
		balanceSlip = writer.toString();
	}

	public String isBalanceEqualTo(long balance) {
		return balanceSlip.contains(Long.toString(balance)) ? "correctly" : "incorrectly";
	}

	public boolean isDateAndTimeEqualTo(String isoUtcDateTime) {
		DateTimeFormatter formatter = ofLocalizedDateTime(SHORT).withLocale(UK).withZone(systemDefault());
		return balanceSlip.contains(formatter.format(parse(isoUtcDateTime)));
	}

	// todo remove this
	public String getActualBalanceFromSlip() {
		return "???";
	}
}
