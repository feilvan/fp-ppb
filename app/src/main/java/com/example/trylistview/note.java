package com.example.trylistview;

public class note {
    private String title;
    private String content;
    private int id;
    private String created_date;
    private String edited_date;
    public note(String title, String content, int id, String created_date, String edited_date){
        this.title = title;
        this.content = content;
        this.id = id;
        this.created_date = created_date;
        this.edited_date = edited_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String phone) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getEdited_date() {
        return edited_date;
    }

    public void setEdited_date(String edited_date) {
        this.edited_date = edited_date;
    }
}
