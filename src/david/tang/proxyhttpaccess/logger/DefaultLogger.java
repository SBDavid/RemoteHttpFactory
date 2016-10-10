package david.tang.proxyhttpaccess.logger;

public class DefaultLogger implements NormalLoggerI {

	@Override
	public void proxyBlocked(String url, String proxyName, String headerName,
			String Domain) {
		System.out.println(String.format("procy is blocked, url : %s", url));
		System.out.println(String.format("proxyname : %s", proxyName));
		System.out.println(String.format("headername : %s", headerName));
		System.out.println(String.format("Domain : %s", Domain));
	}

	@Override
	public void proxyRemoved(String proxyName, String proxyType, String ip, String port,
			String connectionRemain) {
		System.out.println(String.format("procy is removed, proxyName : %s", proxyName));
		System.out.println(String.format("proxyType : %s", proxyType));
		System.out.println(String.format("ip : %s", ip));
		System.out.println(String.format("port : %s", port));
		System.out.println(String.format("connectionRemain : %s", connectionRemain));

	}

	@Override
	public void proxyPoolUseOut(String proxyName, String proxyType,
			String useCounter) {
		System.out.println(String.format("procy pool is user, proxyName : %s", proxyName));
		System.out.println(String.format("proxyType : %s", proxyType));
		System.out.println(String.format("useCounter : %s", useCounter));

	}

	@Override
	public void displayProxyUseCounter(String proxyUseCounter) {
		System.out.println(String.format("proxyUseCounter : %s", proxyUseCounter));

	}

	@Override
	public void error(String msg) {
		System.out.println(msg);

	}

	@Override
	public void error(String msg, Throwable e) {
		System.out.println(e.getMessage());

	}

	@Override
	public void info(String msg) {
		System.out.println(msg);

	}

}
