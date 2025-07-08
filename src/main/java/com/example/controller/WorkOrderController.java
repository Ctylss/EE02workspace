package com.example.controller;

import com.example.model.WorkOrder;
import com.example.model.Product;
import com.example.service.WorkOrderService;
import com.example.service.ProductService;
import com.example.exception.InvalidWorkOrderDataException;
import com.example.exception.WorkOrderNotFoundException;
import com.example.exception.ProductNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder; // Import WebDataBinder
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException; // Import for parsing exceptions
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// For custom property editor
import java.beans.PropertyEditorSupport; // Import PropertyEditorSupport

@Controller
@RequestMapping("/workorders") // Maps all requests starting with /workorders to this controller
public class WorkOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkOrderController.class);

    private final WorkOrderService workOrderService;
    private final ProductService productService;

    // Define a formatter for dates to be displayed in JSP
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Autowired // Spring automatically injects service instances
    public WorkOrderController(WorkOrderService workOrderService, ProductService productService) {
        this.workOrderService = workOrderService;
        this.productService = productService;
        LOGGER.info("WorkOrderController initialized.");
    }

    /**
     * Custom InitBinder to handle LocalDateTime binding from String form inputs.
     * This method will be called to initialize WebDataBinder for any request
     * coming into this controller.
     * It registers a custom PropertyEditor for LocalDateTime.
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Register a custom editor for LocalDateTime
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null); // Allow null values for empty strings
                    return;
                }
                try {
                    // Attempt to parse with DATE_FORMATTER (yyyy-MM-dd) first, assuming time is 00:00
                    setValue(LocalDateTime.parse(text + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                } catch (DateTimeParseException e) {
                    try {
                        // If that fails, try parsing with DATETIME_FORMATTER (yyyy-MM-dd HH:mm)
                        setValue(LocalDateTime.parse(text, DATETIME_FORMATTER));
                    } catch (DateTimeParseException e2) {
                        // If both fail, throw an IllegalArgumentException
                        LOGGER.error("Failed to parse date string '{}' into LocalDateTime.", text, e2);
                        throw new IllegalArgumentException("無法解析日期/時間格式，請使用 YYYY-MM-DD 或 YYYY-MM-DD HH:MM 格式。", e2);
                    }
                }
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return (value != null ? ((LocalDateTime) value).format(DATETIME_FORMATTER) : "");
            }
        });
    }


    /**
     * Handles various GET requests for WorkOrder operations (list, new, edit, view, delete, byStatus).
     * @param action The requested action (default "list")
     * @param id WorkOrder ID or Product ID depending on action
     * @param model Spring's Model for passing data to the view
     * @param redirectAttributes For adding flash attributes for redirect scenarios
     * @return Logical view name (JSP path) or redirect URL
     */
    @GetMapping // Handles GET requests to /workorders or /workorders?action=...
    public String handleGetRequests(@RequestParam(value = "action", defaultValue = "list") String action,
                                    @RequestParam(value = "id", required = false) Integer id,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        LOGGER.info("Received GET request, action: {}", action);

        try {
            switch (action) {
                case "new":
                    return showNewWorkOrderForm(model);
                case "edit":
                    return showEditWorkOrderForm(id, model, redirectAttributes);
                case "view":
                    return viewWorkOrderDetail(id, model, redirectAttributes);
                case "delete":
                    return deleteWorkOrder(id, redirectAttributes);
                case "start":
                    return startWorkOrder(id, redirectAttributes);
                case "complete":
                    return completeWorkOrder(id, redirectAttributes);
                case "byStatus":
                    // This case is handled by the @GetMapping(params = "status") method
                    // If it reaches here, it means 'action=byStatus' was present but 'status' param might be missing or empty.
                    // We'll let the listAllWorkOrders handle the default display.
                    // However, the specific @GetMapping(params="status") will catch it first.
                    break;
                case "list":
                default:
                    return listAllWorkOrders(model);
            }
        } catch (Exception ex) {
            LOGGER.error("Error during GET request for action {}: {}", action, ex.getMessage(), ex);
            String errorMessage = "操作失敗：";
            if (ex instanceof WorkOrderNotFoundException) {
                errorMessage += "工單未找到。";
            } else if (ex instanceof ProductNotFoundException) {
                errorMessage += "產品未找到。";
            } else if (ex instanceof IllegalArgumentException) {
                errorMessage += "請求參數無效：" + ex.getMessage();
            } else if (ex instanceof IllegalStateException) {
                errorMessage += "狀態不允許的操作：" + ex.getMessage();
            } else {
                errorMessage += "發生未知錯誤：" + ex.getMessage();
            }
            model.addAttribute("errorMessage", errorMessage);
            return "error"; // Assuming a generic error.jsp exists
        }
        return "redirect:/workorders?action=list"; // Fallback redirect in case of unexpected flow
    }

    /**
     * Handles POST requests for adding or updating WorkOrder items.
     * @param action The requested action ("add" or "update")
     * @param workOrder The WorkOrder object bound from form parameters
     * @param redirectAttributes For adding flash attributes for redirect scenarios
     * @param model Model for potential error forwarding
     * @return Redirect URL
     */
    @PostMapping // Handles POST requests to /workorders
    public String handlePostRequests(@RequestParam("action") String action,
                                     @ModelAttribute WorkOrder workOrder, // Spring automatically binds form data to this object
                                     @RequestParam(value = "productId", required = false) Integer productId, // Make productId optional
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        LOGGER.info("Received POST request, action: {}", action);

        try {
            // Handle productId from form submission
            if (productId == null || productId <= 0) {
                // This handles the case where "--請選擇產品--" is selected or productId is otherwise invalid/missing.
                // It will prevent the ProductNotFoundException or other issues down the line.
                throw new IllegalArgumentException("產品ID無效，請選擇一個有效的產品。");
            }

            // Manually set the Product on the WorkOrder object based on productId
            Product product = productService.getProductById(productId)
                                            .orElseThrow(() -> new ProductNotFoundException(productId));
            workOrder.setProduct(product); // Set the fetched Product object

            if ("add".equals(action)) {
                insertWorkOrder(workOrder, redirectAttributes);
            } else if ("update".equals(action)) {
                updateWorkOrder(workOrder, redirectAttributes);
            } else {
                LOGGER.warn("Unknown POST action: {}", action);
                redirectAttributes.addFlashAttribute("errorMessage", "未知的操作。");
                return "redirect:/workorders?action=list";
            }
        } catch (Exception ex) {
            LOGGER.error("Error during POST request for action {}: {}", action, ex.getMessage(), ex);
            String errorMessage = "操作失敗：";
            if (ex instanceof InvalidWorkOrderDataException) {
                errorMessage += "提交的數據無效：" + ((InvalidWorkOrderDataException) ex).getErrors();
            } else if (ex instanceof ProductNotFoundException) {
                errorMessage += "產品不存在。";
            } else if (ex instanceof IllegalArgumentException) { // Catch the new IllegalArgumentException for productId
                errorMessage += "請求參數無效：" + ex.getMessage();
            } else {
                errorMessage += "發生未知錯誤：" + ex.getMessage();
            }

            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            // Ensure workOrder and products are available for the redirect target (new/edit form)
            redirectAttributes.addFlashAttribute("workOrder", workOrder);
            // Re-populate dropdowns for the error view via flash attributes
            List<Product> productsForDropdown = productService.getAllProducts();
            redirectAttributes.addFlashAttribute("products", productsForDropdown);

            return "redirect:/workorders?action=" + ("add".equals(action) ? "new" : "edit");
        }
        return "redirect:/workorders?action=list"; // Redirect to list on successful operation
    }

    /**
     * Handles GET request for filtering work orders by status.
     * @param status The status to filter by. Can be empty for "All Statuses".
     * @param model Spring's Model for passing data to the view.
     * @return Logical view name (JSP path).
     */
    @GetMapping(params = "status") // Handles GET requests to /workorders?status=...
    public String listWorkOrdersByStatus(@RequestParam(value = "status", required = false) String status, Model model) {
        LOGGER.info("Received GET request to list work orders by status: {}", status);
        List<WorkOrder> workOrders;

        // If status is null or empty, fetch all work orders
        if (status == null || status.trim().isEmpty()) {
            LOGGER.info("Status parameter is empty or null, fetching all work orders.");
            workOrders = workOrderService.getAllWorkOrders();
            model.addAttribute("pageTitle", "所有生產工單");
        } else {
            // Otherwise, filter by the provided status
            workOrders = workOrderService.getWorkOrdersByStatus(status);
            model.addAttribute("pageTitle", "狀態為 '" + status + "' 的工單");
        }
        
        // Convert LocalDateTime to String for JSP display
        List<Map<String, Object>> formattedWorkOrders = workOrders.stream().map(wo -> {
            Map<String, Object> map = new HashMap<>();
            map.put("workOrderId", wo.getWorkOrderId());
            map.put("workOrderNumber", wo.getWorkOrderNumber());
            map.put("product", wo.getProduct()); // Keep product object for nested access
            map.put("quantity", wo.getQuantity());
            map.put("unit", wo.getUnit());
            map.put("scheduledStartDate", wo.getScheduledStartDate() != null ? wo.getScheduledStartDate().format(DATE_FORMATTER) : null);
            map.put("scheduledDueDate", wo.getScheduledDueDate() != null ? wo.getScheduledDueDate().format(DATE_FORMATTER) : null);
            map.put("actualStartDate", wo.getActualStartDate() != null ? wo.getActualStartDate().format(DATETIME_FORMATTER) : null);
            map.put("actualCompletionDate", wo.getActualCompletionDate() != null ? wo.getActualCompletionDate().format(DATETIME_FORMATTER) : null);
            map.put("status", wo.getStatus());
            map.put("notes", wo.getNotes());
            map.put("createDate", wo.getCreateDate() != null ? wo.getCreateDate().format(DATETIME_FORMATTER) : null);
            map.put("updateDate", wo.getUpdateDate() != null ? wo.getUpdateDate().format(DATETIME_FORMATTER) : null);
            return map;
        }).collect(Collectors.toList());

        model.addAttribute("workOrders", formattedWorkOrders);
        model.addAttribute("action", "byStatus");
        return "/workOrderList";
    }

    // --- Private Helper Methods ---

    /**
     * Retrieves all WorkOrder items and prepares the model for workOrderList.jsp.
     */
    private String listAllWorkOrders(Model model) {
        List<WorkOrder> workOrders = workOrderService.getAllWorkOrders();
        LOGGER.info("Retrieved {} work orders from Service layer.", workOrders != null ? workOrders.size() : 0);
        
        // Convert LocalDateTime to String for JSP display
        List<Map<String, Object>> formattedWorkOrders = workOrders.stream().map(wo -> {
            Map<String, Object> map = new HashMap<>();
            map.put("workOrderId", wo.getWorkOrderId());
            map.put("workOrderNumber", wo.getWorkOrderNumber());
            map.put("product", wo.getProduct()); // Keep product object for nested access
            map.put("quantity", wo.getQuantity());
            map.put("unit", wo.getUnit());
            map.put("scheduledStartDate", wo.getScheduledStartDate() != null ? wo.getScheduledStartDate().format(DATE_FORMATTER) : null);
            map.put("scheduledDueDate", wo.getScheduledDueDate() != null ? wo.getScheduledDueDate().format(DATE_FORMATTER) : null);
            map.put("actualStartDate", wo.getActualStartDate() != null ? wo.getActualStartDate().format(DATETIME_FORMATTER) : null);
            map.put("actualCompletionDate", wo.getActualCompletionDate() != null ? wo.getActualCompletionDate().format(DATETIME_FORMATTER) : null);
            map.put("status", wo.getStatus());
            map.put("notes", wo.getNotes());
            map.put("createDate", wo.getCreateDate() != null ? wo.getCreateDate().format(DATETIME_FORMATTER) : null);
            map.put("updateDate", wo.getUpdateDate() != null ? wo.getUpdateDate().format(DATETIME_FORMATTER) : null);
            return map;
        }).collect(Collectors.toList());

        model.addAttribute("workOrders", formattedWorkOrders);
        model.addAttribute("action", "list"); // Set action for JSP to render list view
        model.addAttribute("pageTitle", "所有生產工單");
        return "/workOrderList";
    }

    /**
     * Displays the form for adding a new WorkOrder item, populating product dropdowns.
     */
    private String showNewWorkOrderForm(Model model) {
        populateDropdowns(model); // Helper to fetch dropdown data
        // For new form, provide an empty WorkOrder object. Dates will be null.
        model.addAttribute("workOrder", new WorkOrder());
        model.addAttribute("action", "new");
        model.addAttribute("pageTitle", "新增生產工單");
        return "/workOrderList";
    }

    /**
     * Displays the form for editing an existing WorkOrder item, populating form fields and dropdowns.
     */
    private String showEditWorkOrderForm(Integer workOrderId, Model model, RedirectAttributes redirectAttributes) {
        if (workOrderId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "工單ID不能為空。");
            return "redirect:/workorders?action=list";
        }

        Optional<WorkOrder> workOrderOptional = workOrderService.getWorkOrderById(workOrderId);
        if (workOrderOptional.isPresent()) {
            WorkOrder workOrder = workOrderOptional.get();
            populateDropdowns(model); // Fetch all products for dropdowns
            
            // Convert LocalDateTime to String for JSP form display
            Map<String, Object> formattedWorkOrder = new HashMap<>();
            formattedWorkOrder.put("workOrderId", workOrder.getWorkOrderId());
            formattedWorkOrder.put("workOrderNumber", workOrder.getWorkOrderNumber());
            formattedWorkOrder.put("product", workOrder.getProduct()); // Keep product object for nested access
            formattedWorkOrder.put("quantity", workOrder.getQuantity());
            formattedWorkOrder.put("unit", workOrder.getUnit());
            formattedWorkOrder.put("scheduledStartDate", workOrder.getScheduledStartDate() != null ? workOrder.getScheduledStartDate().format(DATE_FORMATTER) : null);
            formattedWorkOrder.put("scheduledDueDate", workOrder.getScheduledDueDate() != null ? workOrder.getScheduledDueDate().format(DATE_FORMATTER) : null);
            formattedWorkOrder.put("actualStartDate", workOrder.getActualStartDate() != null ? workOrder.getActualStartDate().format(DATETIME_FORMATTER) : null);
            formattedWorkOrder.put("actualCompletionDate", workOrder.getActualCompletionDate() != null ? workOrder.getActualCompletionDate().format(DATETIME_FORMATTER) : null);
            formattedWorkOrder.put("status", workOrder.getStatus());
            formattedWorkOrder.put("notes", workOrder.getNotes());
            formattedWorkOrder.put("createDate", workOrder.getCreateDate() != null ? workOrder.getCreateDate().format(DATETIME_FORMATTER) : null);
            formattedWorkOrder.put("updateDate", workOrder.getUpdateDate() != null ? workOrder.getUpdateDate().format(DATETIME_FORMATTER) : null);

            model.addAttribute("workOrder", formattedWorkOrder);
            model.addAttribute("action", "edit");
            model.addAttribute("pageTitle", "編輯生產工單");
            LOGGER.info("Displaying edit work order form for WorkOrder ID: {}", workOrderId);
            return "/workOrderList";
        } else {
            LOGGER.warn("Work order to edit not found, ID: {}", workOrderId);
            redirectAttributes.addFlashAttribute("errorMessage", "生產工單未找到，ID: " + workOrderId);
            return "redirect:/workorders?action=list";
        }
    }

    /**
     * Views details of a single WorkOrder item.
     */
    private String viewWorkOrderDetail(Integer workOrderId, Model model, RedirectAttributes redirectAttributes) {
        if (workOrderId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "工單ID不能為空。");
            return "redirect:/workorders?action=list";
        }

        Optional<WorkOrder> workOrderOptional = workOrderService.getWorkOrderById(workOrderId);
        if (workOrderOptional.isPresent()) {
            WorkOrder workOrder = workOrderOptional.get();
            // Convert LocalDateTime to String for JSP display
            Map<String, Object> formattedWorkOrder = new HashMap<>();
            formattedWorkOrder.put("workOrderId", workOrder.getWorkOrderId());
            formattedWorkOrder.put("workOrderNumber", workOrder.getWorkOrderNumber());
            formattedWorkOrder.put("product", workOrder.getProduct()); // Keep product object for nested access
            formattedWorkOrder.put("quantity", workOrder.getQuantity());
            formattedWorkOrder.put("unit", workOrder.getUnit());
            formattedWorkOrder.put("scheduledStartDate", workOrder.getScheduledStartDate() != null ? workOrder.getScheduledStartDate().format(DATE_FORMATTER) : null);
            formattedWorkOrder.put("scheduledDueDate", workOrder.getScheduledDueDate() != null ? workOrder.getScheduledDueDate().format(DATE_FORMATTER) : null);
            formattedWorkOrder.put("actualStartDate", workOrder.getActualStartDate() != null ? workOrder.getActualStartDate().format(DATETIME_FORMATTER) : null);
            formattedWorkOrder.put("actualCompletionDate", workOrder.getActualCompletionDate() != null ? workOrder.getActualCompletionDate().format(DATETIME_FORMATTER) : null);
            formattedWorkOrder.put("status", workOrder.getStatus());
            formattedWorkOrder.put("notes", workOrder.getNotes());
            formattedWorkOrder.put("createDate", workOrder.getCreateDate() != null ? workOrder.getCreateDate().format(DATETIME_FORMATTER) : null);
            formattedWorkOrder.put("updateDate", workOrder.getUpdateDate() != null ? workOrder.getUpdateDate().format(DATETIME_FORMATTER) : null);

            model.addAttribute("workOrder", formattedWorkOrder);
            model.addAttribute("action", "view");
            model.addAttribute("pageTitle", "生產工單詳情");
            LOGGER.info("Displaying work order detail for WorkOrder ID: {}", workOrderId);
            return "/workOrderList";
        } else {
            LOGGER.warn("Work order to view not found, ID: {}", workOrderId);
            redirectAttributes.addFlashAttribute("errorMessage", "生產工單未找到，ID: " + workOrderId);
            return "redirect:/workorders?action=list";
        }
    }

    /**
     * Inserts a new WorkOrder item into the database.
     */
    private void insertWorkOrder(WorkOrder newWorkOrder, RedirectAttributes redirectAttributes) {
        workOrderService.createWorkOrder(newWorkOrder);
        LOGGER.info("Successfully added work order: Number {}", newWorkOrder.getWorkOrderNumber());
        redirectAttributes.addFlashAttribute("message", "工單已成功新增。");
    }

    /**
     * Updates an existing WorkOrder item in the database.
     */
    private void updateWorkOrder(WorkOrder updatedWorkOrder, RedirectAttributes redirectAttributes) {
        if (updatedWorkOrder.getWorkOrderId() == null) {
            throw new IllegalArgumentException("更新工單時，工單ID不能為空。");
        }
        workOrderService.updateWorkOrder(updatedWorkOrder);
        LOGGER.info("Successfully updated work order, ID: {}", updatedWorkOrder.getWorkOrderId());
        redirectAttributes.addFlashAttribute("message", "工單已成功更新。");
    }

    /**
     * Deletes a WorkOrder item from the database.
     */
    private String deleteWorkOrder(Integer workOrderId, RedirectAttributes redirectAttributes) {
        if (workOrderId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "工單ID不能為空。");
            return "redirect:/workorders?action=list";
        }

        workOrderService.deleteWorkOrder(workOrderId);
        LOGGER.info("Successfully deleted work order, ID: {}", workOrderId);
        redirectAttributes.addFlashAttribute("message", "工單已成功刪除。");
        return "redirect:/workorders?action=list";
    }

    /**
     * Starts a WorkOrder.
     */
    private String startWorkOrder(Integer workOrderId, RedirectAttributes redirectAttributes) {
        try {
            workOrderService.startWorkOrder(workOrderId, LocalDateTime.now());
            redirectAttributes.addFlashAttribute("message", "工單已成功啟動。");
        } catch (WorkOrderNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "啟動工單失敗：" + e.getMessage());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "啟動工單失敗：" + e.getMessage());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "啟動工單失敗：" + e.getMessage());
        }
        return "redirect:/workorders?action=view&id=" + workOrderId;
    }

    /**
     * Completes a WorkOrder.
     */
    private String completeWorkOrder(Integer workOrderId, RedirectAttributes redirectAttributes) {
        try {
            workOrderService.completeWorkOrder(workOrderId, LocalDateTime.now());
            redirectAttributes.addFlashAttribute("message", "工單已成功完成。");
        } catch (WorkOrderNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "完成工單失敗：" + e.getMessage());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "完成工單失敗：" + e.getMessage());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "完成工單失敗：" + e.getMessage());
        }
        return "redirect:/workorders?action=view&id=" + workOrderId;
    }

    /**
     * Helper method to populate dropdown lists (Products) for the form.
     */
    private void populateDropdowns(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
    }
}
