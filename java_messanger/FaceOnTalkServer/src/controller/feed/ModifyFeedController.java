package controller.feed;

import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import controller.WebCommandController;
import dao.FeedDao;
import dto.FeedVO;
import dto.WriterVO;
import request.Request;

/*
 * �Խñ� ���� ��û ��Ʈ�� Ŭ����
 */
public class ModifyFeedController implements WebCommandController	{
	@Override
	public Request execute(Request request) {
		FeedDao feedDao = FeedDao.getInstance();
		Request response = new Request(request.getType());
		Map<String,Boolean> errors = new Hashtable<>();
		response.setAttribute("errors",errors);			
		try {
			FeedVO feed = feedDao.selectByFeedNumber(Integer.parseInt(request.getParameter("feed_no")));
			
			if(feed == null){
				errors.put("cantfindfeed",Boolean.TRUE);
				return response;
			} 			
			if(!canModify(request.getParameter("id"),feed)){
				errors.put("permissiondenied",Boolean.TRUE);
				return response;
			}				
			if(!(feedDao.update(toModifyFeed(request))>0)){
				errors.put("failed",Boolean.TRUE);			
			}
			feedDao.update(toModifyFeed(request));
		} catch(SQLException e) {
			errors.put("SQLException",Boolean.TRUE);
		}
		return response;					
	}
	
	//��û�� id�� , �۾��� id �� ��
	private boolean canModify(String idVal,FeedVO feed){
		int modifingId = Integer.parseInt(idVal);
		return (feed.getWriter().getId() == modifingId);
	}
	
	//Request -> Feed�ν��Ͻ� ����
	private FeedVO toModifyFeed(Request request){	
		return new FeedVO(
				Integer.parseInt(request.getParameter("feed_no"))
				,new WriterVO(
						Integer.parseInt(request.getParameter("id"))
						,request.getParameter("name")
						,request.getParameter("email"))
				,request.getParameter("modcontent")
				,new Date());				
	}
}
