package org.saqib.dev.mnemonic;

/**
 * Created by Saqib on 6/16/2017.
 */

public class ListItem_Task {
    String title;
    String time;
    String Date;
    String category;
    String status;

    public ListItem_Task() {
        this.title = "";
    }

    public ListItem_Task(String title, String time, String date, String category, String status) {
        this.title = title;
        this.time = time;
        Date = date;
        this.category = category;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
