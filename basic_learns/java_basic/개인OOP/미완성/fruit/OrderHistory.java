package fruit;
public class OrderHistory { //�ֹ� ������ ��� Ŭ����
	int[] orderCnt = new int[Fruit.SIZE]; //���ϴ� ���� ������ ��� �迭
	public OrderHistory(){} 	
	
	public void add(int idx){ //�ֹ��Ǹ� ���� �ø�
		orderCnt[idx]++;
	}	
	public void cancel(int idx){ //����ϸ� ���� ����
		if(orderCnt[idx]<1)
			return;
		orderCnt[idx]--;
	}	
	public int getCount(int idx){ //���� �ֹ��� �� ��ȯ
		return orderCnt[idx];
	}	
	public boolean isSaled(int idx){ //�Ǹż����� 0���� ũ�� true ,�ƴϸ� false
		return orderCnt[idx]!=0;
	}		
	public synchronized void addTableOrder(OrderHistory tableOrder){ //table�� OrderHistory��ü�� �޾Ƽ� ������ ��ü ������ �����ش�
		for(int i=0;i<orderCnt.length;i++){
			orderCnt[i]+=(tableOrder.getCount(i));
		}
	}		
	public String getSalesInfo(int idx){ //�Ǹ� ������ ���ڿ��� ��ȯ
		return Fruit.NAME[idx]+"(����:"+orderCnt[idx]+") �� : "+(Fruit.PRICE[idx]*orderCnt[idx])+"��";
	}	
	public int getTotal(){ //��ü �Ǹ� �ݾ��� ���Ѵ�
		int total = 0;
		for(int i=0;i<orderCnt.length;i++){
			total+=(orderCnt[i]*Fruit.PRICE[i]);
		}
		return total;
	}	
}
