package nonblocking;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server extends JFrame {
	private static int cnt=1;
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private List<Client> connections=new Vector<>();
	
	
	//���� ���� �ڵ�
	private void startServer(){
		try{
			selector=Selector.open(); //Selector ����
			serverSocketChannel=ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false); //�ͺ��ŷ ����
			serverSocketChannel.bind(new InetSocketAddress(5001));
			
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); //Selector�� �۾� ���� ���
		}catch(Exception e){
			if(serverSocketChannel.isOpen())
				stopServer();
			return;
		}
		
		Thread thread=new Thread(){
			@Override
			public void run(){
				while(true){
					try{
						int keyCount=selector.select(); //�۾� ó�� �غ� �� ä���� ���� ������ ���
						if(keyCount==0)
							continue;
						Set<SelectionKey> selectedKeys=selector.selectedKeys();
						Iterator<SelectionKey> itr=selectedKeys.iterator();
						while(itr.hasNext()){
							SelectionKey selectionKey=itr.next();
							if(selectionKey.isAcceptable()){ //���� ���� �۾��� ���
								accept(selectionKey);
							}else if(selectionKey.isReadable()){ // �б� �۾��� ���
								Client client=(Client)selectionKey.attachment();
								client.receive(selectionKey);
							}else if(selectionKey.isWritable()){ //���� �۾��� ���
								Client client=(Client)selectionKey.attachment();
								client.send(selectionKey);
							}
							itr.remove(); //���õ� Ű�¿��� ó�� �Ϸ�� SelectionKey�� ����
						}						
					}catch(Exception e){
						if(serverSocketChannel.isOpen())
							stopServer();
						break;
					}
				}
			}
		};
		thread.start(); //������ ����
		displayText("[���� ����]");
		btnStartStop.setText("stop");		
	}
	
	
	//���� ���� �ڵ�
	private void stopServer(){
		try{
			Iterator<Client> itr=connections.iterator();
			while(itr.hasNext()){
				Client client=itr.next();
				client.socketChannel.close();
				itr.remove();
			}
			if(serverSocketChannel!=null && serverSocketChannel.isOpen())
				serverSocketChannel.close();
			if(selector!=null && selector.isOpen())
				selector.close();
			
			displayText("[���� ����]");
			btnStartStop.setText("start");
		}catch(Exception e){
			
		}
	}
	
	//���� ���� �ڵ�
	private void accept(SelectionKey selectionKey){
		try{
			//ServerSocketChannel ���
			ServerSocketChannel serverSocketChannel=(ServerSocketChannel)selectionKey.channel();
			//���� ����
			SocketChannel socketChannel=serverSocketChannel.accept();
			
			String message="[���� ���� : "+socketChannel.getRemoteAddress()+": "+Thread.currentThread().getName()+"]";
			displayText(message);
			
			Client client=new Client(socketChannel);
			connections.add(client);
			
			displayText("[���� ���� : "+connections.size()+"]");			
		}catch(Exception e){
			if(serverSocketChannel.isOpen())
				stopServer();
		}
	}
	
	//������ ��� �ڵ�
	class Client{
		int id;
		SocketChannel socketChannel;
		String sendData;
		
		Client(SocketChannel socketChannel) throws IOException {
			id=cnt++;
			this.socketChannel=socketChannel;
			socketChannel.configureBlocking(false);			
			SelectionKey selectionKey=socketChannel.register(selector, SelectionKey.OP_READ); //�б� �۾� �������� Selector�� ���
			selectionKey.attach(this); //SelectionKey�� �ڱ� �ڽ��� ÷�� ��ü�� ����
		}
		
		//������ �ޱ� �ڵ�
		void receive(SelectionKey selectionKey){
			try{
				ByteBuffer byteBuffer=ByteBuffer.allocate(100);
				
				//������ ������ ���Ḧ ���� ��� �ڵ� IOException �߻�
				int byteCount=socketChannel.read(byteBuffer);
				
				//������ SocketChannel�� close() �޼ҵ带 ȣ���� ���
				if(byteCount==-1)
					throw new IOException();
				
				String message="[��û ó�� : "+socketChannel.getRemoteAddress()+": "+Thread.currentThread().getName()+"]";
				displayText(message);
				
				byteBuffer.flip();
				Charset charset=Charset.forName("UTF-8");
				String data=Client.this.id+"] : "+charset.decode(byteBuffer).toString();
				sendAll(data);								
			}catch(Exception e){
				try{
					String data=Client.this.id+"]���� �������ϴ�.";
					sendAll(data);
					connections.remove(this);
					String message="[Ŭ���̾�Ʈ ��� �ȵ� : "+socketChannel.getRemoteAddress()+": "+Thread.currentThread().getName()+"]";
					displayText(message);
					socketChannel.close();
				}catch(IOException e2){
					
				}				
			}
		}
		
		void sendAll(String data){
			for(Client client : connections){
				if(Client.this!=client){
					client.sendData=data;
					SelectionKey key=client.socketChannel.keyFor(selector);
					key.interestOps(SelectionKey.OP_WRITE);
				}
			}
			selector.wakeup(); //����� �۾� ������ �����ϵ��� �ϱ� ���� Selector�� select() ���ŷ�� �����ϰ� �ٽ� ����
		}
		
		
		//������ ���� �ڵ�
		void send(SelectionKey selectionKey){
			try{
				Charset charset=Charset.forName("UTF-8");
				ByteBuffer byteBuffer=charset.encode(sendData);
				socketChannel.write(byteBuffer);
				selectionKey.interestOps(SelectionKey.OP_READ); //�۾� ���� ����
				selector.wakeup(); //����� �۾� ������ �����ϵ��� Selector�� select() ���ŷ ����
			}catch(Exception e){
				try{
					String message="[Ŭ���̾�Ʈ ��� �ȵ�: "+socketChannel.getRemoteAddress()+": "+Thread.currentThread().getName()+"]";
					displayText(message);
					connections.remove(this);
					socketChannel.close();
				}catch(IOException e2){
					
				}
			}
		}
	}
	
	
	public Server(){
		initJFrame();
	}
	

	//////////////////
	// UI �ڵ�
	private JTextArea txtDisplay;
	private JButton btnStartStop;
	private JScrollPane scroll;

	public void initJFrame() {
		this.setBounds(120, 200, 400, 400);
		this.setLayout(new BorderLayout());

		txtDisplay = new JTextArea();
		txtDisplay.setEditable(false);
		scroll = new JScrollPane(txtDisplay);

		btnStartStop = new JButton("start");
		btnStartStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnStartStop.getText().equals("start")) {
					startServer();
				} else if (btnStartStop.getText().equals("stop")) {
					stopServer();
				}
			}
		});

		this.add(scroll, BorderLayout.CENTER);
		this.add(btnStartStop, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
	}	
	
	private void displayText(String text){		
		txtDisplay.append(text+"\n");
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}
	
	

	public void start() {
		this.setVisible(true);
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.start();

	}
}
