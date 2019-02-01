package findmine;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame implements MouseListener,ActionListener {
	//���
	private final int[] DY = new int[] { -1, -1, -1, 0, 0, 1, 1, 1 };
	private final int[] DX = new int[] { -1, 0, 1, -1, 1, -1, 0, 1 };
	private final int SIZE = 9;
	private final int NUM_OF_MINE = 10;

	//variables
	private int[][] board;	
	private boolean isGamming;
	private int flagCount;

	//UI�ڵ�
	private JButton btnStartStop;
	private JButton btnReset;
	private JButton btnExit;
	private MineButton[][] mineBtn;
	private TimeThread timeThread;
	private JLabel timeLabel;
	private JLabel timeDisplayLabel;
	private int timeHistory;
	private JPanel centerPanel;

	//constructor
	public MainFrame() {
		super("Zac`s Fine Mine Game");
		init();
	}
	
	private void init() {
		///////////////////////////////////////////
		// �ʱ⼳�� JFrame ����
//		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
//		int xpos = (int) (screen.getWidth() / 2.0 - this.getWidth());
//		int ypos = (int) (screen.getHeight() / 2.0 - this.getHeight());
		setBounds(400, 200, 600, 600);
		setResizable(false);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		///////////////////////////////////////////

		makeCenterPanel();
		btnStartStop = makeButton("Start");
		btnReset = makeButton("Reset");
		btnExit = makeButton("Exit");		
		timeLabel = makeLabel("��� �ð� : ",JLabel.CENTER);
		timeDisplayLabel = makeLabel("",JLabel.CENTER);		
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(1, 5));
		northPanel.add(timeLabel);

		northPanel.add(timeDisplayLabel);
		northPanel.add(btnStartStop);
		northPanel.add(btnReset);
		northPanel.add(btnExit);

		add(centerPanel, BorderLayout.CENTER);
		add(northPanel, BorderLayout.NORTH);
		setVisible(true);
	}

	private JButton makeButton(String caption) {
		JButton btn = new JButton(caption);
		btn.setFont(new Font("", Font.BOLD, 15));
		btn.addActionListener(this);
		return btn;
	}
	
	private JLabel makeLabel(String caption,int position){
		JLabel label = new JLabel(caption,position);
		label.setFont(new Font("",Font.BOLD,15));
		return label;
	}

	private void makeCenterPanel() {
		flagCount = NUM_OF_MINE;
		makeMine();
		centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(SIZE, SIZE));
		mineBtn = new MineButton[SIZE][SIZE];
		for (int i = 0; i < mineBtn.length; i++) {
			for (int j = 0; j < mineBtn[i].length; j++) {
				mineBtn[i][j] = new MineButton(board[i][j], i, j);
				mineBtn[i][j].addMouseListener(this);
				centerPanel.add(mineBtn[i][j]);
			}
		}
	}		
	@Override
	public void actionPerformed(ActionEvent event) {
		String comm = event.getActionCommand();		
		if(comm.equals("Start")){
			btnStartStop.setText("Pause");
			isGamming = true;
			timeThread = new TimeThread(timeDisplayLabel);
			timeThread.start();			
		}else if(comm.equals("Pause")){
			isGamming = false;
			btnStartStop.setText("Start");
			timeThread.setPause();			
		}else if(comm.equals("Reset")){
			setVisible(false);
			remove(centerPanel);
			makeCenterPanel();
			add(centerPanel, BorderLayout.CENTER);
			btnStartStop.setText("Start");
			timeHistory = 1;
			timeDisplayLabel.setText("0");
			repaint();
			setVisible(true);			
		}else if(comm.equals("Exit")){
			System.exit(0);
		}	
	}

	// �׽�Ʈ �ڵ�(������ ����ϱ�)
	public void showBoard() {
		System.out.println("-------------------------------------");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j] + "  ");
			}
			System.out.println();
		}
		System.out.println("-------------------------------------");
	}
	
	
	/*
	 * ���� ���� 
	 */
	private void makeMine() {
		board = new int[SIZE][SIZE]; // 2���� �迭�� ���� ���� ���
		int limit = NUM_OF_MINE;
		while (limit > 0) {
			int row = (int) (Math.random() * board.length);
			int col = (int) (Math.random() * board.length);
			// int isMine = (int) (Math.random() * (SIZE * SIZE / NUM_OF_MINE));
			if (board[row][col] != 9) {
				board[row][col] = 9;
				increaseNumber(row, col);
				limit--;
			}
		}
		showBoard();
	}
	// ���� �ֺ� ���� ����
	private void increaseNumber(int y, int x) {
		for (int dir = 0; dir < DX.length; dir++) {
			int row = y + DY[dir], col = x + DX[dir];
			if (isRange(row, col)) { // �ε��� �Ѿ�� ������
				if (board[row][col] != 9)
					board[row][col]++;
			}
		}
	}	
	// �迭�� �ε����ȿ� �ִ��� Ȯ��
		private boolean isRange(int y, int x) {
			if (x < 0 || x >= board.length || y < 0 || y >= board.length)
				return false;
			return true;
		}

	/*
	 * ���� �ڵ鸵	
	 */		
	// MouseEvent Handler
	public void mouseReleased(MouseEvent event) {
		// //���ʹ�ư �ڵ鸵 �غ���
		// select[idx] = event.getButton();
		// idx = (idx+1)%2;
		if (isGamming) {
			MineButton selectedBtn = (MineButton) event.getSource();
			int x = selectedBtn.getCol();
			int y = selectedBtn.getRow();
			int select = event.getButton();

			if (selectedBtn.isClicked()) { //��ư�� Ŭ���Ǿ��� �����̸� button2�� �ڵ鸵
				if (select == MouseEvent.BUTTON2) 
					confirmAroundHandler(y, x);
				return;
			}  			
			
			if (select == MouseEvent.BUTTON1) { // ���� ��ư
				if (board[y][x] == 9) { // ��ź�̸�
					selectedBtn.showMineImage(); // ���� �̹����� �ٲٱ�
					gameOver(); //��������
				} else { //�����̸�
					if (board[y][x] == 0) //0�̸�
						checkAroundWithZero(y, x);
					else //1~8�̸�
						selectedBtn.showNumberImage();
				}
			} else if (select == MouseEvent.BUTTON3) { // ������ ��ư
				//��ź�ε� üũ�ϸ� -1 // ��ź�ε� üũ Ǯ��  +1
				//�����ε� üũ�ϸ� +1 // �����ε� üũ Ǯ�� -1
				flagCount += (selectedBtn.changeFlagImage(board[y][x]==9));
				//=>��Ȯ�� ���ڿ� ����� üũ�ؾ߸� flagCount == 0�̵�
			}
			
			//���� �����̸�
			if (flagCount == 0) {
				timeThread.setPause();
				isGamming = false;
				JOptionPane.showMessageDialog(this, "Clear Stage!!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	// 0�ֺ� üũ ����
	private void checkAroundWithZero(int y, int x) {
		//�������
		if (board[y][x] == 9) //��ź�̸� ����
			return;
		if (mineBtn[y][x].isClicked() || mineBtn[y][x].isChecked()) //Ŭ����ų� ��߷� üũ������ ����
			return;
		
		//0�� ��ư�� �̹����� �ٲ���
		mineBtn[y][x].showNumberImage();
		//0�� ��ư  �ֺ� �˻�
		for (int k = 0; k < DX.length; k++) {
			int row = y + DY[k], col = x + DX[k];
			if (isRange(row, col)) { // �ε��� �Ѿ�� ������
				if (board[row][col] != 9) { //���ڰ� �ƴϸ�
					if (board[row][col] == 0) //0�̸� ���
						checkAroundWithZero(row, col);
					else //���ڸ� ���� ������
						mineBtn[row][col].showNumberImage();
				}
			}
		}
	}
	
	//2) ���� Ŭ���� Ȯ�����ִ� �ڵ鷯
	private void confirmAroundHandler(int row, int col) {
		HashMap<String, List<Coordinate>> hMap = getAroundMineState(row, col);

		List<Coordinate> nMineList = hMap.get("notCheckedMine");
		List<Coordinate> cNumberList = hMap.get("checkedNumber");
		List<Coordinate> nNumberList = hMap.get("notCheckedNumber");
		
		if (nMineList == null) { // �ֺ��� üũ���� ���� ���ڰ� ������			
			if (nNumberList != null) {//�ֺ� ���ڵ� �̹��� �ٲ��ֱ�
				for (int i = 0; i < nNumberList.size(); i++) {
					Coordinate coord = nNumberList.get(i);					
					mineBtn[coord.y][coord.x].showNumberImage();
				}
			} 
		} else { // �ֺ��� üũ���� ���� ���ڰ� ����
			if (cNumberList != null) { // ���ڿ� ��߷� üũ�� ��� ���� ����
				for (int i = 0; i < nMineList.size(); i++) {
					Coordinate coord = nMineList.get(i);					
					mineBtn[coord.y][coord.x].showMineImage();
				}
				for(int i=0;i<cNumberList.size();i++){
					Coordinate coord = cNumberList.get(i);					
					mineBtn[coord.y][coord.x].showWrongCheck();
				}
				gameOver();
				return;
			}			
			//�ֺ� ���ڹ�ư�� doClick()ȿ�� �ֱ�
			for (int i = 0; i < nMineList.size(); i++) {
				Coordinate coord = nMineList.get(i);				
				mineBtn[coord.y][coord.x].doClick();
			}
			if (nNumberList != null) {
				for (int i = 0; i < nNumberList.size(); i++) {
					Coordinate coord = nNumberList.get(i);					
					mineBtn[coord.y][coord.x].doClick();
				}
			}
		}
	}
	//board�� y,x�� ��� Ŭ����
	class Coordinate {
		int x, y;
		public Coordinate(int y, int x) {
			this.y = y;
			this.x = x;
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
	}	
	//���� ��ư Ŭ�� ��  �ֺ��� ���¸� HashMap�� ����
	private HashMap<String, List<Coordinate>> getAroundMineState(int row, int col) {
		HashMap<String, List<Coordinate>> hMap = new HashMap<>();
		List<Coordinate> notCheckedMineList = new ArrayList<>(8); // üũ�ȵ� ��ź
		List<Coordinate> checkedNumberList = new ArrayList<>(8); // üũ�� ����
		List<Coordinate> notCheckedNumberList = new ArrayList<>(8); // üũ �ȵ� ����
		
		for (int dir = 0; dir < DX.length; dir++) {
			int nextRow = row + DY[dir], nextCol = col + DX[dir];
			if (isRange(nextRow, nextCol)) { // �ε��� �Ѿ�� ������
				if (board[nextRow][nextCol] == 9) {
					if (!mineBtn[nextRow][nextCol].isChecked())					
						notCheckedMineList.add(new Coordinate(nextRow, nextCol));
				} else {
					if (!mineBtn[nextRow][nextCol].isClicked()) {
						if (mineBtn[nextRow][nextCol].isChecked())
							checkedNumberList.add(new Coordinate(nextRow, nextCol));
						else
							notCheckedNumberList.add(new Coordinate(nextRow, nextCol));
					}
				}
			}
		}
		if (!notCheckedMineList.isEmpty())
			hMap.put("notCheckedMine", notCheckedMineList);
		if (!checkedNumberList.isEmpty())
			hMap.put("checkedNumber", checkedNumberList);
		if (!notCheckedNumberList.isEmpty())
			hMap.put("notCheckedNumber", notCheckedNumberList);
		return hMap;
	}
	
	private void gameOver() {
		timeThread.setPause(); // �ð� ����
		isGamming = false;
		btnStartStop.setText("Game Over");
		JOptionPane.showMessageDialog(this, "Game Over!!", "END", JOptionPane.WARNING_MESSAGE);
	}

	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	// TimeThread
	class TimeThread extends Thread {
		private JLabel timeLabel;
		private boolean isGamming = true;

		public TimeThread(JLabel timeLabel) {
			this.timeLabel = timeLabel;
		}

		public void setPause() {
			isGamming = false;
		}

		public void run() {
			while (isGamming) {
				try {
					timeLabel.setText(String.valueOf(timeHistory));
					timeHistory++;
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	// Main method
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainFrame();
			}
		});
	}	
}
