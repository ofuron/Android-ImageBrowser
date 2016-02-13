package com.ofu.app.imageloader;

import java.io.Serializable;

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
    this.date = date;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
