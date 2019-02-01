package tcpblockingchannel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server extends JFrame {
	private ExecutorService executorService; //������ Ǯ
	private ServerSocketChannel serverSocketChannel; //���� ���� ä��
	private List<Client> connections=new Vector<Client>(); //���� Client ����
	
	public Server(){
		super("Zac`s Chatting program");
		initJFrame();
	}
	
	private void startServer(){
		//���� ���� �ڵ�
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); //�ھ� �� ��ŭ �۾� ������ ����
		
		//5001�� ��Ʈ���� Ŭ���̾�Ʈ�� ������ �����ϴ� ServerSocketChannel ����
		try{
			serverSocketChannel=ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(true); //��ŷ ���(�⺻����)
			serverSocketChannel.bind(new InetSocketAddress(5001)); //5001 ��Ʈ ��ȣ�� ���ε�
			//5001��Ʈ�� ������̸� BindException �߻�
		}catch(Exception e){
			if(serverSocketChannel.isOpen())
				stopServer();
			return;
		}
		//���� �����ϴ� ������ ����
		Runnable runnable=new Runnable(){
			@Override
			public void run(){
				displayText("[���� ����]");
				btnStartStop.setText("stop");				
				//���� ����
				while(true){
					try{
						System.out.println("���� �����");
						SocketChannel socketChannel=serverSocketChannel.accept(); //���� ����(���� ������ ���ŷ�Ǿ�����)
						System.out.println("���� �Ϸ�");
						String message="[���� ���� : "+socketChannel.getRemoteAddress()+": "
										+Thread.currentThread().getName()+" ]";
						displayText(message);
						
						Client client=new Client(socketChannel);
						connections.add(client);
						
					}catch(Exception e){
						if(!serverSocketChannel.isOpen())
							stopServer();
						break;
					}
				}
				
			}
		};
		executorService.submit(runnable); //������Ǯ�� ���		
	}

	private void stopServer(){
		//���� ���� �ڵ�		
		try{
			//��� SocketChannel �ݱ�
			Iterator<Client> iterator=connections.iterator();
			while(iterator.hasNext()){
				Client client=iterator.next();
				client.socketChannel.close();
				iterator.remove();
			}
			
			//ServerSocketChannel �ݱ�
			if(serverSocketChannel!=null && !executorService.isShutdown())
				serverSocketChannel.close();
			
			//ExecutorService ����
			if(executorService!=null && !executorService.isShutdown())
				executorService.shutdown();
			
			//UIó��
			displayText("[���� ����]");
			btnStartStop.setText("start");			
		}catch(Exception e){}
	}
	
	//Client Ŭ���� : ������ �����ϴ� Ŭ���̾�Ʈ ����
	private class Client{
		SocketChannel socketChannel;
		
		Client(SocketChannel socketChannel){
			this.socketChannel=socketChannel;
			receive();
		}
		
		//������ �ޱ�
		void receive(){
			Runnable runnable=new Runnable(){
				@Override
				public void run(){
					while(true){
						try{
							ByteBuffer byteBuffer=ByteBuffer.allocate(100);
						
							//Ŭ���̾�Ʈ�� ������ ���Ḧ ���� ��� IOException �߻�
							int readByteCount=socketChannel.read(byteBuffer);
							
							//Ŭ���̾�Ʈ�� ���������� SocketChannel�� close()�� ȣ��
							if(readByteCount==-1)
								throw new IOException();
							
							String message="[��û ó�� : "+socketChannel.getRemoteAddress()+": "
												+Thread.currentThread().getName()+"]";
							displayText(message);
							
							//���ڿ��� ��ȯ
							byteBuffer.flip();
							Charset charset=Charset.forName("UTF-8");
							String data=charset.decode(byteBuffer).toString();
							
							//��� Ŭ���̾�Ʈ���� ����
							for(Client client : connections){
								if(client.socketChannel!=socketChannel)
									client.send(data);			
							}
						}catch(Exception e){
							try{
								connections.remove(Client.this);
								String message="[Ŭ���̾�Ʈ ��� �ȵ� : "+socketChannel.getRemoteAddress()+": "
											+Thread.currentThread().getName()+"]";
								displayText(message);
								socketChannel.close();
							}catch(IOException e2){}
							break;
						}
						
					}
				}
			};
			//������Ǯ���� ó��
			executorService.submit(runnable);			
		}
		
		//������ ���� 
		void send(String data){
			Runnable runnable=new Runnable(){
				@Override
				public void run(){
					try{
						Charset charset=Charset.forName("UTF-8"); 
						ByteBuffer byteBuffer=charset.encode(data); //data�� UTF-8�� ���ڵ� & byteBuffer�� ����Ʈ�� ����
						socketChannel.write(byteBuffer);						
					}catch(Exception e){ //write()�޼ҵ� ���� �� ���� �߻� 
						try{
							String message="[Ŭ���̾�Ʈ ��� �ȵ�: "+socketChannel.getRemoteAddress()+": "
											+Thread.currentThread().getName()+"]";
							displayText(message); //�޽��� ���
							connections.remove(Client.this); //connections �÷��ǿ��� ���ܰ� �߻��� Client ����
							socketChannel.close(); 
						}catch(IOException e2){}
					}
				}
			};
			executorService.submit(runnable);			
		}
	}
	
	private void displayText(String txt){
		txtDisplay.append(txt+"\n");
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}
	
	/////////////////////////
	//UI ���� �ڵ�
	private JTextArea txtDisplay;
	private JButton btnStartStop;
	private JScrollPane scroll;
	
	private void initJFrame() {		
		this.setBounds(120, 200,400, 200);
		this.setLayout(new BorderLayout());

		txtDisplay = new JTextArea();
		txtDisplay.setEditable(false);
		
		scroll=new JScrollPane(txtDisplay);		

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
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed(WindowEvent e){
				System.exit(0);
			}
		});		
	}
	
	public void start(){
		this.setVisible(true);
	}
		
	
	
	//Main �Լ�
	public static void main(String[] args){
		Server server=new Server();
		server.start();
		
	}
	

}
