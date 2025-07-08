package com.machine.Servlet.maintenance;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.machine.Service.maintenance.MachineMaintenanceService;

@WebServlet("/DeleteMaintenanceServlet")
public class DeleteMaintenanceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  private MachineMaintenanceService maintenanceService = new MachineMaintenanceService();
    public DeleteMaintenanceServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  try {
	            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
	            maintenanceService.deleteMaintenance(scheduleId);
	            response.sendRedirect(request.getContextPath() + "/AdminMaintenanceServlet");
	        } catch (Exception e) {
	            e.printStackTrace();
	            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "刪除失敗：" + e.getMessage());
	        }
	    }
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
