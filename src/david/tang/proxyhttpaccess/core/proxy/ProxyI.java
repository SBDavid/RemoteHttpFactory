package david.tang.proxyhttpaccess.core.proxy;

public interface ProxyI {
	/**
	 * ��������Ƿ����ʹ��
	 * @return Ture������ʹ��|False���Ѿ�������
	 */
	boolean isWornOut();
	/**
	 * ֪ͨ�������������������������Ѿ�������
	 * @param connection ��������
	 */
	void removeConnection(Connection connection);
	/**
	 * ��Ӵ�������
	 * @param connection
	 */
	void addConnection(Connection connection);
	/**
	 * ��ȡ��������
	 * @return ���õĴ�������
	 */
	Connection getConnection();
}
