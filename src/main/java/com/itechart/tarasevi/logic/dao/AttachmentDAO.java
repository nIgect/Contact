package com.itechart.tarasevi.logic.dao;

import com.itechart.tarasevi.logic.domain.Attachment;

import java.util.List;

public interface AttachmentDAO{
     void addAttachment(Attachment attachment);

     int updateAttachment(Attachment attachment);

     List<Attachment> getAttachmentList(int ID);

     void deleteAttachments(List<Attachment> attachments);

    void insertOrUpdateAttachments(List<Attachment> attachments, int EMPLOYEE_ID);
}
