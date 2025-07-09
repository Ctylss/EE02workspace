package com.machine.Servlet.machine;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.machine.Bean.MachinesBean;
import com.machine.Service.machine.MachinesService;

@WebServlet("/GoUpdateMachineServlet")
public class GoUpdateMachineServlet extends HttpServlet {
    private MachinesService machinesService = new MachinesService();
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int machineId = Integer.parseInt(request.getParameter("machineId"));
            MachinesBean machine = machinesService.findMachineById(machineId);

            if (machine == null) {
                // 找不到資料就直接回到後台頁面
                response.sendRedirect(request.getContextPath() + "/backstage");
                return;
            }

            System.out.println("machineId=" + request.getParameter("machineId"));
            request.setAttribute("machine", machine);
            request.getRequestDispatcher("/JSP/cy/machine/updateMachine.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // 發生錯誤也回到後台頁面
            response.sendRedirect(request.getContextPath() + "/backstage");
        }
    }
}