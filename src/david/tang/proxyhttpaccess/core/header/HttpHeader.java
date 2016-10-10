package david.tang.proxyhttpaccess.core.header;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

public class HttpHeader {
	private Map<String, String> headerMap = new HashMap<String, String>();

	private String name;
	
	public Map<String, String> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(Map<String, String> headerMap) {
		this.headerMap = headerMap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static HttpHeader load(Element e) {
		if (e == null) {
			return null;
		}

		HttpHeader httpHeader = new HttpHeader();
		String name = e.getAttributeValue("name");
		httpHeader.setName(name);
		
		List<Element> headers = e.getChildren("header");
		if (headers != null) {
			for (Element header : headers) {
				httpHeader.getHeaderMap().put(header.getAttributeValue("name"), header.getAttributeValue("value"));
			}
		}
		
		return httpHeader;
	}
}
