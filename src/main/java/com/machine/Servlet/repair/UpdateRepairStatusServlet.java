package com.machine.Servlet.repair;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.machine.Service.repair.MachineRepairService;

@WebServlet("/UpdateRepairStatusServlet")
public class UpdateRepairStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MachineRepairService machineRepairService = new MachineRepairService();

    public UpdateRepairStatusServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String repairIdStr = request.getParameter("repairId");
        String newStatus = request.getParameter("newStatus"); // 統一用 newStatus

        if (repairIdStr == null || repairIdStr.trim().isEmpty() ||
                newStatus == null || newStatus.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必填欄位不可為空白");
            return;
        }

        try {
            int repairId = Integer.parseInt(repairIdStr); // 修正變數名稱
            machineRepairService.updateRepairStatus(repairId, newStatus);
            response.sendRedirect("AdminRepairListServlet");

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "更新失敗");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
