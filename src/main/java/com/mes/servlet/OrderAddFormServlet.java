package com.mes.servlet;

import com.mes.dao.MaterialDAO;
import com.mes.dao.SupplierDAO;
import com.mes.bean.Material;
import com.mes.bean.Supplier;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


@WebServlet("/OrderAddFormServlet")
public class OrderAddFormServlet extends HttpServlet {
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try (Connection conn = dataSource.getConnection()) {

            // 取得供應商清單
            SupplierDAO supplierDAO = new SupplierDAO();
            List<Supplier> supplierList = supplierDAO.getActiveSuppliers(conn);
            // 設定供應商清單到 request 中
            request.setAttribute("supplierList", supplierList);

            // 取得物料清單
            MaterialDAO materialDAO = new MaterialDAO();
            List<Material> materialList = materialDAO.getActiveMaterials(conn);
            request.setAttribute("materialList", materialList);
            
            // 導向 AddOrder.jsp
            request.getRequestDispatcher("/JSP/zt/AddOrder.jsp").forward(request, response);

        } catch (Exception e) {
        	e.printStackTrace();
        	response.getWriter().write("發生錯誤：" + e.getMessage());

        }
    }
}
