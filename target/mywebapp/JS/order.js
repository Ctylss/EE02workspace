/**
 * 
 */

        function addRow() {
            const table = document.getElementById("detailTable").getElementsByTagName('tbody')[0];
            const rowCount = table.rows.length;
            const row = table.insertRow();

            const cell0 = row.insertCell(0);
            const cell1 = row.insertCell(1);
            const cell2 = row.insertCell(2);
            const cell3 = row.insertCell(3);
            const cell4 = row.insertCell(4);
            const cell5 = row.insertCell(5);

            cell0.textContent = rowCount + 1;
            cell1.innerHTML = `<select name="materialId[]" class="material-select">` + materialOptionsHTML + `</select>`;
            cell2.innerHTML = '<input type="number" name="quantity[]" class="qty" oninput="updateTotal()">';
            cell3.innerHTML = '<input type="number" name="unitPrice[]" class="price" oninput="updateTotal()">';
            cell4.className = "subtotal";
            cell4.textContent = '0';
            cell5.innerHTML = '<button class="delete-btn" onclick="deleteRow(this)">刪除</button>';

            updateTotal();
        }

        function deleteRow(button) {
            const row = button.parentElement.parentElement;
            row.remove();
            updateTotal();
            updateRowNumbers();
        }

        function validateForm() {
            const orderDate = document.getElementById("orderDate").value;
            if (!orderDate) {
                alert("請選擇訂單日期");
                document.getElementById("orderDate").focus();
                return false; // 阻止表單送出
            }
            return true;
        }
        
        function updateRowNumbers() {
            const rows = document.querySelectorAll("#detailTable tbody tr");
            rows.forEach((row, index) => {
                row.cells[0].textContent = index + 1;
            });
        }

        function updateTotal() {
            const rows = document.querySelectorAll("#detailTable tbody tr");
            let total = 0;

            rows.forEach(row => {
				const qtyInput = row.querySelector('.qty');
				const qty = parseFloat(qtyInput ? qtyInput.value : 0);

				const priceInput = row.querySelector('.price');
				const price = parseFloat(priceInput ? priceInput.value : 0);

                const subtotal = qty * price;
                row.querySelector('.subtotal').textContent = subtotal;
                total += subtotal;
            });

            document.getElementById("totalAmount").textContent = "總金額：$" + total;
        }

        document.querySelectorAll('.qty, .price').forEach(input => {
            input.addEventListener('input', updateTotal);
        });
  