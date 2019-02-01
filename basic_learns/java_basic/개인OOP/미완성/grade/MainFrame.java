package grade;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class MainFrame extends JFrame implements ActionListener{	
	private JButton btnInput; 
	private JButton btnModify;
	private JButton btnDelete;
	private JButton btnExit;
	
	private JTextArea txtDisplay;
	
	private InputFrame inputFrm;
		
	//private InputFrame inputFrm ;		
	public MainFrame(){
		super("Zac`s Student Manager");
		init();	
	}
	
	private void init(){
		///////////////////////////////////////////
		//�ʱ⼳��		
		this.setSize(600,600);			
		Dimension screen=Toolkit.getDefaultToolkit().getScreenSize(); 		
		int xpos=(int)(screen.getWidth()/2.0-this.getWidth()/2.0); 
		int ypos=(int)(screen.getHeight()/2.0-this.getHeight()/2.0);		
		this.setLocation(xpos, ypos); 
		this.setResizable(false);		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		///////////////////////////////////////////
		this.setLayout(new BorderLayout());
		
		
		btnInput = makeButton("�Է�"); 
		btnModify = makeButton("����");
		btnDelete = makeButton("����");
		btnExit = makeButton("����");
		
		txtDisplay = new JTextArea();
		txtDisplay.setFont(new Font("",Font.BOLD,15));
		txtDisplay.setText("�̸�\t����\t����\t����\t����\n");	
		txtDisplay.setEditable(false);
		
		Panel eastPanel = new Panel();
		eastPanel.setLayout(new GridLayout(4,1));
		eastPanel.add(btnInput); eastPanel.add(btnModify);
		eastPanel.add(btnDelete); eastPanel.add(btnExit);
		
		this.add(txtDisplay, BorderLayout.CENTER);		
		this.add(eastPanel, BorderLayout.EAST);	
		
		this.setVisible(true);		
//		inputFrm = new InputFrame(txtDisplay);
//		executorService.submit(inputFrm);		
	}	
		
	private JButton makeButton(String caption){
		JButton btn = new JButton(caption);
		btn.addActionListener(this);
		return btn;		
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		String command = e.getActionCommand();
		if(command == "�Է�"){
			System.out.println("�Է�");
			//inputFrm.setCommand("input");
		}else if(command == "����"){
			System.out.println("����");
			//inputFrm.setCommand("modify");
		}else if(command == "����"){
			System.out.println("����");
			//inputFrm.setCommand("delete");
		}else if(command == "����"){
			System.out.println("����");
			//System.exit(0);
		}
		//inputFrm.showInputFrame();
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				new MainFrame();
			}
		});		
	}
}
