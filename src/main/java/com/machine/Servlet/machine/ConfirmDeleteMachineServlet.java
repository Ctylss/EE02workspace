package com.machine.Servlet.machine;

import java.io.IOException;
import com.machine.Service.machine.MachinesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ConfirmDeleteMachineServlet")
public class ConfirmDeleteMachineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MachinesService machinesService = new MachinesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idStr = request.getParameter("machineId");
        
        try {
            int machineId = Integer.parseInt(idStr);
            machinesService.deleteMachine(machineId);
            
            // 刪除成功，重導向到後台頁面並顯示成功訊息
            response.sendRedirect(request.getContextPath() + "/backstage?success=delete");
            
        } catch (IllegalArgumentException e) {
            // 找不到資料或業務邏輯錯誤，回到後台並顯示錯誤
            response.sendRedirect(request.getContextPath() + "/backstage?error=" + 
                                java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (IllegalStateException e) {
            // 狀態錯誤（如運行中的機台不能刪除）
            response.sendRedirect(request.getContextPath() + "/backstage?error=" + 
                                java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            // 系統錯誤
            response.sendRedirect(request.getContextPath() + "/backstage?error=" + 
                                java.net.URLEncoder.encode("系統錯誤，請稍後再試", "UTF-8"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}