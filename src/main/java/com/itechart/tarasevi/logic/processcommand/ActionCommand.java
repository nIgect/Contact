package com.itechart.tarasevi.logic.processcommand;

import javax.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface ActionCommand {
    String execute(HttpServletRequest request);
}
