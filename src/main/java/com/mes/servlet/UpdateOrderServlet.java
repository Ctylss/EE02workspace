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

@WebServlet("/UpdateOrderServlet")
public class UpdateOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Session session = HibernateUtil.getSessionFactory().openSession();
		try  {
			session.beginTransaction();
			
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
			dao.updateOrder(session, orderId, supplierId, orderDate, orderStatus, subTotal);
			dao.updateOrderItems(session, orderId, materialIds, quantities, unitPrices);
			
			session.getTransaction().commit();
			response.sendRedirect("OrderListServlet");

		}catch (Exception e) {
				session.getTransaction().rollback();
				e.printStackTrace();
				response.getWriter().println("更新失敗：" + e.getMessage());
			}finally{
				session.close();
			}
//		doGet(request, response);
	}

}
