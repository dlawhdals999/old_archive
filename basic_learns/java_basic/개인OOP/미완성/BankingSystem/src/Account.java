public class Account {
	private int accID;
	private String name;
	private int balance;

	public Account(int accID, String name, int balance) {
		this.accID = accID;
		this.name = name;
		this.balance = balance;
	}

	public void depositMoney(int money) throws MinusException {
		if(money<0) {
			throw new MinusException(money);
		}
		balance += money;
	}

	public int withdrawMoney(int money) 
			throws MinusException, InsuffException {
		if(money<0) {
			throw new MinusException(money);
		} else if(balance<money) {
			throw new InsuffException(money);
		}		
		balance -= money;
		return money;
	}

	public int getAccId() {
		return accID;
	}

	public void showAccInfo() {
		System.out.println("���� ID : " + accID);
		System.out.println("�̸� : " + name);
		System.out.println("�ܾ� : " + balance);
	}
}

class NormalAccount extends Account {
	private int interest;

	NormalAccount(int accID, String name, int balance, int rate) {
		super(accID, name, balance);
		interest = rate;
	}

	public void depositMoney(int money) throws MinusException {
		super.depositMoney(money);
		super.depositMoney(money * interest);
	}
	
	public void showAccInfo() {
		super.showAccInfo();
		System.out.println("������ : "+interest);
	}
}

class CreditAccount extends NormalAccount {
	private int specialRate;
	
	public CreditAccount(int accID,String name, int balance, int rate, int specialRate) {
		super(accID,name,balance,rate);
		this.specialRate=specialRate;
	}
	
	public void depositMoney(int money) throws MinusException {
		super.depositMoney(money);
		(super.super).depositMoney(money*specialRate));
	}
	
	public void showAccInfo() {
		super.showAccInfo();
		System.out.println("Ư�� ���� : "+specialRate);
	}
	
}




















