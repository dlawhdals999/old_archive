package dto;

import java.io.Serializable;
import java.util.List;

import dto.FeedVO;

public class FeedPage implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final int pageSize = 5;
	/*
	 * ��)
	 * ��ü �Խñۼ� 53, �������� ������ �ǵ� ���� 5�� , ȭ�� �ϴܿ� ������ ������ prev [1][2][3][4][5] next or prev [6][7][8][9][10] next
	 * total = 53 totalPages = 11��
	 * feedList�� ��û�������� �ǵ� ���� size��    
	 *  startPage = 1 , endPage = 5 or startPage = 6 , endPage=10  
	 * 
	 */
	private int total;		//��ü �ǵ� �� 
	private int currentPage; //����ڰ� ��û�� ������ ��ȣ
	private List<FeedVO> feedList;	//ȭ�鿡 ������ feed�� ���� list
	private int totalPages; //��ü ������ ���� 
	private int startPage; //ȭ�� �ϴܿ� ������ ������ �̵� ��ũ ���� 
	private int endPage;	//ȭ�� �ϴܿ� ������ ������ �̵� ��ũ ��	
	
	public FeedPage(int total,int currentPage,int size,List<FeedVO> feedList) {
		this.total = total;
		this.currentPage = currentPage;
		this.feedList = feedList;
			
		if(total == 0){ //�ۼ��� �Խñ��� ������
			totalPages = 0;
			startPage = 0;
			endPage = 0;			
		}else{
			//��ü feed / ������ ���� �� ���� ��ü ������ �� ���ϱ�
			//e.g) total 34 , size 10 -> (34/10) == 3 , (34%10)==4�� �Խñ� -> 1������ �� �ʿ� 
			totalPages = total / size;  
			if(total%size!=0)
				totalPages++;
			
			//ȭ�� �ϴܿ� ������ ������ �̵� ��ũ prev [1][2][3][4][5] next || prev [6][7][8][9][10] next
			/*
			 * e.g1) currentPage = 3 , pageSize = 5			  
			 * -> startPage == 3/5*5+1 ==1
			 * -> modVal == 3%5 == 3
			 * -> endPage == 1+5-1 == 5
			 * +��ü �������� [1][2][3][4]�� ��� endPage == 4�� �ٲ���
			 * 
			 * e.g2) currentPage =5 , pageSize = 5  
			 * ->startPage == 5/5*5+1 == 6
			 * ->modVal == 5%5 == 0
			 * ->startPage == 6-5 == 1
			 * ->endPage == 1+5-1 == 5
			 */
			
			//[1] || [6] �� ���� ���� ������ ����
			startPage = currentPage/pageSize*pageSize+1;
			int modVal = currentPage % pageSize; //������ ������ ���� �˻�			
			if(modVal == 0) //������ ������ �� ���
				startPage-=pageSize;
			//[5] || [10]�� ���� ������ ������ ����
			endPage = startPage+pageSize-1;
			if(endPage > totalPages) //�������������� totalPages���� ū ���
				endPage = totalPages;			
		}		
	}
	
	public boolean hasFeed(){
		return total>0;
	}
	public int getTotal(){
		return total;
	}
	public int getCurrentPage() {
		return currentPage;
	}

	public List<FeedVO> getFeedList() {
		return feedList;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}
}
