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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import connector.WebConnector;
import noticeframe.MessageFrame;
import request.Request;
import util.RegularExpression;

public class JoinFrame extends JFrame implements ActionListener,FocusListener, CaretListener {	
	private boolean isChecked = false;
	private String checkedEmail;
	
	public JoinFrame(JFrame loginFrm) {
		super("Face On Talk join");
		this.loginFrm = loginFrm;		
		this.init();
	}	
	///////////////////
	//GUI
	private final int WIDTH = 370, HEIGHT = 500;
	private JPanel contentPane = new JPanel();
	private JPanel panel = new JPanel();
	private JLabel mainLabel = new JLabel("ȸ �� �� ��");

	private JTextField txtInputName = new JTextField("��     ��(NAME)");
	private JLabel nameLabel = new JLabel("ex) ȫ�浿");
	
	private JTextField txtInputId = new JTextField("�� �� ��(ID)");
	private JLabel idLabel = new JLabel("ex) hyungss71@naver.com");	

	private JPasswordField txtInputPassword = new JPasswordField("�� �� �� ȣ �� ��");
	private JLabel ex_pwLabel = new JLabel("ex) ����+����8~12��");	

	private JPasswordField txtInputRpwd = new JPasswordField();
	private JLabel rpwComparisonLabel = new JLabel("");
	private JLabel repasswordLabel = new JLabel("ex) ��й�ȣ ���Է� ");

	private JTextField txtInputPhone = new JTextField("�� �� ��(PHONE)");
	private JLabel phoneLabel = new JLabel("ex) 010-7777-7777");

	private JLabel birthdayLabel = new JLabel("ex) 890202 - 1****** (�� 1�ڸ�)");
	private JTextField txtInputFirstBirth = new JTextField("�������(BIRTH)");
	private JTextField txtInputSecondBirth = new JTextField();
	private JLabel slashBirthLabel = new JLabel("-");
	private JLabel starBirthLabel = new JLabel("****** (�� 1�ڸ�)");

	private JFrame loginFrm;
	private JButton btnIdCheck = makeButton("ID�ߺ�üũ");
	private JButton btnJoin = makeButton("����");
	private JButton btnCancel = makeButton("���");	

	public void init() {
		setSize(WIDTH, HEIGHT);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int) (screen.getWidth() / 2 - this.getWidth() / 2);
		int ypos = (int) (screen.getHeight() / 2 - this.getHeight() / 2);
		setLocation(xpos, ypos);
		setResizable(false);
		
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(0, 0, WIDTH, HEIGHT);
		contentPane.add(panel);
		panel.setLayout(null);
		
		panel.setBackground(new Color(255,255,255));
		

		// ===================ȸ������
		panel.add(mainLabel);
		
		mainLabel.setFont(new Font("����ü", Font.BOLD, 18));
		mainLabel.setBounds(135, 21, 116, 31);

		// ==============�̸��Է�
		panel.add(txtInputName);//�Է¶�
		txtInputName.addFocusListener(this);
		txtInputName.setColumns(10);
		txtInputName.setBounds(50, 80, 175, 21);

		panel.add(nameLabel);//��
		nameLabel.setBounds(50, 100, 300, 21);
		nameLabel.setForeground(Color.GRAY);
		
		// =======���̵�
		panel.add(txtInputId);   //�Է�
		txtInputId.setBounds(50, 130, 175, 21);
		txtInputId.setForeground(Color.GRAY);
		txtInputId.addFocusListener(this);
		txtInputId.setColumns(30);
		
		
		panel.add(idLabel);		//��
		idLabel.setBounds(50, 150, 300, 21);
		idLabel.setForeground(Color.GRAY);
		
		// ========��й�ȣ
		panel.add(txtInputPassword);
		txtInputPassword.addFocusListener(this);
		txtInputPassword.setBounds(50, 180, 175, 21);
		txtInputPassword.setForeground(Color.GRAY);
		txtInputPassword.setColumns(20);

		panel.add(ex_pwLabel);
		ex_pwLabel.setForeground(Color.gray);
		ex_pwLabel.setBounds(50, 200, 300, 21);

		
		// ���Է� ��й�ȣ����==
		panel.add(txtInputRpwd); 
		txtInputRpwd.addFocusListener(this);
		txtInputRpwd.setColumns(10);
		txtInputRpwd.addCaretListener(this); //��ġ����ġ �̺�Ʈó������
		txtInputRpwd.setBounds(50, 230, 175, 21);

		panel.add(rpwComparisonLabel); //��ġ ����ġ �󺧱���
		rpwComparisonLabel.setForeground(Color.BLUE);
		//rpwComparisonLabel.setBounds(235, 230, 180, 21);
		rpwComparisonLabel.setBounds(235, 230, 200, 21);

		panel.add(repasswordLabel);  //��
		repasswordLabel.setBounds(50, 250, 300, 21);
		repasswordLabel.setForeground(Color.gray);
		
		
		// ==============�ڵ���
		panel.add(txtInputPhone);
		txtInputPhone.addFocusListener(this);
		txtInputPhone.setColumns(15);
		txtInputPhone.setForeground(Color.GRAY);
		txtInputPhone.setBounds(50, 280, 175, 21);
		

		panel.add(phoneLabel);  //��
		phoneLabel.setBounds(50, 300, 300, 21);		
		phoneLabel.setForeground(Color.GRAY);
		
		// ==============�ֹι�ȣ

		panel.add(txtInputFirstBirth);
		txtInputFirstBirth.setBounds(50, 330, 96, 21);
		txtInputFirstBirth.setForeground(Color.GRAY);
		txtInputFirstBirth.addFocusListener(this);
		txtInputFirstBirth.setColumns(10);

	
		panel.add(slashBirthLabel);
		slashBirthLabel.setBounds(148, 330, 20, 21);  //-����
		
		panel.add(txtInputSecondBirth);
		txtInputSecondBirth.setBounds(156, 330, 15, 21);
		txtInputSecondBirth.addFocusListener(this);
		txtInputSecondBirth.setColumns(1);
		
		
		panel.add(starBirthLabel);
		starBirthLabel.setBounds(172, 330, 180, 21);  //*����

		panel.add(birthdayLabel);   //��
		birthdayLabel.setBounds(50, 350, 300, 21);
		birthdayLabel.setForeground(Color.GRAY);
		
		
		// ========��ư����=======
		panel.add(btnIdCheck);
		btnIdCheck.setBounds(235, 130, 100, 23);
		
		btnJoin.setBounds(90, 400, 73, 23);
		panel.add(btnJoin);

		btnCancel.setBounds(210, 400, 73, 23);
		panel.add(btnCancel);
		
		txtInputId.addCaretListener(e -> {
			if(isChecked) {
				if(!txtInputId.getText().equals(checkedEmail)){
					btnIdCheck.setEnabled(true);
					isChecked = false;
					checkedEmail = "";
				}
			} 
		});
		
		//��ư�� ��Ŀ�� �ִ� ����
		addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				mainLabel.requestFocusInWindow();
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				loginFrm.setVisible(true);
			}
		});	
		setVisible(true);		
	}
	
	private JButton makeButton(String value) {
		JButton btn = new JButton(value);
		btn.addActionListener(this);
		btn.setBackground(new Color(0,0,0));
		btn.setForeground(Color.WHITE);
		return btn;
	}

	@Override	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand() == "���") {
			dispose();
			loginFrm.setVisible(true);
		} else if (e.getActionCommand() == "����") {
			joinHandler();
		} else if (e.getActionCommand() == "ID�ߺ�üũ") {
			confirmIdHandler();
		}
	}
	
	private void joinHandler() {
		String name = txtInputName.getText();
		String id = txtInputId.getText();
		String pw = (new String(txtInputPassword.getPassword()));
		String hp = txtInputPhone.getText(); //.replaceAll("-", "");//���ڿ� ��ü�ϱ�
		String birth = txtInputFirstBirth.getText();
		String gender = txtInputSecondBirth.getText();
		
		if(!isChecked) {
			new MessageFrame("���̵� �ߺ�üũ�� ���ּ���.",false);			
		}else if (checkEmpty(id,hp,birth,pw,gender,name,gender)) {			
			new MessageFrame("��� ���� �Է����ּ���",false);
		} else {
			if (gender.equals("1"))
				gender = "m";
			else if (gender.equals("2"))
				gender = "f";

			if (!RegularExpression.isEmailExpression(id)) {
				new MessageFrame("�̸���(ID) ������ �����ּ���\n " + "ex) hyungss71@naver.com", false);
			} else if (!RegularExpression.isPhoneExpression(hp)) {
				new MessageFrame("�ڵ�����ȣ ������ �����ּ���\n" + "ex) 010-5091-7946 ", false);
			} else if (!RegularExpression.isSecondBirthExpression(gender)) {
				new MessageFrame("�ֹε�Ϲ�ȣ ���ڸ� ������ �����ּ���\n" + "ex) 1 or 2", false);
			} else if (!RegularExpression.isFirstBirthExpression(birth)) {
				new MessageFrame("�ֹε�Ϲ�ȣ ���ڸ� ������ �����ּ���\n" + "ex) 890202", false);
			} else if (new String(txtInputRpwd.getPassword())
					.equals(new String(txtInputPassword.getPassword())) == false) {
				new MessageFrame("���Է� ��й�ȣ�� Ʋ���ϴ�", false);
			} else if (!RegularExpression.isPasswordExpression(pw)) {
				new MessageFrame("����+�������� 8~12�ڸ� ������ �����ּ���", false);
			} else {
				Request request = new Request("join");
				request.setParameter("name", name);
				request.setParameter("email", id);
				request.setParameter("password", pw);
				request.setParameter("phone", hp);
				request.setParameter("birth", birth);
				request.setParameter("gender", gender);
				Thread thread = new Thread() {
					@Override
					public void run() {
						Request res = WebConnector.connect(request);
						if (res == null) {
							new MessageFrame("�������� ������ ���� �ʽ��ϴ�.", false);
						} else if (res.getType().equals("join")) {
							Map<String, Boolean> errors = (Map<String, Boolean>) res.getAttribute("errors");
							if (errors.isEmpty()) {
								new MessageFrame("ȸ������ ����", true);
								loginFrm.setVisible(true);
								dispose();
							} else {
								new MessageFrame("�������� ������ ���� �ʽ��ϴ�.", false);
							}
						}
					}
				};
				thread.start();
			}
		}
	}
	
	private void confirmIdHandler() {		
		String id = txtInputId.getText();
		if (id.isEmpty()) {
			new MessageFrame("���̵� �Է����ּ���.",false);
		} else if (!RegularExpression.isEmailExpression(id)) {
			new MessageFrame("�̸���(ID) ������ �����ּ���\n " + "ex) hyungss71@naver.com",false);//
		} else {
			Request request = new Request("confirmid");
			request.setParameter("email", id);
			Thread thread = new Thread() {
				@Override
				public void run() {
					Request res = WebConnector.connect(request);
					if (res == null) {
						new MessageFrame("�������� ������ ���� �ʽ��ϴ�.",false);
					} else if (res.getType().equals("confirmid")) {
						Map<String, Boolean> errors = (Map<String, Boolean>) res.getAttribute("errors");
						if (errors.isEmpty()) {
							new MessageFrame("��밡���� ���̵� �Դϴ�.",true);
							isChecked = true;
							checkedEmail = id;
							btnIdCheck.setEnabled(false);						
						} else {
							new MessageFrame("�̹� �����ϴ� ���̵� �Դϴ�.",false);
						}
					}
				}
			};
			thread.start();		
		}
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
		case "��     ��(NAME)" :
		case "�� �� ��(ID)" :
		case "�� �� ��(PHONE)" :
		case "�������(BIRTH)" :
			return true;
		default :
			return false;
		}
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == txtInputId) {
			if (txtInputId.getText().equals("�� �� ��(ID)")) {
				txtInputId.setText("");
				txtInputId.setForeground(Color.BLACK);
			}

		} else if (e.getSource() == txtInputPhone) {
			if (txtInputPhone.getText().equals("�� �� ��(PHONE)")) {
				txtInputPhone.setText("");
				txtInputPhone.setForeground(Color.BLACK);
			}
		} else if (e.getSource() == txtInputFirstBirth) {
			if (txtInputFirstBirth.getText().equals("�������(BIRTH)")) {
				txtInputFirstBirth.setText("");
				txtInputFirstBirth.setForeground(Color.BLACK);
			}
		} else if (e.getSource() == txtInputName) {
			if (txtInputName.getText().equals("��     ��(NAME)")) {
				txtInputName.setText("");
				txtInputName.setForeground(Color.BLACK);
			}
		} else if(e.getSource()==txtInputPassword){
			   if (new String(txtInputPassword.getPassword()).equals("�� �� �� ȣ �� ��")) {
					txtInputPassword.setText("");
					txtInputPassword.setForeground(Color.BLACK);
				}
		}		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		if (txtInputId.getText().isEmpty()) {
			txtInputId.setForeground(Color.GRAY);
			txtInputId.setText("�� �� ��(ID)");
		}

		else if (txtInputPhone.getText().isEmpty()) {
			txtInputPhone.setForeground(Color.GRAY);
			txtInputPhone.setText("�� �� ��(PHONE)");
		} 
		else if (txtInputFirstBirth.getText().isEmpty()) {
			txtInputFirstBirth.setForeground(Color.GRAY);
			txtInputFirstBirth.setText("�������(BIRTH)");
		}
		else if (txtInputName.getText().isEmpty()) {
			txtInputName.setForeground(Color.GRAY);
			txtInputName.setText("��     ��(NAME)");
		}
		else if(new String(txtInputPassword.getPassword()).isEmpty()){
			txtInputPassword.setForeground(Color.GRAY);
			txtInputPassword.setText("�� �� �� ȣ �� ��");
		}

	}

	// Ű���� �ϳ��ϳ� �Էµɶ� ���� �̺�Ʈ ó���Ǵ� ���� !!
	@Override
	public void caretUpdate(CaretEvent e) {
		String confirmPassword = new String(txtInputRpwd.getPassword());
		if (confirmPassword.isEmpty()) {
			rpwComparisonLabel.setText("");
		} else {
			if (!new String(txtInputPassword.getPassword()).equals(confirmPassword)) {
				rpwComparisonLabel.setText("��й�ȣ ����ġ");
				rpwComparisonLabel.setForeground(Color.RED);
			} else {
				rpwComparisonLabel.setText("��й�ȣ ��ġ");
				rpwComparisonLabel.setForeground(Color.BLUE);
			}
		}
	}
}
// ===============================================