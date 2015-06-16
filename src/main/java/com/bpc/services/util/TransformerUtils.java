package com.bpc.services.util;

import java.io.CharArrayWriter;
import java.io.StringReader;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class TransformerUtils {
	/**
	 * 
	 */
	private String XSLFile;

	/**
	 * 
	 * @param rawXmlInput
	 * @return
	 */
	public String transform(String rawXmlInput) {
		Transformer transformer;
		try {
			transformer = makeTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			return "<E>TransformerConfigurationException:" + e.getMessage()
					+ "</E>";
		}
		CharArrayWriter output = new CharArrayWriter();
		try {
			transformer.transform(new StreamSource(
					new StringReader(rawXmlInput)), new StreamResult(output));
		} catch (TransformerException e) {
			e.printStackTrace();
			return "<E>TransformerException:" + e.getMessage() + "</E>";
		}
		return output.toString();
	}

	/**
	 * 
	 * @return
	 * @throws TransformerConfigurationException
	 */
	private Transformer makeTransformer()
			throws TransformerConfigurationException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		Source stylesheetSource;
		if (getXSLFile() != null) {
			stylesheetSource = new StreamSource(TransformerUtils.class
					.getClassLoader().getResourceAsStream(getXSLFile()));
			transformer = tFactory.newTransformer(stylesheetSource);
		} else {
			throw new TransformerConfigurationException(
					"No XSL or XSL file set");
		}
		return transformer;
	}

	/**
	 * 
	 * @param inFile
	 * @param outFile
	 * @return
	 */
	public String transformFile(String inFile, String outFile) {
		Transformer transformer = null;
		try {
			transformer = makeTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			return "<E>TransformerConfigurationException:" + e.getMessage()
					+ "</E>";
		}
		try {
			transformer.transform(new StreamSource(inFile), new StreamResult(
					outFile));
		} catch (TransformerException e) {
			e.printStackTrace();
			return "<E>TransformerException:" + e.getMessage() + "</E>";
		}

		return outFile;
	}

	/**
	 * 
	 * @return
	 */
	public String getXSLFile() {
		return this.XSLFile;
	}

	/**
	 * 
	 * @param XSLFile
	 */
	public void setXSLFile(String XSLFile) {
		this.XSLFile = XSLFile;
	}
}