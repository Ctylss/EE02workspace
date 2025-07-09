package com.machine.Servlet.maintenance;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AdminInsertMaintenanceServlet")
public class AdminInsertMaintenanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AdminInsertMaintenanceServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 嘗試轉發到正確的 JSP 路徑
            request.getRequestDispatcher("/JSP/cy/maintenance/maintenanceInsert.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            // 如果上面的路徑不存在，嘗試根目錄
            try {
                request.getRequestDispatcher("/maintenanceInsert.jsp")
                        .forward(request, response);
            } catch (Exception e2) {
                // 如果都找不到，提供錯誤訊息
                response.getWriter().println("<h2>找不到新增表單頁面</h2>");
                response.getWriter().println("<p>請檢查 maintenanceInsert.jsp 是否存在於以下位置之一：</p>");
                response.getWriter().println("<ul>");
                response.getWriter().println("<li>/JSP/cy/maintenance/maintenanceInsert.jsp</li>");
                response.getWriter().println("<li>/maintenanceInsert.jsp</li>");
                response.getWriter().println("</ul>");
                response.getWriter()
                        .println("<p><a href='" + request.getContextPath() + "/AdminMaintenanceServlet'>返回後台</a></p>");
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}