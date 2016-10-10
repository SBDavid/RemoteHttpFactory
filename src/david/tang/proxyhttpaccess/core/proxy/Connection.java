package david.tang.proxyhttpaccess.core.proxy;

/**
 * 代理连接
 * @author David Tang
 *
 */
public class Connection{
	private String ip;
	private String port;
	
	public Connection(String ip, String port) {
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	boolean equals(Connection conn) {
		if (conn == null) {
			return false;
		}
		else if (this.ip.equals(conn.getIp()) && this.port.equals(conn.getPort())) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
