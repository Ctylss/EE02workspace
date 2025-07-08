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
import org.hibernate.Session;

@WebServlet("/EditSupplierServlet")
public class EditSupplierServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String supplierIdStr = request.getParameter("supplierId");
        Session session = HibernateUtil.getSessionFactory().openSession();

        if (supplierIdStr != null && !supplierIdStr.isEmpty()) {
        	try  {
                session.beginTransaction();
                int supplierId = Integer.parseInt(supplierIdStr);
                SupplierDAO dao = new SupplierDAO();
                Supplier supplier = dao.getSupplierById(session, supplierId);
                session.getTransaction().commit();
                if (supplier != null) {
                    request.setAttribute("supplier", supplier);
                    request.getRequestDispatcher("/JSP/zt/EditSupplier.jsp").forward(request, response);
                } else {
                    response.getWriter().println("找不到該供應商資料");
                }
            } catch (Exception e) {
                session.getTransaction().rollback();
                e.printStackTrace();
                response.getWriter().println("發生錯誤：" + e.getMessage());
            } finally{
                session.close();
            }
        } else {
            response.getWriter().println("缺少供應商 ID 參數");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
