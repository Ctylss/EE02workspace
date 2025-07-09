package com.machine.Servlet.repair;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.machine.Bean.MachineRepairBean;
import com.machine.Service.repair.MachineRepairService;

@WebServlet("/SearchRepairByStatusServlet")
public class SearchRepairByStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MachineRepairService machineRepairService = new MachineRepairService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String status = request.getParameter("status");

        if (status == null || status.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "請選擇狀態");
            return;
        }

        try {
            List<MachineRepairBean> repairList = machineRepairService.findRepairsByStatus(status);

            request.setAttribute("repairList", repairList);
            request.setAttribute("searchType", "按狀態篩選");
            request.setAttribute("searchValue", "狀態: " + status);

            request.getRequestDispatcher("/JSP/cy/repair/adminRepairList.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "查詢失敗：" + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}