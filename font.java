package com.sxt.work.util;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class font extends JDialog{
    public static final int CANCEL_OPTION=0;
    public static final int APPROVE_OPTION=1;
    private static final String CHINA_STRING="程序设计综合实验";
    private static final String ENGLISH_STRING="JAVA";
    private static final String NUMBER_STRING="201806061332";
    private Font font;
    private Box box;
    private JTextField fontText=null;
    private JTextField styleText=null;
    private JTextField previewText=null;
    private JTextField sizeText=null;
    private JRadioButton chinaButton=null;
    private JRadioButton englishButton=null;
    private JRadioButton numberButton=null;
    private JList fontList=null;
    private JList styleList=null;
    private JList sizeList=null;
    private JButton cancelButton=null;
    private JButton approveButton=null;
    private JButton colorButton=null;
    private JButton bgButton=null;
    private String [] fontArray=null;
    private String [] styleArray={"常规","粗体","斜体","粗斜体"};
    private String [] sizeArray={"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "初号", "小初", "一号", "小一", "二号", "小二", "三号", "小三", "四号", "小四", "五号", "小五", "六号", "小六", "七号", "八号"};
    private int [] sizeIntArray={8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 42, 36, 26, 24, 22, 18, 16, 15, 14, 12, 10, 9, 8, 7, 6, 5};
    private int returnValue=CANCEL_OPTION;

    public font(JTextArea ta){
        setTitle("字体选择");
        Font font=ta.getFont();
        this.font=font;
        init();
        addListener();
        setup();
        setPT(ta);
        setModal(true);
        setResizable(false);
        pack();
    }
    private void setPT(JTextArea ta){
        previewText.setForeground(ta.getForeground());
        previewText.setBackground(ta.getBackground());
    }

    private void setup() {
        String fontName=font.getFamily();
        int fontStyle=font.getStyle();
        int fontSize=font.getSize();
        boolean b =false;
        for(int i=0;i<sizeArray.length;i++){
            if(sizeArray[i].equals(String.valueOf(fontSize))){
                b=true;
                break;
            }
        }
        if(b){
                sizeList.setSelectedValue(String.valueOf(fontSize),true);
        }else{
            sizeText.setText(String.valueOf(fontSize));
        }
        fontList.setSelectedValue(fontName,true);
        styleList.setSelectedIndex(fontStyle);
        chinaButton.doClick();
        setPreview();
    }

    private void setPreview() {
        Font f=groupFont();
        previewText.setFont(f);
    }

    private Font groupFont() {
        String fontName=fontText.getText();
        int fontStyle=styleList.getSelectedIndex();
        String sizeStr=sizeText.getText().trim();
        if(sizeStr.length()==0){
            showErrorDialog("字体大小必须是有效数值");
            return null;
        }
        int fontSize = 0;
        for(int i=0;i<sizeArray.length;i++){
            if(sizeStr.equals(sizeArray[i])){
                fontSize=sizeIntArray[i];
                break;
            }
        }
        if(fontSize==0){
            try{
                fontSize=Integer.parseInt(sizeStr);
                if(fontSize<1){
                    showErrorDialog("字体（大小）必须是有效数值");
                    return null;
                }
            }catch(NumberFormatException nfe){
                showErrorDialog("字体（大小必须是有效数值）");
                return null;
            }
        }
        return new Font(fontName,fontStyle,fontSize);
    }

    private void init() {
        GraphicsEnvironment eq =GraphicsEnvironment.getLocalGraphicsEnvironment();
        fontArray=eq.getAvailableFontFamilyNames();
        box=Box.createVerticalBox();
        box.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        fontText=new JTextField();
        fontText.setEditable(false);
        fontText.setBackground(Color.WHITE);
        sizeText=new JTextField("12");
        Document doc =new PlainDocument(){
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                    if(str==null){
                        return;
                    }
                    if(getLength()>=3){
                        return;
                    }
                    if(!str.matches("[0-9]+")&& !str.equals("初号") && !str.equals("小初") && !str.equals("一号") && !str.equals("小一") && !str.equals("二号") && !str.equals("小二") && !str.equals("三号") && !str.equals("小三") && !str.equals("四号") && !str.equals("小四") && !str.equals("五号") && !str.equals("小五") && !str.equals("六号") && !str.equals("小六") && !str.equals("七号") && !str.equals("八号")){
                        return;
                    }
                    super.insertString(offs,str,a);
                    sizeList.setSelectedValue(sizeText.getText(),true);
            }
        };
        sizeText.setDocument(doc);
        previewText=new JTextField(20);
        previewText.setHorizontalAlignment(JTextField.CENTER);
        previewText.setBackground(Color.WHITE);
        chinaButton=new JRadioButton("中文预览",true);
        englishButton=new JRadioButton("英文预览");
        numberButton=new JRadioButton("数字预览");
        ButtonGroup bg =new ButtonGroup();
        bg.add(chinaButton);
        bg.add(englishButton);
        bg.add(numberButton);
        fontList=new JList(fontArray);
        styleList=new JList(styleArray);
        sizeList=new JList(sizeArray);
        approveButton=new JButton("确定");
        cancelButton=new JButton("取消");
        colorButton=new JButton("字体颜色");
        bgButton=new JButton("背景颜色");
        Box box1=Box.createHorizontalBox();
        JLabel l1=new JLabel("字体");
        JLabel l2=new JLabel("字形");
        JLabel l3=new JLabel("大小");
        l1.setPreferredSize(new Dimension(165,14));
        l1.setMaximumSize(new Dimension(165,14));
        l1.setMinimumSize(new Dimension(165,14));
        l2.setPreferredSize(new Dimension(165,14));
        l2.setMaximumSize(new Dimension(1165,14));
        l2.setMinimumSize(new Dimension(165,14));
        l3.setPreferredSize(new Dimension(165,14));
        l3.setMaximumSize(new Dimension(1165,14));
        l3.setMinimumSize(new Dimension(165,14));
        box1.add(l1);
        box1.add(l2);
        box1.add(l3);
        Box box2=Box.createHorizontalBox();
        fontText.setPreferredSize(new Dimension(160,20));
        fontText.setMaximumSize(new Dimension(160,20));
        fontText.setMinimumSize(new Dimension(1160,20));
        box2.add(fontText);
        box2.add(Box.createHorizontalStrut(5));
        //styleText.setPreferredSize(new Dimension(90,20));
        //styleText.setMinimumSize(new Dimension(90,20));
        //styleText.setMaximumSize(new Dimension(90,20));
        //box2.add(styleText);
        //box2.add(Box.createHorizontalStrut(5));
        sizeText.setPreferredSize(new Dimension(80,20));
        sizeText.setMaximumSize(new Dimension(80,20));
        sizeText.setMinimumSize(new Dimension(80,20));
        box2.add(sizeText);
        Box box3=Box.createHorizontalBox();
        JScrollPane sp1=new JScrollPane(fontList);
        sp1.setPreferredSize(new Dimension(160,100));
        sp1.setMaximumSize(new Dimension(160,100));
        sp1.setMinimumSize(new Dimension(160,100));
        box3.add(sp1);
        box3.add(Box.createHorizontalStrut(5));
        JScrollPane sp2=new JScrollPane(styleList);
        sp1.setPreferredSize(new Dimension(90,100));
        sp1.setMaximumSize(new Dimension(90,100));
        sp1.setMinimumSize(new Dimension(90,100));
        box3.add(sp2);
        box3.add(Box.createHorizontalStrut(5));
        JScrollPane sp3=new JScrollPane(sizeList);
        sp1.setPreferredSize(new Dimension(80,100));
        sp1.setMaximumSize(new Dimension(80,100));
        sp1.setMinimumSize(new Dimension(80,100));
        box3.add(sp3);
        Box box4=Box.createHorizontalBox();
        Box box5=Box.createVerticalBox();
        JPanel box6=new JPanel(new BorderLayout());
        box5.setBorder(BorderFactory.createTitledBorder("字符集"));
        box6.setBorder(BorderFactory.createTitledBorder("实例"));
        box5.add(chinaButton);
        box5.add(englishButton);
        box5.add(numberButton);
        box5.setPreferredSize(new Dimension(90,95));
        box5.setMaximumSize(new Dimension(90,95));
        box5.setMinimumSize(new Dimension(90,95));
        box6.add(previewText);
        box6.setPreferredSize(new Dimension(250,95));
        box6.setMaximumSize(new Dimension(250,95));
        box6.setMinimumSize(new Dimension(250,95));
        box4.add(box5);
        box4.add(Box.createHorizontalStrut(4));
        box4.add(box6);
        Box box7=Box.createHorizontalBox();
        box7.add(Box.createHorizontalGlue());
        box7.add(colorButton);
        box7.add(Box.createHorizontalStrut(5));
        box7.add(bgButton);
        box7.add(Box.createHorizontalStrut(5));
        box7.add(approveButton);
        box7.add(Box.createHorizontalStrut(5));
        box7.add(cancelButton);
        box.add(box1);
        box.add(box2);
        box.add(box3);
        box.add(Box.createVerticalStrut(5));
        box.add(box4);
        box.add(Box.createVerticalStrut(5));
        box.add(box7);
        getContentPane().add(box);
    }

    private void addListener() {
        sizeText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                sizeText.selectAll();
            }
            @Override
            public void focusLost(FocusEvent focusEvent) {
                setPreview();
            }
        });
        fontList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if(!listSelectionEvent.getValueIsAdjusting()){
                    fontText.setText(String.valueOf(fontList.getSelectedValue()));
                    setPreview();
                }
            }
        });
        /*styleList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if(!listSelectionEvent.getValueIsAdjusting()){
                    styleText.setText(String.valueOf(styleList.getSelectedValue()));
                    setPreview();
                }
            }
        });*/
        sizeList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if(!listSelectionEvent.getValueIsAdjusting()){
                    if(!sizeText.isFocusOwner()){
                        sizeText.setText(String.valueOf(sizeList.getSelectedValue()));
                    }
                    setPreview();
                }
            }
        });
        EncodeAction ea =new EncodeAction();
        colorButton.addActionListener(ea);
        bgButton.addActionListener(ea);
        chinaButton.addActionListener(ea);
        englishButton.addActionListener(ea);
        numberButton.addActionListener(ea);
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                font=groupFont();
                returnValue=APPROVE_OPTION;
                disposeDialog();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                disposeDialog();
            }
        });
    }
    public final int showFontDialog(JFrame owner){
        setLocationRelativeTo(owner);
        setVisible(true);
        return returnValue;
    }
    public final Font getSelectFont(){
        return font;
    }
    private void disposeDialog(){
        font.this.removeAll();
        font.this.dispose();
    }
    private void showErrorDialog(String errorMessage){
        JOptionPane.showMessageDialog(this,errorMessage,"错误",JOptionPane.ERROR_MESSAGE);
    }
    public Color rFColor(){
        return previewText.getForeground();
    }
    public Color rBColor(){
        return previewText.getBackground();
    }
    class EncodeAction extends Component implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(e.getSource().equals(chinaButton)){
                previewText.setText(CHINA_STRING);
            }else if(e.getSource().equals(englishButton)){
                previewText.setText(ENGLISH_STRING);
            }else if(e.getSource().equals(numberButton)){
                previewText.setText(NUMBER_STRING);
            }else if(e.getSource().equals(colorButton)){
                JColorChooser jcc=new JColorChooser();
                JOptionPane.showMessageDialog(this,jcc,"选择字体颜色", JOptionPane.PLAIN_MESSAGE);
                Color color=jcc.getColor();
                previewText.setForeground(color);
            }else if(e.getSource().equals(bgButton)){
                JColorChooser jcc1= new JColorChooser();
                JOptionPane.showMessageDialog(this, jcc1,"选择背景颜色", JOptionPane.PLAIN_MESSAGE);
                Color color = jcc1.getColor();
                previewText.setBackground(color);
            }
        }
    }
}