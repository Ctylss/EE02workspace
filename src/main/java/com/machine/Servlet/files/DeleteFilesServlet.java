package com.machine.Servlet.files;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;

import com.machine.Service.files.MachineFilesService;

@WebServlet("/DeleteFilesServlet")
public class DeleteFilesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DeleteFilesServlet() {
        super();
    }

    private MachineFilesService machineFilesService = new MachineFilesService();

    // FindFilesServlet.java 範例 (簡化)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String jspPath = "/JSP/cy/files/fileList.jsp"; // 或是從資料庫拿的路徑

        ServletContext context = getServletContext();
        String realPath = context.getRealPath(jspPath);

        if (realPath != null && new File(realPath).exists()) {
            request.getRequestDispatcher(jspPath).forward(request, response);
        } else {
            request.setAttribute("error", "找不到指定檔案頁面：" + jspPath);
            request.getRequestDispatcher("/JSP/cy/error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String fileIdStr = request.getParameter("fileId");

        try {
            int fileId = Integer.parseInt(fileIdStr);

            // 直接使用 Service 刪除，不驗證
            boolean success = machineFilesService.deleteFile(fileId);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/FindFilesServlet?message=" +
                        java.net.URLEncoder.encode("檔案刪除成功", "UTF-8"));
            } else {
                response.sendRedirect(request.getContextPath() + "/FindFilesServlet?error=" +
                        java.net.URLEncoder.encode("檔案刪除失敗", "UTF-8"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/FindFilesServlet?error=" +
                    java.net.URLEncoder.encode("刪除檔案時發生錯誤：" + e.getMessage(), "UTF-8"));
        }
    }
}