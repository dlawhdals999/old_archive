package tcpblockingchannel;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame {
	SocketChannel socketChannel;
	
	public Client(){
		super("Zac`s client program");
		initJFrame();
	}

	// ���� ���� �ڵ�
	void startClient() {
		Thread thread=new Thread(){
			@Override 
			public void run(){
				//���� 
				try{
					socketChannel=SocketChannel.open(); //Socket Channel ����
					socketChannel.configureBlocking(true); //��ŷ ��� ����(�⺻����)
					socketChannel.connect(new InetSocketAddress("localhost",5001)); //5001��Ʈ�� ����
					//UIó��
					displayText("[����Ϸ� : "+socketChannel.getRemoteAddress()+"]");
					btnConn.setText("stop");
					btnSend.setEnabled(true);										
				}catch(Exception e){
					displayText("[���� ��� �ȵ�]");
					if(socketChannel.isOpen())
						stopClient();
					return;
				}
				
				//�������� ���� ������ �ޱ�
				receive(); 
				
			}			
		};
		thread.start(); //������ ����
	}

	// ���� ���� �ڵ�
	void stopClient() {
		try{
			//UI ó��
			displayText("[���� ����]");
			btnConn.setText("start");
			btnSend.setEnabled(false);
			
			//socketChannel�� ���� ������ �ݱ� 
			if(socketChannel!=null && socketChannel.isOpen())
				socketChannel.close();
		}catch(IOException e){}
	}
	
	// ������ �ޱ� �ڵ�
	void receive() {
		while(true){
			try{
				ByteBuffer byteBuffer=ByteBuffer.allocate(100); //ByteBuffer 100����Ʈ �Ҵ�
				
				//������ ������������ �������� ��� IOExcpetion �߻�
				int readByteCount=socketChannel.read(byteBuffer); //������ ������ �������� ���� ������ ��ŷ ������ 
				
				//������ ���������� Socket�� close()�� ȣ������ ��� IOException �߻� ��Ű��
				if(readByteCount==-1)
					throw new IOException();
				
				byteBuffer.flip(); // limit�� pos�� pos 0 ����
				Charset charset=Charset.forName("UTF-8");
				String data=charset.decode(byteBuffer).toString(); //byteBuffer�� �ִ� ������ UTF-8�� ���ڵ� & CharBuffer.toString
				
				displayText("[����] : "+data);				
			}catch(Exception e){
				displayText("[���� ��� �ȵ�]");
				stopClient(); 
				break; //while�� ����
			}
		}
	}

	// ������ ���� �ڵ�
	void send(String data) {
		Thread thread=new Thread(){
			@Override
			public void run(){
				try{
					Charset charset=Charset.forName("UTF-8");
					ByteBuffer byteBuffer=charset.encode(data); //data�� UTF-8�� ���ڵ��� ����Ʈ ������ & ���ۿ� ����
					socketChannel.write(byteBuffer); //���� & ���� �߻� ����
					displayText("[��] : "+data);
				}catch(Exception e){ //wrtie()�������� ���� �߻� �� 
					displayText("[���� ��� �ȵ�]");
					stopClient();
				}
				
			}
		};
		thread.start();
	}
	
	private void displayText(String str){
		txtDisplay.append(str+"\n");
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}

	///////////////////////////
	// UI �����ڵ�
	//////////////////
	// UI �ڵ�
	JTextArea txtDisplay;
	JTextField txtInput;
	JButton btnConn, btnSend;
	JScrollPane scroll;

	void initJFrame() {		
		this.setBounds(300, 300, 400, 400);
		this.setLayout(new BorderLayout());

		txtDisplay = new JTextArea();
		txtDisplay.setEditable(false);		
		scroll=new JScrollPane(txtDisplay);

		txtInput = new JTextField();
		txtInput.addActionListener(event -> {
			send(txtInput.getText());
			txtInput.setText("");
			});

		btnConn = new JButton("start");
		btnConn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnConn.getText().equals("start")) {
					startClient();
				} else if (btnConn.getText().equals("stop")) {
					stopClient();
				}
			}
		});

		btnSend = new JButton("send");
		btnSend.setEnabled(false);
		btnSend.addActionListener(event -> {
			send(txtInput.getText());
			txtInput.setText("");
			});

		Panel panel = new Panel(new BorderLayout());

		panel.add(txtInput, BorderLayout.CENTER);
		panel.add(btnConn, BorderLayout.WEST);
		panel.add(btnSend, BorderLayout.EAST);

		this.add(panel, BorderLayout.SOUTH);
		this.add(scroll, BorderLayout.CENTER);

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setVisible(true);
	}
	
	public void start(){
		this.setVisible(true);
	}
	
	
	public static void main(String[] args){
		Client client=new Client();
		client.start();
	}

}
