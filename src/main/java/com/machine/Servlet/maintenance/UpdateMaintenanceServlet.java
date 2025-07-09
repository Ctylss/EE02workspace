package com.machine.Servlet.maintenance;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.machine.Bean.MachineMaintenanceBean;
import com.machine.Service.maintenance.MachineMaintenanceService;

@WebServlet("/UpdateMaintenanceServlet")
public class UpdateMaintenanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MachineMaintenanceService maintenanceService = new MachineMaintenanceService();

    public UpdateMaintenanceServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. 取得參數並轉型
            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
            int machineId = Integer.parseInt(request.getParameter("machineId"));
            String description = request.getParameter("maintenanceDescription");
            String status = request.getParameter("maintenanceStatus");
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));

            // 2. 封裝 Bean
            MachineMaintenanceBean maintenance = new MachineMaintenanceBean(scheduleId, machineId, description, status,
                    employeeId);

            // 3. 呼叫 Service 更新
            maintenanceService.updateMaintenance(maintenance);

            // 4. 更新成功，跳轉到成功頁面
            request.setAttribute("maintenance", maintenance);
            request.getRequestDispatcher("/JSP/cy/maintenance/maintenanceUpdateSucceed.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "資料格式錯誤");
            request.getRequestDispatcher("/JSP/cy/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "更新失敗：" + e.getMessage());
            request.getRequestDispatcher("/JSP/cy/error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}