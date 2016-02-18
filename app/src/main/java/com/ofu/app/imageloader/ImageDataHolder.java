package com.ofu.app.imageloader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by olivier on 2/10/16.
 */
public class ImageDataHolder implements Serializable{

  private ArrayList<ImageListItem> list = new ArrayList<ImageListItem>();

  private Map<Long, ImageListItem> selectedItems = new HashMap<>();

  public ArrayList<ImageListItem> getList() {
    return list;
  }

  public void setList(ArrayList<ImageListItem> list) {
    this.list = list;
  }

  public void addItemToSelected(Long id, ImageListItem item) {
    selectedItems.put(id, item);
  }

  public void removeItemFromSelected(Long id) {
    if(selectedItems.containsKey(id)) {
      selectedItems.remove(id);
    }
  }

  public Map<Long, ImageListItem> getSelectedItems() {
    return selectedItems;
  }
}
