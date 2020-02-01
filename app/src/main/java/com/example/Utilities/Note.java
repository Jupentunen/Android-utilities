package com.example.Utilities;

public class Note {
    private String title, content;
    private boolean isSelected;

    /**
     * Builder fot Note object
     * @param title
     * @param content
     */
    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //Getters
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public boolean getIsSelected() { return isSelected; }

    //Setters
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setIsSelected(boolean selected) {this.isSelected = selected; }
}
