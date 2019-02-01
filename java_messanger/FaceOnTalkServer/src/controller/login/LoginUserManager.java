package controller.login;

import java.util.Hashtable;
import java.util.Iterator;

import chat.ClientHandler;

/*
 * �α��� �������� �����ϴ� Ŭ����
 */

public class LoginUserManager {
	//singleton
	private static LoginUserManager inst = null;
	public static LoginUserManager getInstance(){
		if(inst == null)
			inst = new LoginUserManager();
		return inst;
	}
	
	private Hashtable<Integer,ClientHandler> connections; //�α��� �������� ��� hashtable	
	private LoginUserManager(){
		connections = new Hashtable<>(100);		
	}	
	//�α��� ��
	public ClientHandler addUser(Integer id,ClientHandler client){
		return connections.put(id, client);
	}	
	//��ġ
	public ClientHandler getUser(Integer id){
		return connections.get(id);
	}	
	public ClientHandler getUser(String id) {
		return connections.get(Integer.parseInt(id));
	}
	//����
	public boolean removeUser(Integer id){
		return connections.remove(id)!=null;
	}
	//��� �������� ����
	public void removeAllUser(){		
		Iterator<Integer> itr = connections.keySet().iterator();
		while(itr.hasNext()){
			Integer id = itr.next();
			ClientHandler c = connections.remove(id);
			c.closeSocket();
		}		
	}
	//�α��� ���� ����
	public boolean isLogin(Integer id){
		return connections.get(id)!=null;
	}
	public boolean isLogin(String id) {
		return connections.get(Integer.parseInt(id))!=null;
	}
}











