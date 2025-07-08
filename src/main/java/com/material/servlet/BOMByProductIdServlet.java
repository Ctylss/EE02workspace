package com.material.servlet;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.material.dao.BOMDao;
import com.material.model.BOM;

@WebServlet("/BOMByProductIdServlet")
public class BOMByProductIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BOMDao bomDao = new BOMDao();
       

    public BOMByProductIdServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("application/json; charset=UTF-8");
		try(PrintWriter outToFront = response.getWriter()){
			String productIdString = request.getParameter("productId");
			if (productIdString == null || productIdString.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少 productId 參數");
                return;}
			
			int productId = Integer.parseInt(productIdString);
			List<BOM> bomList = bomDao.findBOMByProductId(productId);
			Gson gson = new Gson();
			String javaToJsString = gson.toJson(bomList);
			outToFront.print(javaToJsString);			
		} catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
