
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class MyFrame08 extends Frame {
	// ��� �⺻
	private MenuBar menuBar = new MenuBar();
	private Menu fileMenu = new Menu("����");
	private Menu helpMenu = new Menu("����");

	// ��� ����
	private MenuItem openMenuItem = new MenuItem("����");
	private MenuItem saveMenuItem = new MenuItem("����");
	private MenuItem diffNameSaveMenuItem = new MenuItem("�ٸ� �̸����� ����");
	private MenuItem exitMenuItem = new MenuItem("����");

	private MenuItem showHelpMenuItem = new MenuItem("��������");

	private Dialog helpDialog;
	private FileDialog fdlOpen;
	private FileDialog fdlSave;

	private TextArea txtDisplay = new TextArea();
	private Label labelVersion = new Label("���� 1.0 �Դϴ�.", Label.CENTER);
	private FileHandler fileHandler;
	
	
	private String fileName;
	private String path;

	public MyFrame08(String title) {
		super(title);
		this.init();
	}

	private void init() {
		///////////////////////////////////////////
		// �ʱ� ����
		this.setSize(600, 800);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int) (screen.getWidth() / 2.0 - this.getWidth() / 2.0);
		int ypos = (int) (screen.getHeight() / 2.0 - this.getHeight() / 2.0);
		this.setLocation(xpos, ypos);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		///////////////////////////////////////////
		this.setLayout(new BorderLayout());
		this.add(txtDisplay, BorderLayout.CENTER);
		txtDisplay.setFont(new Font("", Font.BOLD, 20));
		// UI ���
		// �⺻ ���
		this.setMenuBar(menuBar);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		// ����
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(diffNameSaveMenuItem);
		fileMenu.add(exitMenuItem);

		helpMenu.add(showHelpMenuItem);

		// �������� �̺�Ʈ �ڵ鸵
		showHelpMenuItem.addActionListener(event -> {
			showVersion();
		});

		// ���� ���� �̺�Ʈ �ڵ鸵
		openMenuItem.addActionListener(event -> fileOpenHandler());
		// ���� ���� �̺�Ʈ �ڵ鸵
		saveMenuItem.addActionListener(event -> fileSaveHandler());
		// �ٸ� �̸� ���� ���� �̺�Ʈ �ڵ鸵
		diffNameSaveMenuItem.addActionListener(event -> newNameSaveHandler() );
		// ���� �̺�Ʈ �ڵ鸵
		exitMenuItem.addActionListener(event -> {
			System.exit(0);
		});
	}
	
	//���� ���� �̺�Ʈ �ڵ鸵
	private void fileOpenHandler(){
		fdlOpen = new FileDialog(this, "����", FileDialog.LOAD);
		fdlOpen.setVisible(true);
		fileName = fdlOpen.getFile();
		path = fdlOpen.getDirectory();
		if (fileName != null) {
			if (fileHandler == null)
				fileHandler = new FileHandler(fileName, path, txtDisplay);
			else
				fileHandler.setFilePath(fileName, path);
			fileHandler.readFile();
		}
	}
	
	//���� �̸����� ���� �̺�Ʈ �ڵ鸵
	private void fileSaveHandler(){
		if(fileName == null){
			newNameSaveHandler();				
		}else{
			if (fileHandler == null)
				fileHandler = new FileHandler(fileName, path, txtDisplay);
			else
				fileHandler.setFilePath(fileName, path);
			fileHandler.saveFile();
		}
	}
	
	//���ο� �̸����� ����
	private void newNameSaveHandler(){
		fdlSave = new FileDialog(this, "����", FileDialog.SAVE);
		fdlSave.setVisible(true);
		String file = fdlSave.getFile();
		String path = fdlSave.getDirectory();
		if (file != null) {
			if (fileHandler == null)
				fileHandler = new FileHandler(file, path, txtDisplay);
			else
				fileHandler.setFilePath(file, path);

			fileHandler.saveFile();
		}
	}

	// ���� ������
	private void showVersion() {
		helpDialog = new Dialog(this, "Show Version", true);
		helpDialog.setBounds(120, 120, 200, 200);
		helpDialog.setLayout(new BorderLayout());

		labelVersion.setFont(new Font("", Font.BOLD, 15));

		Button btnExit = new Button("����");

		btnExit.addActionListener(event -> {
			helpDialog.setVisible(false);
			helpDialog = null;
		});

		Panel bottomPanel = new Panel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		bottomPanel.add(btnExit);

		helpDialog.add(labelVersion, BorderLayout.CENTER);
		helpDialog.add(bottomPanel, BorderLayout.SOUTH);

		helpDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		helpDialog.setVisible(true);
	}
	
	public void start() {
		this.setVisible(true);
	}
	
	
	//////////////////////////////////////
	//���� ���� Ŭ����	
	class FileHandler {
		private String fileName;
		private String path;
		private TextArea txtDisplay;

		public FileHandler(String fileName, String path, TextArea txtDisplay) {
			this.fileName = fileName;
			this.path = path;
			this.txtDisplay = txtDisplay;
		}

		public void readFile() {
			txtDisplay.setText("");
			BufferedReader buffIn = null;
			try {
				buffIn = new BufferedReader(new FileReader(path + fileName));
				while (true) {
					String data = buffIn.readLine();
					if (data == null)
						break;
					displayText(data);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {buffIn.close();} catch (IOException e2) {}
			}
		}

		public void saveFile() {
			String saveData = txtDisplay.getText();
			BufferedWriter buffOut = null;
			try {
				buffOut = new BufferedWriter(new FileWriter(path + fileName));
				buffOut.write(saveData);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					buffOut.close();
				} catch (IOException e2) {
				}
			}
		}

		public void setFilePath(String fileName, String path) {
			this.fileName = fileName;
			this.path = path;
		}

		private void displayText(String data) {
			txtDisplay.append(data + "\n");
		}
	}
}

public class MenuSelf {
	public static void main(String[] args) {
		MyFrame08 frm = new MyFrame08("Test");
		frm.start();

	}
}
