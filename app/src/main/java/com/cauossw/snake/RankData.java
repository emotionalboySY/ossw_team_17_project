package com.cauossw.snake;

public class RankData {
    private String name;
    private String score;

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

    public String getData() {
        return this.name + "," + this.score;
    }
}
