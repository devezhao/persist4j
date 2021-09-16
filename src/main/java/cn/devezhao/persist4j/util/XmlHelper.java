package cn.devezhao.persist4j.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import java.io.InputStream;
import java.util.List;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @since 0.1, Feb 4, 2009
 * @version $Id: XmlHelper.java 8 2015-06-08 09:09:03Z zhaofang123@gmail.com $
 */
public class XmlHelper {

	private static final Log LOG = LogFactory.getLog(XmlHelper.class);

	public static final EntityResolver DEFAULT_DTD_RESOLVER = new DTDEntityResolver();
	
	public static final XmlHelper INSTANCE = new XmlHelper();

	private DOMReader domReader;
	private SAXReader saxReader;

	/**
	 * Create a dom4j SAXReader which will append all validation errors to
	 * errorList
	 */
	public SAXReader createSAXReader(String file, List<String> errorsList,
			EntityResolver entityResolver) {
		if (saxReader == null) {
			saxReader = new SAXReader();
		}
		
		if (entityResolver != null) {
			saxReader.setEntityResolver(entityResolver);
			saxReader.setValidation(true);
		}
		
		saxReader.setErrorHandler(new ErrorLogger(file, errorsList));
		saxReader.setMergeAdjacentText(true);
		return saxReader;
	}

	/**
	 * Create a dom4j DOMReader
	 */
	public DOMReader createDOMReader() {
		if (domReader == null) {
			domReader = new DOMReader();
		}
		return domReader;
	}

	/**
	 * Create a dom4j Element
	 */
	public static Element createDom4jElement(String elName) {
		return DocumentFactory.getInstance().createElement(elName);
	}
	
	/**
	 * Create a dom4j Document
	 * @return
	 */
	public static Document createDom4jDocument() {
		return DocumentFactory.getInstance().createDocument("UTF-8");
	}

	/**
	 * @param in
	 * @return
	 * @throws DocumentException
	 */
	public static Document read(InputStream in) throws DocumentException {
		return read(in, null);
	}
	
	public static Document read(InputStream in, EntityResolver resolver) throws DocumentException {
		SAXReader reader = INSTANCE.createSAXReader("#INPUT_STREAM_FILE", null, resolver);
		return reader.read(in);
	}
	
	public static void dump(Element element) {
		try {
			OutputFormat outformat = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(System.out, outformat);
			writer.write(element);
			writer.flush();
			System.out.println();
		} catch (Throwable t) {
			System.err.println(element.asXML());
		}
	}

	
	static public class ErrorLogger implements ErrorHandler {
		private String file;
		private List<String> errors;

		ErrorLogger(String file, List<String> errors) {
			this.file = file;
			this.errors = errors;
		}

		@Override
        public void error(SAXParseException error) {
			String message = file + '(' + error.getLineNumber() + ") " + error.getMessage();
			LOG.error("Error parsing XML[" + file + "]: " + message);
			if (errors != null) {
                errors.add(message);
            }
		}

		@Override
        public void fatalError(SAXParseException error) {
			error(error);
		}

		@Override
        public void warning(SAXParseException warn) {
			LOG.warn("Warning parsing XML: " + file + '(' + warn.getLineNumber() + ") " + warn.getMessage());
		}
	}

	static public class DTDEntityResolver implements EntityResolver {
		public static final String PUBLIC_ID = "metadata.dtd";
		public static final String SYSTEM_ID = "https://raw.githubusercontent.com/devezhao/persist4j/master/src/main/resources/metadata.dtd";

		DTDEntityResolver() {
			super();
		}

		@Override
        public InputSource resolveEntity(String publicId, String systemId) {
			InputStream stream = getClass().getClassLoader().getResourceAsStream(PUBLIC_ID);
			InputSource source = new InputSource(stream);
			source.setPublicId(PUBLIC_ID);
			source.setSystemId(SYSTEM_ID);
			source.setEncoding("UTF-8");
			return source;
		}
	}
}
