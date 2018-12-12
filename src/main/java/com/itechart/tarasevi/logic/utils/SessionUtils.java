package com.itechart.tarasevi.logic.utils;

import com.itechart.tarasevi.logic.domain.Employee;

import javax.servlet.http.HttpServletRequest;

public class SessionUtils {

    private SessionUtils(){}

    public static void setEmployeeToSession(HttpServletRequest request, Employee employee) {
        request.getSession().setAttribute("employee", employee);
    }

    public static Employee getEmployeeFromSession(HttpServletRequest request) {
        return (Employee) request.getSession().getAttribute("employee");
    }
}
