
$(function(){
	console.log('notice.js 로드됨');
	console.log('jQuery 버전:', $.fn.jquery);
	
	// 공지사항 헤더 요소 확인
	console.log('공지사항 헤더 개수:', $('.notice-header').length);
	
	/*---------------------------------
	 * 공지사항 아코디언 토글
	 *---------------------------------*/
	$('.notice-header').click(function(){
		const noti_num = $(this).attr('data-num');
		const content = $(this).next('.notice-content');
		const btn = $(this).find('.toggle-btn');
		
		console.log('공지사항 클릭됨:', noti_num);
		console.log('content 요소:', content);
		console.log('btn 요소:', btn);
		
		if(content.is(':visible')){
			// 현재 열려있으면 닫기
			console.log('공지사항 닫기');
			content.slideUp(300);
			btn.text('+');
			$(this).removeClass('active');
		} else {
			// 현재 닫혀있으면 열기
			if(content.hasClass('loaded')){
				// 이미 로딩된 경우 바로 표시
				console.log('이미 로딩된 공지사항 열기');
				content.slideDown(300);
				btn.text('-');
				$(this).addClass('active');
			} else {
				// Ajax로 상세 내용 로딩
				console.log('Ajax로 공지사항 상세 로딩 시작');
				loadNoticeDetail(noti_num, content, btn, $(this));
			}
		}
	});
	
	/*---------------------------------
	 * 공지사항 상세 내용 로딩
	 *---------------------------------*/
	function loadNoticeDetail(noti_num, content, btn, header){
		console.log('loadNoticeDetail 호출됨:', noti_num);
		
		// 로딩 표시
		content.html('<div class="loading"><i class="fa fa-spinner fa-spin"></i> 로딩 중...</div>');
		content.slideDown(300);
		btn.text('-');
		header.addClass('active');
		
		// CSRF 토큰 확인
		const csrfHeader = $('meta[name="csrf-header"]').attr('content');
		const csrfToken = $('meta[name="csrf-token"]').attr('content');
		console.log('CSRF Header:', csrfHeader);
		console.log('CSRF Token:', csrfToken);
		
		// 서버와 통신
		$.ajax({
			url: '/notice/noticeDetail/' + noti_num,
			type: 'get',
			dataType: 'json',
			beforeSend: function(xhr){
				if(csrfHeader && csrfToken) {
					xhr.setRequestHeader(csrfHeader, csrfToken);
				}
			},
			success: function(param){
				console.log('Ajax 성공 응답:', param);
				if(param.result == 'success'){
					displayNoticeContent(param, content);
					content.addClass('loaded'); // 로딩 완료 표시
				} else if(param.result == 'notFound'){
					content.html('<div class="error-message">존재하지 않는 공지사항입니다.</div>');
				} else {
					content.html('<div class="error-message">공지사항 로딩 중 오류가 발생했습니다.</div>');
				}
			},
			error: function(xhr, status, error){
				console.log('Ajax 오류:', xhr.status, status, error);
				console.log('응답 텍스트:', xhr.responseText);
				
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
		if(param.user_num && param.login_user_num && param.user_num == param.login_user_num){
			output += `
			<div class="notice-actions">
			    <a href="/notice/update?noti_num=${param.noti_num}" class="btn btn-edit">수정</a>
			    <a href="/notice/delete?noti_num=${param.noti_num}" class="btn btn-cancel btn-delete-link">삭제</a>
			</div>
			`;
		}
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
	
	// 아코디언 헤더에 버튼 추가 (작성자만)
	$('.notice-header').each(function(){
		const noti_num = $(this).attr('data-num');
		const user_num = $(this).attr('data-user-num');
		const login_user_num = $('meta[name="login-user-num"]').attr('content');
		if(user_num && login_user_num && user_num === login_user_num){
			const btns = `
				<a href="/notice/update?noti_num=${noti_num}" class="btn btn-edit btn-sm">수정</a>
				<button type="button" class="btn btn-cancel btn-sm btn-delete-notice" data-num="${noti_num}">삭제</button>
			`;
			$(this).prepend(btns);
		}
	});

	// 삭제 링크 클릭 시 confirm만 띄우고, 취소하면 이동 막기
	$(document).on('click', '.btn-delete-link', function(e){
	    if(!confirm('정말 삭제하시겠습니까?')) {
	        e.preventDefault();
	    }
	});
	
	// 공지사항 등록 성공 메시지 2.5초 후 사라지게 (중복 방지)
	var $alert = $('.alert-success');
	if ($alert.length > 0) {
		setTimeout(function() {
			$alert.fadeOut(500);
		}, 2500);
	}
	
}); 