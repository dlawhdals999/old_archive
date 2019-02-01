package asynchronous;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame{
	private AsynchronousChannelGroup channelGroup;
	private AsynchronousSocketChannel socketChannel;
	
	public Client(){
		super("Zac`s program[Client]");
		initJFrame();
	}
	
	//���� ����
	private void startClient(){
		try{
			channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(), 
					Executors.defaultThreadFactory());
			
			socketChannel = AsynchronousSocketChannel.open(channelGroup);
			socketChannel.connect(new InetSocketAddress("localhost",5001),null,
				new CompletionHandler<Void,Void>(){
					@Override
					public void completed(Void result,Void attachment){
						try{
						displayText("[���� �Ϸ� : "+socketChannel.getRemoteAddress()+"]");
						btnConn.setText("stop");
						btnSend.setEnabled(true);
						}catch(Exception e){}
						receive(); //�������� ���� ������ �ޱ�
					}
					
					@Override
					public void failed(Throwable exc,Void attachment){
						displayText("[���� ��� �ȵ�]");
						if(socketChannel.isOpen())
							stopClient();						
					}
				}
			);	
			txtInput.setEditable(true);
		}catch(IOException e){}
	}
	
	//���� ����
	private void stopClient(){
		try{
			displayText("[���� ����]");
			btnConn.setText("start");
			btnSend.setEnabled(false);
			txtInput.setEditable(false);
			
			if(channelGroup!=null && !channelGroup.isShutdown())
				channelGroup.shutdownNow();
			
		}catch(IOException e){}
	}
	
	//������ �ޱ�(�������� ���� ������)
	private void receive(){
		ByteBuffer byteBuffer = ByteBuffer.allocate(100);
		socketChannel.read(byteBuffer, byteBuffer, 
			new CompletionHandler<Integer,ByteBuffer>(){
				@Override
				public void completed(Integer result,ByteBuffer attachment){
					try{
						attachment.flip();
						Charset charset=Charset.forName("UTF-8");
						String data=charset.decode(attachment).toString();
						displayText("[�͸� "+data);
						
						ByteBuffer byteBuffer=ByteBuffer.allocate(100);
						socketChannel.read(byteBuffer, byteBuffer, this); // �ٽ� ������ �б�
					}catch(Exception e){}
				}
				@Override
				public void failed(Throwable exc,ByteBuffer attachment){
					displayText("[���� ��� �ȵ�]");
					stopClient();
				}
			
			}
		);
	}
	
	//������ ����
	private void send(String data){
		Charset charset=Charset.forName("UTF-8");
		ByteBuffer byteBuffer=charset.encode(data);
		
		socketChannel.write(byteBuffer,null,new CompletionHandler<Integer,Void>(){
			@Override
			public void completed(Integer result,Void attachment){
				displayText("[��] "+data);
			}
			@Override
			public void failed(Throwable exc,Void attachment){
				displayText("[���� ��� �ȵ�]");
				stopClient();
			}		
		});		
	}
	
	
	void displayText(String text) {
		txtDisplay.append(text + '\n');
		//�ڵ����� �ǹ����� ��ũ�� �̵�
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}

	void sendText() {
		send(txtInput.getText());
		txtInput.setText("");
	}
	
	//////////////////
	// UI �ڵ�
	private JTextArea txtDisplay;
	private JTextField txtInput;
	private JButton btnConn, btnSend;	
	private JScrollPane scroll;

	private void initJFrame() {
		this.setBounds(250, 200, 400, 400);
		this.setLayout(new BorderLayout());

		txtDisplay = new JTextArea();
		txtDisplay.setEditable(false);	
		txtDisplay.addFocusListener(new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent event){
				txtInput.requestFocusInWindow();
			}
			
		});
		
		
		scroll = new JScrollPane(txtDisplay);

		txtInput = new JTextField();
		txtInput.setEditable(false);
		txtInput.addActionListener(e -> sendText());

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
		btnSend.addActionListener(e -> sendText());

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
	}

	public void start() {
		this.setVisible(true);
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.start();

	}
}
