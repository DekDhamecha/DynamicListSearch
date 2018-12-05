package com.example.dd.dynamiclistsearch;

public class DataModel {
    private String title, subtitle;

    DataModel(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof DataModel) {
            return this.title.equals(((DataModel) obj).getTitle());
        } else {
            return false;
        }
    }
}
