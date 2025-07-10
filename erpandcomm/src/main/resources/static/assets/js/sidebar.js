console.log('sidebar.js loaded');
// sidebar.js - 사이드바 메뉴 토글 및 활성화

document.addEventListener('DOMContentLoaded', function() {
  // 메뉴 토글 기능
  document.querySelectorAll('.menu-toggle').forEach(function(toggle) {
    toggle.addEventListener('click', function(e) {
      e.preventDefault();
      var parent = this.closest('.menu-item');
      var submenu = parent.querySelector('.submenu');
      var arrow = parent.querySelector('.arrow');
      
      if (submenu) {
        // 현재 메뉴의 하위 메뉴만 토글
        if (submenu.classList.contains('open')) {
          submenu.classList.remove('open');
          parent.classList.remove('open');
          if (arrow) arrow.style.transform = 'rotate(0deg)';
        } else {
          // 다른 메뉴의 하위 메뉴는 모두 닫기
          document.querySelectorAll('.menu-item.open').forEach(item => {
            if (item !== parent) {
              item.classList.remove('open');
              var sub = item.querySelector('.submenu');
              if (sub) sub.classList.remove('open');
              var arrow = item.querySelector('.arrow');
              if (arrow) arrow.style.transform = 'rotate(0deg)';
            }
          });
          
          // 현재 메뉴의 하위 메뉴 열기
          submenu.classList.add('open');
          parent.classList.add('open');
          if (arrow) arrow.style.transform = 'rotate(90deg)';
        }
      }
    });
  });

  // 현재 페이지에 해당하는 메뉴 항목 활성화
  var currentPath = window.location.pathname;
  document.querySelectorAll('.sidebar-menu a[href]').forEach(function(link) {
    var href = link.getAttribute('href');
    if (href && currentPath.includes(href.replace('@{', '').replace('}', ''))) {
      link.classList.add('active');
      
      // 부모 메뉴 아이템 찾기
      var parentMenuItem = link.closest('.menu-item');
      while (parentMenuItem) {
        parentMenuItem.classList.add('open');
        var submenu = parentMenuItem.querySelector('.submenu');
        var arrow = parentMenuItem.querySelector('.arrow');
        
        if (submenu) submenu.classList.add('open');
        if (arrow) arrow.style.transform = 'rotate(90deg)';
        
        parentMenuItem = parentMenuItem.closest('.menu-item.has-sub');
      }
    }
  });
});