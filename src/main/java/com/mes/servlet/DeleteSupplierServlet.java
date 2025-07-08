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

@WebServlet("/DeleteSupplierServlet")
public class DeleteSupplierServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();	
		try {				
			
				session.beginTransaction();
				int supplierId = Integer.parseInt(request.getParameter("supplierId"));
				SupplierDAO dao = new SupplierDAO(); 
				dao.deactivateSupplier(session, supplierId);//執行下架動作
				session.getTransaction().commit();

				response.sendRedirect("SupplierListServlet");
				
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
