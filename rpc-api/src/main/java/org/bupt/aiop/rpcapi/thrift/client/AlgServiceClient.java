package org.bupt.aiop.rpcapi.thrift.client;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.bupt.aiop.rpcapi.constant.RpcEnvConsts;
import org.bupt.aiop.rpcapi.thrift.inter.AlgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ken on 2017/10/23.
 */
@Component
public class AlgServiceClient {

	public static volatile AlgService.Client instance = null;

	@Autowired
	private static RpcEnvConsts rpcEnvConsts;


	private AlgServiceClient () {}

	public static AlgService.Client getInstance() {

		if(instance == null) {

			synchronized (AlgServiceClient.class) {

				if(instance == null) {

					System.out.println(rpcEnvConsts.ALG_SERVER_IP);
					System.out.println(rpcEnvConsts.ALG_SERVER_PORT);

					TTransport transport = new TSocket(rpcEnvConsts.ALG_SERVER_IP, rpcEnvConsts.ALG_SERVER_PORT);


					try {
						transport.open();
					} catch (TTransportException e) {
						e.printStackTrace();
					}
					TProtocol protocol = new TBinaryProtocol(transport);
					instance = new AlgService.Client(protocol);
				}
			}
		}

		return instance;
	}
}
