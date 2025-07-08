package com.mes.servlet;

import com.example.dao.util.HibernateUtil;
import com.mes.dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.hibernate.Session;

@WebServlet("/OrderInsertServlet")
public class OrderInsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();			
			double subTotal = 0;		
			for (int i = 0; i < quantities.length; i++) {
			    int qty = Integer.parseInt(quantities[i]);
			    double price = Double.parseDouble(unitPrices[i]);
			    subTotal += qty * price;
			}		
			OrderDAO dao = new OrderDAO();
			int orderId = dao.insertOrder(session, Integer.parseInt(supplierId) , orderDate, orderStatus, subTotal);
			dao.insertOrderItems(session, orderId, materialIds, quantities, unitPrices);
			session.getTransaction().commit();
			response.sendRedirect("OrderListServlet");
			System.out.println("已接收到訂單資料");

		}catch (Exception e) {
				session.getTransaction().rollback();
				e.printStackTrace();
				response.sendRedirect("OrderListServlet");

		}finally{
			session.close();
		}
//		doGet(request, response);
	}

}
