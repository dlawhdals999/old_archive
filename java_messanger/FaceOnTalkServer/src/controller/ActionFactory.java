package controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import controller.feed.ModifyFeedController;
import controller.feed.ReadFeedController;
import controller.feed.RemoveFeedController;
import controller.feed.WriteFeedController;
import controller.member.ConfirmIdController;
import controller.member.FindUserInfoController;
import controller.member.JoinController;
import controller.member.ModifyInfoController;
import controller.member.SearchFriendController;
import controller.schedule.ReadScheduleController;
import controller.schedule.RemoveScheduleController;
import controller.schedule.WriteScheduleController;

public class ActionFactory {
	// singleton
	private static ActionFactory instance = null;	
	public static ActionFactory getInstance() throws RuntimeException {
		if (instance == null)
			instance = new ActionFactory();
		return instance;
	}	
	
	//variables
	Map<String,WebCommandController> commandsMap;
	private ActionFactory(){
		commandsMap = new HashMap<String,WebCommandController>(16,0.95f);
		initProperties();
	}	
	
	private void initProperties() throws RuntimeException {
		Properties prop = new Properties();
		try(FileReader fis = new FileReader(new File("commands.properties"))) {
			prop.load(fis);
		} catch(IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		Iterator keyIter = prop.keySet().iterator();
		while(keyIter.hasNext()) {
			String command = (String) keyIter.next();			
			String handlerClassName = prop.getProperty(command);
			System.out.println("�ʱ�ȭ command : "+command+"\t,className : "+handlerClassName);
			try {
				Class<?> handlerClass = Class.forName(handlerClassName);
				WebCommandController handlerInst = (WebCommandController) handlerClass.newInstance();
				if(handlerInst == null) {
					System.out.println(command+"Ŭ���� ��");
				}
				commandsMap.put(command, handlerInst);
			} catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}		
		System.out.println("�ؽ��� ������ : "+commandsMap.size());
	}

	public WebCommandController getAction(String type) {
		
		WebCommandController controller = null;		
		controller = commandsMap.get(type);
		if(controller ==null)
			System.out.println("controller null");
		return controller;			

//		////////////////////////////////////////////
//		// ȸ�� �� ģ�� ����
//		///////////////////////////////////////////
//		if (type.equals("join")) { // ȸ������
//			// System.out.println("���Կ�û�ؼ�");
//			controller =  new JoinController();
//		} else if (type.equals("modify")) { // ȸ�� ���� ����
//			// System.out.println("�������� ��û �ؼ�");
//			controller =  new ModifyInfoController();
//		} else if (type.equals("searchfriend")) { // ģ��ã��
//			// System.out.println("ģ��ã���ؼ�");
//			controller =  new SearchFriendController();
//		} else if (type.equals("confirmid")) {
//			// System.out.println("���̵� �ߺ� Ȯ�� �ؼ�");
//			controller =  new ConfirmIdController();
//		} else if (type.equals("finduser")) {
//			controller =  new FindUserInfoController();
//		}
//
//		////////////////////////////////////////////
//		// �ǵ� ����
//		///////////////////////////////////////////
//		else if (type.equals("writefeed")) {// ����
//			System.out.println("�Խñ� ���� �ؼ�");
//			controller =  new WriteFeedController();
//		} else if (type.equals("readfeed")) {// �б�
//			System.out.println("�Խñ� �б� �ؼ�");
//			controller =  new ReadFeedController();
//		} else if (type.equals("modifyfeed")) {// ����
//			System.out.println("�Խñ� ���� �ؼ�");
//			controller =  new ModifyFeedController();
//		} else if (type.equals("removefeed")) {// ����
//			System.out.println("�Խñ� ���� �ؼ�");
//			controller =  new RemoveFeedController();
//		}
//
//		////////////////////////////////////////////
//		// ������ ����
//		///////////////////////////////////////////
//		else if (type.equals("writeschedule")) { // ����
//			// System.out.println("������ ���� �ؼ� �Ϸ�");
//			controller =  new WriteScheduleController();
//		} else if (type.equals("readschedule")) { // �б�
//			System.out.println("������ �б� �ؼ� �Ϸ�");
//			controller =  new ReadScheduleController();
//		} else if (type.equals("modifyschedule")) { // ����
//			// System.out.println("������ ���� �ؼ� �Ϸ�");
//			// controller =  new ModifyScheduleController();
//		} else if (type.equals("removeschedule")) { // ����
//			//System.out.println("������ ����� �ؼ� �Ϸ�");
//			controller =  new RemoveScheduleController();
//		}
//		return controller;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("ActionFactory�Ҹ�");
	}
	
	
	
}
