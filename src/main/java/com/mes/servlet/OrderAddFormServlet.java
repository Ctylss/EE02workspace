package com.mes.servlet;

import com.mes.dao.MaterialDAO;
import com.mes.dao.SupplierDAO;
import com.example.dao.util.HibernateUtil;
import com.mes.bean.Material;
import com.mes.bean.Supplier;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import org.hibernate.Session;


@WebServlet("/OrderAddFormServlet")
public class OrderAddFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Session session = HibernateUtil.getSessionFactory().openSession();
        try  {
            session.beginTransaction();
            // 取得供應商清單
            SupplierDAO supplierDAO = new SupplierDAO();
            List<Supplier> supplierList = supplierDAO.getActiveSuppliers(session);
            // 設定供應商清單到 request 中
            request.setAttribute("supplierList", supplierList);

            // 取得物料清單
            MaterialDAO materialDAO = new MaterialDAO();
            List<Material> materialList = materialDAO.getActiveMaterials(session);
            request.setAttribute("materialList", materialList);
            session.getTransaction().commit();
            // 導向 AddOrder.jsp
            request.getRequestDispatcher("/JSP/zt/AddOrder.jsp").forward(request, response);

        } catch (Exception e) {
            session.getTransaction().rollback();
        	e.printStackTrace();
        	response.getWriter().write("發生錯誤：" + e.getMessage());

        }finally{
            session.close();
        }
    }
}
