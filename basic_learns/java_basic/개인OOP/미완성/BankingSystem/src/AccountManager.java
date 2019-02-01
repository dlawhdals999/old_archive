public class AccountManager {
	private final int MAX_ACC = 100;
	private Account[] acc;
	private int accNum;
	public static AccountManager inst = null;

	private AccountManager() {
		acc = new Account[MAX_ACC];
		accNum = 0;
	}

	public static AccountManager makeManagerInst() {
		if (inst == null) {
			inst = new AccountManager();
		}
		return inst;
	}

	public int searchID(int id) { // ���̵� ������ �ε���, ������ -1 ��ȯ
		for (int idx = 0; idx < accNum; idx++) {
			if (acc[idx].getAccId() == id) {
				return idx;
			}
		}
		return -1;
	}

	public void makeAccount() throws MenuChoiceException {
		MenuViewer.makeAccountMenu();
		int choice = MenuViewer.keyboard.nextInt();
		if (choice < INPUT_SELECT.NORMAL || choice > INPUT_SELECT.CREDIT) {
			throw new MenuChoiceException(choice);
		}

		switch (choice) {
		case INPUT_SELECT.NORMAL:
			acc[accNum++] = makeNormalAccount();
			break;
		case INPUT_SELECT.CREDIT:
			acc[accNum++] = makeCreditAccount();
			break;
		}
	}

	public NormalAccount makeNormalAccount() throws MinusException {
		System.out.println("--Create of NormalAccount--");
		System.out.print("���� ID : ");
		int id = MenuViewer.keyboard.nextInt();
		int result = searchID(id);
		if (result == -1) {

			System.out.print("�� �� : ");
			String name = MenuViewer.keyboard.nextLine();
			System.out.print("�Աݾ� : ");
			int money = MenuViewer.keyboard.nextInt();

			if (money < 0) { // 0���� ���� �ݾ� �Ա�
				throw new MinusException(money);
			}

			System.out.print("���� : ");
			int rate = MenuViewer.keyboard.nextInt();
			return new NormalAccount(id, name, money, rate);
		}
	else{
		System.out.println("������ ID�� �����մϴ�.");
		return null;
	}

	}

	public void deposit() throws MinusException{
		
	}

	public void withdraw() throws MinusException,InsuffException{

	}

	public void searchData() {

	}

	public void searchAllData() {

	}

	public void exit() {
		System.exit(0);
	}
}
