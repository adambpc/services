package com.bpc.services.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileUtils {
	public static String read(String fileResource) {
		InputStream resourceStream = FileUtils.class.getClassLoader()
				.getResourceAsStream(fileResource);
		if (resourceStream == null) {
			throw new RuntimeException("Failed to find resource : "
					+ fileResource);
		}
		return read(resourceStream);
	}

	public static String read(InputStream inStream) {
		BufferedReader reader = null;
		StringBuffer buf = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(inStream, "ISO-8859-1"));
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				if (buf.length() > 0) {
					buf.append(System.getProperty("line.separator"));
				}
				buf.append(line);
			}
		} catch (IOException e) {
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException ignore) {
				}
		}
		return buf.toString();
	}

	public static String read(Reader reader) throws IOException {
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufReader = null;
		if (reader instanceof BufferedReader)
			bufReader = (BufferedReader) reader;
		else
			bufReader = new BufferedReader(reader);
		try {
			while (true) {
				String line = bufReader.readLine();
				System.out.println("FileUtils.read line=" + line);
				if (line == null) {
					break;
				}
				stringBuffer.append(line);
				stringBuffer.append(System.getProperty("line.separator"));
			}
			return stringBuffer.toString();

		} finally {
			if (bufReader != null)
				bufReader.close();
		}
	}
}
