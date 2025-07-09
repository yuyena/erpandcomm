/*모달창 띄우는 js */
// 모달 엘리먼트
document.addEventListener('DOMContentLoaded', function() {
	console.log('attendanceForm.js 실행됨');
	const modal = document.getElementById('attendanceModal');
	const openBtn = document.getElementById('openModalBtn'); // id로 접근하도록 변경
	const closeBtn = document.getElementById('closeModalBtn');
	const cancelBtn = document.getElementById('cancelBtn');
	const form = document.getElementById('attendanceForm');
	/*
	  // 모달 열기 함수
	  function openwriteModal() {
		modal.style.display = 'flex';
	  }
	
	  // 모달 닫기 함수
	  function closeModal() {
		modal.style.display = 'none';
		form.reset();  // 폼 초기화
	  }
	*/
	function openwriteModal() {
		modal.style.display = 'flex';
		//modal.classList.add('show');  // display: flex; pointer-events: auto;
	}

	function closeModal() {
		//modal.classList.remove('show');  // 다시 숨김
		modal.style.display = 'none';
		form.reset();
	}

	// 이벤트 바인딩
	openBtn.addEventListener('click', openwriteModal);
	closeBtn.addEventListener('click', closeModal);
	cancelBtn.addEventListener('click', closeModal);

	// 폼 제출 처리 (예: 서버에 AJAX 전송 or 그냥 알림)
	form.addEventListener('submit', (e) => {
		e.preventDefault();

		// 폼 데이터 가져오기
		const formData = new FormData(form);
		const data = {};
		formData.forEach((value, key) => data[key] = value);

		// 여기서 서버로 전송하는 코드 작성 가능
		// 예: fetch('/attendance/save', { method: 'POST', body: formData }) 등

		alert('근태가 등록되었습니다!\n' + JSON.stringify(data, null, 2));

		closeModal();
	});
	modal.addEventListener('click', e => {
	  if (e.target === modal) {
	    closeModal();
	  }
	});
});