package web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.ActionFactory;
import main.ServerFrame;
import serverinfo.ServerInfo;
import util.SocketUtil;


/*
 * Class MainServer
 * :���� ��û�� ���� -> �ڵ鷯 -> ����
 */
public class WebServer extends ServerFrame {
	public static ExecutorService executorService;
	private ServerSocket serverSocket;	

	public WebServer() {
		super("Web Server");		
		initFrame();
	}
	
	//���� ����
	public void startServer() {
		///////////////////
		//���߿� �����
		getStartStopButton().setText("stop");		
		///////////////////////
		//������Ǯ <<���� �׽�Ʈ�غ���		
		
		executorService = Executors.newFixedThreadPool(100);
		ActionFactory.getInstance();
		//���� ���ε�
		try {
			serverSocket = new ServerSocket(ServerInfo.WEB_PORT);			
		}catch(IOException e) {
			if(serverSocket!=null && !serverSocket.isClosed()) {
				stopServer();				
			}
			return;
		}		
		//�����û ������ Ǯ ���
		executorService.submit( () -> {			
			while (true) {
				Socket socket = null;
				try {
					socket = serverSocket.accept();
					displayText("connect : "+socket.getInetAddress());
					new ClientHandler(socket);
				} catch (IOException e) {
					SocketUtil.close(socket);
				}
			}
		});		
		displayText("[Web Server start]");
	}	
	
	//���� ����
	public void stopServer(){		
		SocketUtil.close(serverSocket);
		SocketUtil.close(executorService);
	}	
}
