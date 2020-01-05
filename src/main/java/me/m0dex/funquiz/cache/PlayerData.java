package me.m0dex.funquiz.cache;

import java.text.DecimalFormat;

public class PlayerData {

    private int answeredRight;
    private int answeredWrong;

    public PlayerData() {

        answeredRight = 0;
        answeredWrong = 0;
    }

    public PlayerData(int _answeredRight, int _answeredWrong) {

        answeredRight = _answeredRight;
        answeredWrong = _answeredWrong;
    }

    public boolean isEmpty() {

        return answeredRight == 0 && answeredWrong == 0;
    }

    public void addAnsRight() {
        answeredRight++;
    }

    public void addAnsWrong() {
        answeredWrong++;
    }

    public void setAnsRight(int right) {
        answeredRight = right;
    }

    public void setAnsWrong(int wrong) {
        answeredRight = wrong;
    }

    public int getAnsRight() { return answeredRight; }
    public int getAnsWrong() { return answeredWrong; }
    public String getRatio() {
        DecimalFormat df = new DecimalFormat("#0.##");
        return df.format(answeredRight / (answeredWrong == 0 ? 1 : answeredWrong));
    }
}
