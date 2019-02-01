package grade;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class InputFrame extends Frame implements Runnable,FocusListener{
	private GradeHandler manager = GradeHandler.getInstance();
	
	private String notExistName = "�̸��� �������� �ʽ��ϴ�.";
	private String duplicateName = "������ �̸��� �����մϴ�.";
	private String invalidScore = "0-100������ ������ �Է����ּ���.";
		
	private TextField[] txtInputScore;	

	private Button btnInput;
	private Button btnCancel;
	
	private Label labelName;
	private Label[] labelScoreName;
	
	private TextArea txtDisplay;	
	
	public InputFrame(TextArea txtDisplay){	
		this.txtDisplay = txtDisplay;		
	}
	
	public void run(){	
		System.out.println("��");
		initFrame();
	}
	
	public void showInputFrame(){
		this.setVisible(true);
	}
	
	
	public void setCommand(String comm){
		command = comm;
	}
	
	private void initFrame(){
		///////////////////////////////////////////
		//�ʱ⼳��		
		this.setSize(300,300);			
		Dimension screen=Toolkit.getDefaultToolkit().getScreenSize(); 		
		int xpos=(int)(screen.getWidth()/2.0-this.getWidth()/2.0); 
		int ypos=(int)(screen.getHeight()/2.0-this.getHeight()/2.0);	
		this.setLocation(xpos, ypos); 
		this.setResizable(false);		
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				hideFrame();
			}			
		});
		///////////////////////////////////////////
		this.setLayout(new BorderLayout());
		
		btnInput = new Button("�Է�");
		btnCancel = new Button("���");
		
		labelName = new Label("�̸� : ");
		labelKor = new Label("���� : ");
		labelEng = new Label("���� : ");
		
		txtInputName = new TextField();
		txtInputKor = new TextField();
		txtInputEng = new TextField();
		
		txtInputName.addFocusListener(this);
		txtInputKor.addFocusListener(this);
		txtInputEng.addFocusListener(this);
		
		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(3,1));
		centerPanel.add(makePanel(labelName,txtInputName));
		centerPanel.add(makePanel(labelKor,txtInputKor));
		centerPanel.add(makePanel(labelEng,txtInputEng));
		
		Panel southPanel = new Panel();
		southPanel.setLayout(new FlowLayout());
		southPanel.add(btnInput); southPanel.add(btnCancel);
		
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);	
		
		
		//�̺�Ʈ ó��
		btnInput.addActionListener(e -> inputBtnHandler());	
		btnCancel.addActionListener(e -> hideFrame());		
	}
	
	private void clearText(TextField selectedField){
		
	}	
	
	private int isValidNumber(String input){
		int score = 0;
		try{
			score = Integer.parseInt(input);
			if(score<0 || score >100)
				return -1;
			return score;
		}catch(Exception e){
			return -1;
		}
	}
	
	private void printDisplay(){
		List<String> contentList = manager.getAllStuInfo();		
		txtDisplay.setText("�̸�\t����\t����\t����\t����\n");
		txtDisplay.append("-----------------------------------------------------\n");
		for(int i=0; i< contentList.size();i++){
			txtDisplay.append(contentList.get(i));
		}
	}
	
	private void hideFrame(){
		txtInputName.setText("");
		txtInputKor.setText("");
		txtInputEng.setText("");
		this.setVisible(false);
	}
	
	private void inputHandler(){
		String name = txtInputName.getText();
		int kor = isValidNumber(txtInputKor.getText());
		int eng = isValidNumber(txtInputEng.getText());
		if(kor==-1 || eng == -1){
			txtInputKor.setText(invalidScore);
			txtInputEng.setText(invalidScore);
			return;
		}
		
		if(!manager.inputStuInfo(name, kor, eng)){ // �������
			txtInputName.setText(duplicateName);
			return;
		}	
		printDisplay();
		hideFrame(); //���强��		
	}
	
	private void modifyHandler(){
		String name = txtInputName.getText();
		int kor = isValidNumber(txtInputKor.getText());
		int eng = isValidNumber(txtInputEng.getText());
		
		if(kor== -1 || eng == -1){
			txtInputKor.setText(invalidScore);
			txtInputEng.setText(invalidScore);
			return;
		}
		
		if(!manager.modify(name, kor, eng)){ // �������
			txtInputName.setText(notExistName);
			return;
		}	
		printDisplay();
		hideFrame();
	}
	
	private void removeHandler(){
		String name = txtInputName.getText();
				
		if(!manager.remove(name)){ // �������
			txtInputName.setText(notExistName);
			return;
		}	
		
		printDisplay();
		hideFrame();
	}
	
	public void inputBtnHandler(){
		if(command.equals("input"))
			inputHandler();
		else if(command.equals("modify"))
			modifyHandler();
		else if(command.equals("delete"))
			removeHandler();		
	}	
	
	private Panel makePanel(Label label,TextField txt){
		Panel panel = new Panel();
		panel.setLayout(new GridLayout(2,2,5,5));
		panel.add(label);
		panel.add(txt);
		return panel;
	}

	@Override
	public void focusGained(FocusEvent e) {
		TextField selectedField = (TextField)e.getSource();
		String text = selectedField.getText();
		if(text.equals(notExistName) || text.equals(duplicateName) || text.equals(invalidScore))
			selectedField.setText("");		
	}

	@Override
	public void focusLost(FocusEvent e) {}	
}