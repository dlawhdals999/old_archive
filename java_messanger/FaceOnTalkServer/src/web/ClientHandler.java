package web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import controller.ActionFactory;
import controller.WebCommandController;
import request.Request;
import util.SocketUtil;


/*
 * Model Class : Client
 * ������� ��û  
 */

public class ClientHandler {
	//variables	
	Socket socket;	
	private static ActionFactory af = ActionFactory.getInstance();
	
	public ClientHandler(Socket socket) {
		this.socket = socket;		
		receive();
	}		
	private void receive() {
		//Client ��û�� �ڵ鸵
		WebServer.executorService.submit( () -> {						
			try {
				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

				// ��ɿ� ���� �ڵ鸵
				Request req = (Request) ois.readObject();
				String type = req.getType();								
				WebCommandController controller = af.getAction(type);
				
				Request response = null;
				if(controller!=null)
					response = controller.execute(req);
				else 
					response = new Request("command_error");
				
				//����
				System.out.println("���� Ÿ�� : "+response.getType());
				oos.writeObject(response);
				oos.flush();
				ois.close();
				oos.close();	
				SocketUtil.close(socket); //���� �ݱ�
			}catch(IOException e) {				
				e.printStackTrace();
				SocketUtil.close(socket);
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}
		});				
	}
}
