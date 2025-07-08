
    function toggleDetails(orderId) {
        const detail = document.getElementById("details-" + orderId);
        if (detail.style.display === "none") {
            detail.style.display = "table-row";
        } else {
            detail.style.display = "none";
        }
    }

    function filterTable() {
        const keyword = document.getElementById("searchInput").value.toLowerCase();
        const startDate = document.getElementById("startDate").value;
        const endDate = document.getElementById("endDate").value;
        const rows = document.querySelectorAll("#orderTable tbody tr.main-row");

        rows.forEach(row => {
            const supplier = row.children[1].innerText.toLowerCase();
            const dateStr = row.children[2].innerText;
            const status = row.children[3].innerText.toLowerCase();
            const detail = document.getElementById("details-" + row.dataset.orderid);

            let visible = (supplier.includes(keyword) || status.includes(keyword));

            if (startDate && dateStr < startDate) visible = false;
            if (endDate && dateStr > endDate) visible = false;

            row.style.display = visible ? "" : "none";
            if (detail) detail.style.display = "none";
        });
    }

    function sortTable(columnIndex) {
        const table = document.getElementById("orderTable");
        let rows = Array.from(table.rows).slice(1); // 排除 thead

        rows = rows.filter(row => row.classList.contains("main-row")); // 主列排序
        const ascending = table.getAttribute("data-sort-dir") !== "asc";

        rows.sort((a, b) => {
            const valA = a.cells[columnIndex].innerText;
            const valB = b.cells[columnIndex].innerText;
            return ascending ? valA.localeCompare(valB) : valB.localeCompare(valA);
        });

        const tbody = table.tBodies[0];
        rows.forEach(row => {
            const detail = document.getElementById("details-" + row.dataset.orderid);
            tbody.appendChild(row);
            if (detail) tbody.appendChild(detail);
        });

        table.setAttribute("data-sort-dir", ascending ? "asc" : "desc");
    }
