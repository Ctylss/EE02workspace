package com.material.servlet;

import java.io.IOException;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

import com.google.gson.Gson;
import com.material.dao.BOMDao;
import com.material.model.BOM;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@WebServlet("/BOMQueryServlet")
public class BOMQueryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BOMDao bomDao = new BOMDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("application/json; charset=UTF-8");
    	
    	try (PrintWriter out = response.getWriter()) {
            String keyword = request.getParameter("keyword");
            
            List<BOM> bomList = bomDao.findBOMByName(keyword != null ? keyword : "");

            System.out.println("BOM 查詢筆數: " + bomList.size());
            
            Gson gson = new Gson();
            out.print(gson.toJson(bomList));
            out.flush();

        } catch (Exception e) {
            // 印在 server console，方便你從 Tomcat log 看錯誤
            e.printStackTrace();

            // 回傳錯誤訊息到前端 (XHR responseText 就會看到這段)
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
