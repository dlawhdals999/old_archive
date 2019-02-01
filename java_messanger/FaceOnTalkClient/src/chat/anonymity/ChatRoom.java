package chat.anonymity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.GregorianCalendar;

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

public class ChatRoom extends JFrame implements KeyListener, ActionListener {
	static JTextArea showText = new JTextArea();
	JTextField inputText = new JTextField();
	List IDlist = new List();
	// ��ư �߰��۾�

	static JLabel inputid;// ����� IDǥ�ÿ�
	static String title; // ä�ù� �̸�
	String id;
	JPopupMenu chatpopup; // ��Ŭ���Ͽ� �˾��޴�â
	JMenuItem kick;
	JScrollPane chatJsp;
	JScrollBar chatJsb;
	JScrollPane userJsp;
	JScrollBar userJsb;
	int chiefcode = 0;
	

	///////////////////
	// test code
	////////////////////
	JButton userInvite = new JButton("�ʴ�");
	JButton send = new JButton("����");
	JButton talkSave = new JButton(new ImageIcon("image/anonymity/talkSave.png"));
	JButton userlistAll = new JButton(new ImageIcon("image/anonymity/userlistall.png"));
	JButton exit = new JButton(new ImageIcon("image/anonymity/chatExit.jpg"));

	//////////////////////

	ChatRoom(String title) {
		super("�� �̸� : " + title);
		ChatRoom.title = title;
	}

	public void showFrame(String name) {
		id = name;
		inputid = new JLabel(id);
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		inputText.addKeyListener(this);

		// ������ ��ư�� Ŭ�� ������.
		chatpopup = new JPopupMenu();
		kick = new JMenuItem("����");
		kick.addActionListener(this);

		// ��ũ�ѹ� �߰�
		chatJsp = new JScrollPane(showText);
		chatJsp.setWheelScrollingEnabled(true);
		chatJsb = chatJsp.getVerticalScrollBar();

		// JTextArea(��ȭâ) ��Ȱ��ȭ
		showText.setEditable(false);
		// ��ȭâ �ڵ�����
		showText.setLineWrap(true);

		JPanel leftinputText = new JPanel();
		leftinputText.setLayout(new BorderLayout());
		leftinputText.add(inputText, "Center");
		leftinputText.add(send, "East");

		JPanel left = new JPanel();
		left.setLayout(new BorderLayout());
		left.add(inputid, "North");
		left.add(chatJsp, "Center");
		left.add(leftinputText, "South");
		left.setBorder(loweredetched);

		/*
		 * =================== �� ������ ��� ���� =====================
		 */
		// ��ũ�ѹ� �߰�
		userJsp = new JScrollPane(IDlist);
		userJsp.setWheelScrollingEnabled(true);
		userJsb = userJsp.getVerticalScrollBar();

		JPanel right = new JPanel();
		right.setLayout(new GridLayout(1, 1));
		right.add(userJsp);
		right.setBorder(loweredetched);

		IDlist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					// System.out.println("���� �ڵ� : " + chiefcode);
					if (chiefcode == 1) { // �����̸�
						chatpopup.add(kick); // ������ �߰�
						chatpopup.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});

		JPanel south = new JPanel();
		south.setLayout(new FlowLayout(FlowLayout.LEFT));
		south.setBackground(new Color(255, 255, 255));

		south.add(talkSave);
		south.add(userlistAll);
		south.add(exit);
		south.setBorder(loweredetched);

		// Button Action
		send.addActionListener(this);
		talkSave.addActionListener(this);
		userlistAll.addActionListener(this);
		exit.addActionListener(this);

		talkSave.setBorderPainted(false);
		talkSave.setContentAreaFilled(false);
		talkSave.setFocusPainted(false);
		talkSave.setToolTipText("��ȭ���� ����");
		userlistAll.setBorderPainted(false);
		userlistAll.setContentAreaFilled(false);
		userlistAll.setFocusPainted(false);
		userlistAll.setToolTipText("����� �ʴ�");
		exit.setBorderPainted(false);
		exit.setContentAreaFilled(false);
		exit.setFocusPainted(false);
		exit.setToolTipText("�� ������");

		/*
		 * =================== ��ü ���̾ƿ� ���� ============================
		 */
		setBounds(100, 100, 500, 421);
		setLayout(new BorderLayout());
		add(left, "Center");
		add(right, "East");
		add(south, "South");
		setVisible(true);
		// X�� ���������� ���� ��ȭ�濡�� ������.
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				System.out.println("ä�ù� ����");// "ChatRoom Frame ����");
				try {
					anonyChatFrame.sendMsg(MsgInfo.GOWAIT, inputid.getText() + "/" + title);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	/*
	 * ========================== ��ȭ���� �����ޱ� ���� �޼��� ============================
	 */
	public void talkSave() {
		FileDialog fdial = new FileDialog(this, "����", FileDialog.SAVE); // ������.
		fdial.setVisible(true);
		/*
		 * ��¥�� �ð��� ���� GregorianCalendar�� DateFormat�� �̿��Ѵ�.
		 */
		GregorianCalendar gc = new GregorianCalendar();
		DateFormat df = DateFormat.getInstance();
		String data = df.format(gc.getTime()) + "\r\n[" + title + "]" + "������ ��ȭ����" + "\r\n"; // �ð�
																								// �߰�
		data += ChatRoom.showText.getText().replaceAll("\n", "\r\n");
		// \n�� ���� \r\n���� �ؾ� �޸��忡�� ���� ����� �̷�� ����.
		BufferedWriter bw;
		try {
			try {
				// BufferedWriter�� ���� ��ġ�� ����.
				bw = new BufferedWriter(new FileWriter(fdial.getDirectory() + "\\" + fdial.getFile()));
				bw.write(data);
				bw.close();
			} catch (FileNotFoundException file) {
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == '\n') {

			chatJsb.setValue(chatJsb.getMaximum()); // ���͸� ġ�� ��ũ�ѹٵ�
													// ������ ���������� ��
			String consoleData = title + "/" + inputText.getText(); // �Է��Ѱ� ����
			inputText.setText(""); // ������ ������ ��.
			Send_Message.chattingStart(consoleData); // ä�� ����
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String receiveid = IDlist.getSelectedItem();
		if (e.getSource() == exit) {
			this.setVisible(false);
			// System.out.println("ChatRoom Frame ����"); //Ȯ��
			try {
				anonyChatFrame.sendMsg(MsgInfo.GOWAIT, inputid.getText() + "/" + title);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			showText.setText("");// ���� ������ ä�ó��� �ʱ�ȭ

			/*
			 * ================== ��ȭ���� ���� ====================
			 */
		} else if (e.getSource() == talkSave) {
			talkSave();
			/*
			 * ================== ���� ��ư ====================
			 */
		} else if (e.getSource() == send) {
			String msg = inputText.getText();
			if (msg.equals(""))
				return;
			showText.append("id : " + msg + "\n");
			showText.setCaretPosition(showText.getDocument().getLength());
			inputText.setText("");

			/*
			 * ================== ������ ���� Ŭ�� �� ====================
			 */
		} else if (e.getSource() == userlistAll) {
			try {
				anonyChatFrame.sendMsg(MsgInfo.SHOWUSER, null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			/*
			 * ================== ������ (������ ��ư) ���� �������� ====================
			 */
		} else if (e.getSource() == kick) {
			if (receiveid == null) { // �޴� ����� ������
				JOptionPane.showMessageDialog(null, "����� �����ϼ���.");
			} else if (receiveid.startsWith("[����]")) {
				JOptionPane.showMessageDialog(null, "������ �����ų �� �����ϴ�.");// "�ڽ���
																		// ���� ��ų
																		// �� ����
			} else {
				try {
					anonyChatFrame.sendMsg(MsgInfo.KICK, receiveid + "/" + title); // ����
																					// ��ɾ�
																					// ����
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
