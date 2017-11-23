package org.bupt.aiop.aialg.bean;

public class WordVector {

    private String word;
    private float[] elementArray;

    public float[] getElementArray() {
        return elementArray;
    }

    public void setElementArray(float[] elementArray) {
        this.elementArray = elementArray;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
