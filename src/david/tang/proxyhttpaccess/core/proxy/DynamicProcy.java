package david.tang.proxyhttpaccess.core.proxy;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import david.tang.proxyhttpaccess.logger.NormalLoggerI;

public class DynamicProcy extends ProxyBase implements ProxyI, Cloneable{
	
	
	
	private int useCounter = 0;
	

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
	
	public List<Connection> getConnections() {
		return connections;
	}

	public void setConnections(List<Connection> connections) {
		this.connections = connections;
	}

	public int getUseCounter() {
		return useCounter;
	}

	public void setUseCounter(int useCounter) {
		this.useCounter = useCounter;
	}

	public Object clone() {  
		DynamicProcy proxy = new DynamicProcy();  
        proxy.setName(this.name);
		proxy.setType(this.type);
		proxy.setUseCounter(this.useCounter);
		if (this.connections != null) {
			for (Connection conn : this.connections) {
				proxy.connections.add(new Connection(conn.getIp(), conn.getPort()));
			}
			
		}  
        return proxy;  
    }  
	
	public static ProxyI load(NormalLoggerI loggerI, Element e) {
		if (e == null) {
			return null;
		}
		
		logger = loggerI;
		
		String type = e.getAttributeValue("type");
		String name = e.getAttributeValue("name");
		if (type.equals("dynamic")) {
			DynamicProcy dynamicProcy = new DynamicProcy();
			dynamicProcy.setType(type);
			dynamicProcy.setName(name);
			
			List<Element> connections = e.getChildren("connection");
			if (connections != null) {
				for (Element connection : connections) {
					dynamicProcy.addConnection(new Connection(connection.getAttributeValue("ip"), connection.getAttributeValue("port")));
				}
			}
			
			return dynamicProcy;
		}
		else {
			return null;
		}
	}
}
