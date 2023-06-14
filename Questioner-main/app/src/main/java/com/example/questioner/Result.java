package com.example.questioner;

public class Result {
    String username,date,marks,topic;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Result(String username, String date, String marks, String topic) {
        this.username = username;
        this.date = date;
        this.marks = marks;
        this.topic = topic;
    }
}
