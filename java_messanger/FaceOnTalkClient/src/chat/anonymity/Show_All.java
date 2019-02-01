package chat.anonymity;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/*
 * ��� �����ڸ� �����ִ� Ŭ����.
 * �޴��ٿ��� ȣ��ȴ�.
 */
public class Show_All extends JFrame implements ActionListener{
	static List idlist;
	private JButton confirm;
	private JButton exit;
	public Show_All(){
		JLabel name = new JLabel("��ü ������");
		idlist = new List();

		JPanel south = new JPanel();
		south.setLayout(new FlowLayout());
		confirm = new JButton("Ȯ��");
		confirm.addActionListener(this);
		south.add(confirm);

		this.setLayout(new BorderLayout());
		add(name, "North");
		add(idlist, "Center");	
		add(south, "South");
		setBounds(300, 200, 200, 300);
		setVisible(true);
		
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String id = idlist.getSelectedItem();
		if(e.getSource() == confirm){
			dispose();
		}
	}	// �׼� �̺�Ʈ ����
	public static void main(String[] args) {
		Show_All id = new Show_All();
	}
}
