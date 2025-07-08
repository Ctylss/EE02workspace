package com.mes.servlet;

import com.example.dao.util.HibernateUtil;
import com.mes.bean.Order;
import com.mes.bean.OrderItem;
import com.mes.bean.Supplier;
import com.mes.dao.OrderDAO;
import com.mes.dao.SupplierDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import org.hibernate.Session;

@WebServlet("/EditOrderServlet")
public class EditOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        Session session = HibernateUtil.getSessionFactory().openSession();
        if (orderIdStr != null) {
        	try  {
                session.beginTransaction();
                int orderId = Integer.parseInt(orderIdStr);

                OrderDAO orderDAO = new OrderDAO();
                Order order = orderDAO.getOrderById(session, orderId);
                if (order == null) {
                    response.getWriter().println("<h3>找不到該筆訂單資料（orderId=" + orderId + "）</h3>");
                    return;
                }

                List<OrderItem> items = orderDAO.getItemsByOrderId(session, orderId);

                // 這裡加入取得供應商清單
                SupplierDAO supplierDAO = new SupplierDAO();
                List<Supplier> supplierList = supplierDAO.getActiveSuppliers(session);

                // 資料塞入 request 範圍
                request.setAttribute("order", order);
                request.setAttribute("items", items);
                request.setAttribute("supplierList", supplierList);

                session.getTransaction().commit();
                // 導向 JSP
                request.getRequestDispatcher("/JSP/zt/EditOrder.jsp").forward(request, response);

            } catch (Exception e) {
                if(session.getTransaction().isActive()){
                  session.getTransaction().rollback();  
                }              
                e.printStackTrace();
                response.getWriter().println("讀取訂單失敗：" + e.getMessage());
            }finally{
                session.close();
            }
        } else {
            response.getWriter().println("缺少訂單編號參數");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
