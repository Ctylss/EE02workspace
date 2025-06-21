package com.machine.Servlet.machine;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.machine.Bean.MachinesBean;
import com.machine.Service.machine.MachinesService;

@WebServlet("/FindAllMachinesServlet")
public class FindAllMachinesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public FindAllMachinesServlet() {
        super();
    }
    private MachinesService machinesService = new MachinesService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
            List<MachinesBean> machinesList = machinesService.findAllMachines();
            request.setAttribute("machines", machinesList);
            request.getRequestDispatcher("/WEB-INF/JSP/machine/findAllMachines.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "系統錯誤，請稍後再試");
        }
    }
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
