package dto;

import java.io.Serializable;
import java.util.Date;

public class FeedVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int feedNumber; //�Խñ� ��ȣ
	private WriterVO writer; //�ۼ���
	private String content; //����
	private Date regDate; //��� ����
	private Date modifiedDate; //���� ����
	private int likeCount; //���ƿ� ��
	
	//�Խñ� ���� ���� ������
	public FeedVO(WriterVO writer,String content, Date regDate, Date modifiedDate, int likeCount){
		this.writer = writer;
		this.content = content;
		this.regDate = regDate;
		this.modifiedDate = modifiedDate;
		this.likeCount = likeCount;
	}
	
	//
	public FeedVO(int number,WriterVO writer,String content, Date regDate, Date modifiedDate, int likeCount){
		this(writer,content,regDate,modifiedDate,likeCount);
		this.feedNumber = number;		
	}			
	
	//�Խñ� ���� ���� ������
	public FeedVO(int feedNumber,WriterVO writer,String content,Date modifiedDate){
		this.feedNumber = feedNumber;
		this.writer = writer;
		this.content = content;		
		this.modifiedDate = modifiedDate;		
	}
	
	//getter
	public Integer getFeedNumber() {
		return feedNumber;
	}
	public WriterVO getWriter() {
		return writer;
	}
	public String getContent() {
		return content;
	}
	public Date getRegDate() {
		return regDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public int getLikeCount() {
		return likeCount;
	}
}
