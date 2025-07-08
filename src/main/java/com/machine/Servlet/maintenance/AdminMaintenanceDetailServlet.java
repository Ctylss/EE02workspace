package com.machine.Servlet.maintenance;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.machine.Bean.MachineMaintenanceJoinBean;
import com.machine.Service.maintenance.MachineMaintenanceService;


@WebServlet("/AdminMaintenanceDetailServlet")
public class AdminMaintenanceDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private MachineMaintenanceService  maintenanceService=new MachineMaintenanceService(); 
    public AdminMaintenanceDetailServlet() {
        super();
    }

	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String scheduleIdStr = request.getParameter("scheduleId");
        if (scheduleIdStr == null || scheduleIdStr.isEmpty()) {
            response.sendRedirect("AdminMaintenanceServlet");
            return;
        }

        try {
            int scheduleId = Integer.parseInt(scheduleIdStr);
            MachineMaintenanceJoinBean maintenance = maintenanceService.findMaintenanceDetailById(scheduleId);
            if (maintenance == null) {
                request.setAttribute("error", "查無該筆保養資料");
                return;
            }
            request.setAttribute("maintenance", maintenance);
            request.getRequestDispatcher("/JSP/maintenance/adminMaintenanceDetail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("FindAllMachineMaintenance");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "系統錯誤：" + e.getMessage());
            request.getRequestDispatcher("/JSP/error.jsp").forward(request, response);
        }
    }
		
	
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
