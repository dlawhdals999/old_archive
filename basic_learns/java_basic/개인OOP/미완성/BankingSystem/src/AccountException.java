public class AccountException extends Exception {	
	AccountException() {
		super("���� ���� ���� �߻�");
	}	
}

class MinusException extends AccountException {
	
	private int inputMoney;
	
	MinusException(int money) {
		super();
		inputMoney=money;
	}
	
	public void showReason() {
		System.out.println(super.getMessage());
		System.out.println(inputMoney+"�� �Ա��� �� �����ϴ�.");		
	}	
}

class InsuffException extends AccountException {
	private int balance;
	
	InsuffException(int balance) {
		super();
		this.balance=balance;
	}
	
	public void showReason() {
		System.out.println(super.getMessage());
		System.out.println("�ܾ� : "+balance+" �� �����մϴ�.");		
	}	
}

class MenuChoiceException extends Exception {
	private int inputNum;
	
	MenuChoiceException(int inputNum) {
		super("�޴� ���� �����Դϴ�.");
		this.inputNum=inputNum;
	}
	
	public void showReason() {
		System.out.println(super.getMessage());
		System.out.println(inputNum+"���� ������ �� �����ϴ�.");
	}
}




