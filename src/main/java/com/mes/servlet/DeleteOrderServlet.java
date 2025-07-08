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

@WebServlet("/DeleteOrderServlet")
public class DeleteOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idStr = request.getParameter("orderId");
		Session session = HibernateUtil.getSessionFactory().openSession();
		try  {
			session.beginTransaction();
			 int orderId = Integer.parseInt(idStr);
			 OrderDAO dao = new OrderDAO();
			 dao.deleteOrderWithItems(session, orderId);
			session.getTransaction().commit();
			 response.sendRedirect("OrderListServlet");
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			session.close();
		} 		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doGet(request, response);
	}
}
