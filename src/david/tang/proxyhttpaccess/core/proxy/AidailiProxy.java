package david.tang.proxyhttpaccess.core.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Element;

import david.tang.proxyhttpaccess.logger.NormalLoggerI;

public class AidailiProxy extends ProxyBase implements ProxyI, Cloneable{

	private int useCounter = 0;
	
	private static String requestUrl;
	
	@Override
	public boolean isWornOut() {
		if (this.connections.size() == 0) {
			PullNewConnection();
		}
		return this.connections.size() == 0;
	}
	
	private boolean PullNewConnection() {
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(requestUrl);
		String result = null;
		StringBuffer resultBuffer = new StringBuffer();
		try {
			client.executeMethod(get);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					get.getResponseBodyAsStream(), get.getResponseCharSet()));

			String inputLine = null;

			while ((inputLine = in.readLine()) != null) {
				resultBuffer.append(inputLine);
				resultBuffer.append("\n");
			}

			in.close();

			result = resultBuffer.toString();
			
			if (StringUtils.isEmpty(result) || result.startsWith("ERROR")) {
				logger.error("AidailiProxy : fail to get connection !");
				if (!StringUtils.isEmpty(result)) {
					logger.error(result);
				}
				return false;
			}
			
			String[] connections = result.split("\n");
			for (String connection : connections) {
				String[] ip_port = connection.split(":");
				addConnection(new Connection(ip_port[0], ip_port[1]));
			}
			
			return true;
		} catch (IOException e) {
			logger.error("AidailiProxy : fail to get connection !", e);
			logger.error(result);
			return false;
		}
		finally {
			get.releaseConnection();
		}
	}
	
	@Override
	public void addConnection(Connection connection) {
		this.connections.add(connection);
	}

	@Override
	public Connection getConnection() {
		if (isWornOut()) {
			logger.proxyPoolUseOut(this.name, this.type, this.useCounter + "");
			return null;
		}
		
		int useableConnectionAmount = this.connections.size();
		int fatchConnectionIndex = this.useCounter % useableConnectionAmount;
		
		if (useCounter % 100 == 0) {
			logger.displayProxyUseCounter(useCounter + "");
		}
		this.useCounter++;
		return this.connections.get(fatchConnectionIndex);
	}
	
	public static ProxyI load(NormalLoggerI loggerI, Element e) {
		if (e == null) {
			return null;
		}
		
		logger = loggerI;
		
		String type = e.getAttributeValue("type");
		String name = e.getAttributeValue("name");
		String api = e.getChildText("api");
		StringBuffer params = new StringBuffer();
		
		for (Element param : e.getChild("params").getChildren("param")) {
			params.append(param.getText());
			params.append("&");
		}
		
		AidailiProxy.requestUrl = api + "?" + params.toString();
		AidailiProxy proxy = new AidailiProxy();
		proxy.setLogger(loggerI);
		proxy.setName(name);
		proxy.setType(type);
		
		return proxy;
	}
}
