package kr.spring.notice.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.notice.dao.NoticeMapper;
import kr.spring.notice.vo.NoticeVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class NoticeServiceImpl implements NoticeService {
	
	@Autowired
	private NoticeMapper noticeMapper;

	@Override
	public List<NoticeVO> selectList(Map<String, Object> map) {
		return noticeMapper.selectList(map);
	}

	@Override
	public Integer selectRowCount(String keyfield, String keyword) {
		Integer result = noticeMapper.selectRowCount(keyfield, keyword);
		log.debug("<<NoticeServiceImpl>> selectRowCount 결과: {}", result);
		return result;
	}

	@Override
	public void insertNotice(NoticeVO noticeVO) {
		noticeMapper.insertNotice(noticeVO);
	}

	@Override
	public NoticeVO selectNotice(Long noti_num) {
		return noticeMapper.selectNotice(noti_num);
	}

	@Override
	public void deleteNotice(Long noti_num) {
		noticeMapper.deleteNotice(noti_num);
	}

	@Override
	public void updateNotice(NoticeVO noticeVO) {
		noticeMapper.updateNotice(noticeVO);
	}

}
