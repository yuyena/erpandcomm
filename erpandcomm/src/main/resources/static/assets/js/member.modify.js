// 정보 변경 버튼 클릭 시 AJAX로 수정 폼을 불러와서 영역 교체
$('#editInfoBtn').on('click', function() {
  $.ajax({
    url: 'modifyForm',
    type: 'get',
	dataType: 'json',
    success: function(param) {
      if(param.result == 'success') {
        // JS로 직접 HTML 생성 (employee_code는 제외)
        /*var html = '';
        html += '<form id="member_modify">';
        html += '  <input type="text" name="user_name" value="' + (data.user_name || '') + '">';
        html += '  <input type="email" name="email" value="' + (data.email || '') + '">';
        html += '  <input type="text" name="phone" value="' + (data.phone || '') + '">';
        html += '  <input type="text" name="extension_num" value="' + (data.extension_num || '') + '">';
        html += '  <button type="submit">저장</button>';
        html += '</form>';
        $('#mypageSection').html(html);*/
		
		alert('ㅇㅇㅇㅇ')
      } else if(param.result == 'logout') {
        alert('로그인이 필요합니다.');
      }
    },
    error: function() {
      alert('수정 폼을 불러오지 못했습니다.');
    }
  });
});