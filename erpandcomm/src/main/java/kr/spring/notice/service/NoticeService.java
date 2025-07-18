package kr.spring.notice.service;

import java.util.List;
import java.util.Map;

import kr.spring.notice.vo.NoticeVO;

public interface NoticeService {
	
	public List<NoticeVO> selectList(Map<String, Object> map);
	public Integer selectRowCount(Map<String, Object> map);
	public void insertNotice(NoticeVO noticeVO);
	public void updateNotice(NoticeVO noticeVO);
	public NoticeVO selectNotice(Long noti_num);
	public void deleteNotice(Long noti_num);

}
