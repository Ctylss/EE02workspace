package com.example.controller;

import com.example.model.Product; // 導入你的產品實體類
import com.example.service.ProductService; // 導入你的服務介面
import com.example.exception.InvalidProductDataException; // 導入自定義業務異常：無效數據
import com.example.exception.ProductNotFoundException; // 導入自定義業務異常：未找到產品
import com.example.exception.ProductReferencedException; // 導入自定義業務異常：產品被引用

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * ProductController 作為 Spring MVC 的控制器層，負責處理產品相關的 Web 請求。
 * 它接收客戶端的 HTTP 請求，將其路由到相應的業務邏輯（通過 ProductService），
 * 並準備數據以供 JSP 視圖渲染，最終返回 JSP 頁面。
 *
 * @Controller 標識這是一個 Spring MVC 的控制器，用於渲染視圖。
 * @RequestMapping("/products") 設定了此控制器所有端點的基礎 URL 路徑。
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
        LOGGER.info("ProductController 已初始化，並透過建構子注入 ProductService。");
    }

    /**
     * 處理獲取所有產品的 HTTP GET 請求，並顯示產品列表頁面。
     * 映射到 /products 或 /products/list
     *
     * @param category 可選的產品類別，用於篩選。
     * @param model Spring 的 Model 物件，用於將產品列表傳遞給 JSP。
     * @return 產品列表 JSP 視圖的邏輯名稱。
     */
    @GetMapping({"", "/list"}) // 映射到 /products 或 /products/list 的 GET 請求
    public String listAllProducts(
            @RequestParam(name = "category", required = false) String category, // 添加可選的 category 參數
            Model model) {
        LOGGER.debug("Received request to list products. Category filter: {}", category);

        List<Product> products;
        if (category != null && !category.trim().isEmpty()) {
            products = productService.getProductsByCategory(category); // 假設 ProductService 有此方法
            model.addAttribute("pageTitle", "產品主檔列表 (類別: " + category + ")");
        } else {
            products = productService.getAllProducts();
            model.addAttribute("pageTitle", "產品主檔列表");
        }

        model.addAttribute("products", products); // 將產品列表添加到 Model
        LOGGER.debug("Returning {} products to productList.jsp.", products.size());
        return "productList"; // 返回產品列表 JSP 的邏輯名稱 (例如 productList.jsp)
    }

    /**
     * 顯示新增產品的表單頁面。
     * 映射到 /products/new (GET)
     *
     * @param model Spring 的 Model 物件，用於提供一個空的 Product 物件給表單。
     * @return 新增/編輯產品表單 JSP 視圖的邏輯名稱。
     */
    @GetMapping("/new") // 映射到 /products/new 的 GET 請求
    public String showNewProductForm(Model model) {
        LOGGER.debug("Received request to show new product form.");
        model.addAttribute("product", new Product()); // 提供一個空的 Product 物件給表單
        model.addAttribute("pageTitle", "新增產品"); // 設定頁面標題
        return "productDetail"; // 返回產品表單 JSP 的邏輯名稱 (例如 productForm.jsp)
    }

    /**
     * 顯示編輯產品的表單頁面。
     * 映射到 /products/edit (GET) - 使用 @RequestParam 以匹配您 JSP 中的連結格式
     *
     * @param id URL 參數中的產品 ID。
     * @param model Spring 的 Model 物件，用於提供要編輯的產品物件給表單。
     * @param redirectAttributes 用於在重定向時添加錯誤訊息。
     * @return 新增/編輯產品表單 JSP 視圖的邏輯名稱，或重定向到列表頁。
     */
    @GetMapping("/edit") // 映射到 /products/edit?id=... 的 GET 請求
    public String showEditProductForm(@RequestParam("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        LOGGER.debug("Received request to show edit product form for ID: {}", id);

        Optional<Product> productOptional = productService.getProductById(id);

        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get()); // 將找到的產品添加到 Model
            model.addAttribute("pageTitle", "編輯產品"); // 設定頁面標題
            LOGGER.debug("Found product with ID: {} for editing.", id);
            return "productDetail"; // 返回產品表單 JSP 的邏輯名稱 (例如 productForm.jsp)
        } else {
            LOGGER.warn("Product with ID {} not found for editing. Redirecting to list.", id);
            redirectAttributes.addFlashAttribute("errorMessage", "產品 ID " + id + " 未找到，無法編輯。");
            return "redirect:/products/list"; // 如果未找到產品，重定向到列表頁
        }
    }

    /**
     * 處理創建新產品的 HTTP POST 請求。
     * 映射到 /products/add (POST)
     *
     * @param product 包含產品詳細資訊的 Product 物件，從表單綁定而來。
     * @ModelAttribute 註解負責將表單數據映射到 Product 物件。
     * @param redirectAttributes 用於在成功或失敗時添加 Flash 訊息。
     * @param model 用於在驗證失敗時將數據回傳給表單。
     * @return 重定向到產品列表頁面，或返回到表單頁面顯示錯誤。
     */
    @PostMapping("/add") // 映射到 /products/add 的 POST 請求
    public String addProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes, Model model) {
        LOGGER.info("Received request to add new product: {}", product.getProductName());

        try {
            Product savedProduct = productService.saveProduct(product);

            LOGGER.info("Successfully added product with ID: {}", savedProduct.getProductId());
            redirectAttributes.addFlashAttribute("message", "產品 '" + savedProduct.getProductName() + "' 已成功新增。");
            return "redirect:/products/list"; // 成功後重定向到列表頁
        } catch (InvalidProductDataException ex) {
            LOGGER.warn("Invalid product data received for add operation: {}", ex.getMessage());
            model.addAttribute("errorMessage", "新增產品失敗：" + ex.getMessage());
            model.addAttribute("product", product); // 將原始產品數據回傳給表單，以便用戶修正
            model.addAttribute("pageTitle", "新增產品"); // 確保頁面標題正確
            return "productDetail"; // 返回到表單頁面顯示錯誤
        }
    }

    /**
     * 處理更新現有產品的 HTTP POST 請求。
     * 映射到 /products/update (POST)
     *
     * @param product 包含更新後產品數據的 Product 物件，從表單綁定而來。
     * @param redirectAttributes 用於在成功或失敗時添加 Flash 訊息。
     * @param model 用於在驗證失敗時將數據回傳給表單。
     * @return 重定向到產品列表頁面，或返回到表單頁面顯示錯誤。
     */
    @PostMapping("/update") // 映射到 /products/update 的 POST 請求
    public String updateExistingProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes, Model model) {
        LOGGER.info("Received request to update product with ID: {}", product.getProductId());

        try {
            Product updatedProduct = productService.saveProduct(product);

            LOGGER.info("Successfully updated product with ID: {}", updatedProduct.getProductId());
            redirectAttributes.addFlashAttribute("message", "產品 '" + updatedProduct.getProductName() + "' 已成功更新。");
            return "redirect:/products/list"; // 成功後重定向到列表頁
        } catch (InvalidProductDataException ex) {
            LOGGER.warn("Invalid product data received for update operation: {}", ex.getMessage());
            model.addAttribute("errorMessage", "更新產品失敗：" + ex.getMessage());
            model.addAttribute("product", product); // 將原始產品數據回傳給表單，以便用戶修正
            model.addAttribute("pageTitle", "編輯產品"); // 確保頁面標題正確
            return "productDetail"; // 返回到表單頁面顯示錯誤
        } catch (ProductNotFoundException ex) {
            LOGGER.warn("Product with ID {} not found for update: {}", product.getProductId(), ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "更新失敗：" + ex.getMessage());
            return "redirect:/products/list"; // 如果產品不存在，重定向到列表頁
        }
    }

    

    /**
     * 顯示產品詳情頁面。
     * **此處已修改**：從處理請求參數 `?id=` 改為處理路徑變數 `/{id}`。
     * 映射到 /products/view/{id} (GET)
     *
     * @param id URL 路徑中的產品 ID。
     * @param model Spring 的 Model 物件。
     * @param redirectAttributes 用於在產品未找到時添加錯誤訊息。
     * @return 產品詳情 JSP 視圖的邏輯名稱，或重定向到列表頁。
     */
    @GetMapping("/view/{id}") // <-- 關鍵修改：從 "/view" 改為 "/view/{id}"
    public String viewProductDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) { // <-- 關鍵修改：從 @RequestParam("id") 改為 @PathVariable("id")
        LOGGER.debug("Received request to view product details for ID: {}", id);
        Optional<Product> productOptional = productService.getProductById(id);

        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            model.addAttribute("pageTitle", "產品詳情");
            LOGGER.debug("Found product with ID: {} for viewing.", id);
            return "productDetail"; // 返回產品詳情 JSP
        } else {
            LOGGER.warn("Product with ID {} not found for viewing. Redirecting to list.", id);
            redirectAttributes.addFlashAttribute("errorMessage", "產品 ID " + id + " 未找到，無法查看。");
            return "redirect:/products/list";
        }
    }



    /**
     * 處理根據產品 ID 刪除產品的 HTTP GET 請求。
     * 映射到 /products/delete (GET) - 使用 @RequestParam 以匹配您 JSP 中的連結格式
     *
     * @param id URL 參數中的產品 ID。
     * @param redirectAttributes 用於在成功或失敗時添加 Flash 訊息。
     * @return 重定向到產品列表頁面。
     */
    @GetMapping("/delete") // 映射到 /products/delete?id=... 的 GET 請求
    public String deleteProductById(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
        LOGGER.info("Received request to delete product by ID: {}", id);

        try {
            productService.deleteProduct(id);
            LOGGER.info("Successfully deleted product with ID: {}", id);
            redirectAttributes.addFlashAttribute("message", "產品 ID " + id + " 已成功刪除。");
        } catch (ProductNotFoundException ex) {
            LOGGER.warn("Product with ID {} not found for deletion: {}", id, ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "刪除失敗：" + ex.getMessage());
        } catch (ProductReferencedException ex) {
            LOGGER.warn("Product with ID {} cannot be deleted as it is referenced: {}", id, ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "刪除失敗：" + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.warn("Invalid argument for deleting product ID {}: {}", id, ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "刪除失敗：" + ex.getMessage());
        }
        return "redirect:/products/list";
    }
}