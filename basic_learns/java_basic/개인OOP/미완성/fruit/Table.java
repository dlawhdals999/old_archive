package fruit;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;

public class Table extends Panel {
	//static variables
	private static Font labelFont = new Font("", Font.BOLD, 15);
	private static Font defaultFont = new Font("", Font.BOLD, 13);
	
	//variables
	private int tableNumber;
	private OrderHistory tableOrder;
	private OrderHistory todayAmount;
	private FruitPos orderThread;
	
	//UI
	private Label numberLabel;
	private TextArea txtDisplay;
	
	private Button btnEnter = new Button("����");
	private Button btnOrder = new Button("�ֹ�");
	private Button btnExit = new Button("����");
	
	public Table(int num,Button exit,OrderHistory todayAmount) {	
		tableNumber = num;
		this.btnExit = exit;
		this.todayAmount = todayAmount;
		btnOrder.setEnabled(false);
		btnExit.setEnabled(false);
		initPanel();		
	}
		
	public void exitBtnHandler(){
		orderThread = null;
		if(tableOrder ==null)
			return;
		btnOrder.setEnabled(false);		
		btnExit.setEnabled(false);		
		txtDisplay.setText("");
		todayAmount.addTableOrder(tableOrder);
		tableOrder = null;		
	}

	public void initPanel() {
		btnEnter.setFont(defaultFont);
		btnOrder.setFont(defaultFont);		
		btnExit.setFont(defaultFont);
		numberLabel = new Label(String.valueOf(tableNumber)+"�� ���̺�", Label.CENTER);
		numberLabel.setFont(labelFont);
		this.setLayout(new BorderLayout());

		Panel southPanel = new Panel();
		southPanel.setLayout(new FlowLayout());
		southPanel.add(btnEnter);
		southPanel.add(btnOrder);		
		southPanel.add(btnExit);
		txtDisplay = new TextArea(10, 40);
		txtDisplay.setEditable(false);

		// �̺�Ʈ ó��
		btnOrder.addActionListener(event -> {			
			if(orderThread != null)
				orderThread.run();
			//SwingUtilities.invokeLater(new FruitPos(tableNumber,txtDisplay,tableOrder));
		});	
		btnEnter.addActionListener(e -> {
			btnOrder.setEnabled(true);
			btnExit.setEnabled(true);
			if(tableOrder == null)
				tableOrder = new OrderHistory();
			orderThread = new FruitPos(tableNumber,txtDisplay,tableOrder);
		});		
		this.add(numberLabel, BorderLayout.NORTH);
		this.add(txtDisplay, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
	}
}
