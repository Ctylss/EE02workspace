package com.machine.Servlet.files;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import com.machine.Bean.MachineFilesBean;
import com.machine.Service.files.MachineFilesService;

@WebServlet("/DeleteFilesConfirmServlet")
public class DeleteFilesConfirmServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MachineFilesService machineFilesService = new MachineFilesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String fileIdStr = request.getParameter("fileId");
        if (fileIdStr == null || fileIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/FileManagementServlet?error=" + 
                java.net.URLEncoder.encode("缺少檔案ID", "UTF-8"));
            return;
        }

        try {
            int fileId = Integer.parseInt(fileIdStr.trim());
            MachineFilesBean file = machineFilesService.getFileById(fileId);
            if (file == null) {
                response.sendRedirect(request.getContextPath() + "/FileManagementServlet?error=" + 
                    java.net.URLEncoder.encode("找不到該檔案", "UTF-8"));
                return;
            }
            request.setAttribute("file", file);
            request.getRequestDispatcher("/JSP/cy/files/fileDeleteConfirm.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/FileManagementServlet?error=" + 
                java.net.URLEncoder.encode("檔案ID格式錯誤", "UTF-8"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 不允許 POST 進入此 Servlet 執行刪除，直接跳轉
        response.sendRedirect(request.getContextPath() + "/FileManagementServlet");
    }
}
