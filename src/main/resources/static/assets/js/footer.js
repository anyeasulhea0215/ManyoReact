 const selectBox = document.getElementById('customSelect');
      const selectElement = selectBox.querySelector('select');

     let isOpen = false;

    selectElement.addEventListener('click', (e) => {
        isOpen = !isOpen;
        selectBox.classList.toggle('open', isOpen);
    });

  document.addEventListener('click', (e) => {
     if (!selectBox.contains(e.target)) {
       selectBox.classList.remove('open');
       isOpen = false;
    }
  });

  console.log("sciript test");