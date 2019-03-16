package org.xpdojo.bank;

import java.io.IOException;
import java.io.Writer;

interface Statement {

	void write(Account account, Writer writer) throws IOException;
}
