package com.sokolmn.siebellogreplacer;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlBindReplacerTest {

	private String readString(String filename) {
		try {
			URL url = Resources.getResource(filename);
			return Resources.toString(url, Charsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Can't read file " + filename);
		}
	}

	@Test
	void replace() {
		String source = readString("LogSelect.txt");
		String expectedResult = readString("ResultLog.txt");

		assertEquals(expectedResult, SqlBindReplacer.replace(source));
	}
}
