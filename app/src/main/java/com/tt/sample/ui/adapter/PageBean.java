package com.tt.sample.ui.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class PageBean implements MultiItemEntity {
    private String name;
    private int type;


    public PageBean(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
