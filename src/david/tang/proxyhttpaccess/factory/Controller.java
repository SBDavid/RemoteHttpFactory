package david.tang.proxyhttpaccess.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import david.tang.proxyhttpaccess.core.header.HttpHeader;
import david.tang.proxyhttpaccess.core.proxy.AidailiProxy;
import david.tang.proxyhttpaccess.core.proxy.Connection;
import david.tang.proxyhttpaccess.core.proxy.DynamicProcy;
import david.tang.proxyhttpaccess.core.proxy.ProxyBase;
import david.tang.proxyhttpaccess.core.proxy.ProxyI;
import david.tang.proxyhttpaccess.core.rule.Rule;
import david.tang.proxyhttpaccess.logger.DefaultLogger;
import david.tang.proxyhttpaccess.logger.NormalLoggerI;

public class Controller {
	
	private static NormalLoggerI logger = null;
	
	private static MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
	private static int connectionTimeOut = 10000;
	private static int socketTimeOut = 10000;
	private static int maxConnectionPerHost = 1;
	private static int maxTotalConnections = 40;
	private static boolean initialed = false;
	
	public static void SetPara() {
		manager.getParams().setConnectionTimeout(connectionTimeOut);
		manager.getParams().setSoTimeout(socketTimeOut);
		manager.getParams().setDefaultMaxConnectionsPerHost(
				maxConnectionPerHost);
		manager.getParams().setMaxTotalConnections(maxTotalConnections);
		initialed = true;
	}
	
	private static String ConverterStringCode(String source, String srcEncode,
			String destEncode) {
		if (source != null) {
			try {
				return new String(source.getBytes(srcEncode), destEncode);
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		} else {
			return "";
		}
	}
	
	private static Map<String, Rule> ruleList = new HashMap<String, Rule>();
	
	public static boolean loadConfig(String name, String configPath, NormalLoggerI loggerI) throws  IOException, Exception {
		SAXBuilder builder = new SAXBuilder();
		Document jdomDoc = null;
		Element root;
		
		jdomDoc = builder.build(new File(configPath));
		root = jdomDoc.getRootElement();
		
		Element controllerNode = root
				.getChild("controllers")
				.getChildren()
				.stream()
				.filter(controller -> controller.getAttributeValue("name").equals(name))
				.findFirst()
				.orElse(null)
				;
		
		if (controllerNode == null) {
			return false;
		}
		
		// load logger
		if (loggerI != null) {
			logger = loggerI;
		}
		else {
			logger = new DefaultLogger();
		}
		// load rules
		List<Element> rules = controllerNode.getChild("rules").getChildren("rule");
		if (ruleList != null) {
			for (Element rule : rules) {
				Rule r = new Rule();
				r.setDomain(rule.getChildText("domain"));
				
				// load proxy
				ProxyI proxyI = null;
				String proxyName = rule.getChildText("proxy");
				if (!StringUtils.isEmpty(proxyName)) {
					List<Element> proxys = root.getChild("Proxys").getChildren("Proxy");
					for (Element proxy : proxys) {
						String proxyType = proxy.getAttributeValue("type");
						if (proxyType.endsWith("dynamic") && proxyName.equals(proxy.getAttributeValue("name"))) {
							proxyI = DynamicProcy.load(logger, proxy);
						}
						else if(proxyType.endsWith("aidaili") && proxyName.equals(proxy.getAttributeValue("name"))) {
							proxyI = AidailiProxy.load(logger, proxy);
						}
					}
				}
				
				// load header
				HttpHeader header = HttpHeader.load(root.getChild("headers")
						.getChildren("headermap")
						.stream()
						.filter(h -> h.getAttributeValue("name").equals(rule.getChildText("header")))
						.findFirst()
						.orElse(null));
				
				// load limit
				String limit = rule.getChildText("limit");
				r.setLimit(StringUtils.isEmpty(limit) ? 10 : Integer.parseInt(limit));
				
				r.setProxy(proxyI);
				r.setHeader(header);
				
				ruleList.put(r.getDomain(), r);
			}
		}
		
		return true;
	}
	
	private static String getDomain(String url) {
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		Pattern p = Pattern.compile("([\\w-]+\\.)+[\\w-]+/?",Pattern.CASE_INSENSITIVE);  
		Matcher matcher = p.matcher(url);  
		matcher.find();
		return matcher.group().substring(0, matcher.group().length()- 1);
	}
	
	private static Rule getRule(String domain){
		if (StringUtils.isEmpty(domain)) {
			return null;
		}
		
		return ruleList.get(domain);
	}
	
	private static void logProxyBlocked(Rule rule, String url) {
		if (rule != null) {
			ProxyBase proxyB = (ProxyBase)rule.getProxy();
			logger.proxyBlocked(url, proxyB.getName(), rule.getHeader() == null ? "NotUserHeader" : rule.getHeader().getName(), rule.getDomain());
		}
		else {
			logger.proxyBlocked(url, "NotUserProxy", "NotUserHeader", "NotUserDomain");
		}
	}
	
	
	public static String getGetResponseWithHttpClient(String url, String encode, Map<String, String> headerMap, int limit) {
		
		Rule rule = getRule(getDomain(url));
		
		if (rule != null && limit == rule.getLimit() - 1) {
			rule = null;
		}
		else if (rule != null && limit == rule.getLimit()) {
			return null;
		}
		
		Connection conn = null;
		if (!initialed) {
			SetPara();
		}
		
		HttpClient client = new HttpClient(manager);
		
		GetMethod get = new GetMethod(url);
		get.setFollowRedirects(true);
		
		if (headerMap != null) {
			for (String headerName : headerMap.keySet()) {
				String headerValue = headerMap.get(headerName);
				get.addRequestHeader(headerName, headerValue);
			}
		}
		if (rule != null) {
			
			if (rule.getProxy() != null && !rule.getProxy().isWornOut()) {
				conn = rule.getProxy().getConnection();
				client.getHostConfiguration().setProxy(conn.getIp(), 
						Integer.valueOf(conn.getPort())); 
				client.getParams().setAuthenticationPreemptive(true);
			}
			
			if (rule.getHeader() != null) {
				for (String headerName : rule.getHeader().getHeaderMap().keySet()) {
					String headerValue = rule.getHeader().getHeaderMap().get(headerName);
					get.addRequestHeader(headerName, headerValue);
				}
			}
		}
		
		String result = null;
		StringBuffer resultBuffer = new StringBuffer();
		
		try {

			client.executeMethod(get);
			
			if (get.getStatusCode() != 200 && conn != null) {
				logger.error("the StatusCode is : " + get.getStatusCode());
				logger.error("the limit is : " + limit);
				rule.getProxy().removeConnection(conn);
				logProxyBlocked(rule, url);
				return getGetResponseWithHttpClient(url, encode, headerMap, limit + 1);
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(
					get.getResponseBodyAsStream(), get.getResponseCharSet()));

			String inputLine = null;

			while ((inputLine = in.readLine()) != null) {
				resultBuffer.append(inputLine);
				resultBuffer.append("\n");
			}

			in.close();

			result = resultBuffer.toString();

			result = ConverterStringCode(
					resultBuffer.toString(), get.getResponseCharSet(), encode);
			return result;
		} catch (Exception e) {
			logger.error("getGetResponseWithHttpClient : nuknown error", e);
			logger.error("the limit is : " + limit);
			rule.getProxy().removeConnection(conn);
			logProxyBlocked(rule, url);
			return getGetResponseWithHttpClient(url, encode, headerMap, limit + 1);
		}
		finally {
			get.releaseConnection();
		}
	
	}

	public static String getGetResponseWithHttpClient(String url, String encode, Map<String, String> headerMap) {
		return  getGetResponseWithHttpClient(url, encode, headerMap, 0);
	}
}
