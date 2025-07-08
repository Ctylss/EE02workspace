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

@WebServlet("/ProdBOMQueryServlet")
public class ProdBOMQueryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BOMDao bomDao = new BOMDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (PrintWriter toFront = response.getWriter()) {
            Integer productId = Integer.parseInt(request.getParameter("productId"));
            List<BOM> materialList = bomDao.findProdBomByProdId(productId);

            Gson gson = new Gson();
            response.setContentType("application/json; charset=UTF-8");
            String jSToJava = gson.toJson(materialList);
            toFront.print(jSToJava); //寫入response
//            toFront.flush(); 
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
