//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.itechart.tarasevi.logic.commands.maincommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.tarasevi.logic.configuration.ConfigurationManager;
import com.itechart.tarasevi.logic.dao.AttachmentDAO;
import com.itechart.tarasevi.logic.dao.MySqlFactory;
import com.itechart.tarasevi.logic.dao.PhoneDAO;
import com.itechart.tarasevi.logic.dao.PhotoDAO;
import com.itechart.tarasevi.logic.dao.mysqlImpl.EmployeeDAOImpl;
import com.itechart.tarasevi.logic.domain.Attachment;
import com.itechart.tarasevi.logic.domain.ContactPhone;
import com.itechart.tarasevi.logic.domain.Employee;
import com.itechart.tarasevi.logic.domain.Photo;
import com.itechart.tarasevi.logic.exceptions.ExecutingCommandsException;
import com.itechart.tarasevi.logic.exceptions.IncorrectDataException;
import com.itechart.tarasevi.logic.processcommand.ActionCommand;
import com.itechart.tarasevi.logic.utils.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SaveCommand implements ActionCommand {
    private static final Logger LOGGER = LogManager.getLogger(SaveCommand.class);
    private MySqlFactory mySqlFactory = new MySqlFactory();

    @Override
    public String execute(HttpServletRequest request) {
        this.saveContact(request);
        return "";
    }

    private Employee getEmployeeFromJSON(HttpServletRequest request) {

        StringBuilder sb = new StringBuilder();
        Employee employee = new Employee();

        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        String jsonEmployee = new String(sb);
        jsonEmployee = jsonEmployee.replaceAll("_", "");

            if (StringUtils.isNotEmpty(sb.toString())) {
                ObjectMapper mapper = new ObjectMapper();
                employee = mapper.readValue(jsonEmployee, Employee.class);
            }
        } catch (IOException e) {
            LOGGER.error(e);
            throw new IncorrectDataException("Не удаётся извлечь сотрудники из JSON",e);
        }
        return employee;
    }

    private void saveContact(HttpServletRequest request) {
        Employee employee = getEmployeeFromJSON(request);

        savePhoto(employee.getPhotoName(), request);
        savePhones(employee.getPhoneList(), employee.getId());
        saveAttachment(employee.getAttachmentList(), employee.getId(), request);

        EmployeeDAOImpl contactDAO = new EmployeeDAOImpl();
        contactDAO.editEmployee(employee);
        contactDAO.saveContact();
    }

    private void savePhones(List<ContactPhone> phones, final int EMPLOYEE_ID) {
        PhoneDAO phoneDAO = mySqlFactory.getPhoneDAO();
        phoneDAO.deletePhones(getDeletePhoneList(phones, phoneDAO, EMPLOYEE_ID));
        phoneDAO.insertOrUpdatePhones(phones, EMPLOYEE_ID);
    }

    @SuppressWarnings("unchecked")
    private List<ContactPhone> getDeletePhoneList(List<ContactPhone> phones, PhoneDAO phoneDAO, final int EMPLOYEE_ID) {
        List<ContactPhone> oldPhoneList = phoneDAO.getPhoneList(EMPLOYEE_ID);
        List<ContactPhone> deleteList = new ArrayList<>();
        oldPhoneList.forEach(oldPhone -> {
            boolean isDeletedElement = true;
            for (ContactPhone phone : phones) {
                if (Objects.equals(phone.getId(), oldPhone.getId())) {
                    isDeletedElement = false;
                }
            }
            if (isDeletedElement) {
                deleteList.add(oldPhone);
            }
        });
        return deleteList;

    }

    private void saveAttachment(List<Attachment> attachments, final int EMPLOYEE_ID, HttpServletRequest request) {
        AttachmentDAO attachmentDAO = mySqlFactory.getAttachmentDAO();
        attachmentDAO.deleteAttachments(getDeleteAttachmentsList(attachments, request));
        attachmentDAO.insertOrUpdateAttachments(getAttachmentListFromSession(request), EMPLOYEE_ID);

        String filePath = ConfigurationManager.getPathProperty("path.saveFile") + EMPLOYEE_ID + "/";
        for (Attachment attachment : getAttachmentListFromSession(request)) {
            String resultFileName = filePath +
                    attachment.getId();
            if (attachment.isDeleted()) {
                deleteAttachmentFromDisk(resultFileName);
            }
            if (!(attachment.isSaveOnDisk() || attachment.isDeleted())) {
                try {
                    Path path = Paths.get(resultFileName);
                    Files.write(path, attachment.getBytes());
                } catch (IOException e) {
                    LOGGER.error(e);
                    throw new ExecutingCommandsException("Не удаётся сохранить файл на сервере");
                }
            }
        }
    }

    private List<Attachment> getAttachmentListFromSession(HttpServletRequest request) {
        return SessionUtils.getEmployeeFromSession(request).getAttachmentList();
    }

    private List<Attachment> getDeleteAttachmentsList(List<Attachment> attachments, HttpServletRequest
            request) {
        List<Attachment> oldAttachmentList = getAttachmentListFromSession(request);
        List<Attachment> deleteList = new ArrayList<>();
        oldAttachmentList.forEach(oldAttachment -> {
            boolean isDeletedElement = true;
            for (Attachment attachment : attachments) {
                if (Objects.equals(attachment.getId(), oldAttachment.getId())) {
                    isDeletedElement = false;
                }
            }
            if (isDeletedElement) {
                oldAttachment.setDeleted(true);
                deleteList.add(oldAttachment);
            }
        });
        return deleteList;
    }

    private void deleteAttachmentFromDisk(String fileName) {
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            LOGGER.error("can't delete file from server ", e);
            throw new ExecutingCommandsException("Не удаётся удалить файл с сервера");
        }

    }


    private void savePhoto(String photoName, HttpServletRequest request) {
        Photo photo = SessionUtils.getEmployeeFromSession(request).getPhoto();
        PhotoDAO photoDAO = mySqlFactory.getPhotoDAO();
        if (("delete".equals(photoName) && photo.getPhotoName().equals(photoName) && photo.isSaved()) ||
                StringUtils.isEmpty(photoName)) {
            return;
        }
        if ("delete".equals(photoName)) {
            deletePhotoFromDisk(photo);
            photo.setPhotoName(null);
            photoDAO.updatePhoto(photo);
            return;
        }
        photoDAO.updatePhoto(photo);
        String resultFileName = ConfigurationManager.getPathProperty("path.saveFile") + photo.getEmployeeID() + "/photo/";
        File uploadDir = new File(resultFileName);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        resultFileName += photo.getPhotoName();
        try {
            Path path = Paths.get(resultFileName);
            Files.write(path, photo.getBytes());
        } catch (IOException e) {
            LOGGER.error("can't write photo on disk: ", e);
            throw new ExecutingCommandsException("Не удаётся сохранить фото на сервере");}


    }

    private void deletePhotoFromDisk(Photo photo) {
        String resultFileName = ConfigurationManager.getPathProperty("path.saveFile") +
                photo.getEmployeeID() + "/photo/" + photo.getPhotoName();
        deleteAttachmentFromDisk(resultFileName);
    }


}
