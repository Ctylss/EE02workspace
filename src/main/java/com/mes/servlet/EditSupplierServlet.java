package com.mes.servlet;

import com.mes.bean.Supplier;
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

@WebServlet("/EditSupplierServlet")
public class EditSupplierServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String supplierIdStr = request.getParameter("supplierId");

        if (supplierIdStr != null && !supplierIdStr.isEmpty()) {
        	try (Connection conn = dataSource.getConnection()) {
                int supplierId = Integer.parseInt(supplierIdStr);
                SupplierDAO dao = new SupplierDAO();
                Supplier supplier = dao.getSupplierById(conn, supplierId);

                if (supplier != null) {
                    request.setAttribute("supplier", supplier);
                    request.getRequestDispatcher("/JSP/zt/EditSupplier.jsp").forward(request, response);
                } else {
                    response.getWriter().println("找不到該供應商資料");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().println("發生錯誤：" + e.getMessage());
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
