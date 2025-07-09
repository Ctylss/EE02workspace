package com.machine.Servlet.machine;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.machine.Bean.MachinesBean;
import com.machine.Service.machine.MachinesService;

@WebServlet("/FindMachineByIdServlet")
public class FindMachineByIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FindMachineByIdServlet() {
		super();
	}

	private MachinesService machinesService = new MachinesService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idStr = request.getParameter("machineId");
		if (idStr == null || idStr.isEmpty()) {
			return;
		}

		int machineId = Integer.parseInt(idStr);
		try {
			MachinesBean machine = machinesService.findMachineById(machineId);
			if (machine == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "找不到該機台資料");
				return;
			}
			request.setAttribute("machine", machine);
			request.getRequestDispatcher("/JSP/cy/machine/findMachineById.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "機台ID格式錯誤");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "系統錯誤，請稍後再試");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
