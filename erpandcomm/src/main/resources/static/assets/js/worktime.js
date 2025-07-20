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
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const timeString = `${hours}:${minutes}`;
	const dateString = now.toISOString().slice(0,10);
	
	// 상태 결정
	let status = hours < 9 ? "present":"late";
	
	document.getElementById("checkInTimeInput").value = timeString;
	document.getElementById("displayCheckInTime").value = timeString;
	document.getElementById("statusInput").value = status;
	document.getElementById("checkOutTimeInput").value = "";
		
	
	document.getElementById("checkInBtn").disabled=true;
	document.getElementById("checkInBtn").textContent="출근완료";
	document.getElementById("checkOutBtn").disabled=false;
			
	
    // 세션 저장(페이지 세션 유지하는 동안 저장)
	sessionStorage.setItem("checkInTime", timeString);
	sessionStorage.setItem("status", status);
		
	// 폼 제출 worktimeForm
	document.getElementById("worktimeForm").submit();
  }
}

// 퇴근 버튼 처리
function handleCheckOut() {
  if (confirm("퇴근하시겠습니까?")) {
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const timeString = `${hours}:${minutes}`;

    document.getElementById("checkOutTimeInput").value = timeString;
    document.getElementById("checkOutBtn").disabled = true;
    document.getElementById("checkOutBtn").textContent = "퇴근 완료";

    // 세션 저장
    sessionStorage.setItem("checkOutTime", timeString);
  }
}

// 페이지 복귀 시 세션 상태 복원
function restoreState() {
  const checkInTime = sessionStorage.getItem("checkInTime");
  const checkOutTime = sessionStorage.getItem("checkOutTime");

  if (checkInTime) {
    document.getElementById("checkInTimeInput").value = checkInTime;
    document.getElementById("checkInBtn").disabled = true;
    document.getElementById("checkInBtn").textContent = "출근 완료";
    document.getElementById("checkOutBtn").disabled = false;
  }

  if (checkOutTime) {
    document.getElementById("checkOutTimeInput").value = checkOutTime;
    document.getElementById("checkOutBtn").disabled = true;
    document.getElementById("checkOutBtn").textContent = "퇴근 완료";
  }
}

document.addEventListener("DOMContentLoaded", function () {
  updateDateTime();
  setInterval(updateDateTime, 60000);
  restoreState(); // 상태 복원
});