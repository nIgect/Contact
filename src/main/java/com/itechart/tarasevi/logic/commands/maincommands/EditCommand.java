package com.itechart.tarasevi.logic.commands.maincommands;

import com.itechart.tarasevi.logic.configuration.ConfigurationManager;
import com.itechart.tarasevi.logic.dao.*;
import com.itechart.tarasevi.logic.dao.mysqlImpl.EmployeeDAOImpl;
import com.itechart.tarasevi.logic.domain.*;
import com.itechart.tarasevi.logic.processcommand.ActionCommand;
import com.itechart.tarasevi.logic.utils.SessionUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

import static com.itechart.tarasevi.logic.configuration.ConfigurationManager.getProperty;

public class EditCommand implements ActionCommand {
    private static final Logger LOGGER = LogManager.getLogger(EditCommand.class.getName());
    private MySqlFactory mySqlFactory = new MySqlFactory();

    @Override
    public String execute(HttpServletRequest request) {

        Employee employee = getEmployeeOnId(request);
        SessionUtils.setEmployeeToSession(request, employee);
        fillAllParameters(request);
        startEditContact();
        return getProperty("path.page.edit");
    }

    private Employee getEmployeeOnId(HttpServletRequest request) {
        Employee employee = new Employee();
        String employeeId = request.getParameter("employee_id");
        final int ID;
        if (employeeId == null) {
            String[] selectedEmpl = request.getParameterValues("check_selected");
            ID = Integer.parseInt(selectedEmpl[0]);
        } else {
            ID = Integer.parseInt(employeeId);
        }
        employee.setId(ID);

        EmployeeDAO contactDAO = mySqlFactory.getEmployeeDAO();
        employee = contactDAO.getEmployeeOnId(employee.getId());

        AttachmentDAO attachmentDAO = mySqlFactory.getAttachmentDAO();
        employee.setAttachmentList(attachmentDAO.getAttachmentList(employee.getId()));

        PhoneDAO phoneDAO = mySqlFactory.getPhoneDAO();
        employee.setPhoneList(phoneDAO.getPhoneList(employee.getId()));

        PhotoDAO photoDAO = mySqlFactory.getPhotoDAO();
        Photo photo = photoDAO.getPhoto(employee.getId());
        employee.setPhoto(photo);

        return employee;
    }

    private void startEditContact() {
        AbstractDAO abstractDAO = new EmployeeDAOImpl();
        abstractDAO.startEditContact();
    }

    public void fillAllParameters(HttpServletRequest request) {
        Employee employee = SessionUtils.getEmployeeFromSession(request);
        this.fillEmployeeParameters(request, employee);
        this.fillAddressParameters(request, employee.getAddress());
        this.fillPhoneParameters(request, employee.getPhoneList());
        this.fillAttachmentParameters(request, employee.getAttachmentList());
        this.fillPhotoParameter(request, employee);
    }

    private void fillEmployeeParameters(HttpServletRequest request, Employee employee) {
        request.setAttribute("employee_id", employee.getId());
        request.setAttribute("first_name", employee.getFirstName());
        request.setAttribute("last_name", employee.getLastName());
        request.setAttribute("patronymic", employee.getPatronymic());
        request.setAttribute("date_of_birth", employee.getDateOfBirth());
        request.setAttribute("gender", employee.getGender());
        request.setAttribute("nationality", employee.getNationality());
        request.setAttribute("family_status", employee.getFamilyStatus());
        request.setAttribute("web_site", employee.getWebSite());
        request.setAttribute("email", employee.getEmail());
        request.setAttribute("work_place", employee.getWorkPlace());

    }

    private void fillAddressParameters(HttpServletRequest request, Address address) {
        request.setAttribute("country", address.getCountryName());
        request.setAttribute("city", address.getCityName());

        request.setAttribute("street", address.getStreetName());
        request.setAttribute("house", address.getHouseNumber());
        request.setAttribute("flat", address.getFlatNumber());
        request.setAttribute("index", address.getIndex());
    }

    private void fillPhoneParameters(HttpServletRequest request, List<ContactPhone> phoneList) {
        request.setAttribute("phoneList", phoneList);
    }

    private void fillAttachmentParameters(HttpServletRequest request, List<Attachment> attachmentList) {
        String filePath = ConfigurationManager.getPathProperty("path.saveFile");
        request.setAttribute("file_path", filePath);
        request.setAttribute("attachList", attachmentList);
    }

    private void fillPhotoParameter(HttpServletRequest request, Employee employee) {
        Photo photo = employee.getPhoto();
        request.setAttribute("show_default_photo", "inline-block");
        try {
            request.setAttribute("photo", getPhotoForJSP(photo, request));
            request.setAttribute("default_photo", getDefaultPhotoForJSP());
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    private String getDefaultPhotoForJSP() throws IOException {
        String resultFileName;
        byte[] data;
        byte[] encodeBase64;

        resultFileName = ConfigurationManager.getPathProperty("path.defaultPhoto");
        File file = new File(resultFileName);

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            data = IOUtils.toByteArray(bufferedInputStream);
            encodeBase64 = Base64.encodeBase64(data);

            return new String(encodeBase64, "UTF-8");
        }
    }

    private String getPhotoForJSP(Photo photo, HttpServletRequest request) throws IOException {

        String resultFileName;
        byte[] encodeBase64;
        if (photo.getBytes() == null || photo.isDeleted()) {
            if (!photo.isSaved() || photo.isDeleted()) {
                resultFileName = ConfigurationManager.getPathProperty("path.defaultPhoto");
                request.setAttribute("show_default_photo", "inline-block");
                request.setAttribute("show_photo", "none");
            } else {
                resultFileName = ConfigurationManager.getPathProperty("path.saveFile") +
                        photo.getEmployeeID() + "/photo/" + photo.getPhotoName();
                request.setAttribute("show_default_photo", "none");
                request.setAttribute("show_photo", "inline-block");
            }


            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(resultFileName))) {

                byte[] data = IOUtils.toByteArray(bufferedInputStream);
                encodeBase64 = Base64.encodeBase64(data);
            }
        } else {
            encodeBase64 = Base64.encodeBase64(photo.getBytes());
        }
        try {
            return new String(encodeBase64, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e);
        }
        return "default";
    }
}
