package david.tang.proxyhttpaccess.core.rule;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Element;

import david.tang.proxyhttpaccess.core.header.HttpHeader;
import david.tang.proxyhttpaccess.core.proxy.ProxyI;

public class Rule {
	private String domain;
	private ProxyI proxy = null;
	private HttpHeader header = null;
	private int limit;
	
	
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public ProxyI getProxy() {
		return proxy;
	}
	public void setProxy(ProxyI proxy) {
		this.proxy = proxy;
	}
	public HttpHeader getHeader() {
		return header;
	}
	public void setHeader(HttpHeader header) {
		this.header = header;
	}
	public static Rule load(Element e, Map<String, ProxyI> proxys, Map<String, HttpHeader> headers) {
		if (e == null) {
			return null;
		}
		
		Rule rule = new Rule();
		if (!StringUtils.isEmpty(e.getChildText("header"))) {
			rule.setHeader(headers.get(e.getChildText("header")));
		}
		
		if (!StringUtils.isEmpty(e.getChildText("proxy"))) {
			rule.setProxy(proxys.get(e.getChildText("proxy")));
		}
		
		String domain = e.getChildText("domains");
		rule.setDomain(domain);
		
		return rule;
	}
}
