package org.bupt.common.util.excel;

/**
 * Created by ken on 2017/6/6.
 * <p>
 * excel单元格
 */
public class ExcelCell {

    private int row;
    private int col;
    private String content;


    public ExcelCell(int row, int col, String content) {
        this.row = row;
        this.col = col;
        this.content = content;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
