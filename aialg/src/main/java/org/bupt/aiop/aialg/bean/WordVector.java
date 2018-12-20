package org.bupt.aiop.aialg.bean;

public class WordVector {

    private String word;
    private float[] vec;

    public float[] getVector() {
        return vec;
    }

    public void setVector(float[] vec) {
        this.vec = vec;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
