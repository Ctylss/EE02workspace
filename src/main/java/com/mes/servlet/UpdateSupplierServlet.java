package com.mes.servlet;

import com.mes.dao.SupplierDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@WebServlet("/UpdateSupplierServlet")
public class UpdateSupplierServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            // 從 context.xml 中透過 JNDI 取得資料庫連線池
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/MESDB");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("無法載入資料庫連線池：" + e.getMessage(), e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try (Connection conn = dataSource.getConnection()) {
            int supplierId = Integer.parseInt(request.getParameter("supplierId"));
            String name = request.getParameter("supplierName");
            String pm = request.getParameter("pm");
            String phone = request.getParameter("supplierPhone");
            String email = request.getParameter("supplierEmail");
            String address = request.getParameter("supplierAddress");

            SupplierDAO dao = new SupplierDAO();
            boolean success = dao.updateSupplier(conn, supplierId, name, pm, phone, email, address);

            if (success) {
                response.sendRedirect("SupplierListServlet"); // 回到供應商列表
            } else {
                response.getWriter().println("更新失敗！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("發生錯誤：" + e.getMessage());
        }
    }
}
