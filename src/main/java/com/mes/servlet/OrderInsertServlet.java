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

@WebServlet("/OrderInsertServlet")
public class OrderInsertServlet extends HttpServlet {
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

	
    public OrderInsertServlet() {
       
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
	    response.getWriter().println("<h3>請使用表單送出 POST 請求</h3>");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String supplierId = request.getParameter("supplier"); //從前端取得值(supplier)f
		String orderDate = request.getParameter("orderDate");
		String orderStatus = request.getParameter("status");
		
		 if (orderDate == null || orderDate.trim().isEmpty()) {
		        request.setAttribute("errorMessage", "請選擇訂單日期");
		        request.getRequestDispatcher("/JSP/zt/AddOrder.jsp").forward(request, response);
		        return; // 中止執行
		    }
		 
		String[] materialIds = request.getParameterValues("materialId[]");
		String[] quantities = request.getParameterValues("quantity[]");
		String[] unitPrices = request.getParameterValues("unitPrice[]");
		
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);//關閉自動提交，確保「訂單主檔 + 明細」一起成功或一起失敗。
				
			double subTotal = 0;
			
			for (int i = 0; i < quantities.length; i++) {
			    int qty = Integer.parseInt(quantities[i]);
			    double price = Double.parseDouble(unitPrices[i]);
			    subTotal += qty * price;
			}
			
			OrderDAO dao = new OrderDAO();
			int orderId = dao.insertOrder(conn, Integer.parseInt(supplierId) , orderDate, orderStatus, subTotal);
			dao.insertOrderItems(conn, orderId, materialIds, quantities, unitPrices);
			
			conn.commit();//手動提交交易，表示主檔與明細都成功寫入，資料才會正式存進資料庫。
			response.sendRedirect("OrderListServlet");
			System.out.println("已接收到訂單資料");

		}catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect("OrderListServlet");

			}
//		doGet(request, response);
	}

}
