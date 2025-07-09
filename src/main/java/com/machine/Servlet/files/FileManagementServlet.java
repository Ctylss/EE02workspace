package com.machine.Servlet.files;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.machine.Bean.MachineFilesBean;
import com.machine.Service.files.MachineFilesService;

@WebServlet("/FileManagementServlet")
public class FileManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MachineFilesService machineFilesService = new MachineFilesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 處理搜尋參數
            String keyword = request.getParameter("keyword");
            String machineIdStr = request.getParameter("machineId");

            List<MachineFilesBean> files;

            // 根據搜尋條件取得檔案列表
            if (keyword != null && !keyword.trim().isEmpty()) {
                files = machineFilesService.searchFiles(keyword.trim());
                request.setAttribute("keyword", keyword);
                request.setAttribute("searchMode", true);
            } else if (machineIdStr != null && !machineIdStr.trim().isEmpty()) {
                try {
                    int machineId = Integer.parseInt(machineIdStr);
                    files = machineFilesService.getFilesByMachineId(machineId);
                    request.setAttribute("selectedMachineId", machineId);
                } catch (NumberFormatException e) {
                    files = machineFilesService.getFilesWithMachineInfo();
                }
            } else {
                files = machineFilesService.getFilesWithMachineInfo();
            }

            // 設定檔案列表和統計資訊
            request.setAttribute("files", files);
            request.setAttribute("totalFiles", files.size());

            // 處理成功/錯誤訊息
            String message = request.getParameter("message");
            String error = request.getParameter("error");
            if (message != null) {
                request.setAttribute("successMessage", message);
            }
            if (error != null) {
                request.setAttribute("errorMessage", error);
            }

            // 轉發到檔案管理頁面
            request.getRequestDispatcher("/JSP/cy/files/fileManagement.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "載入檔案資料時發生錯誤：" + e.getMessage());
            request.getRequestDispatcher("/JSP/cy/files/fileManagement.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // POST 用於搜尋，重定向到 GET
        String keyword = request.getParameter("keyword");
        String machineId = request.getParameter("machineId");

        StringBuilder redirectUrl = new StringBuilder(request.getContextPath() + "/FileManagementServlet");
        boolean hasParam = false;

        if (keyword != null && !keyword.trim().isEmpty()) {
            redirectUrl.append("?keyword=")
                    .append(java.net.URLEncoder.encode(keyword.trim(), "UTF-8"));
            hasParam = true;
        }

        if (machineId != null && !machineId.trim().isEmpty()) {
            if (hasParam) {
                redirectUrl.append("&");
            } else {
                redirectUrl.append("?");
            }
            redirectUrl.append("machineId=").append(machineId);
        }

        response.sendRedirect(redirectUrl.toString());
    }
}