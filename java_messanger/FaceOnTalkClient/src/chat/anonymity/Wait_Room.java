package chat.anonymity;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


class Wait_Room extends JFrame implements KeyListener, ActionListener{
	static JTextArea showText = new JTextArea();
	static JLabel inputid;
	static String id;
	
	JTextField inputText = new JTextField();
	List IDlist = new List();
	List roomList = new List();
	JButton waitroomSend = new JButton("����"); 
//	JButton make = new JButton("�� �����");
//	JButton enter = new JButton("Enter");
//	JButton exit = new JButton("EXIT");
	
	JButton make = new JButton(new ImageIcon("image/anonymity/creat_1.png"));
	JButton enter = new JButton(new ImageIcon("image/anonymity/enter_1.png"));
	JButton exit = new JButton(new ImageIcon("image/anonymity/exit_1.png"));
	
	JPopupMenu waitpopup;  
	JMenuItem sendmemo, mantoman, transferFile;
	String roomtitle;
	JScrollPane waitJsp;
	JScrollPane userListJsp;
	JScrollPane roomListJsp;
	JScrollBar waitJsb;
	JScrollBar userListJsb;
	JScrollBar roomListJsb;
	
	//���� ��Ʈ
	Font font = new Font("TimesRoman", Font.BOLD, 20); 

	Wait_Room(String title){
		super(title);
		roomtitle = title;
	}
	
	//���� Ŭ���̾�Ʈ
	public void showFrame(String name){

	/*===============
	 * ���� ä�� �Է�â
	 *================*/
		Wait_Room.id = name;
		inputid = new JLabel(" �����ID : "+id);
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		inputText.addKeyListener(this);

		//���� ä��â�� ��ũ�ѹ� �߰�.
		waitJsp = new JScrollPane(showText);
		waitJsp.setWheelScrollingEnabled(true);
		waitJsb = waitJsp.getVerticalScrollBar();
		showText.setEditable(false);
		showText.setLineWrap(true);
		
	/*==================
	 *  roomList
	 *====================*/
		//�� ��� ��ũ�ѹ� �߰�.
		roomListJsp = new JScrollPane(roomList);
		/////////
		//test code
		roomList.setFont(font);
		roomList.setFont(new Font("Serif", Font.BOLD, 14));
	
	/*
		//roomList.setBackground(new Color());
		roomList.setBackground(Color.MAGENTA);
	*/	

		////////
		
		
		roomListJsp.setPreferredSize(new Dimension(340, 330)); //roomlist�� panel�� ���, list�� ����� �����ϱ� ���� panel�� �ɼ��� ����
		roomListJsp.setWheelScrollingEnabled(true);
		roomListJsb = roomListJsp.createVerticalScrollBar();
		
		JPanel left = new JPanel();
		left.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "��ȭ�� ���"));
		left.setBackground(new Color(255, 255, 255));
		
		left.add(roomListJsp);
		
	/*==================
	 *  userList
	 *====================*/
		//������ ��ũ�ѹ� �߰�.
		userListJsp = new JScrollPane(IDlist);
		userListJsp.setPreferredSize(new Dimension(220, 150));	//userlist�� panel�� ���, list�� ����� �����ϱ� ���� panel�� �ɼ��� ����
		userListJsp.setWheelScrollingEnabled(true);
		userListJsb = userListJsp.createVerticalScrollBar();
		
		JPanel right_up = new JPanel();
		right_up.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "������"));
		right_up.setBackground(new Color(255, 255, 255));
		right_up.add(userListJsp);
		
		/*
		 *=========================
		 * test code
		 * ==========================
		 * //������ ��ư Ŭ��������
		IDlist.addMouseListener(
				new MouseAdapter(){
					@Override
					public void mouseClicked(MouseEvent e){
						//�͸�ä�ÿ����� Jpopupmenu ��Ȱ��ȭ��Ŵ
//						if(e.getButton() == 3){
//							waitpopup.add(sendmemo);
//							waitpopup.add(mantoman);
//							waitpopup.add(transferFile);
//							waitpopup.show(e.getComponent(), e.getX(), e.getY());
//						}
					}
				});*/
		
		JPanel right_down_south = new JPanel();
		right_down_south.setLayout(new BorderLayout());
		right_down_south.add("Center", inputText);
		right_down_south.add("East", waitroomSend);
		
		JPanel right_down = new JPanel(); 
		right_down.setLayout(new BorderLayout());
		right_down.add(inputid, "North");
		right_down.add(waitJsp, "Center");
		right_down.add(right_down_south, "South");
		
		right_down.setBorder(loweredetched);
		//(ä��â)��Ȱ��ȭ
		showText.setEditable(false);
		
		JPanel right = new JPanel();
		right.setBorder(loweredetched);
		right.setLayout(new GridLayout(2,1));
		right.add(right_up);	
		right.add(right_down);
		right.add(left);
				
		enter.setBorderPainted(false);
		enter.setContentAreaFilled(false);
		enter.setFocusPainted(false);
		enter.setToolTipText("Enter");
		
		make.setBorderPainted(false);
		make.setContentAreaFilled(false);
		make.setFocusPainted(false);
		make.setToolTipText("Create");
		
		exit.setBorderPainted(false);
		exit.setContentAreaFilled(false);
		exit.setFocusPainted(false);
		exit.setToolTipText("Exit");
		///////////
		
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		south.setBackground(new Color(255, 255, 255));
		south.add(make);	south.add(enter);	south.add(exit);
		enter.addActionListener(this);
		make.addActionListener(this);
		exit.addActionListener(this);
		waitroomSend.addActionListener(this);
		
		/*==================
		 * 		��ü ���̾ƿ� ����
		 *=====================*/
		south.setBorder(loweredetched);
		setBounds(100, 100, 650, 450);	//����â ũ��
		setLayout(new BorderLayout());
		//add(left, "Center");
		//add(right, "East");
		add(left,"Center");
		add(right,"East");
		add(south, "South");
		setResizable(false);
		setVisible(true);

		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				String consoleData = "EXIT";
				Send_Message.chattingStart(consoleData);			//Exit ����
			}
		});
	}	
	
	//Ű �̺�Ʈ
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar() == '\n'){
			waitJsp.getVerticalScrollBar().setValue(waitJsp.getVerticalScrollBar().getMaximum());//TextArea ��ũ�� ���������� ������Ʈ
			waitJsb.setValue(waitJsb.getMaximum());
			String consoleData = "Main"+"/"+inputText.getText();		//�Է��Ѱ� ����
			inputText.setText("");								//������ ������ ��.
			Send_Message.chattingStart(consoleData);			//ä�� ����
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}

	//��ư�� ���� �̺�Ʈ
	@Override
	public void actionPerformed(ActionEvent ac) {
		if(ac.getSource()==make){
			dispose();	dispose();

			final JFrame makeroom = new JFrame();
			final JTextField roomname;	//���̸�
			Button ok;		//Ȯ��
			JLabel title = new JLabel("  ���̸��� �Է��ϼ���");
			roomname = new JTextField(10);
			ok = new Button("OK");
			ok.addActionListener(this);
			makeroom.add(title, "North");
			Panel southPanel = new Panel();
			southPanel.add(roomname);	southPanel.add(ok);
			makeroom.add(southPanel, "Center");
			makeroom.setBounds(500, 300, 200, 100);//�� �̸� �Է�â ũ�⼳��
			makeroom.setVisible(true);

			/*==================================================================
			 *				 �� �̸� �Է��ϰ� Ok������
			 * ==================================================================*/
			ok.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					if(e.getActionCommand().equals("OK")){
						makeroom.dispose();	//�� â�� �������� �Ѵ�.
						//Room room = Room_Manager.roomMap.get("Main");
						//System.out.println(room);
						try {
							anonyChatFrame.sendMsg(MsgInfo.MAKEROOM, roomname.getText());
						} catch (IOException e1) { e1.printStackTrace(); }
					}
				}
			});
			/*==================================================================
			 *				 �� Ŭ���ϰ� ���⴩����
			 * ==================================================================*/
		}else if(ac.getSource()==enter){
			String roomname = roomList.getSelectedItem();
			try{
				if(roomname == null){		//���� ���µ� ���°� �����ؼ� ���� �����°� ����.
					JOptionPane.showMessageDialog(null, "���õ� ���� �����ϴ�. ���� ���弼��.");
				}else{
					this.setVisible(false);
					anonyChatFrame.sendMsg(MsgInfo.ENTER, roomname);
				}
			}catch(IOException e1) { e1.printStackTrace(); }
			/*==================================================================
			 *				 ������ ��������
			 * ==================================================================*/
		}else if(ac.getSource()==exit){
			dispose();
			String consoleData = "EXIT";
			Send_Message.chattingStart(consoleData);			//Exit ������
			//System.exit(0);
			dispose();
			/*==================================================================
			 * 				���� ä�� - ��ư���� ������
			 * ==================================================================*/
		}else if(ac.getSource() == waitroomSend) {	//���� ä�� ���� ��ư
			String consoleData = inputText.getText();
			if (consoleData.equals(""))
				return;
			showText.append(id +" : " + consoleData + "\n");
			inputText.setText("");
			waitJsp.getVerticalScrollBar().setValue(waitJsp.getVerticalScrollBar().getMaximum());//TextArea ��ũ�� ���������� ������Ʈ
		}		
	}
}
