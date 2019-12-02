package com.sxt.work.util;

import java.awt.*;

public class testLine extends  javax.swing.JComponent{
    private final Font DEFAULT_FONT=new Font(Font.MONOSPACED,Font.PLAIN,13);
    public final Color DEFAULT_BACKGROUD=new Color(228,228,228);
    public final Color DEFAULT_FOREGROUD=Color.red;
    public final int nHEIGHT=Integer.MAX_VALUE-1000000;
    public final int MARGIN=5;
    private int lineHeight;
    private int fontLineHeight;
    private int currentRowWidth;
    private FontMetrics fontMetrics;
    public testLine(){
        setFont(DEFAULT_FONT);
        setForeground(DEFAULT_FOREGROUD);
        setBackground(DEFAULT_BACKGROUD);
        setPreferredSize(9999);
    }

    public testLine(Font font){
        setFont(font);
        setForeground(DEFAULT_FOREGROUD);
        setBackground(DEFAULT_BACKGROUD);
        setPreferredSize(9999);
    }

    private void setPreferredSize(int row) {
        int width =fontMetrics.stringWidth(String.valueOf(row));
        if(currentRowWidth<width){
            currentRowWidth=width;
            setPreferredSize(new Dimension(2*MARGIN+width+1,nHEIGHT));
        }
    }
    public void setFont(Font font){
        super.setFont(font);
        fontMetrics=getFontMetrics(getFont());
        fontLineHeight=fontMetrics.getHeight();
    }
    public int getLineHeight(){
        if(lineHeight==0){
            return fontLineHeight;
        }
        return lineHeight;
    }
    public void setLineHeight(){
        if(lineHeight>0){
            this.lineHeight=lineHeight;
        }
    }
    public int getStartOffset(){
        return 4;
    }
    protected void paintComponent(Graphics graphics){
        int nlineHeight=getLineHeight();
        int startOffset=getStartOffset();
        Rectangle drawHere=graphics.getClipBounds();
        graphics.setColor(getBackground());
        graphics.fillRect(drawHere.x,drawHere.y,drawHere.width,drawHere.height);
        graphics.setColor(getForeground());
        int startLineNum=(drawHere.y/nlineHeight)+1;
        int endLineNum=startLineNum+(drawHere.height/nlineHeight);
        int start = (drawHere.y/nlineHeight)*nlineHeight+nlineHeight-startOffset;
        for(int i=startLineNum;i<endLineNum;i++){
            String lineNum=String.valueOf(i);
            int width=fontMetrics.stringWidth(lineNum);
            graphics.drawString(lineNum+" ",MARGIN+currentRowWidth-width-1,start);
            start+=nlineHeight;
        }
        setPreferredSize(endLineNum);
    }
}