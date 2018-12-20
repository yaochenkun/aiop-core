package org.bupt.aiop.aialg.bean;

import com.hankcs.hanlp.corpus.tag.Nature;

public class WordPos {

    private String pos;
    private int byteOffset;
    private int byteLen;
    private Nature nature;

    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
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
