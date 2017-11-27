package org.bupt.aiop.aialg.bean;

public class WordVector {

    private String word;
    private float[] vector;

    public float[] getVector() {
        return vector;
    }

    public void setVector(float[] vector) {
        this.vector = vector;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
