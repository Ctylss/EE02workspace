package com.machine.Servlet.repair;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.machine.Bean.MachineRepairBean;
import com.machine.Service.repair.MachineRepairService;

@WebServlet("/MachineRepairViewServlet")
public class MachineRepairViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MachineRepairService machineRepairService = new MachineRepairService();

	public MachineRepairViewServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<MachineRepairBean> repairList = null;
		try {
			repairList = machineRepairService.machineRepairView();
			// 把查到的列表放進 request
			request.setAttribute("repairList", repairList);
			// 導向顯示頁面（假設是 repair_list.jsp）
			request.getRequestDispatcher("/JSP/cy/repair/repairList.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "伺服器錯誤：" + e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
