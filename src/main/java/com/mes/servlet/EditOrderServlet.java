package com.mes.servlet;

import com.mes.bean.Order;
import com.mes.bean.OrderItem;
import com.mes.bean.Supplier;
import com.mes.dao.OrderDAO;
import com.mes.dao.SupplierDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@WebServlet("/EditOrderServlet")
public class EditOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            // 從 context.xml 中透過 JNDI 取得資料庫連線池
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/MESDB");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("無法載入資料庫連線池：" + e.getMessage(), e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");

        if (orderIdStr != null) {
        	try (Connection conn = dataSource.getConnection()) {
                int orderId = Integer.parseInt(orderIdStr);

                OrderDAO orderDAO = new OrderDAO();
                Order order = orderDAO.getOrderById(conn, orderId);
                if (order == null) {
                    response.getWriter().println("<h3>找不到該筆訂單資料（orderId=" + orderId + "）</h3>");
                    return;
                }

                List<OrderItem> items = orderDAO.getItemsByOrderId(conn, orderId);

                // 這裡加入取得供應商清單
                SupplierDAO supplierDAO = new SupplierDAO();
                List<Supplier> supplierList = supplierDAO.getActiveSuppliers(conn);

                // 資料塞入 request 範圍
                request.setAttribute("order", order);
                request.setAttribute("items", items);
                request.setAttribute("supplierList", supplierList);

                // 導向 JSP
                request.getRequestDispatcher("/JSP/zt/EditOrder.jsp").forward(request, response);

            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().println("讀取訂單失敗：" + e.getMessage());
            }
        } else {
            response.getWriter().println("缺少訂單編號參數");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
