package org.xpdojo.bank;

import java.io.PrintStream;
import java.io.StringWriter;

interface Statement {

	void writeTo(StringWriter writer);
}
