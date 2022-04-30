package com.cauossw.snake;

public class RankData {
    private String name;
    private String score;
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getData() {
        return this.name + "," + this.score + "," + this.time;
    }
}
