package com.machine.Servlet.machine;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.machine.Bean.MachinesBean;
import com.machine.Service.machine.MachinesService;

@WebServlet("/UpdateMachineServlet")
public class UpdateMachineServlet extends HttpServlet {
    private MachinesService machinesService = new MachinesService();
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int machineId = Integer.parseInt(request.getParameter("machineId"));
            String machineName = request.getParameter("machineName");
            String serialNumber = request.getParameter("serialNumber");
            String status = request.getParameter("mstatus"); // 與表單一致
            String location = request.getParameter("machineLocation"); // 與表單一致

            MachinesBean machine = new MachinesBean(machineId, machineName, serialNumber, status, location);
            machinesService.updateMachine(machine);

            // 更新成功後回到後台頁面
            response.sendRedirect(request.getContextPath() + "/backstage?success=update");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/JSP/cy/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "系統錯誤，請稍後再試");
            request.getRequestDispatcher("/JSP/cy/error.jsp").forward(request, response);
        }
    }
}