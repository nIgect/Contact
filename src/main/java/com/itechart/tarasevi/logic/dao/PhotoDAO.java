package com.itechart.tarasevi.logic.dao;

import com.itechart.tarasevi.logic.domain.Photo;

 public interface PhotoDAO{
     Photo getPhoto(final int ID);

     void updatePhoto(Photo photo);
 }
