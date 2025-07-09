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

@WebServlet("/AdminRepairListServlet")
public class AdminRepairListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MachineRepairService machineRepairService = new MachineRepairService();

    public AdminRepairListServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 查詢所有報修記錄
            List<MachineRepairBean> repairList = machineRepairService.machineRepairView();

            // 傳遞資料給 JSP
            request.setAttribute("repairList", repairList);

            // 轉發到後台管理頁面
            request.getRequestDispatcher("/JSP/cy/repair/adminRepairList.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "載入資料失敗：" + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}