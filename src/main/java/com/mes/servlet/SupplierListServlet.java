package com.mes.servlet;

import com.example.dao.util.HibernateUtil;
import com.mes.bean.Supplier;
import com.mes.dao.SupplierDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.hibernate.Session;

@WebServlet("/SupplierListServlet")
public class SupplierListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
				session.beginTransaction();
				
				SupplierDAO dao = new SupplierDAO(); 
				List<Supplier> supplierList = dao.getActiveSuppliers(session);
				session.getTransaction().commit();

				request.setAttribute("supplierList",supplierList);
				request.getRequestDispatcher("/JSP/zt/SupplierList.jsp").forward(request, response);
			
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
