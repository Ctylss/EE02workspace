package com.machine.Servlet.repair;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.machine.Bean.MachineRepairBean;
import com.machine.Service.repair.MachineRepairService;

@WebServlet("/RepairDetailServlet")
public class RepairDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MachineRepairService machineRepairService = new MachineRepairService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idStr = request.getParameter("repairId");
		if (idStr == null || idStr.trim().isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少 repairId");
			return;
		}

		try {
			int repairId = Integer.parseInt(idStr);
			MachineRepairBean repair = machineRepairService.findRepairById(repairId);

			if (repair == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "找不到報修資料");
				return;
			}

			request.setAttribute("repair", repair);
			request.getRequestDispatcher("/JSP/cy/repair/repairDetail.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "系統錯誤：" + e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
