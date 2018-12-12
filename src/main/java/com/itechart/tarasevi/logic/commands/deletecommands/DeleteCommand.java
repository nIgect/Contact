package com.itechart.tarasevi.logic.commands.deletecommands;

import com.itechart.tarasevi.logic.commands.maincommands.ContactCommand;
import com.itechart.tarasevi.logic.configuration.ConfigurationManager;
import com.itechart.tarasevi.logic.dao.mysqlImpl.EmployeeDAOImpl;
import com.itechart.tarasevi.logic.exceptions.ExecutingCommandsException;
import com.itechart.tarasevi.logic.processcommand.ActionCommand;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeleteCommand implements ActionCommand {
    private static final Logger LOGGER = LogManager.getLogger(DeleteCommand.class);


    @Override
    public String execute(HttpServletRequest request) {
        String[] selectedEmpl = request.getParameterValues("check_selected");

        for (String aSelectedEmpl : selectedEmpl) {
            this.deleteEmployee(Integer.parseInt(aSelectedEmpl));
        }
        ContactCommand contactCommand = new ContactCommand();
        return contactCommand.execute(request);
    }

    private void deleteEmployee(final int ID) {
        EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
        employeeDAO.deleteEmployee(ID);
        deleteAttachmentDirectory(ID);
    }

    private void deleteAttachmentDirectory(final int ID) {
        try {
            String resultFileName = ConfigurationManager.getPathProperty("path.saveFile") +
                    ID;
            Path path = Paths.get(resultFileName);
            FileUtils.deleteDirectory(path.toFile());
            LOGGER.info("deleted directory from server");
        } catch (IOException e) {
            LOGGER.error("can't delete directory from server ", e);
            throw new ExecutingCommandsException("Не удаётся удалить дерикторию");
        }
    }
}
