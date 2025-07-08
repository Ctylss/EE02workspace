package com.example.controller;

import com.example.model.ProductionSchedule;
import com.example.model.Product;
import com.example.model.WorkOrder;
import com.example.service.ProductionScheduleService;
import com.example.service.ProductService;
import com.example.service.WorkOrderService;
import com.example.exception.InvalidMaterialDataException;

import com.example.exception.ProductNotFoundException;
import com.example.exception.WorkOrderNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat; // 確保有這個導入
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder; // 導入 WebDataBinder
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter; // 導入 DateTimeFormatter
import java.time.format.DateTimeParseException; // 導入 DateTimeParseException
import java.util.List;
import java.util.Optional; // 導入 Optional
import java.util.HashMap; // 導入 HashMap
import java.util.Map; // 導入 Map
import java.util.stream.Collectors; // 導入 Collectors

import java.beans.PropertyEditorSupport; // 導入 PropertyEditorSupport

@Controller
@RequestMapping("/productionschedules")
public class ProductionScheduleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductionScheduleController.class);

    private final ProductionScheduleService productionScheduleService;
    private final ProductService productService;
    private final WorkOrderService workOrderService;

    // 定義用於 JSP 顯示的日期格式化器
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public ProductionScheduleController(ProductionScheduleService productionScheduleService,
                                        ProductService productService,
                                        WorkOrderService workOrderService) {
        this.productionScheduleService = productionScheduleService;
        this.productService = productService;
        this.workOrderService = workOrderService;
        LOGGER.info("ProductionScheduleController initialized.");
    }

    /**
     * 自定義 InitBinder，用於處理來自表單輸入的 String 到 LocalDateTime 的綁定。
     * 此方法將在處理此控制器接收的任何請求時調用，以初始化 WebDataBinder。
     * 它為 LocalDateTime 註冊一個自定義的 PropertyEditor。
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null); // 允許空字串綁定為 null
                    return;
                }
                try {
                    // 首先嘗試使用 DATE_FORMATTER (yyyy-MM-dd) 解析，假設時間為 00:00:00
                    setValue(LocalDateTime.parse(text + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                } catch (DateTimeParseException e) {
                    try {
                        // 如果失敗，嘗試使用 DATETIME_FORMATTER (yyyy-MM-dd HH:mm) 解析
                        setValue(LocalDateTime.parse(text, DATETIME_FORMATTER));
                    } catch (DateTimeParseException e2) {
                        // 如果兩者都失敗，則拋出 IllegalArgumentException
                        LOGGER.error("無法將日期字串 '{}' 解析為 LocalDateTime。", text, e2);
                        throw new IllegalArgumentException("無法解析日期/時間格式，請使用YYYY-MM-DD 或YYYY-MM-DD HH:MM 格式。", e2);
                    }
                }
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                // 當在表單中顯示時，特別是對於 input type="date"，我們需要YYYY-MM-dd 格式
                // 對於其他顯示，DATETIME_FORMATTER 是可以的。
                if (value instanceof LocalDateTime) {
                    // 檢查當前請求是否是 GET 請求，並且是為了顯示表單（例如 'new' 或 'edit' action）
                    // 這裡簡化處理，直接使用 DATE_FORMATTER，因為 input type="date" 不處理時間
                    return ((LocalDateTime) value).format(DATE_FORMATTER);
                }
                return (value != null ? ((LocalDateTime) value).format(DATETIME_FORMATTER) : "");
            }
        });
    }

    /**
     * 處理所有生產排程的列表顯示、新增表單、編輯表單和詳細檢視。
     *
     * @param action 請求動作 (list, new, edit, view, byStatus, byDate, byProduct, byWorkOrder)
     * @param id 排程ID (用於編輯、檢視、開始、完成、刪除)
     * @param status 狀態 (用於按狀態篩選)
     * @param scheduledDate 日期 (用於按日期篩選)
     * @param model Spring MVC Model
     * @param redirectAttributes 用於重定向時添加 flash 屬性
     * @return JSP 視圖名稱
     */
    @GetMapping
    public String handleGetRequests(@RequestParam(value = "action", defaultValue = "list") String action,
                                    @RequestParam(value = "id", required = false) Integer id,
                                    @RequestParam(value = "status", required = false) String status,
                                    @RequestParam(value = "scheduledDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate scheduledDate,
                                    @RequestParam(value = "productId", required = false) Integer productId,
                                    @RequestParam(value = "workOrderId", required = false) Integer workOrderId,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        LOGGER.info("Received GET request, action: {}", action);

        try {
            switch (action) {
                case "new":
                    return showNewProductionScheduleForm(model);
                case "edit":
                    return showEditProductionScheduleForm(id, model, redirectAttributes);
                case "view":
                    return viewProductionScheduleDetail(id, model, redirectAttributes);
                case "byStatus":
                    return listProductionSchedulesByStatus(status, model);
                case "byDate":
                    return listProductionSchedulesByDate(scheduledDate, model);
                case "byProduct":
                    return listProductionSchedulesByProductId(productId, model, redirectAttributes);
                case "byWorkOrder":
                    return listProductionSchedulesByWorkOrderId(workOrderId, model, redirectAttributes);
                case "list":
                default:
                    return listAllProductionSchedules(model);
            }
        } catch (Exception ex) { // 統一捕獲 Exception 以便處理所有可能的錯誤
            LOGGER.error("Error during GET request for action {}: {}", action, ex.getMessage(), ex);
            String errorMessage = "操作失敗：";
            if (ex instanceof ProductNotFoundException) {
                errorMessage += "生產排程未找到。";
            } else if (ex instanceof ProductNotFoundException) {
                errorMessage += "產品未找到。";
            } else if (ex instanceof WorkOrderNotFoundException) {
                errorMessage += "工單未找到。";
            } else if (ex instanceof IllegalArgumentException) {
                errorMessage += "請求參數無效：" + ex.getMessage();
            } else if (ex instanceof IllegalStateException) {
                errorMessage += "狀態不允許的操作：" + ex.getMessage();
            } else {
                errorMessage += "發生未知錯誤：" + ex.getMessage();
            }
            model.addAttribute("errorMessage", errorMessage);
            return "/error"; // 轉發到一個通用的錯誤頁面 (假設路徑為 /WEB-INF/views/JSP/yw/error.jsp)
        }
    }

    /**
     * 處理生產排程的新增和更新請求。
     *
     * @param action 請求動作 (add, update)
     * @param productionSchedule 從表單綁定到 ProductionSchedule 物件
     * @param redirectAttributes 用於重定向時添加 flash 屬性
     * @return 重定向到列表頁或錯誤頁
     */
    @PostMapping
    public String handlePostRequests(@RequestParam("action") String action,
                                     @ModelAttribute ProductionSchedule productionSchedule,
                                     RedirectAttributes redirectAttributes) {
        LOGGER.info("Received POST request, action: {}", action);

        try {
            // 驗證產品 ID
            if (productionSchedule.getProductId() == null || productionSchedule.getProductId() <= 0) {
                throw new IllegalArgumentException("產品ID無效，請選擇一個有效的產品。");
            }
            // 驗證工單 ID (如果提供)
            if (productionSchedule.getWorkOrderId() != null && productionSchedule.getWorkOrderId() <= 0) {
                throw new IllegalArgumentException("工單ID無效，如果提供，必須是正數。");
            }

            if ("add".equals(action)) {
                insertProductionSchedule(productionSchedule, redirectAttributes);
            } else if ("update".equals(action)) {
                updateProductionSchedule(productionSchedule, redirectAttributes);
            } else {
                LOGGER.warn("Unknown POST action: {}", action);
                redirectAttributes.addFlashAttribute("errorMessage", "未知的操作。");
            }
        } catch (Exception ex) { // 統一捕獲 Exception 以便處理所有可能的錯誤
            LOGGER.error("Error during POST request for action {}: {}", action, ex.getMessage(), ex);
            String errorMessage = "操作失敗：";
            if (ex instanceof InvalidMaterialDataException) {
                errorMessage += "提交的數據無效：" + ((InvalidMaterialDataException) ex).getErrors().values();
            } else if (ex instanceof ProductNotFoundException) {
                errorMessage += "產品不存在。";
            } else if (ex instanceof WorkOrderNotFoundException) {
                errorMessage += "工單不存在。";
            } else if (ex instanceof IllegalArgumentException) {
                errorMessage += "請求參數無效：" + ex.getMessage();
            } else if (ex instanceof IllegalStateException) {
                errorMessage += "狀態不允許的操作：" + ex.getMessage();
            } else {
                errorMessage += "發生未知錯誤：" + ex.getMessage();
            }

            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            // 回填表單數據和下拉選單，以便用戶可以在錯誤頁面重新編輯
            redirectAttributes.addFlashAttribute("productionSchedule", productionSchedule);
            redirectAttributes.addFlashAttribute("products", productService.getAllProducts());
            redirectAttributes.addFlashAttribute("workOrders", workOrderService.getAllWorkOrders());

            return "redirect:/productionschedules?action=" + ("add".equals(action) ? "new" : "edit") + (productionSchedule.getScheduleId() != null ? "&id=" + productionSchedule.getScheduleId() : "");
        }
        return "redirect:/productionschedules?action=list"; // 成功操作後重定向到列表頁
    }

    /**
     * 處理刪除生產排程的請求。
     *
     * @param id 排程ID
     * @param redirectAttributes 用於重定向時添加 flash 屬性
     * @return 重定向到列表頁
     */
    @GetMapping("/delete")
    public String deleteProductionSchedule(@RequestParam("id") Integer id,
                                           RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = productionScheduleService.deleteProductionSchedule(id);
            if (deleted) {
                LOGGER.info("Successfully deleted production schedule, ID: {}", id);
                redirectAttributes.addFlashAttribute("message", "生產排程已成功刪除。");
            } else {
                LOGGER.warn("Failed to delete production schedule or schedule not found, ID: {}", id);
                redirectAttributes.addFlashAttribute("errorMessage", "刪除生產排程失敗或排程不存在，ID: '" + id + "'。");
            }
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Invalid parameter while deleting schedule ID {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "參數錯誤，無法刪除排程: " + e.getMessage());
        } catch (Exception e) { // 捕獲其他潛在錯誤
            LOGGER.error("Error deleting production schedule ID {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "刪除生產排程時發生未知錯誤：" + e.getMessage());
        }
        return "redirect:/productionschedules?action=list";
    }

    /**
     * 處理開始生產排程的請求。
     *
     * @param id 排程ID
     * @param redirectAttributes 用於重定向時添加 flash 屬性
     * @return 重定向到排程詳細頁
     */
    @GetMapping("/start")
    public String startProductionSchedule(@RequestParam("id") Integer id,
                                          RedirectAttributes redirectAttributes) {
        try {
            // 直接使用 LocalDateTime.now() 獲取當前時間
            LocalDateTime actualStartDate = LocalDateTime.now();
            productionScheduleService.startProductionSchedule(id, actualStartDate); // 假設 service 期望 LocalDateTime
            LOGGER.info("Production schedule ID {} successfully started.", id);
            redirectAttributes.addFlashAttribute("message", "生產排程已成功開始生產。");
        } catch (ProductNotFoundException | IllegalStateException | IllegalArgumentException e) {
            LOGGER.warn("Cannot start production schedule ID {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error starting production schedule ID {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "開始生產排程時發生未知錯誤：" + e.getMessage());
        }
        return "redirect:/productionschedules?action=view&id=" + id;
    }

    /**
     * 處理完成生產排程的請求。
     *
     * @param id 排程ID
     * @param actualQuantity 實際生產數量
     * @param redirectAttributes 用於重定向時添加 flash 屬性
     * @return 重定向到排程詳細頁
     */
    @PostMapping("/complete")
    public String completeProductionSchedule(@RequestParam("id") Integer id,
                                             @RequestParam("actualQuantity") BigDecimal actualQuantity,
                                             RedirectAttributes redirectAttributes) {
        try {
            if (actualQuantity == null || actualQuantity.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("完成排程需要提供有效的實際生產數量 (不能為空或負數)。");
            }
            // 直接使用 LocalDateTime.now() 獲取當前時間
            LocalDateTime actualCompletionDate = LocalDateTime.now();
            productionScheduleService.completeProductionSchedule(id, actualQuantity, actualCompletionDate); // 假設 service 期望 LocalDateTime
            LOGGER.info("Production schedule ID {} successfully completed, actual quantity: {}", id, actualQuantity);
            redirectAttributes.addFlashAttribute("message", "生產排程已成功完成。");
        } catch (ProductNotFoundException | IllegalStateException | IllegalArgumentException e) {
            LOGGER.warn("Cannot complete production schedule ID {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error completing production schedule ID {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "完成生產排程時發生未知錯誤：" + e.getMessage());
        }
        return "redirect:/productionschedules?action=view&id=" + id;
    }

    /**
     * 從服務層獲取所有生產排程並轉發到 productionScheduleDetail.jsp。
     */
    private String listAllProductionSchedules(Model model) {
        List<ProductionSchedule> schedules = productionScheduleService.getAllProductionSchedules();
        LOGGER.info("Retrieved {} production schedules from service layer.", schedules != null ? schedules.size() : 0);
        model.addAttribute("productionSchedules", formatProductionSchedulesForJSP(schedules)); // 使用格式化方法
        model.addAttribute("action", "list");
        model.addAttribute("pageTitle", "所有生產排程");
        return "/productionScheduleDetail"; // CORRECTED: 確保 JSP 路徑正確
    }

    /**
     * 根據狀態從服務層獲取生產排程並轉發到 productionScheduleDetail.jsp。
     */
    private String listProductionSchedulesByStatus(String status, Model model) {
        if (status == null || status.trim().isEmpty()) {
            return listAllProductionSchedules(model); // 如果狀態為空，則顯示所有排程
        }
        List<ProductionSchedule> schedules = productionScheduleService.getProductionSchedulesByStatus(status);
        LOGGER.info("Retrieved {} production schedules with status '{}'.", schedules.size(), status);
        model.addAttribute("productionSchedules", formatProductionSchedulesForJSP(schedules)); // 使用格式化方法
        model.addAttribute("action", "byStatus");
        model.addAttribute("pageTitle", "狀態為 '" + status + "' 的生產排程");
        return "/productionScheduleDetail"; // CORRECTED: 確保 JSP 路徑正確
    }

    /**
     * 根據日期從服務層獲取生產排程並轉發到 productionScheduleDetail.jsp。
     */
    private String listProductionSchedulesByDate(LocalDate scheduledDate, Model model) {
        if (scheduledDate == null) {
            throw new IllegalArgumentException("排程日期不能為空。");
        }
        // 將 LocalDate 轉換為 LocalDateTime，例如當天的開始時間 (午夜)
        LocalDateTime startOfDay = scheduledDate.atStartOfDay();

        List<ProductionSchedule> schedules = productionScheduleService.getProductionSchedulesByDate(startOfDay);
        LOGGER.info("Retrieved {} production schedules for date '{}'.", schedules.size(), scheduledDate);
        model.addAttribute("productionSchedules", formatProductionSchedulesForJSP(schedules)); // 使用格式化方法
        model.addAttribute("action", "byDate");
        model.addAttribute("pageTitle", "日期為 '" + scheduledDate.format(DATE_FORMATTER) + "' 的生產排程");
        return "/productionScheduleDetail"; // CORRECTED: 確保 JSP 路徑正確
    }

    /**
     * 處理按產品ID篩選生產排程的請求。
     * @param productId 產品ID
     * @param model Spring's Model for passing data to the view.
     * @param redirectAttributes For adding flash attributes for redirect scenarios
     * @return Logical view name (JSP path).
     */
    private String listProductionSchedulesByProductId(Integer productId, Model model, RedirectAttributes redirectAttributes) {
        LOGGER.info("Received GET request to list production schedules by product ID: {}", productId);
        if (productId == null || productId <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "產品ID無效。");
            return "redirect:/productionschedules?action=list";
        }
        try {
            List<ProductionSchedule> schedules = productionScheduleService.getProductionSchedulesByProductId(productId);
            model.addAttribute("productionSchedules", formatProductionSchedulesForJSP(schedules));
            model.addAttribute("action", "byProduct");
            model.addAttribute("pageTitle", "產品ID為 '" + productId + "' 的生產排程");
            return "/productionScheduleDetail"; // CORRECTED: 確保 JSP 路徑正確
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "產品ID無效或不存在：" + productId);
            return "redirect:/productionschedules?action=list";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "請求參數無效：" + e.getMessage());
            return "redirect:/productionschedules?action=list";
        }
    }

    /**
     * 處理按工單ID篩選生產排程的請求。
     * @param workOrderId 工單ID
     * @param model Spring's Model for passing data to the view.
     * @param redirectAttributes For adding flash attributes for redirect scenarios
     * @return Logical view name (JSP path).
     */
    private String listProductionSchedulesByWorkOrderId(Integer workOrderId, Model model, RedirectAttributes redirectAttributes) {
        LOGGER.info("Received GET request to list production schedules by work order ID: {}", workOrderId);
        if (workOrderId == null || workOrderId <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "工單ID無效。");
            return "redirect:/productionschedules?action=list";
        }
        try {
            List<ProductionSchedule> schedules = productionScheduleService.getProductionSchedulesByWorkOrderId(workOrderId);
            model.addAttribute("productionSchedules", formatProductionSchedulesForJSP(schedules));
            model.addAttribute("action", "byWorkOrder");
            model.addAttribute("pageTitle", "工單ID為 '" + workOrderId + "' 的生產排程");
            return "/productionScheduleDetail"; // CORRECTED: 確保 JSP 路徑正確
        } catch (WorkOrderNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "工單ID無效或不存在：" + workOrderId);
            return "redirect:/productionschedules?action=list";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "請求參數無效：" + e.getMessage());
            return "redirect:/productionschedules?action=list";
        }
    }


    /**
     * 顯示新增生產排程的表單，並準備產品和工單列表供選擇。
     */
    private String showNewProductionScheduleForm(Model model) {
        populateDropdowns(model);
        model.addAttribute("productionSchedule", new ProductionSchedule()); // 提供一個空的 ProductionSchedule 物件給表單
        model.addAttribute("action", "new");
        model.addAttribute("pageTitle", "新增生產排程");
        return "/productionScheduleDetail"; // CORRECTED: 確保 JSP 路徑正確
    }

    /**
     * 顯示編輯現有生產排程的表單，並填充表單字段。
     */
    private String showEditProductionScheduleForm(Integer scheduleId, Model model, RedirectAttributes redirectAttributes) {
        if (scheduleId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "排程ID不能為空。");
            return "redirect:/productionschedules?action=list";
        }

        Optional<ProductionSchedule> scheduleOptional = productionScheduleService.getProductionScheduleById(scheduleId);
        if (scheduleOptional.isPresent()) {
            ProductionSchedule productionSchedule = scheduleOptional.get();
            populateDropdowns(model);
            model.addAttribute("productionSchedule", formatSingleProductionScheduleForJSP(productionSchedule)); // 使用格式化方法
            model.addAttribute("action", "edit");
            model.addAttribute("pageTitle", "編輯生產排程");
            LOGGER.info("Displaying edit form for production schedule ID: {}", scheduleId);
            return "/productionScheduleDetail"; // CORRECTED: 確保 JSP 路徑正確
        } else {
            LOGGER.warn("Production schedule to edit not found, ID: {}", scheduleId);
            redirectAttributes.addFlashAttribute("errorMessage", "要編輯的生產排程未找到。");
            return "redirect:/productionschedules?action=list";
        }
    }

    /**
     * 查看單個生產排程的詳細資訊。
     */
    private String viewProductionScheduleDetail(Integer scheduleId, Model model, RedirectAttributes redirectAttributes) {
        if (scheduleId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "排程ID不能為空。");
            return "redirect:/productionschedules?action=list";
        }

        Optional<ProductionSchedule> scheduleOptional = productionScheduleService.getProductionScheduleById(scheduleId);
        if (scheduleOptional.isPresent()) {
            ProductionSchedule productionSchedule = scheduleOptional.get();
            model.addAttribute("productionSchedule", formatSingleProductionScheduleForJSP(productionSchedule)); // 使用格式化方法
            model.addAttribute("action", "view");
            model.addAttribute("pageTitle", "生產排程詳情");
            LOGGER.info("Displaying details for production schedule ID: {}", scheduleId);
            return "/productionScheduleDetail"; // CORRECTED: 確保 JSP 路徑正確
        } else {
            LOGGER.warn("Production schedule to view not found, ID: {}", scheduleId);
            redirectAttributes.addFlashAttribute("errorMessage", "要查看的生產排程未找到。");
            return "redirect:/productionschedules?action=list";
        }
    }

    /**
     * 將新生產排程插入資料庫。
     */
    private void insertProductionSchedule(ProductionSchedule newSchedule, RedirectAttributes redirectAttributes) {
        // Spring MVC @ModelAttribute 會自動將請求參數綁定到 ProductionSchedule 物件
        // 在此處進行必要的業務邏輯驗證
        // validateProductionSchedule(newSchedule, true); // 驗證已在 Service 層處理
        productionScheduleService.createProductionSchedule(newSchedule);
        LOGGER.info("Successfully added new production schedule, ID: {}", newSchedule.getScheduleId());
        redirectAttributes.addFlashAttribute("message", "生產排程已成功新增。");
    }

    /**
     * 更新資料庫中的現有生產排程。
     */
    private void updateProductionSchedule(ProductionSchedule updatedSchedule, RedirectAttributes redirectAttributes) {
        // Spring MVC @ModelAttribute 會自動將請求參數綁定到 ProductionSchedule 物件
        // 在此處進行必要的業務邏輯驗證
        if (updatedSchedule.getScheduleId() == null) {
            throw new IllegalArgumentException("更新生產排程時，排程ID不能為空。");
        }
        // validateProductionSchedule(updatedSchedule, false); // 驗證已在 Service 層處理
        productionScheduleService.updateProductionSchedule(updatedSchedule);
        LOGGER.info("Successfully updated production schedule, ID: {}", updatedSchedule.getScheduleId());
        redirectAttributes.addFlashAttribute("message", "生產排程已成功更新。");
    }

    /**
     * 輔助方法：填充下拉選單所需的產品和工單列表。
     */
    private void populateDropdowns(Model model) {
        List<Product> products = productService.getAllProducts();
        List<WorkOrder> workOrders = workOrderService.getAllWorkOrders();
        model.addAttribute("products", products);
        model.addAttribute("workOrders", workOrders);
    }

    /**
     * 輔助方法：將 ProductionSchedule 物件列表中的 LocalDateTime 欄位格式化為 String，以便 JSP 顯示。
     * @param schedules 原始 ProductionSchedule 物件列表
     * @return 格式化後的 Map 列表
     */
    private List<Map<String, Object>> formatProductionSchedulesForJSP(List<ProductionSchedule> schedules) {
        return schedules.stream().map(this::formatSingleProductionScheduleForJSP).collect(Collectors.toList());
    }

    /**
     * 輔助方法：將單個 ProductionSchedule 物件中的 LocalDateTime 欄位格式化為 String，以便 JSP 顯示。
     * @param schedule 原始 ProductionSchedule 物件
     * @return 格式化後的 Map
     */
    private Map<String, Object> formatSingleProductionScheduleForJSP(ProductionSchedule schedule) {
        Map<String, Object> map = new HashMap<>();
        map.put("scheduleId", schedule.getScheduleId());
        map.put("scheduleNumber", schedule.getScheduleNumber());
        map.put("workOrderId", schedule.getWorkOrderId());
        map.put("workOrderNumber", schedule.getWorkOrderNumber()); // Transient field
        map.put("productId", schedule.getProductId());
        map.put("productCode", schedule.getProductCode()); // Transient field
        map.put("productName", schedule.getProductName()); // Transient field
        map.put("scheduledDate", schedule.getScheduledDate() != null ? schedule.getScheduledDate().format(DATE_FORMATTER) : null);
        map.put("shift", schedule.getShift());
        map.put("plannedQuantity", schedule.getPlannedQuantity());
        map.put("actualQuantity", schedule.getActualQuantity());
        map.put("status", schedule.getStatus());
        map.put("notes", schedule.getNotes());
        map.put("createDate", schedule.getCreateDate() != null ? schedule.getCreateDate().format(DATETIME_FORMATTER) : null);
        map.put("updateDate", schedule.getUpdateDate() != null ? schedule.getUpdateDate().format(DATETIME_FORMATTER) : null);
        map.put("actualStartDate", schedule.getActualStartDate() != null ? schedule.getActualStartDate().format(DATETIME_FORMATTER) : null);
        map.put("actualCompletionDate", schedule.getActualCompletionDate() != null ? schedule.getActualCompletionDate().format(DATETIME_FORMATTER) : null);
        return map;
    }
}
