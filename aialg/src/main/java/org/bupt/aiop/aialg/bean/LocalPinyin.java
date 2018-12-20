package org.bupt.aiop.aialg.bean;

public class LocalPinyin {

    private char word;
    private String pinyin;
    private int tone;
    private String shenMu;
    private String yunMu;

    public char getWord() {
        return word;
    }

    public void setWord(char word) {
        this.word = word;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getTone() {
        return tone;
    }

    public void setTone(int tone) {
        this.tone = tone;
    }

    public String getShenMu() {
        return shenMu;
    }

    public void setShenMu(String shenMu) {
        this.shenMu = shenMu;
    }

    public String getYunMu() {
        return yunMu;
    }

    public void setYunMu(String yunMu) {
        this.yunMu = yunMu;
    }
}
