package com.itechart.tarasevi.logic.dao;

import com.itechart.tarasevi.logic.dao.mysqlImpl.AttachmentDAOImpl;
import com.itechart.tarasevi.logic.dao.mysqlImpl.EmployeeDAOImpl;
import com.itechart.tarasevi.logic.dao.mysqlImpl.PhoneDAOImpl;
import com.itechart.tarasevi.logic.dao.mysqlImpl.PhotoDAOImpl;

public class MySqlFactory {
    public EmployeeDAO getEmployeeDAO(){
        return new EmployeeDAOImpl();
    }
    public AttachmentDAO getAttachmentDAO(){
        return new AttachmentDAOImpl();
    }
    public PhoneDAO getPhoneDAO(){
        return new PhoneDAOImpl();
    }
    public PhotoDAO getPhotoDAO(){
        return new PhotoDAOImpl();
    }
}
