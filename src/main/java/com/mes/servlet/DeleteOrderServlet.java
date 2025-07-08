package com.mes.servlet;

import com.mes.dao.OrderDAO;
import com.mes.dao.SupplierDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.im.InputContext;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@WebServlet("/DeleteOrderServlet")
public class DeleteOrderServlet extends HttpServlet {
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
		String idStr = request.getParameter("orderId");
		try (Connection conn = dataSource.getConnection()) {
			 int orderId = Integer.parseInt(idStr);
			 OrderDAO dao = new OrderDAO();
			 dao.deleteOrderWithItems(conn, orderId);
			 response.sendRedirect("OrderListServlet");
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doGet(request, response);
	}

}
