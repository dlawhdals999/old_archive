package util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/*
 * ���� �ݴ� ��ƿ Ŭ����
 */

public class SocketUtil {
	/*
	 * ServerSocket, Socket ,ExecutorService closer
	 */
	public static void close(Socket socket){
		if(socket!=null && !socket.isClosed())
			try{socket.close();}catch(IOException e){}
	}	
	public static void close(ServerSocket socket){
		if(socket!=null && !socket.isClosed())
			try{socket.close();}catch(IOException e){}	
	}	
	public static void close(ExecutorService executorService){		
		if(executorService!=null && !executorService.isShutdown())
			executorService.isShutdown();	
	}	
}
