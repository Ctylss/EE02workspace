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

@SuppressWarnings("serial")
@WebServlet("/FindFilesServlet")
public class FindFilesServlet extends HttpServlet {

    private MachineFilesService machineFilesService;

    @Override
    public void init() throws ServletException {
        machineFilesService = new MachineFilesService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 取得搜尋關鍵字
            String keyword = request.getParameter("keyword");
            String machineIdStr = request.getParameter("machineId");

            List<MachineFilesBean> files;

            // 根據不同條件取得檔案列表
            if (keyword != null && !keyword.trim().isEmpty()) {
                // 搜尋模式
                files = machineFilesService.searchFiles(keyword.trim());
                request.setAttribute("keyword", keyword);
                request.setAttribute("searchMode", true);
            } else if (machineIdStr != null && !machineIdStr.trim().isEmpty()) {
                // 依機台篩選
                try {
                    int machineId = Integer.parseInt(machineIdStr);
                    files = machineFilesService.getFilesByMachineId(machineId);
                    request.setAttribute("selectedMachineId", machineId);
                } catch (NumberFormatException e) {
                    files = machineFilesService.getFilesWithMachineInfo();
                }
            } else {
                // 顯示所有檔案
                files = machineFilesService.getFilesWithMachineInfo();
            }

            // 設定屬性
            request.setAttribute("files", files);
            request.setAttribute("totalFiles", files.size());

            // 成功或錯誤訊息
            String message = request.getParameter("message");
            String error = request.getParameter("error");
            if (message != null) {
                request.setAttribute("successMessage", message);
            }
            if (error != null) {
                request.setAttribute("errorMessage", error);
            }

            // 轉發到檔案列表頁面
            request.getRequestDispatcher("/JSP/cy/files/fileList.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "載入檔案列表時發生錯誤：" + e.getMessage());
            request.getRequestDispatcher("/JSP/cy/files/fileList.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // POST 請求用於搜尋
        String keyword = request.getParameter("keyword");
        String machineId = request.getParameter("machineId");

        // 重定向到 GET 請求
        StringBuilder redirectUrl = new StringBuilder("");
        boolean hasParam = false;

        if (keyword != null && !keyword.trim().isEmpty()) {
            redirectUrl.append("?keyword=").append(java.net.URLEncoder.encode(keyword.trim(), "UTF-8"));
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

        response.sendRedirect(request.getContextPath() + "/FindFilesServlet");
    }
}