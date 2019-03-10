package org.xpdojo.bank;

import java.io.IOException;
import java.io.Writer;

interface Statement {

	void writeTo(Writer writer) throws IOException;
}
