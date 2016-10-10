package david.tang.proxyhttpaccess.core.proxy;

public interface ProxyI {
	/**
	 * 这个代理是否可以使用
	 * @return Ture：可以使用|False：已经被屏蔽
	 */
	boolean isWornOut();
	/**
	 * 通知代理代理构造器：这个代理连接已经被屏蔽
	 * @param connection 代理链接
	 */
	void removeConnection(Connection connection);
	/**
	 * 添加代理连接
	 * @param connection
	 */
	void addConnection(Connection connection);
	/**
	 * 获取代理连接
	 * @return 可用的代理连接
	 */
	Connection getConnection();
}
