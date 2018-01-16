package org.bupt.aiop.aialg.bean;

public class WordNer {

    private String word;
    private int byteOffset;
    private int byteLen;
    private String ner;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getNer() {
        return ner;
    }

    public void setNer(String ner) {
        this.ner = ner;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public void setByteOffset(int byteOffset) {
        this.byteOffset = byteOffset;
    }

    public int getByteLen() {
        return byteLen;
    }

    public void setByteLen(int byteLen) {
        this.byteLen = byteLen;
    }
}
