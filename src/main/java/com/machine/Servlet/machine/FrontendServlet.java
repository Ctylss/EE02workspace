package com.machine.Servlet.machine;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.machine.Bean.MachinesBean;
import com.machine.Service.machine.MachinesService;

@WebServlet("/frontend")
public class FrontendServlet extends HttpServlet {
    private MachinesService machinesService = new MachinesService();
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // 取得所有機台資料
            List<MachinesBean> machinesList = machinesService.findAllMachines();
            
            // 前台篩選 (只有查看功能)
            String searchKeyword = request.getParameter("search");
            String statusFilter = request.getParameter("statusFilter");
            
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String keyword = searchKeyword.trim().toLowerCase();
                machinesList = machinesList.stream()
                    .filter(m -> 
                        m.getMachineName().toLowerCase().contains(keyword) ||
                        m.getSerialNumber().toLowerCase().contains(keyword)
                    )
                    .collect(Collectors.toList());
            }
            
            if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                machinesList = machinesList.stream()
                    .filter(m -> m.getMstatus().equals(statusFilter))
                    .collect(Collectors.toList());
            }
            
            // 將資料傳遞給前台 JSP
            request.setAttribute("machines", machinesList);
            
            // Forward 到前台頁面
            request.getRequestDispatcher("/JSP/machine/frontend.jsp")
                   .forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "載入機台資料時發生錯誤：" + e.getMessage());
            request.getRequestDispatcher("/JSP/machine/frontend.jsp")
                   .forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}