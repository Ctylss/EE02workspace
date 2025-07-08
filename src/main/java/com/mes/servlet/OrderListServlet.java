package com.mes.servlet;

import com.example.dao.util.HibernateUtil;
import com.mes.bean.Order;
import com.mes.dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import org.hibernate.Session;


@WebServlet("/OrderListServlet")
public class OrderListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {	
			session.beginTransaction();
			OrderDAO dao = new OrderDAO();
			List<Order> orderList = dao.getAllOrdersWithItems(session);
			session.getTransaction().commit();
			request.setAttribute("orderList", orderList);
			request.getRequestDispatcher("/JSP/zt/OrderList.jsp").forward(request, response);

		}  catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally{
			session.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
	}

}
