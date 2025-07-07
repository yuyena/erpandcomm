console.log('sidebar.js loaded');
// sidebar.js - 사이드바 메뉴 토글 및 활성화

document.addEventListener('DOMContentLoaded', function() {
  document.querySelectorAll('.menu-toggle').forEach(function(toggle) {
    toggle.addEventListener('click', function(e) {
      e.preventDefault();
      var submenu = this.closest('.menu-item').querySelector('.submenu');
      if (submenu) submenu.classList.toggle('open');
    });
  });

  // 현재 페이지에 해당하는 메뉴 항목 활성화
  var currentPath = window.location.pathname;
  document.querySelectorAll('.sidebar-menu a[th\\:href]').forEach(function(link) {
    var href = link.getAttribute('href');
    if (href && currentPath.includes(href.replace('@{', '').replace('}', ''))) {
      link.classList.add('active');
      var parentMenuItem = link.closest('.menu-item.has-sub');
      if (parentMenuItem) {
        parentMenuItem.classList.add('open');
        var submenu = parentMenuItem.querySelector('.submenu');
        var arrow = parentMenuItem.querySelector('.arrow');
        if (submenu) submenu.classList.add('open');
        if (arrow) arrow.style.transform = 'rotate(90deg)';
      }
    }
  });
}); 