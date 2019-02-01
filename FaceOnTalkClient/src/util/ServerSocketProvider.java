package util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/*
 * �̿� ������ port�� ���� ���� �����ִ� Ŭ����
 */
class NotExistAvailablePort extends Exception {	
}

public class ServerSocketProvider {
	private static int startPort = 9000;
	private static int trySize = 5;

	public static ServerSocket getServerSocket(String ip) {		
		ServerSocket serverSocket = null;
		while(true) {
			try {
				serverSocket = createServerSocket(ip,startPort);
				break;
			} catch(NotExistAvailablePort e) {
				startPort += trySize;
			}			
		}
		return serverSocket;		
	}
	
	private static ServerSocket createServerSocket(String ip,int startPort) throws NotExistAvailablePort {
		int[] ports = new int[trySize]; //e.g) 9000~9004���� ��Ʈ �迭�� ����
		for(int i=0;i<ports.length;i++) {
			ports[i] = startPort+i;
		}
		for(int port : ports) {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(ip,port));
				return serverSocket; //���� ������ ��ȯ �ƴϸ� catch������ ���� continue								
			} catch(IOException e) {
				if(serverSocket!=null && !serverSocket.isClosed())
					try{serverSocket.close();}catch(IOException e2) {}
				continue;
			}
		}
		throw new NotExistAvailablePort(); //9000~9004���� ��� �Ұ����ϸ� ���� getServerSocket()�޼ҵ忡 catch�κ� -> 9005�� �ٲ��༭ �ݺ�
	}		
	//private constructor
	private ServerSocketProvider(){}
}
