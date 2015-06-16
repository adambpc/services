/**
 * @author Adam Hardie
 */
package com.bpc.services.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlUtils {
	/**
	 * 
	 * @param xmlString
	 * @return
	 * @throws Exception
	 */
	public static Document stringToXml(String xmlString) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource inputSource = new InputSource(new StringReader(xmlString));
		return builder.parse(inputSource);
	}

	/**
	 * 
	 * @param xmlString
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static NodeList nodesFromXPath(String xmlString, String path)
			throws Exception {
		Document xmlDocument = stringToXml(xmlString);
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile(path);
		NodeList nodelist = (NodeList) expr.evaluate(xmlDocument,
				XPathConstants.NODESET);
		return nodelist;
	}

	/**
	 * 
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public static String nodeToString(Node node) throws Exception {
		StringWriter stringWriter = new StringWriter();
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(node), new StreamResult(
				stringWriter));
		return stringWriter.toString();
	}

	/**
	 * 
	 * @param xmlString
	 * @return
	 * @throws Exception
	 */
	public static String stripProlog(String xmlString) throws Exception {
		return xmlString.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
				"");
	}

	/**
	 * 
	 * @param xmlString
	 * @return
	 * @throws Exception
	 */
	public static String unescapeXml(String xmlString) throws Exception {
		return StringEscapeUtils.unescapeXml(xmlString);
	}

	/**
	 * 
	 * @param xmlString
	 * @return
	 * @throws Exception
	 */
	public static String escapeXml(String xmlString) throws Exception {
		return StringEscapeUtils.escapeXml(xmlString);
	}

	/**
	 * Given a DOM Document, returns the XML code in String with indented
	 * features
	 * 
	 * @param domDoc
	 * @return
	 * @throws TransformerException
	 */
	public static String domDocToString(Document domDoc)
			throws TransformerException {
		try {
			Transformer logTransformer = TransformerFactory.newInstance()
					.newTransformer();
			logTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult logSr = new StreamResult(new StringWriter());
			logTransformer.transform(new DOMSource(domDoc), logSr);
			return logSr.getWriter().toString();
		} catch (TransformerException te) {
			throw new TransformerException(
					"Error: could not transform DOM Document into an XML String",
					te);
		}
	}

	/**
	 * 
	 * @param failureMessage
	 *            to be show in the <message> tag of the output
	 * @return
	 * @throws Exception
	 *             Only when the XML code cannot be build
	 */
	public static String generateFailXmlString(String failureMessage)
			throws ParserConfigurationException, TransformerException {
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document outDoc = docBuilder.newDocument();
		Element reponseElement = outDoc.createElement("response");
		outDoc.appendChild(reponseElement);
		Element successElement = outDoc.createElement("success");
		successElement.appendChild(outDoc.createTextNode("false"));
		reponseElement.appendChild(successElement);
		Element messageElement = outDoc.createElement("message");
		reponseElement.appendChild(messageElement);
		messageElement.appendChild(outDoc.createTextNode(failureMessage));
		return XmlUtils.domDocToString(outDoc);
	}
	
	
	/**
	 * Merges document 1 and document 2 into document 1, wrapping the contents of tag1 in document 1  and tag2 in document 2 under
	 *  a new tag and locates the new wrapper tag under the indicated parentTag in doc1
	 * @param doc1
	 * @param doc2
	 * @param wrapperTag
	 * @param underTag
	 * @return
	 */
	public static Document mergeDocuments(Document doc1,String tag1,Document doc2,String tag2, String wrapperTag,String parentTag) {
		Node n1 = doc1.getElementsByTagName(tag1).item(0);
		Node n2 = doc2.getElementsByTagName(tag2).item(0);
		Node parentNode = doc1.getElementsByTagName(parentTag).item(0);
		if (wrapperTag != null && !wrapperTag.trim().equals("")) {
			Element wrapperElement = doc1.createElement(wrapperTag);
			wrapperElement.appendChild(n1);
			n2 = doc1.adoptNode(n2);
			wrapperElement.appendChild(n2);
			parentNode.appendChild(wrapperElement);
		} else {
			parentNode.appendChild(n1);
			parentNode.appendChild(n2);
		}
		return doc1;		
	}
	

	/**
	 * 
	 * @param xmlString
	 * @param path
	 * @param nodeContent
	 * @return
	 * @throws Exception
	 */
	public static String replaceNodeContent(String xmlString, String path,
			String nodeContent) throws Exception {
		Document calcDataDoc = XmlUtils.stringToXml(xmlString);
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile(path);
		NodeList nodelist = (NodeList) expr.evaluate(calcDataDoc,
				XPathConstants.NODESET);
		Node node = nodelist.item(0);
		System.out.println("Node Content ---------" + node.getTextContent());
		node.setTextContent(nodeContent);
		System.out.println("Node Content ---After------"
				+ node.getTextContent());
		return domDocToString(calcDataDoc);
	}

	/**
	 * Given a DOM document moves the first ocurence of tagName under parentTag
	 * @param doc
	 * @param tagName
	 * @param parentTag
	 * @return
	 */
	public static Document moveDomTag(Document doc, String tagName, String parentTag) {
		Node child = doc.getElementsByTagName(tagName).item(0);
		Node parent = doc.getElementsByTagName(parentTag).item(0);
		parent.appendChild(child);
		return doc;
	}
	
}
