package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.ServerSocket;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import connector.WebConnector;
import controller.ClientHandler;
import controller.MemberController;
import controller.MessageController;
import dto.ScheduleVO;
import file.sender.FileSenderController;
import game.searchmine.MineFrame;
import noticeframe.MessageFrame;
import remote.server.RemoteServer;
import request.Request;
import util.ButtonCursorHandler;
import util.ServerSocketProvider;
import util.UserGroupParser;

public class ChatFrame extends JFrame implements ActionListener{	
	private static final long serialVersionUID = 1L;
	
	private String groupId;
	private String user;	
	private boolean isConnected = false;	
	private ClientHandler clientHandler = ClientHandler.getInstance();	
	
	//���� ó�� ä�� ��û�Ҷ� ������
	public ChatFrame(String title,String groupId,String user) {
		super(title);		
		this.groupId = groupId;
		this.user = user;
		action();		
		init();		
	}	
	//ģ������ ä�� �޽��� �ö� ������
	public ChatFrame(String title,String groupId,String user,String content) {
		this(title,groupId,user);
		chatDisplay.append(content+"\n");
		isConnected = true;
	}
		
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == send_bt) { //�޽��� ����			
			sendMessageHandler();
		} 
		else if(isConnected) { //���� ������ ��츸 ��ư �̺�Ʈ ó��
			if (e.getSource() == file_bt) {
				fileHandler();
			} else if (e.getSource() == game_bt) {
				gameHandler();
			} else if (e.getSource() == cal_bt) {				
				calendarHandler();	
			} else if (e.getSource() == remote_bt) {
				remoteHandler();
			}
		}
	}
		
	//���� ���� �ڵ鷯(�̱���)
	private void fileHandler() {
		JFileChooser fileChooser = new JFileChooser(".");
		int res = fileChooser.showOpenDialog(null);
		if(res == JFileChooser.APPROVE_OPTION) {
			//���� ����
			File selectedFile = fileChooser.getSelectedFile();
			
			//ip,port ���
			String ip = MemberController.getInstance().getIP();
			ServerSocket serverSocket = ServerSocketProvider.getServerSocket(ip);
			
			Request request = new Request("file_req");			
			request.setParameter("groupid",groupId);
			request.setAttribute("file",selectedFile);						
			request.setParameter("port", String.valueOf(serverSocket.getLocalPort()));
			request.setParameter("ip",ip);
			request.setParameter("from",user);
			clientHandler.send(request);
			new FileSenderController(serverSocket,selectedFile);			
		} 
	}
	
	@SuppressWarnings("unchecked")
	//������ �ڵ鷯(������ ��û ���� ��Ƽ� ������ ���� & ���� ��� MemoCalendar �����ӿ� ���)
	private void calendarHandler() {
		Request request = new Request("readschedule");			
        request.setParameter("groupid",groupId);
        request.setParameter("writer",user);        
        Thread thread = new Thread() {
        	@Override
        	public void run() {
        		Request response = WebConnector.connect(request);
				Map<String,ScheduleVO> scheduleMap = (Map<String,ScheduleVO>)response.getAttribute("schedulemap");        		
        		new MemoCalendar(user,groupId,scheduleMap);
        		}
        };
        thread.start();
	}
	
	//���� ��ư
	private void gameHandler() {
		new MineFrame();
	}
	
	//���� ��ư
	private void remoteHandler() {
		int[] users = UserGroupParser.getUserInteger(groupId);
		if(users.length > 2) { //���� ä���� ���
			new MessageFrame("������ 1:1ä�ø� �����մϴ�.",false);
			return;
		}
		int userId = Integer.parseInt(user);
		int friendId = users[0]; //ģ�����̵� users[0]�̶� ����
		if(users[0]==userId) //users[0]�� �ڽ��� ���̵��̸� 
			friendId = users[1]; //ģ�� ���̵� users[1] ������ �ٲ���
		Request request = new Request("remote_req");
		request.setParameter("groupid",groupId);
		request.setParameter("from",user);
		request.setParameter("to",String.valueOf(friendId));
		
		String ip = MemberController.getInstance().getIP();
		ServerSocket serverSocket = ServerSocketProvider.getServerSocket(ip);
		request.setParameter("port", String.valueOf(serverSocket.getLocalPort()));
		request.setParameter("ip",ip);
		clientHandler.send(request);
		new RemoteServer(serverSocket);
	}
	
	//�޽��� ������ �ڵ鷯
	private void sendMessageHandler() {
		if(!isConnected) //���� ���������� Ȯ��
			isConnected = true;
		String content =inputText.getText();
		inputText.setText("");
		chatDisplay.append("�� > "+content+"\n");				
		updateScroll();
		Request request = new Request("message");
		request.setParameter("groupid",groupId);
		request.setParameter("sender",user);
		request.setParameter("content",content);
		clientHandler.send(request);
	}
	
	//�޽��� �޴� �ڵ鷯
	public void receiveMessage(String msg) {
		chatDisplay.append(msg+ "\n");
		updateScroll();		
	}
	
	//���� �޴� �ڵ鷯
	public void receiveNotice(String notice) {
		chatDisplay.append(notice+"\n");
		updateScroll();
	}
	
	//��ũ�� �� ������
	private void updateScroll() {
		displayScroll.getVerticalScrollBar().setValue(displayScroll.getVerticalScrollBar().getMaximum());
	}	
	
	////////////
	//GUI
	private JPanel south_p = new JPanel();
	private JPanel south_innersp = new JPanel();
	private JPanel south_innercp = new JPanel();

	private JTextField inputText = new JTextField();
	//private JScrollPane msg_sp = new JScrollPane(inputText);
	private JButton send_bt = new JButton("����");

	private JTextArea chatDisplay = new JTextArea();
	private JScrollPane displayScroll = new JScrollPane(chatDisplay);

	private JButton file_bt = new JButton(new ImageIcon("image/filesend.png"));
	private JButton remote_bt = new JButton(new ImageIcon("image/remote.png"));
	private JButton cal_bt = new JButton(new ImageIcon("image/cal.png"));
	private JButton game_bt = new JButton(new ImageIcon("image/game.png"));
	private JButton invite_bt = new JButton("�ʴ�");
	private void init() {
		super.setSize(500, 600);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int) (screen.getWidth() / 2 - this.getWidth() / 2);
		int ypos = (int) (screen.getHeight() / 2 - this.getHeight() / 2);		
		super.setLocation(xpos, ypos);
		super.setResizable(false);
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		con.add("Center", displayScroll);
		chatDisplay.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
		con.add("South", south_p);
		south_p.setLayout(new BorderLayout());
		south_p.add("Center", south_innercp);
		south_p.add("South", south_innersp);
		south_innercp.setLayout(new BorderLayout());
		south_innercp.add("Center", inputText);
		south_innercp.add("East", send_bt);
		south_innersp.setLayout(new FlowLayout(FlowLayout.LEFT));
		south_innersp.setBackground(new Color(255, 255, 255));
		south_innersp.add(file_bt);
		south_innersp.add(remote_bt);
		south_innersp.add(cal_bt);
		south_innersp.add(game_bt);
		south_innersp.add(invite_bt);
	
		file_bt.setBorderPainted(false);
		file_bt.setContentAreaFilled(false);
		file_bt.setFocusPainted(false);
		file_bt.setToolTipText("���� ����");
		
		remote_bt.setBorderPainted(false);
		remote_bt.setContentAreaFilled(false);
		remote_bt.setFocusPainted(false);
		remote_bt.setToolTipText("���� ��û");
		
		game_bt.setBorderPainted(false);
		game_bt.setContentAreaFilled(false);
		
		cal_bt.setBorderPainted(false);
		cal_bt.setContentAreaFilled(false);
		cal_bt.setFocusPainted(false);
		cal_bt.setToolTipText("���� ����");
		
		game_bt.setFocusPainted(false);
		game_bt.setToolTipText("���� ����");
		
		send_bt.setBackground(new Color(255,255,255));
		send_bt.setFocusPainted(false);
		
		chatDisplay.setEditable(false);
		chatDisplay.setLineWrap(true);
		chatDisplay.setFont(new Font("",Font.PLAIN,16));
		
		file_bt.addMouseListener(ButtonCursorHandler.getInstance());
		remote_bt.addMouseListener(ButtonCursorHandler.getInstance());
		game_bt.addMouseListener(ButtonCursorHandler.getInstance());
		cal_bt.addMouseListener(ButtonCursorHandler.getInstance());	
		
		//msg_sp.setBorder(null);
		displayScroll.setBorder(null);
		inputText.addActionListener(e -> {			
			sendMessageHandler();
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				MessageController.getInstance().disposeChatRoom(groupId);
				dispose();
			}
		});
		setVisible(true);
	}

	private void action() {
		send_bt.addActionListener(this);
		file_bt.addActionListener(this);
		remote_bt.addActionListener(this);
		game_bt.addActionListener(this);
		cal_bt.addActionListener(this);
	}
	
	
}
