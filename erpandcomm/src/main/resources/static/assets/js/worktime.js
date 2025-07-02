/*
 * 
 */
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

document.addEventListener("DOMContentLoaded", function () {
    updateDateTime();
    setInterval(updateDateTime, 60000);
});

    function handleCheckIn() {
        if (confirm("출근하시겠습니까?")) {
            const now = new Date();
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            const timeString = `${hours}:${minutes}`;

            document.getElementById("worktimeTimes").style.display = "block";
            document.getElementById("checkInTimeInput").value = timeString;
            document.getElementById("checkOutTimeInput").value = "-";

            document.getElementById("checkInBtn").disabled = true;
            document.getElementById("checkInBtn").textContent = "출근 완료";
            document.getElementById("checkOutBtn").disabled = false;
        }
    }

    function handleCheckOut() {
        if (confirm("퇴근하시겠습니까?")) {
            const now = new Date();
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            const timeString = `${hours}:${minutes}`;

            document.getElementById("checkOutTimeInput").value = timeString;
            document.getElementById("checkOutBtn").disabled = true;
            document.getElementById("checkOutBtn").textContent = "퇴근 완료";
        }
    }