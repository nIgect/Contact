package com.itechart.tarasevi.logic.processcommand;

import com.itechart.tarasevi.logic.commands.EmptyCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class ActionFactory {
    private static final Logger LOGGER = LogManager.getLogger(ActionFactory.class);

    public ActionCommand defineCommand(HttpServletRequest request) {
        Object current = new EmptyCommand();
        String action = request.getParameter("command");
        if (StringUtils.isEmpty(action)) {
            action = request.getAttribute("command").toString();
        }
        if (StringUtils.isNotEmpty(action)) {
            try {
                CommandEnum currentEnum = CommandEnum.valueOf(action.toUpperCase());
                current = currentEnum.getCurrentCommand();
                LOGGER.info("start : {} command", action);
            } catch (IllegalArgumentException e) {
                LOGGER.error("wrong command: ",e);
            }
        }
        return (ActionCommand) current;

    }
}
