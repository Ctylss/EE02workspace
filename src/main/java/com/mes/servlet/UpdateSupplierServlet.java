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

@WebServlet("/UpdateSupplierServlet")
public class UpdateSupplierServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = HibernateUtil.getSessionFactory().openSession();
    	try  {
            session.beginTransaction();
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            String name = request.getParameter("supplierName");
            String pm = request.getParameter("pm");
            String phone = request.getParameter("supplierPhone");
            String email = request.getParameter("supplierEmail");
            String address = request.getParameter("supplierAddress");

            SupplierDAO dao = new SupplierDAO();
            boolean success = dao.updateSupplier(session, supplierId, name, pm, phone, email, address);
            session.getTransaction().commit();

            if (success) {
                response.sendRedirect("SupplierListServlet"); // 回到供應商列表
            } else {
                response.getWriter().println("更新失敗！");
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            response.getWriter().println("發生錯誤：" + e.getMessage());
        }finally{
            session.close();
        }
    }
}
