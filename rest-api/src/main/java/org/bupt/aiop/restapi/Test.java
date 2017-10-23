package org.bupt.aiop.restapi;

import org.apache.thrift.TException;
import org.bupt.aiop.rpcapi.thrift.client.AlgServiceClient;
import org.bupt.aiop.rpcapi.thrift.inter.AlgService;

public class Test {

	public static void main(String [] args) {

		try {

//		//推荐：采用客户端公钥-服务端私钥的形式保证安全性
//        /*
//         * Similar to the server, you can use the parameters to setup client parameters or
//         * use the default settings. On the client side, you will need a TrustStore which
//         * contains the trusted certificate along with the public key.
//         * For this example it's a self-signed cert.
//         */
//
//		TSSLTransportParameters params = new TSSLTransportParameters();
//		params.setTrustStore("../../lib/java/test/.truststore", "thrift", "SunX509", "JKS");
//
//        /*
//         * Get a client transport instead of a server transport. The connection is opened on
//         * invocation of the factory method, no need to specifically call open()
//         */
//
//		transport = TSSLTransportFactory.getClientSocket("localhost", 9091, 0, params);

			AlgService.Client algClient = AlgServiceClient.getInstance();

			String helloRes = algClient.hello(666);
			String byeRes = algClient.bye();
			System.out.println(helloRes);
			System.out.println(byeRes);

		} catch (TException x) {
			x.printStackTrace();
		}
	}
}