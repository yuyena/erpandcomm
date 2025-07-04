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

// DOM이 준비되면 시간 표시 및 매 분마다 갱신
document.addEventListener("DOMContentLoaded", function () {
  updateDateTime();
  setInterval(updateDateTime, 60000); // 1분마다 시간 갱신
});

// 출근 버튼 클릭 처리
function handleCheckIn() {
  if (localStorage.getItem("checkInTime")) {
    alert("이미 출근하셨습니다.");
    return;
  }

  if (confirm("출근하시겠습니까?")) {
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const timeString = `${hours}:${minutes}`;

    document.getElementById("checkInTimeInput").value = timeString;
    document.getElementById("checkOutTimeInput").value = "-";

    document.getElementById("checkInBtn").disabled = true;
    document.getElementById("checkInBtn").textContent = "출근 완료";
    document.getElementById("checkOutBtn").disabled = false;

    localStorage.setItem("checkInTime", timeString);
    localStorage.setItem("checkInDone", "true");
  }
}
function restoreState() {
  const checkInTime = localStorage.getItem("checkInTime");
  const checkOutTime = localStorage.getItem("checkOutTime");

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

// 시간 저장
document.addEventListener("DOMContentLoaded", function () {
  updateDateTime();
  setInterval(updateDateTime, 60000);
  restoreState();
});


// 퇴근 버튼 클릭 처리
function handleCheckOut() {
  if (confirm("퇴근하시겠습니까?")) {
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const timeString = `${hours}:${minutes}`;

    // 화면에 퇴근 시간 표시
    document.getElementById("checkOutTimeInput").value = timeString;

    // 버튼 상태 변경
    document.getElementById("checkOutBtn").disabled = true;
    document.getElementById("checkOutBtn").textContent = "퇴근 완료";

    // 퇴근 시간 저장
    localStorage.setItem("checkOutTime", timeString);
  }
}

function restoreState() {
  // 1. 로컬 스토리지에서 출근 시간을 가져옴
  const checkInTime = localStorage.getItem("checkInTime");
  if (checkInTime) {
    // 출근 시간이 있으면 input 박스에 표시
    document.getElementById("checkInTimeInput").value = checkInTime;

    // 출근 버튼 비활성화 및 텍스트 변경
    document.getElementById("checkInBtn").disabled = true;
    document.getElementById("checkInBtn").textContent = "출근 완료";

    // 퇴근 버튼 활성화 (출근했으니 퇴근 가능)
    document.getElementById("checkOutBtn").disabled = false;
  }

  // 2. 로컬 스토리지에서 퇴근 시간을 가져옴
  const checkOutTime = localStorage.getItem("checkOutTime");
  if (checkOutTime) {
    // 퇴근 시간이 있으면 input 박스에 표시
    document.getElementById("checkOutTimeInput").value = checkOutTime;

    // 퇴근 버튼 비활성화 및 텍스트 변경
    document.getElementById("checkOutBtn").disabled = true;
    document.getElementById("checkOutBtn").textContent = "퇴근 완료";
  }
}
