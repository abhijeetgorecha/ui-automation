package com.ptc.integrity.automation.core.framework;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.openqa.selenium.WebDriver;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class OutputXML extends Reporting{
	public OutputXML(WebDriver webDriver, String DT, HashMap<String, String> Env) {
		super(webDriver, DT, Env);
		// TODO Auto-generated constructor stub
	}
	public DocumentBuilderFactory dbFactory;
	public DocumentBuilder dBuilder;
	public Document doc;
	public Element rootElement;

	public TransformerFactory transformerFactory;
	public Transformer transformer;
	public DOMSource source;
	public StreamResult result;
	protected String totalTime;


	public DocumentBuilderFactory getDbFactory() {
		return dbFactory;
	}

	public void setDbFactory(DocumentBuilderFactory dbFactory) {
		this.dbFactory = dbFactory;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public void createXMLReport(File xmlOutputFile) {
		try {
			dbFactory =
					DocumentBuilderFactory.newInstance();
			setDbFactory(dbFactory);
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.newDocument();

			// root element
			rootElement = doc.createElement("testsuite");
			//			setRootElement(rootElement);
			doc.appendChild(rootElement);

			// setting suite name
			Attr name = doc.createAttribute("name");
			name.setValue("Suite1");
			rootElement.setAttributeNode(name);

			//			// setting tests count
			Attr testCount = doc.createAttribute("tests");
			testCount.setValue("0");
			rootElement.setAttributeNode(testCount);

			// setting failure Count
			Attr failureCount = doc.createAttribute("failures");
			failureCount.setValue("0");
			rootElement.setAttributeNode(failureCount);

			// setting skipped Count
			Attr skippedCount = doc.createAttribute("skipped");
			skippedCount.setValue("0");
			rootElement.setAttributeNode(skippedCount);

			// setting hostname
			Attr hostname = doc.createAttribute("hostname");
			hostname.setValue("0");
			rootElement.setAttributeNode(hostname);

			// setting time
			Attr time = doc.createAttribute("time");
			time.setValue("0");
			rootElement.setAttributeNode(time);

			// write the content into xml file
			if(Files.exists(Paths.get("D:\\Suite1.xml"))) {
				System.out.println("Exists");
			}
			else {
				transformerFactory = TransformerFactory.newInstance();
				transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(xmlOutputFile);
				transformer.transform(source, result);
			}
		} catch (Exception e) {
			System.out.println("Unable to create Output XML for Hector. "+OutputXML.class.getEnclosingMethod().getName());
			e.printStackTrace();
		}

	}

	public void addAttributesToTestSuite(String Name, String Value, File xmlOutputFile) {
		try {
			// 1- Build the doc from the XML file
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(xmlOutputFile);

			// 2- Locate the node(s) with xpath
			if(Name.equalsIgnoreCase("time")) {
				Document doc1 = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder().parse(xmlOutputFile);

				// 2- Locate the node(s) with xpath
				XPath xpath = XPathFactory.newInstance().newXPath();
				NodeList nodes = (NodeList)xpath.evaluate("//*[contains(@skipped, '0')]",
						doc1, XPathConstants.NODESET);

				// 3- Make the change on the selected nodes
				for (int idx = 0; idx < nodes.getLength(); idx++) {
					Node value = nodes.item(idx);
					((Element) value).setAttribute("time", Value);
					Transformer xformer = TransformerFactory.newInstance().newTransformer();
					xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
					xformer.transform(new DOMSource(doc1), new StreamResult(xmlOutputFile));
				}
			}
			else {
				XPath xpath = XPathFactory.newInstance().newXPath();
				NodeList nodes = (NodeList)xpath.evaluate("//*[contains(@"+Name+", '0')]",
						doc, XPathConstants.NODESET);

				// 3- Make the change on the selected nodes
				for (int idx = 0; idx < nodes.getLength(); idx++) {
					Node value = nodes.item(idx).getAttributes().getNamedItem(Name);
					String val = value.getNodeValue();
					value.setNodeValue(val.replaceAll("0", Value));
				}

				Transformer xformer = TransformerFactory.newInstance().newTransformer();
				xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				xformer.transform(new DOMSource(doc), new StreamResult(xmlOutputFile));
			}
		}
		catch (Exception e) {
			System.out.println("Unable to create Output XML for Hector. "+OutputXML.class.getEnclosingMethod().getName());
			e.printStackTrace();
		}
	}


	public void addTestCasetoXML(String testName, String testResult, File xmlOutputFile, String Time) {
		try {
			//Setting Test case details

			Element testcase = doc.createElement("testcase");
			// add sub-elements/attributes to car element
			// setting testcase name
			Attr testCaseName = doc.createAttribute("name");
			testCaseName.setValue(testName);
			testcase.setAttributeNode(testCaseName);

			// setting testcase total time taken
			Attr testCaseExecutionTime = doc.createAttribute("time");
			testCaseExecutionTime.setValue(Time);
			testcase.setAttributeNode(testCaseExecutionTime);

			// setting className
			Attr className = doc.createAttribute("classname");
			className.setValue("testClass");
			testcase.setAttributeNode(className);
			if(testResult.equalsIgnoreCase("Fail")) {
				Element testresult = doc.createElement("failure");
				Text text = doc.createTextNode("Console logs");
				testresult.appendChild(text);

				testcase.appendChild(testresult);
			}

			rootElement.appendChild(testcase);

			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			source = new DOMSource(doc);
			result = new StreamResult(xmlOutputFile);
			transformer.transform(source, result);
		}
		catch (Exception e) {
			System.out.println("Unable to create Output XML for Hector. "+OutputXML.class.getEnclosingMethod().getName());
			e.printStackTrace();
		}

	}
}
