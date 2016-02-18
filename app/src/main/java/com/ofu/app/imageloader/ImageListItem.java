package com.ofu.app.imageloader;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by olivier on 2/10/16.
 */
public class ImageListItem implements Serializable{

  private String title;
  private String imageUrl;
  private String date;
  private Long id;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    // Create a DateFormatter object for displaying date in specified format.
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    try {
      this.date = formatter.format(new Date(Long.valueOf(date)));
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
