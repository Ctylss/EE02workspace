package com.machine.Servlet.maintenance;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

import com.machine.Bean.MachineMaintenanceBean;
import com.machine.Service.maintenance.MachineMaintenanceService;

@WebServlet("/InsertMaintenance")
public class InsertMaintenance extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MachineMaintenanceService maintenanceService = new MachineMaintenanceService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String machineIdStr = request.getParameter("machineId");
        String maintenanceDescription = request.getParameter("maintenanceDescription");
        String maintenanceStatus = "待處理"; // 預設狀態
        String employeeIdStr = request.getParameter("employeeId");

        // 驗證必填欄位
        if (machineIdStr == null || machineIdStr.trim().isEmpty() ||
                maintenanceDescription == null || maintenanceDescription.trim().isEmpty() ||
                employeeIdStr == null || employeeIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必填欄位不可為空白");
            return;
        }

        try {
            int machineId = Integer.parseInt(machineIdStr.trim());
            int employeeId = Integer.parseInt(employeeIdStr.trim());
            LocalDateTime now = LocalDateTime.now();

            MachineMaintenanceBean maintenance = new MachineMaintenanceBean(machineId, now, maintenanceDescription,
                    maintenanceStatus, employeeId);
            maintenanceService.insertMaintenance(maintenance);

            // 導向成功頁面
            request.getRequestDispatcher("/JSP/cy/maintenance/maintenanceSucceed.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "數字欄位格式錯誤");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "資料插入失敗：" + e.getMessage());
        }
    }
}
