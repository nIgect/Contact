package com.itechart.tarasevi.logic.commands.deletecommands;

import com.itechart.tarasevi.logic.commands.maincommands.UpdateCommand;
import com.itechart.tarasevi.logic.domain.Employee;
import com.itechart.tarasevi.logic.domain.Photo;
import com.itechart.tarasevi.logic.processcommand.ActionCommand;
import com.itechart.tarasevi.logic.utils.SessionUtils;

import javax.servlet.http.HttpServletRequest;

import static com.itechart.tarasevi.logic.configuration.ConfigurationManager.getProperty;

public class DeletePhotoCommand implements ActionCommand {
    private final UpdateCommand updateCommand = new UpdateCommand();

    @Override
    public String execute(HttpServletRequest request) {

        deletePhoto(request);
        updateCommand.fillAllParameters(request);
        return getProperty("path.page.edit");
    }

    private void deletePhoto(HttpServletRequest request) {
        Employee employee = SessionUtils.getEmployeeFromSession(request);
        Photo photo = employee.getPhoto();
        photo.setDeleted(true);
    }
}
