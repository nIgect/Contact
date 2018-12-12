package com.itechart.tarasevi.logic.commands.emailcommand;

import com.itechart.tarasevi.logic.configuration.ConfigurationManager;
import com.itechart.tarasevi.logic.dao.mysqlImpl.EmployeeDAOImpl;
import com.itechart.tarasevi.logic.processcommand.ActionCommand;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by aefrd on 16.09.2016.
 */
public class EmailCommand implements ActionCommand {
    public String execute(HttpServletRequest request) {
        String[] selectedEmployee = request.getParameterValues("check_selected");
        request.setAttribute("lst_mail", getEmailsFromBD(selectedEmployee));
        return ConfigurationManager.getProperty("path.page.email");
    }
    private StringBuilder getEmailsFromBD(String[] selectedEmployee){
        StringBuilder emails = new StringBuilder();
        EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
        for (String aSelectedEmployee : selectedEmployee) {
            emails.append(employeeDAO.getEmail(Integer.parseInt(aSelectedEmployee))).append(" ");
        }
        return emails;
    }

}
