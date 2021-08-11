package com.qwertynetwork.myapplication.db;

import com.qwertynetwork.myapplication.model.ListItem;

import java.util.List;

public interface OnDataReceived {
    void onReceived(List<ListItem> list);
}
