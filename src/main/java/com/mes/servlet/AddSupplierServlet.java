package com.mes.servlet;

import com.example.dao.util.HibernateUtil;
import com.mes.dao.SupplierDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.hibernate.Session;

@WebServlet("/AddSupplierServlet")
public class AddSupplierServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		request.setCharacterEncoding("UTF-8");
		
		String name=request.getParameter("supplierName");
		String pm=request.getParameter("pm");
		String phone=request.getParameter("supplierPhone");
		String email=request.getParameter("supplierEmail");
		String address=request.getParameter("supplierAddress");
		Session session = HibernateUtil.getSessionFactory().openSession();	

		try {
			session.beginTransaction();
			SupplierDAO dao = new SupplierDAO();
			dao.insertSupplier(session, name, pm, phone, email, address);
			session.getTransaction().commit();
			response.sendRedirect("SupplierListServlet");		
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally{
			session.close();
		}
	}

}
