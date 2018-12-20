package org.bupt.aiop.aialg.bean;

public class Sentiment {

    //情感极性
    private int sentiment;
    //积极类别的概率
    private double positive_prob;
    //消极类别的概率
    private double negative_prob;

    public int getSentiment() {
        return sentiment;
    }

    public void setSentiment(int sentiment) {
        this.sentiment = sentiment;
    }

    public double getPositive_prob() {
        return positive_prob;
    }

    public void setPositive_prob(double positive_prob) {
        this.positive_prob = positive_prob;
    }

    public double getNegative_prob() {
        return negative_prob;
    }

    public void setNegative_prob(double negative_prob) {
        this.negative_prob = negative_prob;
    }
}
