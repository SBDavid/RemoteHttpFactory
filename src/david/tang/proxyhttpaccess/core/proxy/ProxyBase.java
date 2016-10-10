package david.tang.proxyhttpaccess.core.proxy;

import java.util.ArrayList;
import java.util.List;

import david.tang.proxyhttpaccess.logger.NormalLoggerI;

public class ProxyBase{
	protected String name;
	protected String type;
	
	protected List<Connection> connections = new ArrayList<Connection>();
	
	protected static NormalLoggerI logger;
	
	public boolean isWornOut() {
		return connections.size() == 0;
	}
	
	public void removeConnection(Connection connToBeBlock) {
		connections.removeIf(conn -> conn.equals(connToBeBlock));
		logger.proxyRemoved(this.name, this.type,connToBeBlock.getIp(), connToBeBlock.getPort(), this.connections.size() + "");
	}
	
	public void addConnection(Connection connection) {
		this.connections.add(connection);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public NormalLoggerI getLogger() {
		return logger;
	}
	public void setLogger(NormalLoggerI logger) {
		this.logger = logger;
	}
}
