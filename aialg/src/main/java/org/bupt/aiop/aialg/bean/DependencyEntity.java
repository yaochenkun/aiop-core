package org.bupt.aiop.aialg.bean;

public class DependencyEntity {

    // 词的ID
    private int id;
    // 词
    private String word;
    // 词性
    private String postag;
    // 词的父节点的ID
    private int head;
    // 词与父节点的依赖关系
    private String deprel;

    public DependencyEntity(int id, String word, String postag, int head, String deprel) {
        this.id = id;
        this.word = word;
        this.postag = postag;
        this.head = head;
        this.deprel = deprel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPostag() {
        return postag;
    }

    public void setPostag(String postag) {
        this.postag = postag;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public String getDeprel() {
        return deprel;
    }

    public void setDeprel(String deprel) {
        this.deprel = deprel;
    }
}
