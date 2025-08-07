 document.addEventListener('DOMContentLoaded', function () {

    const userId=document.body.dataset.userId;  //로그인한 사용자 정보

    if(!userId) return;

    const storageKey=`recentProducts_${userId}`;  //storageKey설정  <-로그인한 사용자로
    console.log('storageKey:', storageKey);
    const productLinks = document.querySelectorAll('.product a');

    productLinks.forEach(link => {
      link.addEventListener('click', function (e) {

        const productId = new URL(link.href).searchParams.get("productId");
        const productImg = link.querySelector('img').getAttribute('src');
        const productName = link.closest('.product').querySelector('.product-name').textContent;

  console.log('저장할 상품 정보:', productId, productImg, productName);

        const viewedProduct = {
          id: productId,
          img: productImg,
          name: productName,
          time: new Date().getTime()
        };

        // 기존 목록 불러오기
        let recent = JSON.parse(localStorage.getItem(storageKey) || '[]');

        // 동일 상품 제거
        recent = recent.filter(item => item.id !== productId);

        // 맨 앞에 추가
        recent.unshift(viewedProduct);

        if (recent.length > 5) {
          recent = recent.slice(0, 5);
        }

        localStorage.setItem(storageKey, JSON.stringify(recent));
      });
    });
  });