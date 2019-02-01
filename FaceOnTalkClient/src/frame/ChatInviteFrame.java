package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import controller.MemberController;
import controller.MessageController;
import dto.MemberVO;

public class ChatInviteFrame extends JFrame implements ActionListener {
	private MemberController friendsManager = MemberController.getInstance();
	private List<MemberVO> onFriendsList = friendsManager.getLogonUsers();	

	public ChatInviteFrame(String title) {
		super(title);
		invite_list = new JList<>(new DefaultListModel());
		invite_model = (DefaultListModel) invite_list.getModel();
		noninvite_list = new JList<>(new DefaultListModel<>());
		noninvite_model = (DefaultListModel) noninvite_list.getModel();
		init();
		action();
		setVisible(true);
	}

	///////////////////
	// action
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == right_bt) { // �ʴ� ��� -> ģ�� ���
			int index = invite_list.getSelectedIndex();
			if (index == 0) { // �����
				return;
			} else if (invite_model.getSize() == 0) {
				return;
			} else if (index == invite_model.getSize()) {
				index--;
			} else if (invite_list.isSelectionEmpty()) {
				return;
			}
			invite_model.remove(index);
		} else if (e.getSource() == left_bt) { // �ʴ� ��� <- ģ�� ���
			boolean alreadyInvited = false;
			right_bt.setEnabled(true);
			MemberVO member = (MemberVO) noninvite_list.getSelectedValue();
			for (int i = 0; i < invite_model.size(); i++) {
				MemberVO comp = invite_model.get(i);
				if (member.equals(comp))
					alreadyInvited = true;
			}
			if (!alreadyInvited)
				invite_model.addElement(member);
		} else if (e.getSource() == create_bt) {
			// ä�� �� ���� �ڵ�
			if (!invite_model.isEmpty()) {
				makeChatRoomHandler();
			}
			dispose();
		} else if (e.getSource() == cancel_bt) {
			dispose();
		}
	}

	private void makeChatRoomHandler() {
		int[] roomUsers = new int[invite_model.size() + 1];
		for (int i = 0; i < invite_model.size(); i++) {
			roomUsers[i + 1] = invite_model.getElementAt(i).getId();
		}
		String title = getChatRoomTitle(roomUsers);
		MessageController.getInstance().makeChatRoom(title, roomUsers);
	}

	private String getChatRoomTitle(int[] users) {
		String title = null;
		System.out.println("user.length : " + users.length);
		title = invite_model.getElementAt(0).getName(); // �ʴ��ο� 1
		if (users.length <= 2) {
			return title;
		} else if (users.length == 3) { // �ʴ� �ο� 2
			title += "," + invite_model.getElementAt(1).getName();
		} else {
			title += "," + invite_model.getElementAt(1).getName() + "�� " + (users.length - 3) + "��";
		}
		return title;
	}

	///////////////////
	// GUI ���� ��ü����
	private JPanel top_p = new JPanel();
	private JLabel top_lb = new JLabel("    �ʴ��ϴ� �ο��� ���� 1:1 ä�ð� �׷� ä���� �����մϴ�.");
	private JPanel basisTop_p = new JPanel();
	private JPanel basisBottom_p = new JPanel();
	private JLabel invite_lb = new JLabel("�ʴ�� ģ��");
	private JPanel invite_p = new JPanel();
	private JList<MemberVO> invite_list;
	private DefaultListModel<MemberVO> invite_model;
	private JLabel noninvite_lb = new JLabel("�ʴ��� ģ��");
	private JPanel noninvite_p = new JPanel();
	private JList<MemberVO> noninvite_list;
	private DefaultListModel<MemberVO> noninvite_model;
	private JPanel center_p = new JPanel();
	private JButton right_bt = new JButton(new ImageIcon("image/right.png"));
	private JButton left_bt = new JButton(new ImageIcon("image/reft.png"));
	private JButton cancel_bt = new JButton("���");
	private JButton create_bt = new JButton("Ȯ��");

	public void init() {
		super.setSize(500, 400);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int) (screen.getWidth() / 2 - this.getWidth() / 2);
		int ypos = (int) (screen.getHeight() / 2 - this.getHeight() / 2);

		super.setLocation(xpos, ypos);
		super.setResizable(false);

		Container con = this.getContentPane();
		// ��ü Ʋ
		con.setLayout(new BorderLayout());
		con.add("North", top_p);
		top_p.add(top_lb);
		top_p.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		top_p.setPreferredSize(new Dimension(0, 50));
		con.add("Center", basisTop_p);
		con.add("South", basisBottom_p);
		basisTop_p.setLayout(new BorderLayout());
		basisTop_p.add("West", invite_p);
		// ���� JList ���� Ʋ
		invite_p.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		invite_p.setLayout(new BorderLayout());
		invite_p.add("North", invite_lb);
		invite_p.add("Center", new JScrollPane(invite_list));
		invite_p.setPreferredSize(new Dimension(250, 0));
		// ������ JList ���� Ʋ
		basisTop_p.add("East", noninvite_p);
		noninvite_p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		noninvite_p.setLayout(new BorderLayout());
		noninvite_p.add("North", noninvite_lb);
		noninvite_p.add("Center", new JScrollPane(noninvite_list));
		noninvite_p.setPreferredSize(new Dimension(170, 0));
		// �߾� ��ư ���� Ʋ
		basisTop_p.add("Center", center_p);
		center_p.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
		center_p.add(left_bt);
		center_p.add(right_bt);
		// �ϴ� ��ư ���� Ʋ
		basisBottom_p.setLayout(new FlowLayout());
		basisBottom_p.add(create_bt);
		basisBottom_p.add(cancel_bt);

		// ��ư �� �г� �� ���� �� �ٹ̱� ����
		invite_p.setBackground(new Color(255, 255, 255));
		noninvite_p.setBackground(new Color(255, 255, 255));
		top_p.setBackground(new Color(255, 255, 255));
		center_p.setBackground(new Color(255, 255, 255));
		basisBottom_p.setBackground(new Color(255, 255, 255));
		right_bt.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
		right_bt.setContentAreaFilled(false);
		right_bt.setFocusPainted(false);
		right_bt.setPreferredSize(new Dimension(40, 40));
		right_bt.setToolTipText("ģ�� ����");
		left_bt.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
		left_bt.setContentAreaFilled(false);
		left_bt.setFocusPainted(false);
		left_bt.setPreferredSize(new Dimension(40, 40));
		left_bt.setToolTipText("ģ�� �ʴ�");
		create_bt.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
		create_bt.setContentAreaFilled(false);
		create_bt.setPreferredSize(new Dimension(50, 30));
		create_bt.setFocusPainted(false);
		cancel_bt.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
		cancel_bt.setContentAreaFilled(false);
		cancel_bt.setPreferredSize(new Dimension(50, 30));
		cancel_bt.setFocusPainted(false);

		for (int i = 0; i < onFriendsList.size(); i++) {
			noninvite_model.addElement(onFriendsList.get(i));
		}
		invite_list.setModel(invite_model);
		noninvite_list.setModel(noninvite_model);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}

	public void action() {
		right_bt.addActionListener(this);
		left_bt.addActionListener(this);
		cancel_bt.addActionListener(this);
		create_bt.addActionListener(this);
	}
}