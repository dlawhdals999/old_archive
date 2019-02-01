package util;

import java.util.Arrays;
import java.util.StringTokenizer;

/*
 * user group parsing class
 * 1,2,10 �̶�� �׷��� ������ 1:2:10 // 2:1:10 �׷��� �����ϱ� ����
 * 1:2:10 ���� �������� ���� �� ������ : �� �־������ν� group_id�� �����ϰ� �������
 */

public class UserGroupParser {	
	private UserGroupParser(){}	
	//"1:2:3" user group -> [1,2,3] �迭��
	public static int[] getUserInteger(String group_id) throws NumberFormatException {
		StringTokenizer token = new StringTokenizer(group_id, ":");
		int[] users = new int[token.countTokens()];
			
		for(int i=0;i<users.length;i++){
			users[i] = Integer.parseInt(token.nextToken());
		}		
		Arrays.sort(users);		
		return users;		
	}	
	
	//[1,2,3] �迭�� -> "1:2:3" string����
	public static String getUserString(int[] users){
		Arrays.sort(users);
		StringBuffer buff = new StringBuffer();
		buff.append(users[0]);
		for(int i=1;i<users.length;i++){
			buff.append(":"+users[i]);
		}
		return buff.toString();	
	}
	
	public static String getUserStringSort(String group_id) {
		return getUserString(getUserInteger(group_id));
	}
	
}
