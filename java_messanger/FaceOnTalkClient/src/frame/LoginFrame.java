package frame;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.ClientHandler;
import util.ButtonCursorHandler;



public class LoginFrame extends JFrame implements ActionListener,ItemListener,FocusListener {
   //final variables
   private final int WIDTH = 390,HEIGHT = 480;
   private final String[] emailValue = { "naver.com", "nate.com", "google.com", "daum.net", "�����Է�" };   
   private JButton mainLogo = new JButton(new ImageIcon("image/logo.png"));
   //GUI variables
   private JPanel contentPane = new JPanel();
   private JPanel panel = new JPanel();
//   private JLabel emailLabel = new JLabel("�� �� �� : ");
   private JTextField txtInputId = new JTextField("�� �� ��(ID)");
   private JTextField txtInputEmailAddr = new JTextField(emailValue[0]);
   
   private JLabel atLabel = new JLabel("@"); // @ ǥ��
//   private JLabel passwordLabel = new JLabel("��й�ȣ : ");
   private JPasswordField txtInputPwd = new JPasswordField("�� �� �� ȣ �� ��");
  
   private JComboBox<String> emailComboBox = new JComboBox<>(emailValue);   
   private JButton btnLogin = makeButton("�α���");
   private JButton btnJoin = makeButton("ȸ �� �� ��");
   private JButton btnPwFind = makeButton("���̵��н�����ã��");
   
   public LoginFrame(String title) {
      super(title);
      init();
   }

   private void init() {
      /////////////////////
      ////// Frame Size
      setSize(WIDTH,HEIGHT);
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      int xpos = (int) (screen.getWidth() / 2 - this.getWidth() / 2);
      int ypos = (int) (screen.getHeight() / 2 - this.getHeight() / 2);
      setLocation(xpos, ypos);
      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);
      Container con = this.getContentPane();
      panel.setBounds(0, 0, WIDTH, HEIGHT);
      //
 
      contentPane.add(panel);
      panel.setLayout(null);      
      
      panel.setBackground(new Color(255,255,255));

      // ===============IDtext����,IDtext�Է±���==============      
      //email label
//      panel.add(emailLabel);      
//      emailLabel.setBounds(30, 258, 65, 21);   
      
      panel.add(mainLogo);
      mainLogo.setBounds(120, 40, 150, 150);
      mainLogo.setBorderPainted(false);
      mainLogo.setContentAreaFilled(false);
      mainLogo.setFocusPainted(false);
      //id text field
      panel.add(txtInputId);
      txtInputId.setBounds(20, 258, 105, 21);
      txtInputId.setColumns(16);    
      txtInputId.setForeground(Color.GRAY);
	  txtInputId.addFocusListener(this);
      txtInputId.setColumns(30);
      
      //at(@) label
      panel.add(atLabel);      
      atLabel.setBounds(130, 258, 20, 21);
      //email textfield      
      panel.add(txtInputEmailAddr);
      txtInputEmailAddr.setBounds(150, 258, 100, 21);
      txtInputEmailAddr.setEnabled(false); 
      txtInputEmailAddr.setFont(new Font("",Font.BOLD,13));
      
      //emailComboBox
      emailComboBox.setBounds(260, 258, 100, 21);
      emailComboBox.addItemListener(this);
      //emailComboBox.setBackground(new Color(0,0,0));
      emailComboBox.setBackground(Color.blue);
      emailComboBox.setForeground(Color.white);
      panel.add(emailComboBox);
      
      // ====================================================

      // ===============pwtext����,pwtext�Է±���==============
//      passwordLabel.setBounds(30, 292, 65, 21);
//      panel.add(passwordLabel);
      txtInputPwd.setBounds(20, 292, 232, 21);
      panel.add(txtInputPwd);
      txtInputPwd.setForeground(Color.GRAY);
	  txtInputPwd.addFocusListener(this);
      txtInputPwd.addActionListener( (e) -> {
         loginHandler();
      });

      // ==============�α��ι�ư����====================
      btnLogin.setBounds(260, 292, 100, 21);
      panel.add(btnLogin);
      //btnLogin.setBackground(new Color(0,0,0));      
      btnLogin.setBackground(Color.BLUE);
      
      btnLogin.setForeground(Color.WHITE);

      // ================ȸ�����Թ�ư����======================
      btnJoin.setBounds(50, 350, 100, 23);
      panel.add(btnJoin);btnJoin.setBorderPainted(false); btnJoin.setContentAreaFilled(false); btnJoin.setFocusPainted(false); 

      // ================��ư Ŀ�� ���� �̺�Ʈ ���==================
      btnJoin.addMouseListener(ButtonCursorHandler.getInstance());
      btnPwFind.addMouseListener(ButtonCursorHandler.getInstance());
            
      // ================idpwã���ư����==================
      btnPwFind.setBounds(180, 350, 160, 23);
      panel.add(btnPwFind);btnPwFind.setBorderPainted(false); btnPwFind.setContentAreaFilled(false); btnPwFind.setFocusPainted(false); 
      
      // ��Ŀ�� �ִ� ����
      addWindowFocusListener(new WindowAdapter() {
          public void windowGainedFocus(WindowEvent e) {
        	  txtInputEmailAddr.requestFocusInWindow();
          }
      });
      //////////////
      
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
   }
      
   private JButton makeButton(String value) {
      JButton btn = new JButton(value);
      btn.addActionListener(this);
      return btn;
   }

   // ==============�̺�Ʈ ó������============
   @Override
   public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "ȸ �� �� ��") {
			joinHandler();							
		} else if (e.getActionCommand() == "���̵��н�����ã��") {
			 new FindFrame(this).setVisible(true);
			 setVisible(false);
		} else if (e.getActionCommand() == "�α���") {
			loginHandler();
		}
	}
   
   public void joinHandler() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				new JoinFrame(LoginFrame.this).setVisible(true); //JoinFrame Ȱ��ȭ			
				LoginFrame.this.setVisible(false); //�α���â ��Ȱ��ȭ
				txtInputId.setText("�� �� ��(ID)");
				txtInputPwd.setText("�� �� �� ȣ �� ��");	
			}
		};
		thread.start();
   }
   
   public void loginHandler() {
		Thread thread = new Thread(){
			@Override
			public void run() {
				String email = txtInputId.getText()+"@"+txtInputEmailAddr.getText();
				String password = new String(txtInputPwd.getPassword());				
				ClientHandler.getInstance().login(email,password,LoginFrame.this);
			}
		};
		thread.start();
	}

   // ==========text Item
   @Override
   public void itemStateChanged(ItemEvent e) {		
		clearField(txtInputEmailAddr);// �ʱ�ȭ		
		if (!((String)emailComboBox.getSelectedItem() == "�����Է�")) {
			txtInputEmailAddr.setEnabled(false); // �ؽ�Ʈ �ʵ��� enable �Ӽ��� false�� �Ͽ� ���� �Է����� ���ϰ� �Ѵ�.			
			txtInputEmailAddr.setText((String)emailComboBox.getSelectedItem());
			// �޺��ڽ��� ���õ� �����ۿ� ���ڿ��� �ؽ�Ʈ �ʵ忡 text�� �����Ѵ�.
		} else {
			// ���� ���� �Է��̶�� ���� �ؽ�Ʈ �ʵ忡 �����͸� �����ϰ� �� enable �Ӽ��� true�� Ȱ��ȭ
			txtInputEmailAddr.setEnabled(true);			
//			emailComboBox.setToolTipText("123");		
		}
	}	
	
   private void clearField(JTextField field){
		field.setText("");
   }

   @Override
   public void focusGained(FocusEvent e) {
	  //String confirmPassword = new String(txtInputPwd.getPassword());
	   if (e.getSource() == txtInputId) {
			if (txtInputId.getText().equals("�� �� ��(ID)")) {
				txtInputId.setText("");
				txtInputId.setForeground(Color.BLACK);
			}
	   }else if(e.getSource()==txtInputPwd){
		   if (new String(txtInputPwd.getPassword()).equals("�� �� �� ȣ �� ��")) {
				txtInputPwd.setText("");
				txtInputPwd.setForeground(Color.BLACK);
			}
	   }
   }
   @Override
   public void focusLost(FocusEvent e) {
   		if (txtInputId.getText().isEmpty()) {
			txtInputId.setForeground(Color.GRAY);
			txtInputId.setText("�� �� ��(ID)");
		}else if(new String(txtInputPwd.getPassword()).isEmpty()){
			txtInputPwd.setForeground(Color.GRAY);			
			txtInputPwd.setText("�� �� �� ȣ �� ��");
		}   		
   	}
}
