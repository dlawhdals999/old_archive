package remote.server;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import noticeframe.MessageFrame;

public class RemoteServer extends JFrame {
	private ServerSocket serverSocket;
	private Socket socket;
	
	ScreenReceiver screenReceiver;
	CommandsSender commSender;
	
	public RemoteServer(ServerSocket serverSocket) {
		super("Face On Talk Remote Service");
		this.serverSocket = serverSocket;		
		init();
		startServer();
	}	
	
	private void startServer() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {					
					socket = serverSocket.accept();
					receive();			
				} catch(IOException e) {
					e.printStackTrace();
					new MessageFrame("������ ������ϴ�.",false);
					dispose();
				}				
			}
		};
		thread.start();		
	}	
	private void receive() {		
		//////////////
		//�ʱ� ����� ��ũ�� Rectangle�ޱ�
		/////////////
		Rectangle clientScreenDim = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			clientScreenDim = (Rectangle) ois.readObject();			
		} catch(IOException e) {
			e.printStackTrace();
			if(screenReceiver!=null) {
				screenReceiver.interrupt();
			}						
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		screenReceiver = new ScreenReceiver(ois,centerPanel); // Client -> Server ��ũ���� receive
		commSender = new CommandsSender(socket,centerPanel,clientScreenDim); //Server -> Client ���콺,Ű���� �̺�Ʈ send
	}
		
	////////////////////////////
	//GUI
	private JDesktopPane desktopPane = new JDesktopPane();
	private JInternalFrame internalFrm = new JInternalFrame("����� ȭ��",true,true,true);
	private JPanel centerPanel = new JPanel();
	private void init() {
		////////////////
		//init frame				    
	    setLayout(new BorderLayout());
		add(desktopPane,BorderLayout.CENTER);
		setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		
		internalFrm.setLayout(new BorderLayout());
		internalFrm.getContentPane().add(centerPanel,BorderLayout.CENTER);
		internalFrm.setSize(100,100);
		desktopPane.add(internalFrm);
		try {
			internalFrm.setMaximum(true);
		} catch(PropertyVetoException e) {
			e.printStackTrace();
		}
		centerPanel.setFocusable(true);	
		internalFrm.setVisible(true);
		setVisible(true);		
	}
}
