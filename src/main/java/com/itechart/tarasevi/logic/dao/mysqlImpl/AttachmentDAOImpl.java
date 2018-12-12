package com.itechart.tarasevi.logic.dao.mysqlImpl;

import com.itechart.tarasevi.logic.dao.AbstractDAO;
import com.itechart.tarasevi.logic.dao.AttachmentDAO;
import com.itechart.tarasevi.logic.domain.Attachment;
import com.itechart.tarasevi.logic.exceptions.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AttachmentDAOImpl extends AbstractDAO implements AttachmentDAO {
    private static final Logger LOGGER = LogManager.getLogger(AttachmentDAOImpl.class.getName());

    @Override
    public void addAttachment(Attachment attachment) {
        final int EMPLOYEE_ID = attachment.getEmployeeID();
        updatePrepareStatement("INSERT INTO attachments(file_name,date_of_load,comment,employee_id) " +
                "VALUES(?,?,?,?");
        try {
            this.preparedStatement.setString(1, attachment.getFileName());
            this.preparedStatement.setTimestamp(2, Timestamp.valueOf(attachment.getLoadDate()));
            this.preparedStatement.setString(3, attachment.getComment());
            this.preparedStatement.setInt(4,EMPLOYEE_ID);
            this.preparedStatement.executeUpdate();
            attachment.setId(retriveId(preparedStatement));
        } catch (SQLException e) {
            LOGGER.error("can't add attachment to BD ", e);
            throw new DaoException("Не удаётся сохранить файл");
        } finally {
            this.closePreparedStatement("addAttachment");
        }
    }

    @Override
    public int updateAttachment(Attachment attachment) {
        String query = "UPDATE attachments SET file_name=?,date_of_load=?,comment=? WHERE id=?";
        updatePrepareStatement(query);
        try {
            this.preparedStatement.setString(1, attachment.getFileName());
            this.preparedStatement.setTimestamp(2, Timestamp.valueOf(attachment.getLoadDate()));
            this.preparedStatement.setString(3, attachment.getComment());
            this.preparedStatement.setInt(4, attachment.getId());
            this.preparedStatement.executeUpdate();
            return attachment.getId();
        } catch (SQLException e) {
            LOGGER.error("can't update attachment to BD ", e);
            throw new DaoException("Не удаётся обновить файл");
        } finally {
            this.closePreparedStatement("updateAttachment");
        }
    }

    @Override
    public List<Attachment> getAttachmentList(int ID) {
        List<Attachment> attachmentList = new ArrayList<>();
        String query = "select * from attachments WHERE employee_id  = " + ID;

        try {
            this.stmt = connection.createStatement();
            ResultSet e = this.stmt.executeQuery(query);

            while (e.next()) {
                Attachment attachment = new Attachment();
                attachment.setId(e.getInt(1));
                attachment.setEmployeeID(e.getInt(2));
                attachment.setFileName(e.getString(3));
                attachment.setLoadDate(String.valueOf(e.getTimestamp(4).toLocalDateTime()));
                attachment.setComment(e.getString(5));
                attachmentList.add(attachment);
            }
        } catch (SQLException e) {
            LOGGER.error("can't get attachment list ", e);
            throw new DaoException("Не удаётся извлечь файлы");
        } finally {
            this.closeStatement("getAttachmentList");
        }
        return attachmentList;
    }

    @Override
    public void deleteAttachments(List<Attachment> attachments) {
        if(attachments.isEmpty()){
            return;
        }
        String deleteSQL = "DELETE FROM attachments WHERE attachments.id = ?";

        try {
            preparedStatement = connection.prepareStatement(deleteSQL);
            for (Attachment attachment : attachments) {
                preparedStatement.setInt(1, attachment.getId());
                preparedStatement.addBatch();
                LOGGER.info("delete attachment from BD: {}", attachment.getId());
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("can't delete attachment ", e);
            throw new DaoException("Не удаётся удалить файл");
        } finally {
            this.closePreparedStatement("deleteAttachment");
        }
    }

    @Override
    public void insertOrUpdateAttachments(List<Attachment> attachments, final int EMPLOYEE_ID) {
        String query = "INSERT INTO attachments (id,employee_id,file_name, date_of_load,comment) " +
                "VALUES (?,?,?,?,?) " +
                "ON DUPLICATE KEY " +
                "UPDATE employee_id = values(employee_id),file_name = values(file_name)," +
                " date_of_load = values(date_of_load), comment = values(comment)";
        updatePrepareStatement(query);
        try {
            for (Attachment attachment : attachments) {
                if (!attachment.isDeleted()) {
                    preparedStatement.setInt(1, attachment.getId());
                    preparedStatement.setInt(2, EMPLOYEE_ID);
                    preparedStatement.setString(3, attachment.getFileName());
                    preparedStatement.setTimestamp(4, Timestamp.valueOf(attachment.getLoadDate()));
                    preparedStatement.setString(5, attachment.getComment());
                    preparedStatement.addBatch();
                    LOGGER.info("updateOrInsert attachment to BD id: {}", attachment.getId());
                }
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("can't updateOrInsert attachment to BD ", e);
            throw new DaoException("Не удаётся сохранить/обновить список файлов");
        } finally {
            this.closePreparedStatement("insertOrUpdateAttachments");
        }
    }
}
