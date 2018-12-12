package com.itechart.tarasevi.logic.commands;

import com.itechart.tarasevi.logic.processcommand.ActionCommand;

import javax.servlet.http.HttpServletRequest;

import static com.itechart.tarasevi.logic.configuration.ConfigurationManager.getProperty;

public class EmptyCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        return getProperty("path.page.start");
    }
}
