package baseball;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame implements ActionListener {
	private final int WIDTH=700,HEIGHT=800;
	private int currentIdx = 0;
	private TeamManager manager =TeamManager.getInstance();
	
	// UI
	private JButton[] btnTeam = new JButton[Team.defaultTeam.length];
	private ListPanel listPanel[] = new ListPanel[Team.defaultTeam.length];
	private JPanel viewPanel = new JPanel();
	

	public MainFrame() {
		super("�߱��� �������α׷�");		
		init();
	}

	private void init() {
		//		
		setSize(WIDTH,HEIGHT);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int) (screen.getWidth() / 2.0 - this.getWidth() / 2.0);
		int ypos = (int) (screen.getHeight() / 2.0 - this.getHeight() / 2.0);
		this.setLocation(xpos, ypos);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				manager.saveToFile();
				System.exit(0);				
			}			
		});
		///////////////////////////////////////////
		setLayout(new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(1,Team. defaultTeam.length, 10, 10));
		
		//����ư ����
		for (int i = 0; i < btnTeam.length; i++) {
			btnTeam[i] = new JButton(Team.defaultTeam[i]);
			btnTeam[i].addActionListener(this);
			northPanel.add(btnTeam[i]);
		}
		
		//diplay �ǳ� ����
		for(int i=0;i<listPanel.length;i++){
			listPanel[i] = new ListPanel(Team.defaultTeam[i]);
		}	
		
		add(northPanel, BorderLayout.NORTH);
		add(viewPanel,BorderLayout.CENTER);
		setVisible(true);
	}

	// ����ư �ڵ鷯
	@Override
	public void actionPerformed(ActionEvent e) {
		if(currentIdx>=0 && currentIdx<btnTeam.length)
			btnTeam[currentIdx].setEnabled(true);
		
		JButton selectedBtn = (JButton)e.getSource();
		for(int i=0;i<btnTeam.length;i++){
			if(selectedBtn == btnTeam[i]){
				btnTeam[i].setEnabled(false);
				currentIdx = i;
				changeViewPanel(i);
			}
		}
	}
	
	private void changeViewPanel(int idx){		
		remove(viewPanel);
		viewPanel = listPanel[idx];
		add(viewPanel,BorderLayout.CENTER);
		listPanel[idx].displayInfo();
		invalidate();
		repaint();		
	}
	
	
	// center�� list �����ִ� static Ŭ����
	private static class ListPanel extends JPanel implements ActionListener {
		private String teamName;
		private TeamManager manager = TeamManager.getInstance();

		private JButton btnInput;
		private JButton btnDelete;
		private JButton btnModify;
		private JTextArea txtDisplay;
		private JScrollPane displayScroll;

		public ListPanel(String name) {
			teamName = name;
			makePanel();
			displayInfo();
		}

		private void makePanel() {
			setLayout(new BorderLayout());
			// center
			txtDisplay = new JTextArea();
			txtDisplay.setFont(new Font("", Font.BOLD, 15));
			txtDisplay.setEditable(false);
			displayScroll = new JScrollPane(txtDisplay);
			add(displayScroll, BorderLayout.CENTER);

			// south
			JPanel southPanel = new JPanel();
			southPanel.setLayout(new GridLayout(1, 3));
			btnInput = makeButton("�Է�");
			btnDelete = makeButton("����");
			btnModify = makeButton("����");
			southPanel.add(btnInput);
			southPanel.add(btnDelete);
			southPanel.add(btnModify);
			add(southPanel, BorderLayout.SOUTH);
		}
		
		private JButton makeButton(String caption) {
			JButton btn = new JButton(caption);
			btn.addActionListener(this);
			return btn;
		}
		
		//��ư �̺�Ʈ �ڵ鸵
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "�Է�")
				inputHandler();
			else if (e.getActionCommand() == "����")
				removeHandler();
			else if (e.getActionCommand() == "����")
				modifyHandler();
		}
		
		private String input(String value){
			String name = JOptionPane.showInputDialog(this, value,"input",JOptionPane.QUESTION_MESSAGE);			
			if(name!=null && name.isEmpty()){
				name = null;
				JOptionPane.showMessageDialog(this, "�Է��� �ٸ��� �ʽ��ϴ�.");
			}
			return name;
		}	
		
		//�Է�
		private void inputHandler() {	
			//�Է¹ޱ�
			String name = input("�̸� : ");
			if(name==null) return;
			
			String salary = input("���� : ");
			if(salary == null) return;
			
			String position = input("������ : ");
			if(position == null) return;			
			
			//���� �˻�
			Map<String,Boolean> errors = new HashMap<>();			
			manager.addPlayer(teamName, name,salary,position, errors);			
			if(errors.isEmpty()){
				JOptionPane.showMessageDialog(this,"���������� �����Ͽ����ϴ�.");
				displayInfo();
			}else{				
				if(errors.get("duplicateName")==Boolean.TRUE)
					JOptionPane.showMessageDialog(this, "������ �̸��� �����մϴ�.");
				else if(errors.get("invalidSalary")==Boolean.TRUE)
					JOptionPane.showMessageDialog(this, "������ ���ڸ� �Է����ּ���.");				
			}					
		}

		//����
		private void removeHandler() {
			//�Է¹ޱ�
			String name = input("���� �� �̸� : ");
			
			//���ܰ˻�
			Map<String,Boolean> errors = new HashMap<>();
			manager.removePlayer(teamName, name, errors);			
			if(errors.isEmpty()){
				JOptionPane.showMessageDialog(this,"���������� �����Ͽ����ϴ�.");
				displayInfo();
			}else{				
				if(errors.get("notExistName")==Boolean.TRUE)
					JOptionPane.showMessageDialog(this, "�̸��� �������� �ʽ��ϴ�.");				
			}
		}
		
		//����
		private void modifyHandler() {
			//�Է¹ޱ�
			String name = input("���� �� �̸� : ");
			if(name==null) return;
			
			String salary = input("���� �� ���� : ");
			if(salary == null) return;
			
			String position = input("���� �� ������ : ");
			if(position == null) return;
			
			//���ܰ˻�
			Map<String,Boolean> errors = new HashMap<>();
			manager.modifyPlayer(teamName, name,salary,position, errors);			
			
			if(errors.isEmpty()){
				JOptionPane.showMessageDialog(this,"���������� �����Ͽ����ϴ�.","success",JOptionPane.DEFAULT_OPTION);
				displayInfo();
			}else{							
				if(errors.get("notExistName")==Boolean.TRUE)
					JOptionPane.showMessageDialog(this, "�������� �ʴ� �̸��Դϴ�.");
				else if(errors.get("invalidSalary")==Boolean.TRUE)
					JOptionPane.showMessageDialog(this, "������ ���ڸ� �Է����ּ���.");
			}	
		}
	
		//���� ���
		public void displayInfo() {
			Player[] playArr = manager.getPlayersArray(teamName);
			if (playArr.length==0)
				txtDisplay.setText("��ϵ� ������ �����ϴ�.");
			else {
				txtDisplay.setText("��ȣ\t�̸�\t����\t������\n");
				for (int i = 0; i < playArr.length; i++) {					
					txtDisplay.append((i + 1) + "\t" + playArr[i].getPlayerInfo());
				}
			}
		}
	}


	// main
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainFrame();
			}
		});		
	}
}
