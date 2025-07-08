package com.mes.servlet;

import com.mes.dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.im.InputContext;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@WebServlet("/UpdateOrderServlet")
public class UpdateOrderServlet extends HttpServlet {
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
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			
			int orderId = Integer.parseInt(request.getParameter("orderId"));
			int supplierId = Integer.parseInt(request.getParameter("supplierId"));
			String orderDate = request.getParameter("orderDate");
			String orderStatus = request.getParameter("orderStatus");
		
		
			String[] materialIds = request.getParameterValues("materialId[]");
			String[] quantities = request.getParameterValues("quantity[]");
			String[] unitPrices = request.getParameterValues("unitPrice[]");
			
			if (materialIds == null || quantities == null || unitPrices == null) {
			    throw new ServletException("請確認已填寫訂單明細的所有欄位！");
			}
				
			double subTotal = 0;
			
			for (int i = 0; i < quantities.length; i++) {
			    int qty = Integer.parseInt(quantities[i]);
			    double price = Double.parseDouble(unitPrices[i]);
			    subTotal += qty * price;
			}
			
			OrderDAO dao = new OrderDAO();
			dao.updateOrder(conn, orderId, supplierId, orderDate, orderStatus, subTotal);
			dao.updateOrderItems(conn, orderId, materialIds, quantities, unitPrices);
			
			conn.commit();//手動提交交易，表示主檔與明細都成功寫入，資料才會正式存進資料庫。
			response.sendRedirect("OrderListServlet");

		}catch (Exception e) {
				e.printStackTrace();
				 response.getWriter().println("更新失敗：" + e.getMessage());
			}
//		doGet(request, response);
	}

}
