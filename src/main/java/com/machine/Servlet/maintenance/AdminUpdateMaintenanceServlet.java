package com.machine.Servlet.maintenance;

import com.machine.Bean.MachineMaintenanceBean;
import com.machine.Service.maintenance.MachineMaintenanceService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/AdminUpdateMaintenanceServlet")
public class AdminUpdateMaintenanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MachineMaintenanceService maintenanceService = new MachineMaintenanceService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 取得 URL 中的 scheduleId 參數
            String scheduleIdStr = request.getParameter("scheduleId");

            if (scheduleIdStr == null || scheduleIdStr.isEmpty()) {
                response.sendRedirect("AdminMaintenanceServlet");
                return;
            }

            // 驗證 ID 格式並查詢資料
            int scheduleId = Integer.parseInt(scheduleIdStr);
            MachineMaintenanceBean maintenance = maintenanceService.findMaintenanceDetailById(scheduleId);

            if (maintenance == null) {
                request.setAttribute("error", "查無該筆保養資料");
                request.getRequestDispatcher("/JSP/cy/error.jsp").forward(request, response);
                return;
            }

            // 傳遞完整資料到 JSP 用於預填表單
            request.setAttribute("maintenance", maintenance);

            // 轉發到 JSP 頁面
            request.getRequestDispatcher("/JSP/cy/maintenance/UpdateMaintenance.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("AdminMaintenanceServlet");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "讀取資料失敗: " + e.getMessage());
            request.getRequestDispatcher("/JSP/cy/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}