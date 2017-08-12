package request;

import java.io.Serializable;
import java.util.Hashtable;

public class Request implements Serializable { 
	private static final long serialVersionUID = 1L;
	private String type; //��û �� ���� type�� �����ִ� ��� �ʵ�(e.g ���� : "join")
	private Hashtable<String,Object> parameterTable; //������ Ŭ���̾�Ʈ�� ����ϴµ� �ʿ��� �ν��Ͻ���(MemberVO,FeedPage ���)�� ��� �ؽ����̺�	
	
	public Request(String type) {
		this.type = type;		
		parameterTable = new Hashtable<>();		
	}	
	//setter
	public void setParameter(String key,String value) {				
		setAttribute(key,value);
	}	
	public void setAttribute(String key,Object value) {		
		parameterTable.put(key, value);
	}	
	//getter
	public String getType() {
		return type;
	}
	public String getParameter(String key) {		
		Object obj = parameterTable.get(key); //�ϴ� Object�� 		
		//1)
		if(obj instanceof String) //String���� Ÿ�� ��ȯ �����ϸ�
			return (String)obj; //String���� ����ȯ �ؼ� ��ȯ
		else
			return null; //�ƴϸ� null ��ȯ
	}	
	public Object getAttribute(String key) {		
		return parameterTable.get(key);
	}	
}
