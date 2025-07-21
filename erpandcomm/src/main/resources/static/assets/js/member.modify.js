$(document).ready(function() {
    console.log('페이지 로드됨');
    console.log('editInfoBtn 존재:', $('#editInfoBtn').length);
    
    // 정보 변경 버튼 클릭 시 AJAX로 수정 폼을 불러와서 영역 교체
    $('#editInfoBtn').on('click', function() {
        console.log('버튼 클릭됨');
        
        $.ajax({
            url: '/member/modifyForm',
            type: 'GET',
            dataType: 'json',
            success: function(param) {
                console.log('응답:', param);
                
                if(param.result == 'success') {
                    // 기존 테이블의 텍스트를 input 필드로 변경
                    $('.member-info-table tr').each(function() {
                        const $th = $(this).find('th');
                        const $td = $(this).find('td');
                        const fieldName = $th.text().trim();
                        
                        // 필드명에 따라 input 생성
                        
                        // 각 필드별로 통일된 크기의 input 생성
                        if (fieldName === '이메일') {
                            $td.html(`<input type="email" name="email" value="${param.email || ''}" class="table-input">`);
                        } else if (fieldName === '전화번호') {
                            $td.html(`<input type="text" name="phone" value="${param.phone || ''}" class="table-input">`);
                        } else if (fieldName === '내선번호') {
                            $td.html(`<input type="text" name="extension_num" value="${param.extension_num || ''}" class="table-input">`);
                        }
                        
                        // input 필드에 강제로 동일한 크기 적용
                        if ($td.find('input').length > 0) {
                            $td.find('input').css({
                                'width': '280px',
                                'min-width': '280px',
                                'max-width': '280px',
                                'height': '36px',
                                'min-height': '36px',
                                'max-height': '36px',
                                'display': 'block',
                                'box-sizing': 'border-box'
                            });
                        }
                        
                        // 사번은 수정 불가능하므로 그대로 유지
                    });
                    
                    // 기존 테이블을 form으로 감싸기
                    $('.member-info-table').wrap('<form id="member_modify"></form>');
                    
                    // 버튼 변경: "정보 변경" → "저장", "비밀번호 변경" → "취소"
                    $('#editInfoBtn').text('저장').off('click').on('click', function(e) {
                        e.preventDefault();
                        saveUserInfo();
                    });
                    
                    $('.pw-edit-btn').text('취소').off('click').on('click', function() {
                        location.reload();
                    });
                    
                } else if(param.result == 'logout') {
                    alert('로그인이 필요합니다.');
                    location.href = '/member/login';
                } else {
                    alert('수정 폼을 불러오지 못했습니다.');
                }
            },
            error: function(xhr, status, error) {
                console.error('Ajax 에러:', error);
                console.error('상태:', status);
                console.error('응답:', xhr.responseText);
                alert('수정 폼을 불러오지 못했습니다: ' + error);
            }
        });
    });
});

// 저장 함수
function saveUserInfo() {
    const formData = {
        email: $('input[name="email"]').val(),
        phone: $('input[name="phone"]').val(),
        extension_num: $('input[name="extension_num"]').val()
    };
    
    $.ajax({
        url: '/member/modifyForm',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
		beforeSend:function(xhr){
			xhr.setRequestHeader($('meta[name="csrf-header"]').attr('content'),
								 $('meta[name="csrf-token"]').attr('content'));
		},
        success: function(data) {
            if (data.result === 'success') {
                alert('정보가 성공적으로 수정되었습니다.');
                location.reload();
            } else {
                alert('수정에 실패했습니다: ' + (data.message || ''));
            }
        },
        error: function() {
            alert('네트워크 오류 발생.');
        }
    });
}