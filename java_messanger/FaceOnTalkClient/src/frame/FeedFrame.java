package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import connector.WebConnector;
import dto.FeedPage;
import dto.FeedVO;
import dto.MemberVO;
import noticeframe.MessageFrame;
import request.Request;

public class FeedFrame extends JFrame implements ActionListener {
	private MemberVO admin;
	private FeedPage feedPage;
	private int startPage;
	private int endPage;
	private int totalPage;
	private String type;

	public FeedFrame(MemberVO admin) {
		super("Newsfeed");
		this.admin = admin;
		init();
		action();
		setVisible(true);
	}
	
	///////////////
	//��û�� �������� �´�
	//feedPage�� ���� �ʱ�ȭ
	///////////////
	public void fill() {
		friendsPheed_p.removeAll();
		friendsPheed_p.add(writePanel);
		List<FeedVO> feedList = feedPage.getFeedList();
		for(int i=0;i<feedList.size();i++) {
			feedPanel[i] = new Feed(feedList.get(i));			
			friendsPheed_p.add(feedPanel[i]);
		}	
		fillBtnValue();
		invalidate();
		repaint();
	}
	
	////////////////////
	//������ ��ư �̺�Ʈ ó��
	////////////////////
	public void actionPerformed(ActionEvent e) {
		JButton selectedBtn = (JButton)e.getSource();
		int requestPage = 0;
		if(selectedBtn == nextBtn) { //�ؽ�Ʈ ��ư
			requestPage = endPage+1;
		} else if(selectedBtn == prevBtn) {
			if(startPage==1)
				return;
			requestPage = startPage-1;
		} else {
			if(selectedBtn.getText().isEmpty())
				return;
			requestPage = Integer.parseInt(selectedBtn.getText());
		}
		readFeedHandler(requestPage, type);
		friends_sp.getHorizontalScrollBar().setValue(0);
	}
	
	////////////
	//��ư ������ ����
	////////////
	private void fillBtnValue() {
		if(feedPage.getTotal() == 0) {
			hideAllPageButton();
			return;
		}
		
		int btnIdx = 0;	
		for(int i=startPage;i<=endPage;i++,btnIdx++) {
			pageBtn[btnIdx].setVisible(true);
			pageBtn[btnIdx].setText(String.valueOf(i));			
		}		
		for(int i=btnIdx;i<pageBtn.length;i++) {			
			pageBtn[i].setVisible(false);			
		}			
		
		//next��ư
		if(endPage < totalPage) {
			nextBtn.setVisible(true);
		}else {
			nextBtn.setVisible(false);			
		}		
		//prev ��ư
		if(startPage>5) {
			prevBtn.setVisible(true);			
		} else {
			prevBtn.setVisible(false);
		}
	}

	private void hideAllPageButton() {
		for(int i=0;i<pageBtn.length;i++) {
			pageBtn[i].setVisible(false);
		}
		nextBtn.setVisible(false);
		prevBtn.setVisible(false);
	}

	/////////////////////
	// GUI
	Feed[] feedPanel = new Feed[10];
	WriteFeed writePanel = new WriteFeed();
	
	private JTabbedPane feedTabPane = new JTabbedPane();	
	private JPanel friends_p = new JPanel();	
	private JPanel friendsPheed_p = new JPanel();
	
	private JPanel pageBtnPanel = new JPanel();
	private JScrollPane friends_sp = new JScrollPane(friendsPheed_p);	

	private JButton[] pageBtn = new JButton[5];
	private JButton nextBtn = new JButton("next");
	private JButton prevBtn = new JButton("prev");

	public void init() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setSize((int)(screen.getWidth()/2),(int)(screen.getHeight())-40);		
		super.setLocation(0,0);
		super.setResizable(false);
		Container con = this.getContentPane();
		con.add(feedTabPane);
		
		feedTabPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(feedTabPane.getSelectedIndex() == 0) { //ģ��+���� �ǵ�				
					readFeedHandler(0,"all");
					type = "all";
				} else {					
					readFeedHandler(0,"admin");
					type = "admin";
				}				
			}			
		});
		feedTabPane.add("newsfeed", friends_p);
		feedTabPane.add("userfeed", null);
		friends_p.setLayout(new BorderLayout());
		friends_p.add("Center", friends_sp);
		friends_p.add("South", pageBtnPanel);
		friendsPheed_p.setLayout(new GridLayout(0, 1, 0, 15));
		
		con.setBackground(new Color(255, 255, 255));
		feedTabPane.setBackground(new Color(255, 255, 255));
		friendsPheed_p.setBackground(new Color(255, 255, 255));		
		pageBtnPanel.setBackground(new Color(255, 255, 255));
				
		friendsPheed_p.add(writePanel);
		pageBtnPanel.setLayout(new FlowLayout());
		
		///////// ������ ��ư GUI		
		pageBtnPanel.add(prevBtn);
		for(int i = 0;  i < pageBtn.length; i++){
			pageBtn[i] = new JButton();
			pageBtnPanel.add(pageBtn[i]);
			pageBtn[i].setBorder(new MatteBorder(2,2,2,2,Color.BLACK));
			pageBtn[i].setContentAreaFilled(false);
			pageBtn[i].setFocusPainted(false);
			pageBtn[i].setPreferredSize(new Dimension(40, 30));
		}
		pageBtnPanel.add(nextBtn);				
		prevBtn.setBorder(new MatteBorder(2,2,2,2,Color.BLACK));
		prevBtn.setContentAreaFilled(false);
		prevBtn.setFocusPainted(false);
		prevBtn.setPreferredSize(new Dimension(40, 30));
		nextBtn.setBorder(new MatteBorder(2,2,2,2,Color.BLACK));
		nextBtn.setContentAreaFilled(false);
		nextBtn.setFocusPainted(false);
		nextBtn.setPreferredSize(new Dimension(40, 30));	
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}

	public void action() {
		for(int i = 0; i < pageBtn.length; i++){			
			pageBtn[i].addActionListener(this);
		}		
		prevBtn.addActionListener(this);
		nextBtn.addActionListener(this);
	}
	
	private void readFeedHandler(int pageNo,String type) {		
		Request request = new Request("readfeed");
		String pageNoVal = null;
		if(pageNo!=0) {
			pageNoVal = String.valueOf(pageNo);
			request.setParameter("pageNo",pageNoVal);
		}
		request.setParameter("readType",type);
		request.setParameter("id",String.valueOf(admin.getId()));
		Thread thread = new Thread() {
			@Override
			public void run() {
				Request response = WebConnector.connect(request);
				if (response.getType().equals("readfeed")) {
					Map<String, Boolean> errors = (Map<String, Boolean>) response.getAttribute("errors");					
					if (errors.isEmpty()) {
						feedPage = (FeedPage) response.getAttribute("feedPage");
						startPage = feedPage.getStartPage();
						System.out.println(startPage);
						endPage = feedPage.getEndPage();
						System.out.println(endPage);
						totalPage = feedPage.getTotalPages();
						fill();
					} else {
						if(errors.get("hasNoFeed")!=null) {
							prevBtn.setVisible(false);
							nextBtn.setVisible(false);
							for(int i=0;i<pageBtn.length;i++) {
								pageBtn[i].setVisible(false);
							}
						}
						else if (errors.get("SQLException")!=null) {
							new MessageFrame("�������� ������ ���� �ʽ��ϴ�.\n����� �������ּ���.", false);
						}
					}					
				}
			}
		};
		thread.start();		
	}
	
	private void writeHandler(String content) {
		Request request = new Request("writefeed");
		request.setParameter("id",String.valueOf(admin.getId()));		
		request.setAttribute("name",admin.getName());
		request.setParameter("email",admin.getEmail());
		request.setParameter("content",content);				
		///////////
		//	
		Thread thread = new Thread() {
			@Override
			public void run() {
				Request response = WebConnector.connect(request);
				Map<String,Boolean> errors = (Map<String,Boolean>)response.getAttribute("errors");
				if(errors.isEmpty()) {
					new MessageFrame("���������� �����Ͽ����ϴ�.\n",true);
					writePanel.write_jta.setText("���� ������ �ϰ� ��Ű���?");
					readFeedHandler(0,type);
				} else {
					System.out.println("����");
					if(errors.get("SQLException")!=null) {
						new MessageFrame("�������� ������ ���� �ʽ��ϴ�.",false);
					}
				}
			}
		};
		thread.start();	
	}
		
	// �Խñ� �ۼ� �̳�Ŭ����
	class WriteFeed extends JPanel implements ActionListener, FocusListener {
		public WriteFeed() {
			this.init();
			this.action();
		}
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == ok_bt) {
				String content = write_jta.getText();
				if(content.equals("���� ������ �ϰ� ��Ű���?"))
					return;
				writeHandler(content);
			} else if (e.getSource() == cancel_bt) {
				write_jta.setText("");
			}
		}
		public void focusGained(FocusEvent e) {
			if (e.getSource() == write_jta) {
				if (write_jta.getText().equals("���� ������ �ϰ� ��Ű���?")) {
					write_jta.setText("");
					write_jta.setForeground(Color.BLACK);
				}
			}
		}
		public void focusLost(FocusEvent e) {
			if (write_jta.getText().isEmpty()) {
				write_jta.setText("���� ������ �ϰ� ��Ű���?");
				write_jta.setForeground(Color.GRAY);
			}
		}	
		// �Խñ� �Է� GUI
		private JPanel top_p = new JPanel();
		private JPanel write_p = new JPanel();
		private JPanel bottom_p = new JPanel();
		private JButton writerBtn = new JButton(new ImageIcon("image/feedicon.png"));
		private JLabel writer_lb = new JLabel();
		private JLabel writedate_lb = new JLabel();
		private JTextArea write_jta = new JTextArea("���� ������ �ϰ� ��Ű���?");
		private JScrollPane write_sp = new JScrollPane(write_jta);
		private JButton ok_bt = new JButton("Ȯ��");
		private JButton cancel_bt = new JButton("���");

		public void init() {
			////////// GUI ����
			this.setLayout(new BorderLayout());
			this.setBorder(new MatteBorder(3, 3, 3, 3, Color.BLACK));
			this.add("North", top_p);
			this.add("Center", write_sp);
			this.add("South", bottom_p);
			top_p.setLayout(new BorderLayout());
			top_p.add("West", writerBtn);
			top_p.add("Center", write_p);
			write_p.setLayout(new GridLayout(2, 1));
			write_p.add(writer_lb);
			write_p.add(writedate_lb);
			bottom_p.setLayout(new FlowLayout());
			bottom_p.add(ok_bt);
			bottom_p.add(cancel_bt);
			top_p.setBackground(new Color(255, 255, 255));
			write_p.setBackground(new Color(255, 255, 255));
			bottom_p.setBackground(new Color(255, 255, 255));
			writerBtn.setBorderPainted(false);
			writerBtn.setContentAreaFilled(false);
			writerBtn.setFocusPainted(false);
			ok_bt.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
			ok_bt.setContentAreaFilled(false);
			ok_bt.setFocusPainted(false);
			ok_bt.setPreferredSize(new Dimension(40, 30));
			cancel_bt.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
			cancel_bt.setContentAreaFilled(false);
			cancel_bt.setFocusPainted(false);
			cancel_bt.setPreferredSize(new Dimension(40, 30));
			cancel_bt.setToolTipText("�Է��� ���� ����");
			write_jta.setForeground(Color.GRAY);

			write_sp.setPreferredSize(new Dimension(0, 200));
			write_jta.setLineWrap(true);
		}

		public void action() {
			write_jta.addFocusListener(this);
			ok_bt.addActionListener(this);
			cancel_bt.addActionListener(this);
		}
	}
	
	//////////////////////////////
	// DB���� �޾ƿ� �ǵ� �̳�Ŭ����
	class Feed extends JPanel {
		FeedVO feed;		
		//////// ������
		public Feed(FeedVO feed) {
			this.feed = feed;
			init();			
			fillFeed();
		}
		void setFeed(FeedVO feed) {
			this.feed = feed;
		}
		void likeHandler() {
			like_co++;
			like_su.setText(String.valueOf(like_co));
		}
		
		void modifyHandler() {			
			if(modifyBtn.getText().equals("�����ϱ�")) {				
				contentDisplay.setEditable(true);
				modifyBtn.setText("�����ϱ�");
			} else if(modifyBtn.getText().equals("�����ϱ�")) {
				Request request = new Request("modifyfeed");
				request.setParameter("feed_no", String.valueOf(feed.getFeedNumber()));
				request.setParameter("id",String.valueOf(admin.getId()));
				request.setParameter("name",admin.getName());
				request.setParameter("email",admin.getEmail());
				request.setParameter("modcontent",contentDisplay.getText());		
				Thread thread = new Thread() {
					@Override
					public void run() {
						Request response = WebConnector.connect(request);
						Map<String,Boolean> errors =(Map<String,Boolean>) response.getAttribute("errors");				
						if(errors.isEmpty()) {
							new MessageFrame("���������� �����Ͽ����ϴ�.",true);
							readFeedHandler(0,type);
						} else {
							//���� ���� ó�� ���� ����ϱ�
							if(errors.get("cantfindfeed")!=null) {} 
							else if(errors.get("permissiondenied")!=null) {}
							
							//
							else if(errors.get("failed")!=null || errors.get("SQLException") !=null) {
								new MessageFrame("�������� ������ �����ʽ��ϴ�.\n����� �������ּ���.",false);
							} 
						}
						
					}
				};
				thread.start();
			}	
		}
		
		void removeHandler() {
			Request request = new Request("removefeed");
			request.setParameter("feed_no",String.valueOf(feed.getFeedNumber()));
			request.setParameter("id",String.valueOf(admin.getId()));
			
			Thread thread = new Thread() {
				@Override
				public void run() {
					Request response = WebConnector.connect(request);
					Map<String,Boolean> errors =(Map<String,Boolean>) response.getAttribute("errors");				
					if(errors.isEmpty()) {
						new MessageFrame("���������� �����Ͽ����ϴ�.\n",true);
						readFeedHandler(0,type);
					} else {
						if(errors.get("cantfindfeed")!=null) {
							//���� ���� ó�� ���� ����ϱ�
						} else if(errors.get("permissiondenied") == Boolean.TRUE) {
							//���� ���� ó�� ���� ����ϱ�
						} else if(errors.get("failed")!=null|| errors.get("SQLException")!=null) {
							new MessageFrame("�������� ������ �����ʽ��ϴ�.\n����� �������ּ���.",false);
						} 
					}
					
				}
			};
			thread.start();			
		}
	
		
		// �ǵ� GUI
		private JPanel top_p = new JPanel();
		private JPanel write_p = new JPanel();
		private JButton imageBtn = new JButton(new ImageIcon("image/feedicon.png"));
		private JLabel writerLabel = new JLabel(); //�ۼ��� �̸�
		private JLabel dateLabel = new JLabel(); //�ۼ� ����
		private JTextArea contentDisplay = new JTextArea(); //content
		private JScrollPane content_sp = new JScrollPane(contentDisplay, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		private JPanel bottom_p = new JPanel();
		private JButton likeBtn = new JButton(new ImageIcon("image/like.png"));
		private int like_co = 0;
		private JLabel like_su = new JLabel(String.valueOf(like_co));
		private JButton modifyBtn = new JButton("�����ϱ�");
		private JButton deleteBtn = new JButton("�����ϱ�");
		void init() {
			////////// GUI ����
			this.setLayout(new BorderLayout());
			this.setBorder(new MatteBorder(3, 3, 3, 3, Color.BLACK));
			this.add("North", top_p);
			this.add("Center", content_sp);
			this.add("South", bottom_p);
			top_p.setLayout(new BorderLayout());
			top_p.add("West", imageBtn);
			top_p.add("Center", write_p);
			write_p.setLayout(new GridLayout(2, 1));
			write_p.add(writerLabel);
			write_p.add(dateLabel);
			bottom_p.setLayout(new FlowLayout(FlowLayout.LEFT));
			bottom_p.add(likeBtn);
			bottom_p.add(like_su);
			bottom_p.add(modifyBtn);
			bottom_p.add(deleteBtn);

			imageBtn.setBorderPainted(false);
			imageBtn.setContentAreaFilled(false);
			imageBtn.setFocusPainted(false);
			likeBtn.setBorderPainted(false);
			likeBtn.setContentAreaFilled(false);
			likeBtn.setFocusPainted(false);
			modifyBtn.setBorderPainted(false);
			modifyBtn.setContentAreaFilled(false);
			modifyBtn.setFocusPainted(false);
			deleteBtn.setBorderPainted(false);
			deleteBtn.setContentAreaFilled(false);
			deleteBtn.setFocusPainted(false);
			contentDisplay.setEditable(false);
			top_p.setBackground(new Color(255, 255, 255));
			write_p.setBackground(new Color(255, 255, 255));
			bottom_p.setBackground(new Color(255, 255, 255));

			content_sp.setPreferredSize(new Dimension(0, 200));
			contentDisplay.setLineWrap(true);	
			
			modifyBtn.addActionListener(e -> modifyHandler());
			likeBtn.addActionListener(e -> likeHandler());
			deleteBtn.addActionListener(e -> removeHandler());		
		}
		
		public void fillFeed() {
			writerLabel.setText(feed.getWriter().getName());//�ۼ��� �̸�
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");			
			dateLabel.setText(sdf.format(feed.getModifiedDate()));//�ۼ� ����
			contentDisplay.append(feed.getContent());			
			if(feed.getWriter().getId() == admin.getId()) {
				modifyBtn.setVisible(true);				
				deleteBtn.setVisible(true);				
			} else {
				modifyBtn.setVisible(false);				
				deleteBtn.setVisible(false);				
			}
		}
	}
	//feed inner Ŭ���� ��
	////////////////////////////////////	
}