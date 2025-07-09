package com.machine.Servlet.maintenance;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.machine.Bean.MachineMaintenanceBean;
import com.machine.Service.maintenance.MachineMaintenanceService;

@WebServlet("/DeleteMaintenanceConfirmServlet")
public class DeleteMaintenanceConfirmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeleteMaintenanceConfirmServlet() {
		super();
	}

	private MachineMaintenanceService maintenanceService = new MachineMaintenanceService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
			MachineMaintenanceBean maintenance = maintenanceService.findMaintenanceById(scheduleId);
			if (maintenance == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "找不到該保養資料");
				return;
			}
			request.setAttribute("maintenance", maintenance);
			request.getRequestDispatcher("/JSP/cy/maintenance/deleteConfirm.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(500, "伺服器錯誤");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
