package remote.client;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;

public class ScreenSender {
	private final int sleepTime = 80;
	private Socket socket;
	private Robot robot;
	private Rectangle rectangle;
	private boolean isContinue ;
	
	public ScreenSender(Socket socket, Robot robot,Rectangle rectangle) {
		this.socket = socket;
		this.robot = robot;
		this.rectangle = rectangle;
		isContinue = true;
		send();
	}
	
	private void send() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				ObjectOutputStream oos = null;
				
				//////////////////
				// ������ Ŭ���̾�Ʈ â ������ ����
				//////////////////
				
				try {
					oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
					oos.writeObject(rectangle); //Ŭ���̾�Ʈ ��ũ�� ������ ����
					oos.flush();
				}catch(IOException e) {
					e.printStackTrace();
				}
				
				////////////////
				//��ü ĸ�� - > ����
				////////////////
				while(isContinue) {
					BufferedImage buffImage = robot.createScreenCapture(rectangle);
					ImageIcon imageIcon = new ImageIcon(buffImage); //BufferedImage�� serializable ���� X
					
					try {
						oos.writeObject(imageIcon);
						oos.reset();
					} catch(IOException e) {						
						e.printStackTrace();
						break;
					}
					
					//////////////////
					// ��Ʈ��ũ IO traffic  
					/////////////////
					try {
						Thread.sleep(sleepTime);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}							
				}	
			}
		};
		thread.start();
	}
	
	public void interrupt() {
		isContinue = false;
	}
	
}















