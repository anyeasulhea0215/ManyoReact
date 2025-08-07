console.log('스크립트 시작');

function attachLogoutEvent() {
  const logoutBtn = document.getElementById('logoutBtn');
  console.log('logoutBtn:', logoutBtn);

  if (logoutBtn) {
    console.log('로그아웃 버튼 이벤트 등록됨');
    logoutBtn.addEventListener('click', function(event) {
      event.preventDefault();
      console.log('로그아웃 버튼 클릭됨');
      alert('로그아웃되었습니다.');
      fetch('/api/account/logout', { method: 'GET' })
        .finally(() => {
          window.location.href = '/manyo/main';
        });
    });
  } else {
    console.log('logoutBtn 아직 없음, 100ms 후 재시도');
    setTimeout(attachLogoutEvent, 500);
  }
}

attachLogoutEvent();