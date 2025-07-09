package com.machine.Servlet.files;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import com.machine.Bean.MachineFilesBean;
import com.machine.Service.files.MachineFilesService;

@WebServlet("/InsertFilesServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 50, // 50MB
        maxRequestSize = 1024 * 1024 * 100 // 100MB
)
public class InsertFilesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MachineFilesService machineFilesService = new MachineFilesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 顯示新增檔案頁面
        request.getRequestDispatcher("/JSP/cy/files/fileInsert.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fileName = request.getParameter("fileName");
        String machineIdStr = request.getParameter("machineId");

        try {
            // 驗證文字欄位
            if (fileName == null || fileName.trim().isEmpty()) {
                throw new IllegalArgumentException("檔案名稱不能為空");
            }

            if (machineIdStr == null || machineIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("機台 ID 不能為空");
            }

            int machineId = Integer.parseInt(machineIdStr.trim());

            // 取得上傳檔案 Part
            Part filePart = request.getPart("fileUpload");
            if (filePart == null || filePart.getSize() == 0) {
                throw new IllegalArgumentException("請選擇要上傳的檔案");
            }

            // 取得檔案原始名稱（只檔名，不含路徑）
            String submittedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            // 定義本機儲存路徑（可依需要調整）
            String baseUploadPath = getServletContext().getRealPath("/uploaded_files/machine-" + machineId);

            File uploadDir = new File(baseUploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 檔名前加上 timestamp 避免重複
            String storedFileName = System.currentTimeMillis() + "_" + submittedFileName;

            File file = new File(uploadDir, storedFileName);

            // 儲存檔案
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, file.toPath());
            }

            // 檔案在伺服器的相對路徑，存進資料庫
            String filePath = "uploaded_files/machine-" + machineId + "/" + storedFileName;

            // 建立檔案物件並設定欄位
            MachineFilesBean fileBean = new MachineFilesBean();
            fileBean.setFileName(fileName.trim());
            fileBean.setFilePath(filePath);
            fileBean.setMachineId(machineId);
            fileBean.setUploadTime(LocalDateTime.now());

            // 呼叫 Service 層新增資料庫紀錄
            boolean success = machineFilesService.addFile(fileBean);

            if (success) {
                // 新增成功，轉到檔案列表頁，帶成功訊息
                response.sendRedirect(request.getContextPath() +
                        "/FindFilesServlet?message=" +
                        java.net.URLEncoder.encode("檔案新增成功！", "UTF-8"));
            } else {
                throw new RuntimeException("檔案新增失敗，請稍後再試");
            }

        } catch (NumberFormatException e) {
            handleError(request, response, "機台 ID 格式錯誤，請輸入有效的數字", fileName, machineIdStr);
        } catch (IllegalArgumentException e) {
            handleError(request, response, e.getMessage(), fileName, machineIdStr);
        } catch (RuntimeException e) {
            handleError(request, response, e.getMessage(), fileName, machineIdStr);
        } catch (Exception e) {
            e.printStackTrace();
            handleError(request, response, "新增檔案時發生未預期的錯誤：" + e.getMessage(), fileName, machineIdStr);
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response,
            String errorMessage, String fileName, String machineId)
            throws ServletException, IOException {

        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("fileName", fileName);
        request.setAttribute("machineId", machineId);
        request.getRequestDispatcher("/JSP/cy/files/fileInsert.jsp").forward(request, response);
    }
}
