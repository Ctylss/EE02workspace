package com.machine.Servlet.machine;

import com.machine.Bean.MachinesBean;
import com.machine.Service.machine.MachinesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/InsertMachineServlet")
public class InsertMachineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MachinesService machinesService = new MachinesService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("machineName");
        String serial = request.getParameter("serialNumber");
        String status = request.getParameter("mstatus");
        String location = request.getParameter("machineLocation");

        // 預設 forward 頁面是原表單頁面
        String view = "/JSP/cy/machine/insertMachine.jsp";

        try {
            MachinesBean machine = new MachinesBean(name, serial, status, location);
            machinesService.insertMachine(machine); // 含驗證

            // 如果成功，重導向到後台頁面
            response.sendRedirect(request.getContextPath() + "/backstage?success=insert");
            return; // 記得 return！不要再往下走
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "系統錯誤，請稍後再試");
        }

        // 如果發生錯誤，就 forward 回表單頁面，並帶錯誤訊息
        request.getRequestDispatcher(view).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // 所有邏輯集中在 doGet
    }
}