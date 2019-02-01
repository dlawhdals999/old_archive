package controller.feed;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import controller.WebCommandController;
import dao.FeedDao;
import dto.FeedPage;
import dto.FeedVO;
import request.Request;

/*
 * feed �������� ��û�ϸ� ó���ϴ� ��Ʈ�� Ŭ����
 */
public class ReadFeedController implements WebCommandController{
	private static final int DEFAULT_SIZE = 10;
	@Override
	public Request execute(Request request) {		
		FeedDao feedDao = FeedDao.getInstance();
		Request response = new Request(request.getType());				
		Map<String,Boolean> errors = new Hashtable<>();
		response.setAttribute("errors",errors);
		
		////////////////////////////
		//��û ���̵� & ��û ������ & ������ ���� parameter ���
		////////////////////////////
		//��û���̵�
		int id = Integer.parseInt(request.getParameter("id"));
		
		//��û ������
		String pageVal = request.getParameter("pageNo");
		int pageNo = 1;
		if(pageVal!=null){
			pageNo = Integer.parseInt(pageVal);
		}		
		//������ ����
		String sizeVal = request.getParameter("listsize");		
		int listSize = DEFAULT_SIZE;
		if(sizeVal!=null)
			listSize = Integer.parseInt(sizeVal);			
		try {
			String readType = request.getParameter("readType");
			System.out.println("�ǵ� ��û Ÿ��"+readType);
			int total = 0;
			List<FeedVO> feedList = null;
			
			if(readType.equals("all")) { //ģ�� + ���� �ǵ�
				total = feedDao.selectAllCount(id);				
				feedList = feedDao.select(id,(pageNo-1)*listSize+1, listSize);
			} else if(readType.equals("admin")) { //���� �ǵ�
				total = feedDao.selectCount(id);
				System.out.println("total  : "+total);
				feedList = feedDao.selectOnlyUser(id, (pageNo-1)*listSize+1,listSize);			
			}			
			//��ü �Խñ� ���ϱ�					
			response.setAttribute("feedPage",new FeedPage(total,pageNo,listSize,feedList));						
		}catch(SQLException e) { 
			e.printStackTrace();
			errors.put("SQLException",Boolean.TRUE);
		}
		return response;
	}
}
























