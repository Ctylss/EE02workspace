function login() {
  const username = document.getElementById("username").value;
  const role = document.getElementById("role").value;
  



  if (!username) {
    alert("請輸入使用者帳號");
    return;
  }

  document.getElementById("loginContainer").style.display = "none";
  document.getElementById("dashboard").style.display = "block";
  document.getElementById("loggedInUser").innerText = username;
  document.getElementById("userRole").innerText = role === "admin" ? "管理員" : "一般使用者";

  renderMenu(role);
  $('.ui.accordion').accordion();
}

function logout() {
  // 清空登入欄位（可選）
  document.getElementById("username").value = "";
  document.getElementById("password").value = "";
  document.getElementById("role").value = "user";

  // 隱藏 Dashboard，顯示登入畫面
  document.getElementById("dashboard").style.display = "none";
  document.getElementById("loginContainer").style.display = "flex";

  // 清空使用者資訊和選單
  document.getElementById("loggedInUser").innerText = "";
  document.getElementById("userRole").innerText = "";
  document.getElementById("menuAccordion").innerHTML = "";

  // 預設內容區回復初始文字
  const area = document.getElementById("contentArea");
  area.innerHTML = `<h2 class="ui header">歡迎使用工業管理系統</h2><p>請從左側選單選擇功能</p>`;
}

function loadContent(contentName) {
  const area = document.getElementById("contentArea");
  area.innerHTML = `<h3 class="ui header">${contentName}</h3><p>這是 ${contentName} 的模擬頁面內容。</p>`;
}

function renderMenu(role) {
  const menu = document.getElementById("menuAccordion");
  menu.innerHTML = "";

  const modules = [
    {
      label: "1",
      color: "orange",
      title: "人員員工",
      sub: ["員工列表", "新增員工"]
    },
    {
      label: "2",
      color: "blue",
      title: "設備機台",
      sub: ["機台清單", "保養排程"]
    },
    {
      label: "3",
      color: "green",
      title: "工單",
      sub: ["工單查詢", "建立工單"]
    },
    {
      label: "4",
      color: "purple",
      title: "生產物料",
      sub: ["物料庫存", "物料請領"]
    },
    {
      label: "5",
      color: "red",
      title: "出缺勤 / 人資",
      sub: ["打卡記錄", "請假管理"]
    }
  ];

  modules.forEach(module => {
    const isAdminOnly = ["新增員工", "建立工單", "請假管理"].some(sub => module.sub.includes(sub));
    if (role === "user" && isAdminOnly) {
      module.sub = module.sub.filter(item => !["新增員工", "建立工單", "請假管理"].includes(item));
    }

    if (module.sub.length > 0) {
      const title = `
        <div class="title">
          <i class="dropdown icon"></i>
          <span class="ui ${module.color} label">${module.label}</span> ${module.title}
        </div>
      `;
      const content = `
        <div class="content">
          <div class="ui list">
            ${module.sub.map(item => `<a class="item" onclick="loadContent('${item}')">${item}</a>`).join("")}
          </div>
        </div>
      `;
      menu.innerHTML += title + content;
    }
  });
}
