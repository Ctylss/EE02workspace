package com.example.controller;

import com.example.model.BillOfMaterial;
import com.example.model.Product;
import com.example.model.Material;
import com.example.service.BillOfMaterialService;
import com.example.service.MaterialService;
import com.example.service.ProductService;
import com.example.exception.BillOfMaterialNotFoundException; // 引入自定義異常
import com.example.exception.InvalidBillOfMaterialDataException;
import com.example.exception.ProductNotFoundException;
import com.example.exception.MaterialNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
// import java.sql.SQLException; // 不再直接拋出 SQLException
import java.util.List;
import java.util.Optional; // 引入 Optional

@Controller
@RequestMapping("/bom") // Maps all requests starting with /bom to this controller
public class BillOfMaterialController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillOfMaterialController.class);

    private final BillOfMaterialService bomService;
    private final ProductService productService;
    private final MaterialService materialService;

    @Autowired // Spring automatically injects service instances
    public BillOfMaterialController(BillOfMaterialService bomService,
                                    ProductService productService,
                                    MaterialService materialService) {
        this.bomService = bomService;
        this.productService = productService;
        this.materialService = materialService;
        LOGGER.info("BillOfMaterialController initialized.");
    }

    /**
     * Handles various GET requests for BOM operations (list, new, edit, view, delete, byProduct).
     * @param action The requested action (default "list")
     * @param id BOM ID or Product ID depending on action
     * @param model Spring's Model for passing data to the view
     * @param redirectAttributes For adding flash attributes for redirect scenarios
     * @return Logical view name (JSP path) or redirect URL
     */
    @GetMapping // Handles GET requests to /bom or /bom?action=...
    public String handleGetRequests(@RequestParam(value = "action", defaultValue = "list") String action,
                                    @RequestParam(value = "id", required = false) Integer id,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        LOGGER.info("Received GET request, action: {}", action);

        try {
            switch (action) {
                case "new":
                    return showNewBomForm(model);
                case "edit":
                    return showEditBomForm(id, model, redirectAttributes);
                case "view":
                    return viewBomDetail(id, model, redirectAttributes);
                case "delete": // Deleting via GET is generally discouraged, but we'll implement for direct translation
                    return deleteBom(id, redirectAttributes);
                case "byProduct":
                    return listBomsByProduct(id, model, redirectAttributes);
                case "list":
                default:
                    return listAllBoms(model);
            }
        } catch (Exception ex) { // 捕獲更通用的 Exception 或特定的自定義異常
            LOGGER.error("Error during GET request for action {}: {}", action, ex.getMessage(), ex);
            String errorMessage = "操作失敗：";
            if (ex instanceof ProductNotFoundException) {
                errorMessage += "產品不存在。";
            } else if (ex instanceof MaterialNotFoundException) {
                errorMessage += "物料不存在。";
            } else if (ex instanceof BillOfMaterialNotFoundException) {
                errorMessage += "用料清單項目不存在。";
            } else if (ex instanceof IllegalArgumentException) {
                errorMessage += "請求參數無效：" + ex.getMessage();
            } else {
                errorMessage += "發生未知錯誤：" + ex.getMessage();
            }
            model.addAttribute("errorMessage", errorMessage);
            return "error"; // Assuming a generic error.jsp exists
        }
    }

    /**
     * Handles POST requests for adding or updating BOM items.
     * @param action The requested action ("add" or "update")
     * @param bomId The BOM ID (for update, optional)
     * @param productId The Product ID from the form
     * @param materialId The Material ID from the form
     * @param quantity The Quantity from the form
     * @param redirectAttributes For adding flash attributes for redirect scenarios
     * @param model Model for potential error forwarding
     * @return Redirect URL
     */
    @PostMapping // Handles POST requests to /bom
    public String handlePostRequests(@RequestParam("action") String action,
                                     @RequestParam(value = "bomId", required = false) Integer bomId, // For update
                                     @RequestParam("productId") Integer productId,
                                     @RequestParam("materialId") Integer materialId,
                                     @RequestParam("quantity") BigDecimal quantity,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        LOGGER.info("Received POST request, action: {}", action);

        BillOfMaterial bom = new BillOfMaterial();
        bom.setBomId(bomId); // Set BOM ID for update
        bom.setQuantity(quantity);

        try {
            // 根據 ID 查找 Product 和 Material 實體
            Product product = productService.getProductById(productId)
                                            .orElseThrow(() -> new ProductNotFoundException(productId));
            Material material = materialService.getMaterialById(materialId)
                                              .orElseThrow(() -> new MaterialNotFoundException(materialId));

            bom.setProduct(product);
            bom.setMaterial(material);

            if ("add".equals(action)) {
                insertBom(bom, redirectAttributes);
            } else if ("update".equals(action)) {
                updateBom(bom, redirectAttributes);
            } else {
                LOGGER.warn("Unknown POST action: {}", action);
                redirectAttributes.addFlashAttribute("errorMessage", "未知的操作。");
                return "redirect:/bom?action=list"; // Fallback to list
            }
        } catch (Exception ex) { // 捕獲更通用的 Exception 或特定的自定義異常
            LOGGER.error("Error during POST request for action {}: {}", action, ex.getMessage(), ex);
            String errorMessage = "操作失敗：";
            if (ex instanceof InvalidBillOfMaterialDataException) {
                errorMessage += "提交的數據無效：" + ((InvalidBillOfMaterialDataException) ex).getErrors();
            } else if (ex instanceof ProductNotFoundException) {
                errorMessage += "產品不存在。";
            } else if (ex instanceof MaterialNotFoundException) {
                errorMessage += "物料不存在。";
            } else if (ex instanceof BillOfMaterialNotFoundException) {
                errorMessage += "用料清單項目不存在。";
            } else if (ex instanceof IllegalArgumentException) {
                errorMessage += "請求參數無效：" + ex.getMessage();
            } else {
                errorMessage += "發生未知錯誤：" + ex.getMessage();
            }

            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            // Re-populate form data for error redirect
            redirectAttributes.addFlashAttribute("bom", bom); // 傳回完整的 bom 物件
            redirectAttributes.addFlashAttribute("productId", productId);
            redirectAttributes.addFlashAttribute("materialId", materialId);
            redirectAttributes.addFlashAttribute("quantity", quantity);
            try {
                populateDropdowns(model); // Re-populate dropdowns for the error view
            } catch (Exception e) { // 這裡也捕獲 Exception
                LOGGER.error("Error populating dropdowns for error page: {}", e.getMessage(), e);
                // 不再覆蓋主錯誤訊息，而是追加
                redirectAttributes.addFlashAttribute("errorMessage", redirectAttributes.getFlashAttributes().get("errorMessage") + " (無法載入產品或物料列表)");
            }
            redirectAttributes.addFlashAttribute("products", model.getAttribute("products"));
            redirectAttributes.addFlashAttribute("materials", model.getAttribute("materials"));

            return "redirect:/bom?action=" + ("add".equals(action) ? "new" : "edit");
        }
        return "redirect:/bom?action=list"; // Redirect to list on successful operation
    }

    // --- Private Helper Methods ---

    /**
     * Retrieves all BOM items and prepares the model for bomList.jsp.
     */
    private String listAllBoms(Model model) { // 移除 throws SQLException
        List<BillOfMaterial> boms = bomService.getAllBillOfMaterials();
        LOGGER.info("Retrieved {} BOM items from Service layer.", boms != null ? boms.size() : 0);
        model.addAttribute("boms", boms);
        model.addAttribute("action", "list"); // Set action for JSP to render list view
        model.addAttribute("pageTitle", "所有產品用料清單");
        return "production/bomList"; // Path to your JSP
    }

    /**
     * Retrieves BOM items for a specific product and prepares the model for bomList.jsp.
     */
    private String listBomsByProduct(Integer productId, Model model, RedirectAttributes redirectAttributes) { // 移除 throws SQLException, IllegalArgumentException
        if (productId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "產品ID不能為空。");
            return "redirect:/bom?action=list"; // Redirect if ID is missing
        }

        Product product = productService.getProductById(productId).orElse(null);
        if (product == null) {
            LOGGER.warn("Cannot list BOM for non-existent product ID: {}", productId);
            redirectAttributes.addFlashAttribute("errorMessage", "指定的產品不存在，無法查看其用料清單。");
            return "redirect:/bom?action=list"; // Fallback to list all BOMs
        }

        List<BillOfMaterial> boms = bomService.getBillOfMaterialsByProductId(productId);
        LOGGER.info("Retrieved {} BOM items for product ID {}.", boms.size(), productId);

        model.addAttribute("boms", boms);
        model.addAttribute("action", "listByProduct");
        model.addAttribute("currentProductId", productId);
        model.addAttribute("productName", product.getProductName());
        model.addAttribute("pageTitle", "產品 '" + product.getProductName() + "' 的用料清單");
        return "production/bomList";
    }

    /**
     * Displays the form for adding a new BOM item, populating product and material dropdowns.
     */
    private String showNewBomForm(Model model) { // 移除 throws SQLException
        populateDropdowns(model); // Helper to fetch dropdown data
        model.addAttribute("bom", new BillOfMaterial()); // Provide an empty BOM object for the form
        model.addAttribute("action", "new");
        model.addAttribute("pageTitle", "新增產品用料");
        return "production/bomList";
    }

    /**
     * Displays the form for editing an existing BOM item, populating form fields and dropdowns.
     */
    private String showEditBomForm(Integer bomId, Model model, RedirectAttributes redirectAttributes) { // 移除 throws SQLException, IllegalArgumentException
        if (bomId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "BOM ID不能為空。");
            return "redirect:/bom?action=list";
        }

        BillOfMaterial bom = bomService.getBillOfMaterialById(bomId).orElse(null);
        if (bom != null) {
            populateDropdowns(model); // Fetch all products and materials for dropdowns
            model.addAttribute("bom", bom);
            model.addAttribute("action", "edit");
            model.addAttribute("pageTitle", "編輯產品用料");
            LOGGER.info("Displaying edit BOM form for BOM ID: {}", bomId);
            return "production/bomList";
        } else {
            LOGGER.warn("BOM item to edit not found, ID: {}", bomId);
            redirectAttributes.addFlashAttribute("errorMessage", "產品用料項目未找到，ID: " + bomId);
            return "redirect:/bom?action=list"; // Redirect back to list
        }
    }

    /**
     * Views details of a single BOM item.
     */
    private String viewBomDetail(Integer bomId, Model model, RedirectAttributes redirectAttributes) { // 移除 throws SQLException, IllegalArgumentException
        if (bomId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "BOM ID不能為空。");
            return "redirect:/bom?action=list";
        }

        BillOfMaterial bom = bomService.getBillOfMaterialById(bomId).orElse(null);
        if (bom != null) {
            model.addAttribute("bom", bom);
            model.addAttribute("action", "view");
            model.addAttribute("pageTitle", "產品用料詳情");
            LOGGER.info("Displaying BOM detail for BOM ID: {}", bomId);
            return "production/bomList";
        } else {
            LOGGER.warn("BOM item to view not found, ID: {}", bomId);
            redirectAttributes.addFlashAttribute("errorMessage", "產品用料項目未找到，ID: " + bomId);
            return "redirect:/bom?action=list";
        }
    }

    /**
     * Inserts a new BOM item into the database.
     */
    private void insertBom(BillOfMaterial newBom, RedirectAttributes redirectAttributes) { // 移除 throws SQLException, IllegalArgumentException
        validateBom(newBom, true); // Validate input before service call
        // 服務層現在會拋出自定義異常或 RuntimeException，這裡直接呼叫
        bomService.createBillOfMaterial(newBom); // 呼叫 createBillOfMaterial
        LOGGER.info("Successfully added BOM item: Product ID {}, Material ID {}",
                    newBom.getProduct() != null ? newBom.getProduct().getProductId() : "N/A",
                    newBom.getMaterial() != null ? newBom.getMaterial().getMaterialId() : "N/A");
        redirectAttributes.addFlashAttribute("message", "產品用料項目已成功新增。");
    }

    /**
     * Updates an existing BOM item in the database.
     */
    private void updateBom(BillOfMaterial updatedBom, RedirectAttributes redirectAttributes) { // 移除 throws SQLException, IllegalArgumentException
        if (updatedBom.getBomId() == null) {
            throw new IllegalArgumentException("更新產品用料時，BOM ID不能為空。");
        }
        validateBom(updatedBom, false); // Validate input before service call
        // 服務層現在會拋出自定義異常或 RuntimeException，這裡直接呼叫
        bomService.updateBillOfMaterial(updatedBom);
        LOGGER.info("Successfully updated BOM item, ID: {}", updatedBom.getBomId());
        redirectAttributes.addFlashAttribute("message", "產品用料項目已成功更新。");
    }

    /**
     * Deletes a BOM item from the database.
     */
    private String deleteBom(Integer bomId, RedirectAttributes redirectAttributes) { // 移除 throws SQLException, IllegalArgumentException
        if (bomId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "BOM ID不能為空。");
            return "redirect:/bom?action=list";
        }

        // 服務層現在會拋出自定義異常或 RuntimeException，這裡直接呼叫
        bomService.deleteBillOfMaterial(bomId);
        LOGGER.info("Successfully deleted BOM item, ID: {}", bomId);
        redirectAttributes.addFlashAttribute("message", "產品用料項目已成功刪除。");
        return "redirect:/bom?action=list";
    }

    /**
     * Helper method to populate dropdown lists (Products and Materials) for the form.
     */
    private void populateDropdowns(Model model) { // 移除 throws SQLException
        List<Product> products = productService.getAllProducts();
        List<Material> materials = materialService.getAllMaterials();
        model.addAttribute("products", products);
        model.addAttribute("materials", materials);
    }

    /**
     * Helper method to validate the BillOfMaterial object.
     */
    private void validateBom(BillOfMaterial bom, boolean isNew) throws IllegalArgumentException {
        // 驗證 Product 和 Material 實體是否存在及其 ID
        if (bom.getProduct() == null || bom.getProduct().getProductId() == null) {
            throw new IllegalArgumentException("產品為必填項。");
        }
        if (bom.getMaterial() == null || bom.getMaterial().getMaterialId() == null) {
            throw new IllegalArgumentException("物料為必填項。");
        }
        if (bom.getQuantity() == null || bom.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("消耗數量為必填項且必須大於零。");
        }
        // Additional validation specific to new vs. update
        if (isNew && bom.getBomId() != null) {
            LOGGER.warn("Received BOM ID {} for new operation. It should be null.", bom.getBomId());
        }
    }
}
