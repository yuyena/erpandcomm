package kr.spring.notice.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.spring.notice.vo.NoticeVO;

@Mapper
public interface NoticeMapper {
	
	public List<NoticeVO> selectList(Map<String, Object> map);
	public Integer selectRowCount(@Param("keyfield") String keyfield, @Param("keyword") String keyword);
	public void insertNotice(NoticeVO noticeVO);
	public void updateNotice(NoticeVO noticeVO);
	public NoticeVO selectNotice(Long noti_num);
	public void deleteNotice(Long noti_num);

}
