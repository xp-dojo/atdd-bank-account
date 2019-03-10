package org.xpdojo.bank;

import java.io.StringWriter;

interface Statement {

	void writeTo(StringWriter writer);
}
