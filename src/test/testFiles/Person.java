//:XML/Person.java
//Use the XOM library to write and read XML
//{Requires:nu.com.Node: You must install
//the XOM library from http://www.xom.nu}
package test.testFiles;

import java.io.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

public class Person {

	public void buildXMLDoc() throws IOException,JDOMException{
		Element eRoot,e1,e2;
		Document Doc;
		eRoot = new Element("employees_information");
		Doc = new Document(eRoot);
		eRoot = Doc.getRootElement();
		e1 = new Element("name");
		e2 = e1.setText("C.Y. Shen");
		e1 = eRoot.addContent(e2);
		e1 = new Element("age");
		e2 = e1.setText("43");
		e1 = eRoot.addContent(e2);
		e1 = new Element("sex");
		e2 = e1.setText("Male");
		e1 = eRoot.addContent(e2);
		XMLOutputter  XMLOut = new XMLOutputter();
		XMLOut.output(Doc, new FileOutputStream("test.xml"));
		System.out.println(XMLOut);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Person p = new Person();
			System.out.println("Now we are trying to bulid an XML document ......");
			p.buildXMLDoc();
			
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}













