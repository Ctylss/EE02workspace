package com.machine.Servlet.repair;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import com.machine.Bean.MachineRepairBean;
import com.machine.Service.repair.MachineRepairService;

@WebServlet("/InsertRepairServlet")
public class InsertRepairServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MachineRepairService machineRepairService = new MachineRepairService();

	public InsertRepairServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, NumberFormatException {
		String machineIdStr = request.getParameter("machineId");
		String repairDescription = request.getParameter("repairDescription");
		String employeeIdStr = request.getParameter("employeeId");

		if (machineIdStr == null || machineIdStr.trim().isEmpty() || repairDescription == null
				|| repairDescription.trim().isEmpty() || employeeIdStr == null || employeeIdStr.trim().isEmpty()) {
			// 參數空白，回傳錯誤訊息
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必填欄位不可為空白");
			return;
		}

		int machineId = Integer.parseInt(machineIdStr.trim());
		int employeeId = Integer.parseInt(employeeIdStr.trim());
		String repairStatus = "待處理";
		LocalDateTime now = LocalDateTime.now();
		MachineRepairBean repair = new MachineRepairBean(machineId, repairDescription, now, repairStatus, employeeId);
		try {
			machineRepairService.insertRepair(repair);
			request.getRequestDispatcher("/JSP/cy/repair/repairSuccess.jsp").forward(request, response);

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "資料插入失敗：" + e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
