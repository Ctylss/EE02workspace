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

@WebServlet("/SearchRepairByMachineServlet")
public class SearchRepairByMachineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MachineRepairService machineRepairService = new MachineRepairService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String machineIdStr = request.getParameter("machineId");

        if (machineIdStr == null || machineIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "請輸入機台ID");
            return;
        }

        try {
            int machineId = Integer.parseInt(machineIdStr.trim());
            List<MachineRepairBean> repairList = machineRepairService.findRepairsByMachineId(machineId);

            request.setAttribute("repairList", repairList);
            request.setAttribute("searchType", "按機台查詢");
            request.setAttribute("searchValue", "機台ID: " + machineId);

            request.getRequestDispatcher("/JSP/cy/repair/adminRepairList.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "機台ID必須為數字");
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