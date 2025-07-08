package com.material.servlet;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.material.dao.BOMDao;
import com.material.model.BOM;


@WebServlet("/BOMUpdateServlet")
public class BOMUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BOMDao dao = new BOMDao();

    public BOMUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line;
		while((line = reader.readLine())!=null) {
			sb.append(line);
		}
		
		String jsonToJavaString = sb.toString();
		Gson gson = new Gson();
		List<BOM> inputList = gson.fromJson(jsonToJavaString, new TypeToken<List<BOM>>() {}.getType());
		
		for(BOM bom : inputList) {
			dao.updateBOM(bom);
		}
		
		response.setContentType("application/json");
		response.getWriter().write("{\"status\":\"ok\"}");
	}

}
