package david.tang.proxyhttpaccess.logger;

public interface NormalLoggerI {
	void proxyBlocked(String url, String proxyName, String headerName, String Domain);
	void proxyRemoved(String proxyName, String proxyType, String ip, String port, String connectionRemain);
	void proxyPoolUseOut(String proxyName, String proxyType, String useCounter);
	void displayProxyUseCounter(String proxyUseCounter);
	
	void error(String msg);
	void error(String msg, Throwable e);
	void info(String msg);
}
