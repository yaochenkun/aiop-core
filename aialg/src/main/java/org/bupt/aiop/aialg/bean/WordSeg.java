package org.bupt.aiop.aialg.bean;

public class WordSeg {

    private String seg;
    private int byteOffset;
    private int byteLen;

    public String getSeg() {
        return seg;
    }

    public void setSeg(String seg) {
        this.seg = seg;
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
