package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import connector.WebConnector;
import dto.MemberVO;
import noticeframe.MessageFrame;
import request.Request;
import util.RegularExpression;

public class FindFrame extends JFrame implements ActionListener,FocusListener {
	private final int WIDTH = 650, HEIGHT=350;
	private JPanel contentPane = new JPanel();
	private JPanel idPanel = new JPanel();
	private JPanel pwPanel = new JPanel();	
	private JLabel idMainLabel = new JLabel("���̵�ã��");
	
	private JLabel birthdayLabel = new JLabel("ex) 890202 - 1****** (�� 1�ڸ�)");
	private JLabel slashBirthLabel = new JLabel("-");
	private JLabel starBirthLabel = new JLabel("****** (�� 1�ڸ�)");
	private JTextField idTxtInputFirstBirth = new JTextField("�������(BIRTH)");
	private JTextField txtInputSecondBirth = new JTextField();
	private JLabel idPhoneLabel = new JLabel("ex) 010-7777-7777");
	private JTextField idTxtInputPhone = new JTextField("�� �� ��(PHONE)");

	
	private JLabel passwordMainLabel = new JLabel("��й�ȣ ã��");
	private JLabel passwordIdLabel = new JLabel("ex) hyungss71@naver.com");
	private JTextField passwordTxtInputId = new JTextField("�� �� ��(ID)");
	private JLabel passwordPhoneLabel = new JLabel("ex) 010-7777-7777");
	private JTextField passwordTxtInputPhone = new JTextField("�� �� ��(PHONE)");

	
	private JFrame loginFrm;
	private JButton btnIdFind = makeButton("IDã��");
	private JButton btnIdCancel = makeButton("���");
	private JButton btnPwFind = makeButton("PWã��");
	private JButton btnPwCancel = makeButton("���");
	
	public FindFrame(JFrame loginFrm){
		super("Face On Talk Find");
		this.loginFrm = loginFrm;
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				loginFrm.setVisible(true);
			}
		});
		this.init();				
	}
		
	public void init(){
		setSize(WIDTH, HEIGHT);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int) (screen.getWidth() / 2 - this.getWidth() / 2);
		int ypos = (int) (screen.getHeight() / 2 - this.getHeight() / 2);
		setLocation(xpos, ypos);
		setResizable(false);
		setVisible(true);
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		idPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		idPanel.setBounds(0,0,325,HEIGHT);
		contentPane.add(idPanel);
		idPanel.setLayout(null);
		 idPanel.setBackground(new Color(255,255,255));
		
		pwPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pwPanel.setBounds(325,0,WIDTH,HEIGHT);
		contentPane.add(pwPanel);
		pwPanel.setLayout(null);
		 pwPanel.setBackground(new Color(255,255,255));
	
		
//		�Ƶ�ã�� ==
		 //���ζ�
		 idPanel.add(idMainLabel);
		 idMainLabel.setFont(new Font("����ü",Font.BOLD,18));
		 idMainLabel.setBounds(110, 21, 115, 31);
		 
		//������϶�
		idPanel.add(birthdayLabel);
		birthdayLabel.setBounds(50, 100, 200, 21);
		birthdayLabel.setForeground(Color.GRAY);
		//������� �Է�ĭ
		idPanel.add(idTxtInputFirstBirth);
		idTxtInputFirstBirth.setBounds(50, 80, 96, 21);
		idTxtInputFirstBirth.setForeground(Color.GRAY);
		idTxtInputFirstBirth.addFocusListener(this);		
		//��
		slashBirthLabel.setBounds(150,80,15,21);
		idPanel.add(slashBirthLabel);
		//�ι�°��������Է�
		txtInputSecondBirth.setBounds(160,80, 15, 21);
		idPanel.add(txtInputSecondBirth);
		txtInputSecondBirth.setColumns(2);
		//*
		starBirthLabel.setBounds(170,80,100,21);
		idPanel.add(starBirthLabel);
		
		//�ڵ�����
		idPanel.add(idPhoneLabel);
		idPhoneLabel.setBounds(50,170, 200, 21);
		idPhoneLabel.setForeground(Color.GRAY);
		
		//�ڵ���txt
		idPanel.add(idTxtInputPhone);
		idTxtInputPhone.setColumns(20);
		idTxtInputPhone.setForeground(Color.GRAY);
		idTxtInputPhone.setBounds(50, 150, 180, 21);
		idTxtInputPhone.addFocusListener(this);
		
		
//		�Ƶ�ã�� ��ư ==
		btnIdFind.setBounds(85,230,80,21);
		idPanel.add(btnIdFind);//ã��
				
		btnIdCancel.setBounds(175,230,80,21);
		idPanel.add(btnIdCancel);//���
		
//		================================
		
//		�н����� ã��========
		//pw���ζ�
		pwPanel.add(passwordMainLabel);
		passwordMainLabel.setFont(new Font("����ü",Font.BOLD,18));
		passwordMainLabel.setBounds(105, 21, 130, 31);
		
		//pw id��
		pwPanel.add(passwordIdLabel);
		passwordIdLabel.setBounds(50, 100, 200, 21);
		passwordIdLabel.setForeground(Color.GRAY);
		
		//pw Id�Է� txt����
		pwPanel.add(passwordTxtInputId);
		passwordTxtInputId.setBounds(50, 80, 180, 21);
		passwordTxtInputId.setForeground(Color.GRAY);
		passwordTxtInputId.addFocusListener(this);
		passwordTxtInputId.setColumns(30);
		
		//pw�ڵ�����
		pwPanel.add(passwordPhoneLabel);
		passwordPhoneLabel.setBounds(50,170, 200, 21);
		passwordPhoneLabel.setForeground(Color.GRAY);
		
		//pw�ڵ���txt		
		pwPanel.add(passwordTxtInputPhone);
		passwordTxtInputPhone.setColumns(15);
		passwordTxtInputPhone.setForeground(Color.GRAY);
		passwordTxtInputPhone.setBounds(50, 150, 180, 21);
		passwordTxtInputPhone.addFocusListener(this);
		
//		��й�ȣã���ư		
		btnPwFind.setBounds(85,230,80,21);
		pwPanel.add(btnPwFind);
				
		btnPwCancel.setBounds(175,230,80,21);
		pwPanel.add(btnPwCancel);
		
//		====================GUI ��
		//��ư�� ��Ŀ�� �ִ� ����
	      addWindowFocusListener(new WindowAdapter() {
	          public void windowGainedFocus(WindowEvent e) {
	        	  idMainLabel.requestFocusInWindow();
	          }
	      });
	      //////////////
	}
	private JButton makeButton(String value) {
		JButton btn = new JButton(value);
		btn.addActionListener(this);
		btn.setBackground(new Color(0,0,0));
		btn.setForeground(Color.WHITE);
		return btn;
	}
	
	private boolean checkEmpty(String ... val){		
		for(int i=0;i<val.length;i++) {
			if(val[i]==null || val[i].isEmpty() || checkDefaultMessage(val[i]))
				return true;
		}
		return false;
	}
	
	private boolean checkDefaultMessage(String value) {		
		switch(value) {
		case "�������(BIRTH)" :
		case "�� �� ��(PHONE)" :	
		case "�� �� ��(ID)" :		
			return true;
		default :
			return false;
		}		
	}
	
	private void findIdHandler(Request request) {
		String birth = idTxtInputFirstBirth.getText();
		String gender = txtInputSecondBirth.getText();
		String phone = idTxtInputPhone.getText();
		if(checkEmpty(birth,gender,phone)) {
			new MessageFrame("��� ���� �Է����ּ���.",false);			
		}
		
		if(gender.equals("1"))
			gender = "m";
		else if(gender.equals("2"))
			gender = "f";
		
		if(!RegularExpression.isPhoneExpression(phone)) {
			new MessageFrame("�ڵ�����ȣ ������ �����ּ���\n" + "ex) 010-5091-7946 ",false);
		} else if (!RegularExpression.isSecondBirthExpression(gender)) {
			new MessageFrame("�ֹε�Ϲ�ȣ ���ڸ� ������ �����ּ���\n" + "ex) 1 or 2",false);
		} else if (!RegularExpression.isFirstBirthExpression(birth)) {
			new MessageFrame("�ֹε�Ϲ�ȣ ���ڸ� ������ �����ּ���\n" + "ex) 890202",false);
		} else {
			request.setParameter("searchtype","email");			
			request.setParameter("birth",birth);
			request.setParameter("phone",phone.replaceAll("-",""));
			request.setParameter("gender",gender);
			
			Thread thread = new Thread() {
				@Override
				public void run() {
					Request response = WebConnector.connect(request);					
					Map<String,Boolean> errors = (Map<String,Boolean>)response.getAttribute("errors");
					if(errors.isEmpty()) { //ã�� ����
						new MessageFrame(response.getParameter("result"),true);					
					} else { //ã�� ����
						if(errors.get("SQLException") == Boolean.TRUE) {
							new MessageFrame("�������� ������ ���� �ʽ��ϴ�.\n ��� �� �������ּ���.",false);
						}
						else {
							new MessageFrame("�������� �ʴ� ȸ���Դϴ�.",false);
						}
					}
				}
			};
			thread.start();
		}		
	}
	
	private void findPwdHandler(Request request) {				
		String email = passwordTxtInputId.getText();
		String phone = passwordTxtInputPhone.getText();
		
		if(checkEmpty(email,phone)) {
			new MessageFrame("��� ���� �Է����ּ���.",false);			
		} else if(!RegularExpression.isEmailExpression(email)) {
			new MessageFrame("�̸��� ������ �����ּ���\n" + "ex) hyungss71@naver.com",false);
		} else if(!RegularExpression.isPhoneExpression(phone)) {
			new MessageFrame("�ڵ�����ȣ ������ �����ּ���\n" + "ex)010-7777-7777",false);
		} else {
			request.setParameter("searchtype","password");
			request.setParameter("email", email);
			request.setParameter("phone",phone.replaceAll("-",""));
			
			Thread thread = new Thread() {
				@Override
				public void run() {
					Request response = WebConnector.connect(request);					
					Map<String,Boolean> errors = (Map<String,Boolean>)response.getAttribute("errors");
					if(errors.isEmpty()) { //ã�� ����
						//System.out.println((MemberVO)response.getAttribute("user"));
						new PasswordEditFrame((MemberVO)response.getAttribute("user"));
					} else { //ã�� ����
						if(errors.get("SQLException")==Boolean.TRUE) {
							new MessageFrame("�������� ������ ���� �ʽ��ϴ�.\n ��� �� �������ּ���.",false);
						}
						else {
							new MessageFrame("�������� �ʴ� ȸ���Դϴ�.",false);
						}
					}
				}
			};
			thread.start();
		}
	}
		

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()=="���"){
			dispose();
			loginFrm.setVisible(true);
		}  else {		
				Request request = new Request("finduser");
				Request response = null;
				if(e.getActionCommand() == "IDã��") {
					findIdHandler(request);
				} else if(e.getActionCommand() == "PWã��") {
					findPwdHandler(request);
				}
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == passwordTxtInputId) {  //pw
			if (passwordTxtInputId.getText().equals("�� �� ��(ID)")) {
				passwordTxtInputId.setText("");
				passwordTxtInputId.setForeground(Color.BLACK);
			}

		} else if (e.getSource() == passwordTxtInputPhone) {  //pw
			if (passwordTxtInputPhone.getText().equals("�� �� ��(PHONE)")) {
				passwordTxtInputPhone.setText("");
				passwordTxtInputPhone.setForeground(Color.BLACK);
			}
		} else if (e.getSource() == idTxtInputFirstBirth) {  //id
			if (idTxtInputFirstBirth.getText().equals("�������(BIRTH)")) {
				idTxtInputFirstBirth.setText("");
				idTxtInputFirstBirth.setForeground(Color.BLACK);
			}
		} else if (e.getSource() == idTxtInputPhone) {  //id
			if (idTxtInputPhone.getText().equals("�� �� ��(PHONE)")) {
				idTxtInputPhone.setText("");
				idTxtInputPhone.setForeground(Color.BLACK);
			}
		}
	}


	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		if (passwordTxtInputId.getText().isEmpty()) {
			passwordTxtInputId.setForeground(Color.GRAY);
			passwordTxtInputId.setText("�� �� ��(ID)");
		}

		else if (passwordTxtInputPhone.getText().isEmpty()) {
			passwordTxtInputPhone.setForeground(Color.GRAY);
			passwordTxtInputPhone.setText("�� �� ��(PHONE)");
		} 
		else if (idTxtInputFirstBirth.getText().isEmpty()) {
			idTxtInputFirstBirth.setForeground(Color.GRAY);
			idTxtInputFirstBirth.setText("�������(BIRTH)");
		}
		else if (idTxtInputPhone.getText().isEmpty()) {
			idTxtInputPhone.setForeground(Color.GRAY);
			idTxtInputPhone.setText("�� �� ��(PHONE)");
		} 
		
	}

}
