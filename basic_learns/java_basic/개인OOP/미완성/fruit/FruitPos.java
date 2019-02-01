package fruit;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//�ֹ� ��ư
class FruitButton extends Button{
	private int fruitIdx; //�ε��� (Fruit�� �ִ°�)
//	private Image image;
	public FruitButton(int idx) {
		fruitIdx = idx;
//		String imagePath = "images/fruit/image" + i + ".jpg";
//		image = Toolkit.getDefaultToolkit().getImage(imagePath);
	}		
	public int getFruitIndex(){ //���� ��ư�� index�� ��ȯ
		return fruitIdx;
	}
//	@Override
//	public void paint(Graphics g) {
//		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
//	}	
}

//�ֹ����� ���� â ���°�
public class FruitPos extends Frame implements ActionListener, Runnable {
	private FruitButton[] btnFruit = new FruitButton[Fruit.SIZE];
	private OrderHistory order;	//MainFrame���� ������ OrderHistory �ν��Ͻ��� ���� ������
	
	private int totalPrice; // �Ǹ� �ݾ�
	private String content; // MainFrame���� TextArea�� ������ ����
	//UI ���� �ڵ�
	Panel centerPanel;
	private TextArea txtDisplay; //���� â�� �翩�ִ� ����
	private TextArea tableDisplay; //MainFrame�� TextArea�� �翩�ִ°�
	private Label tableLabel;	//�̸�
	
	public FruitPos(int tableNumber,TextArea txt, OrderHistory order) {
		super("--Fruit Order Menu--");
		System.out.println("FruitPos : "+Thread.currentThread().getName());
		tableLabel = new Label(tableNumber+"�� ���̺� �ֹ�(��� ������ ��ư)",Label.CENTER);
		tableDisplay = txt;
		this.order = order;
	}

	private void initFrame() {
		///////////////////////////////////////////
		// �ʱ⼳��
		int xpos = (int) (this.getWidth() / 2.0);
		int ypos = (int) (this.getHeight() / 2.0);
		this.setBounds(xpos, ypos, 600, 600);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();				
			}
		});
		this.setLayout(new BorderLayout());
		txtDisplay = new TextArea(5, 40); //�ֹ������� ������ ��
		txtDisplay.setFont(new Font("", Font.BOLD, 13));
		tableLabel.setFont(new Font("", Font.BOLD, 15));
		tableLabel.setForeground(Color.BLUE);
		
		centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout((int) Math.sqrt(Fruit.SIZE), (int) Math.sqrt(Fruit.SIZE), 5, 5));
		///////////////////////////////////////////////////////////
		for (int i = 0; i < btnFruit.length; i++) {
			btnFruit[i] = new FruitButton(i);
			centerPanel.add(btnFruit[i]);
			btnFruit[i].addActionListener(this);
			btnFruit[i].addMouseListener(new MouseAdapter() { //��ҹ�ư�ڵ鸵
				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.getButton() == 3) { //��Ŭ���̸�
						FruitButton selectedBtn = (FruitButton) e.getSource();
						int fruitIdx = selectedBtn.getFruitIndex();
						System.out.println(fruitIdx);
						order.cancel(fruitIdx);		
						editTextDisplay();
					}
				}
			});
		}
		txtDisplay.setText("");		
		this.add(tableLabel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(txtDisplay, BorderLayout.EAST);
	}
	
	@Override
	public void run() {	
		initFrame();
		this.setVisible(true);		
	}	

	//�ֹ�â ������  TextArea �� Table�� TextArea ���� ���
	private void editTextDisplay() {
		if(order == null)
			dispose();
		totalPrice = 0;
		txtDisplay.setText("");
		txtDisplay.setText("�ֹ�������\n");
		txtDisplay.append("=================================\n");		
		for (int i = 0; i < Fruit.SIZE; i++) {			
			if (order.isSaled(i)) {
				totalPrice += (Fruit.PRICE[i]*order.getCount(i));								
				txtDisplay.append(order.getSalesInfo(i)+ "\n");
			}
		}
		
		tableDisplay.append("=================================\n");
		tableDisplay.append("���� : " + totalPrice);
		content =txtDisplay.getText();
		tableDisplay.setText(content);
	}

	@Override
	public void actionPerformed(ActionEvent e) { //�޴� ��ư Ŭ�� �̺�Ʈ �ڵ鸵
		FruitButton selectedBtn = (FruitButton) e.getSource();
		order.add(selectedBtn.getFruitIndex()); //�ֹ� ������ �ø���
		editTextDisplay(); //TextArea�� �ٲ���
	}

	
//	public static void main(String[] args){		 
//		 SwingUtilities.invokeLater(new FruitPos(1,new TextArea(),new OrderHistory()));		 
//	}
}
