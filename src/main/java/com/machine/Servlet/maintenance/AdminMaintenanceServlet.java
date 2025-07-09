package com.machine.Servlet.maintenance;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.machine.Bean.MachineMaintenanceBean;
import com.machine.Service.maintenance.MachineMaintenanceService;

@WebServlet("/AdminMaintenanceServlet")
public class AdminMaintenanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MachineMaintenanceService maintenanceService = new MachineMaintenanceService();

    public AdminMaintenanceServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<MachineMaintenanceBean> maintenanceList = null;

        try {
            // 取得所有保養資料
            maintenanceList = maintenanceService.findAllMaintenances();

            // 設置到 request attribute
            request.setAttribute("maintenanceList", maintenanceList);

            // 轉發到後台管理頁面 (adminDashboard.jsp)
            request.getRequestDispatcher("/JSP/cy/maintenance/adminDashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "載入保養資料失敗：" + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}