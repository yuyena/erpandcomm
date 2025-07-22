function updateDateTime() {
	const now = new Date();
	const year = now.getFullYear();
	const month = String(now.getMonth() + 1).padStart(2, '0');
	const day = String(now.getDate()).padStart(2, '0');
	const hours = String(now.getHours()).padStart(2, '0');
	const minutes = String(now.getMinutes()).padStart(2, '0');
	const formatted = `${year}년 ${month}월 ${day}일 ${hours}:${minutes}`;

	const elem = document.getElementById('currentDateTime');
	if (elem) elem.textContent = formatted;
}

// 출근 버튼 처리
function handleCheckIn() {
	if (sessionStorage.getItem("checkInTime")) {
		alert("이미 출근하셨습니다.");
		return;
	}

	if (confirm("출근하시겠습니까?")) {
		const now = new Date();
		const hours = now.getHours();
		const minutes = now.getMinutes();
		const hoursStr = String(hours).padStart(2, '0');
		const minutesStr = String(minutes).padStart(2, '0');
		const timeString = `${hoursStr}:${minutesStr}`;

		// 상태 결정 (9시 이전은 "present", 이후는 "late")
		let status = hours < 9 ? "present" : "late";

		document.getElementById("checkInTimeInput").value = timeString;
		document.getElementById("displayCheckInTime").value = timeString;
		document.getElementById("statusInput").value = status;
		document.getElementById("checkOutTimeInput").value = "";
		document.getElementById("displayCheckOutTime").value = "";

		// 버튼 상태 변경
		const checkInBtn = document.getElementById("checkInBtn");
		const checkOutBtn = document.getElementById("checkOutBtn");
		checkInBtn.disabled = true;
		checkInBtn.textContent = "출근 완료";
		checkOutBtn.disabled = false;
		checkOutBtn.textContent = "퇴근하기";

		// 세션 저장
		sessionStorage.setItem("checkInTime", timeString);
		sessionStorage.setItem("status", status);
		sessionStorage.removeItem("checkOutTime"); // 퇴근 시간 초기화
	}
}

// 퇴근 버튼 처리
function handleCheckOut() {
	const now = new Date();
	const hours = now.getHours();
	const minutes = now.getMinutes();
/*
	// 18시 이전이면 퇴근 금지
	if (hours < 18) {
		alert("퇴근은 18시 이후에 가능합니다.");
		return;
	}
*/
	if (confirm("퇴근하시겠습니까?")) {
		const timeString = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;

		document.getElementById("checkOutTimeInput").value = timeString;
		document.getElementById("displayCheckOutTime").value = timeString;

		const checkOutBtn = document.getElementById("checkOutBtn");
		checkOutBtn.disabled = true;
		checkOutBtn.textContent = "퇴근 완료";

		// 세션 저장
		sessionStorage.setItem("checkOutTime", timeString);

		// 폼 제출
		document.getElementById("worktimeForm").submit();
	}
}

// 페이지 로드 시 버튼 상태 복원
function restoreState() {
	const checkInTime = sessionStorage.getItem("checkInTime");
	const checkOutTime = sessionStorage.getItem("checkOutTime");

	const checkInBtn = document.getElementById("checkInBtn");
	const checkOutBtn = document.getElementById("checkOutBtn");

	if (checkInTime) {
		document.getElementById("checkInTimeInput").value = checkInTime;
		document.getElementById("displayCheckInTime").value = checkInTime;

		checkInBtn.disabled = true;
		checkInBtn.textContent = "출근 완료";

		if (!checkOutTime) {
			checkOutBtn.disabled = false;
			checkOutBtn.textContent = "퇴근하기";
		}
	}

	if (checkOutTime) {
		document.getElementById("checkOutTimeInput").value = checkOutTime;
		document.getElementById("displayCheckOutTime").value = checkOutTime;

		checkOutBtn.disabled = true;
		checkOutBtn.textContent = "퇴근 완료";
	}
}

// 페이지 로드 시 실행
document.addEventListener("DOMContentLoaded", () => {
	updateDateTime();
	setInterval(updateDateTime, 60000);
	restoreState();
});