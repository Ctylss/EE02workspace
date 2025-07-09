package com.machine.Servlet.files;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.machine.Bean.MachineFilesBean;
import com.machine.Service.files.MachineFilesService;

@WebServlet("/UpdateFilesServlet")
public class UpdateFilesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 固定的雲端檔案存放路徑前綴
    private static final String CLOUD_STORAGE_PREFIX = "https://cloud-storage.example.com/machine-files/";

    private MachineFilesService machineFilesService = new MachineFilesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fileIdStr = request.getParameter("fileId");

        try {
            if (fileIdStr == null || fileIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("檔案 ID 不能為空");
            }

            int fileId = Integer.parseInt(fileIdStr.trim());
            MachineFilesBean file = machineFilesService.getFileById(fileId);

            if (file == null) {
                throw new RuntimeException("找不到指定的檔案記錄");
            }

            request.setAttribute("file", file);
            request.getRequestDispatcher("/JSP/cy/files/fileUpdate.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            redirectWithError(response, request.getContextPath(), "檔案 ID 格式錯誤");
        } catch (Exception e) {
            e.printStackTrace();
            redirectWithError(response, request.getContextPath(),
                    "載入檔案資料時發生錯誤：" + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fileIdStr = request.getParameter("fileId");
        String fileName = request.getParameter("fileName");
        String machineIdStr = request.getParameter("machineId");
        String keepOriginalPath = request.getParameter("keepOriginalPath");

        try {
            // 驗證必填欄位
            if (fileIdStr == null || fileIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("檔案 ID 不能為空");
            }

            if (fileName == null || fileName.trim().isEmpty()) {
                throw new IllegalArgumentException("檔案名稱不能為空");
            }

            if (machineIdStr == null || machineIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("機台 ID 不能為空");
            }

            int fileId = Integer.parseInt(fileIdStr.trim());
            int machineId = Integer.parseInt(machineIdStr.trim());

            // 取得原始檔案資料
            MachineFilesBean originalFile = machineFilesService.getFileById(fileId);
            if (originalFile == null) {
                throw new RuntimeException("找不到指定的檔案記錄");
            }

            String filePath;
            // 檢查是否需要更新檔案路徑
            if ("true".equals(keepOriginalPath) ||
                    (originalFile.getMachineId() == machineId &&
                            originalFile.getFileName().equals(fileName.trim()))) {
                // 保持原路徑
                filePath = originalFile.getFilePath();
            } else {
                // 生成新的雲端路徑
                filePath = generateCloudFilePath(machineId, fileName.trim());
            }

            boolean success = machineFilesService.updateFile(fileId, fileName.trim(), filePath, machineId);

            if (success) {
                String message = "檔案更新成功！";
                if (!filePath.equals(originalFile.getFilePath())) {
                    message += "新路徑：" + filePath;
                }
                response.sendRedirect(request.getContextPath() +
                        "/FileManagementServlet?message=" +
                        java.net.URLEncoder.encode(message, "UTF-8"));
            } else {
                throw new RuntimeException("檔案更新失敗，請稍後再試");
            }

        } catch (NumberFormatException e) {
            handleUpdateError(request, response, fileIdStr, "ID 格式錯誤");
        } catch (IllegalArgumentException e) {
            handleUpdateError(request, response, fileIdStr, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            handleUpdateError(request, response, fileIdStr,
                    "更新檔案時發生錯誤：" + e.getMessage());
        }
    }

    /**
     * 生成雲端檔案路徑
     */
    private String generateCloudFilePath(int machineId, String fileName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return CLOUD_STORAGE_PREFIX + "machine-" + machineId + "/" +
                timestamp + "_" + fileName;
    }

    /**
     * 重定向並顯示錯誤訊息
     */
    private void redirectWithError(HttpServletResponse response, String contextPath, String errorMessage)
            throws IOException {
        response.sendRedirect(contextPath + "/FileManagementServlet?error=" +
                java.net.URLEncoder.encode(errorMessage, "UTF-8"));
    }

    /**
     * 處理更新錯誤
     */
    private void handleUpdateError(HttpServletRequest request, HttpServletResponse response,
            String fileIdStr, String errorMessage)
            throws ServletException, IOException {

        try {
            // 嘗試重新載入檔案資料
            if (fileIdStr != null && !fileIdStr.trim().isEmpty()) {
                int fileId = Integer.parseInt(fileIdStr.trim());
                MachineFilesBean file = machineFilesService.getFileById(fileId);
                if (file != null) {
                    request.setAttribute("file", file);
                }
            }
        } catch (Exception e) {
            // 如果無法載入，就直接重定向
            redirectWithError(response, request.getContextPath(), errorMessage);
            return;
        }

        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/JSP/cy/files/fileUpdate.jsp")
                .forward(request, response);
    }
}