document.addEventListener('DOMContentLoaded', function () {
  const toggles = document.querySelectorAll('.sidebaar-nav .dropdown-toggle');

  toggles.forEach(toggle => {
    toggle.addEventListener('click', function (e) {
      e.preventDefault();
      this.classList.toggle('active');
      const menu = this.nextElementSibling;
      if (menu && menu.classList.contains('dropdown-menuu')) {
        menu.classList.toggle('open');
      }
    });
  });
});