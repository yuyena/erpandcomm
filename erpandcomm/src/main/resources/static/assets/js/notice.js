$(function(){
	/*---------------------------------
	 * 공지사항 아코디언 토글
	 *---------------------------------*/
	$('.notice-header').click(function(){
		const noti_num = $(this).attr('data-num');
		const content = $(this).next('.notice-content');
		const btn = $(this).find('.toggle-btn');
		
		if(content.is(':visible')){
			// 현재 열려있으면 닫기
			content.slideUp(300);
			btn.text('+');
			$(this).removeClass('active');
		} else {
			// 현재 닫혀있으면 열기
			if(content.hasClass('loaded')){
				// 이미 로딩된 경우 바로 표시
				content.slideDown(300);
				btn.text('-');
				$(this).addClass('active');
			} else {
				// Ajax로 상세 내용 로딩
				loadNoticeDetail(noti_num, content, btn, $(this));
			}
		}
	});
	
	/*---------------------------------
	 * 공지사항 상세 내용 로딩
	 *---------------------------------*/
	function loadNoticeDetail(noti_num, content, btn, header){
		// 로딩 표시
		content.html('<div class="loading"><i class="fa fa-spinner fa-spin"></i> 로딩 중...</div>');
		content.slideDown(300);
		btn.text('-');
		header.addClass('active');
		
		// 서버와 통신
		$.ajax({
			url: '/notice/noticeDetail/' + noti_num,
			type: 'get',
			dataType: 'json',
			beforeSend: function(xhr){
				xhr.setRequestHeader($('meta[name="csrf-header"]').attr('content'),
									 $('meta[name="csrf-token"]').attr('content'));
			},
			success: function(param){
				if(param.result == 'success'){
					displayNoticeContent(param, content);
					content.addClass('loaded'); // 로딩 완료 표시
				} else if(param.result == 'notFound'){
					content.html('<div class="error-message">존재하지 않는 공지사항입니다.</div>');
				} else {
					content.html('<div class="error-message">공지사항 로딩 중 오류가 발생했습니다.</div>');
				}
			},
			error: function(xhr){
				if(xhr.status == 401 || xhr.status == 403){
					alert('로그인이 필요합니다.');
					location.href = '/member/memberLogin';
				} else {
					alert('네트워크 통신 오류 발생');
					content.slideUp(300);
					btn.text('+');
					header.removeClass('active');
				}
			}
		});
	}
	
	/*---------------------------------
	 * 공지사항 내용 표시 공통 함수
	 *---------------------------------*/
	function displayNoticeContent(param, content){
		let output = `
			<div class="notice-detail">
				<div class="notice-content-body">
					${param.noti_content}
				</div>
			</div>
		`;
		content.html(output);
	}
	
	/*---------------------------------
	 * 전체 열기/닫기 기능
	 *---------------------------------*/
	$('#btn-expand-all').click(function(){
		$('.notice-header').each(function(){
			const content = $(this).next('.notice-content');
			if(!content.is(':visible')){
				$(this).trigger('click');
			}
		});
	});
	
	$('#btn-collapse-all').click(function(){
		$('.notice-content:visible').each(function(){
			$(this).slideUp(300);
			$(this).prev('.notice-header').removeClass('active');
			$(this).prev('.notice-header').find('.toggle-btn').text('+');
		});
	});
	
	/*---------------------------------
	 * 검색 기능
	 *---------------------------------*/
	$('#search_form').submit(function(){
		$('.notice-content').slideUp(300);
		$('.notice-header').removeClass('active');
		$('.toggle-btn').text('+');
		$('.notice-content').removeClass('loaded');
	});
	
}); 