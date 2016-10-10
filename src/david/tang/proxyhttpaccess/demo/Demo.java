package david.tang.proxyhttpaccess.demo;

import java.io.IOException;

import org.jdom2.JDOMException;

import david.tang.proxyhttpaccess.factory.Controller;


public class Demo {
	
	public static void main(String[] args) {

		try {
			Controller.loadConfig("test", "remotehttpfactory.xml", null);
			

//			System.out.println(Controller.getGetResponseWithHttpClient("http://hq.sinajs.cn/list=sz399942", "GBK", null));
//			System.out.println(Controller.getGetResponseWithHttpClient("http://hq.sinajs.cn/list=sz399942", "GBK", null));
//			System.out.println(Controller.getGetResponseWithHttpClient("http://hq.sinajs.cn/list=sz399942", "GBK", null));
//			System.out.println(Controller.getGetResponseWithHttpClient("http://hq.sinajs.cn/list=sz399942", "GBK", null));
//			System.out.println(Controller.getGetResponseWithHttpClient("http://hq.sinajs.cn/list=sz399942", "GBK", null));
//			System.out.println(Controller.getGetResponseWithHttpClient("http://hq.sinajs.cn/list=sz399942", "GBK", null));
//			System.out.println(Controller.getGetResponseWithHttpClient("http://hq.sinajs.cn/list=sz399942", "GBK", null));
			System.out.println(Controller.getGetResponseWithHttpClient("http://hq.sinajs.cn/list=sz399942", "GBK", null));
			System.out.println(Controller.getGetResponseWithHttpClient("https://www.jisilu.cn/data/sfnew/fundm_list/?___t=", "ISO-8859-1", null).substring(0,10));

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
