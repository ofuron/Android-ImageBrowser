package com.example.olivier.imageloader;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by olivier on 2/10/16.
 */
public class ImageDataHolder implements Serializable{

  private ArrayList<ImageListItem> list = new ArrayList<ImageListItem>();

  public ArrayList<ImageListItem> getList() {
    return list;
  }

  public void setList(ArrayList<ImageListItem> list) {
    this.list = list;
  }
}
