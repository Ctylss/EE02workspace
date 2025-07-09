package com.machine.Servlet.maintenance;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.machine.Bean.MachineMaintenanceBean;
import com.machine.Service.maintenance.MachineMaintenanceService;

@WebServlet("/FindAllMachineMaintenance")
public class FindAllMachineMaintenance extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MachineMaintenanceService maintenanceService = new MachineMaintenanceService();

    public FindAllMachineMaintenance() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MachineMaintenanceBean> maintenanceList = null;

        try {
            maintenanceList = maintenanceService.findAllMaintenances();

        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("maintenanceList", maintenanceList);
        request.getRequestDispatcher("/JSP/cy/maintenance/maintenanceList.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
