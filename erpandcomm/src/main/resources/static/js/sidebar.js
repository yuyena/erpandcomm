// 사이드바 메뉴 토글
function initSidebar() {
    document.querySelectorAll('.menu-toggle').forEach(function(toggle) {
        toggle.addEventListener('click', function(e) {
            e.preventDefault();
            var parent = this.parentElement;
            var submenu = parent.querySelector('.submenu');
            
            // 모든 메뉴 닫기
            document.querySelectorAll('.menu-item.has-sub').forEach(function(item) {
                if (item !== parent) {
                    item.classList.remove('open');
                    var sub = item.querySelector('.submenu');
                    if (sub) sub.classList.remove('open');
                    var arrow = item.querySelector('.arrow');
                    if (arrow) arrow.innerHTML = '&#9654;';
                }
            });
            
            // 현재 메뉴 토글
            var isOpen = submenu.classList.contains('open');
            submenu.classList.toggle('open', !isOpen);
            parent.classList.toggle('open', !isOpen);
            var arrow = this.querySelector('.arrow');
            if (arrow) {
                arrow.innerHTML = !isOpen ? '&#9660;' : '&#9654;';
            }
        });
    });
}

// DOM이 로드된 후 초기화
document.addEventListener('DOMContentLoaded', function() {
    initSidebar();
});
