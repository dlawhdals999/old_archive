import java.util.Scanner;

interface INPUT_SELECT {
	int NORMAL=1,CREDIT=2;
//	int HIGH=7,MIDDLE=5,LOW=3;
}

interface INIT_MENU {
	int MAKE=1,DEPOSIT=2,WITHDRAW=3,SEARCH=4,SHOWALL=5,EXIT=6;
}

public class MenuViewer {
	
	public static Scanner keyboard=new Scanner(System.in);
	
	public static void showMenu() {
		System.out.println("1.���� ����");
		System.out.println("2.���� �Ա�");
		System.out.println("3.���� ���");
		System.out.println("4.���� �˻�");
		System.out.println("5.���� ���� ���");
		System.out.println("6.���α׷� ����");
		System.out.print("���� : ");
	}
	
	public static void makeAccountMenu()  {
		System.out.println("----���� ����----");
		System.out.println("1.�Ϲ� ����  2.�ſ����");
		System.out.print("���� : ");
		
	}
}
