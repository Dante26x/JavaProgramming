package com.sxt.work.util;

import javafx.scene.control.TextInputDialog;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class addressList extends JFrame{
    private DefaultTableModel tableModel;
    private JTable jtable;
    private int number=0;
    private JScrollPane jScrollPane=new JScrollPane();
    private JPanel jPanel=new JPanel();
    private JButton button1=new JButton("添加");
    private JButton button2=new JButton("删除");
    private JButton button3=new JButton("修改");
    private JButton button4=new JButton("查询");
    private Vector<String> dataTitle=new Vector<String>();
    private Vector<Vector<String>> data=new Vector<Vector<String>>();

    public addressList(){
        setTitle("通讯录");
        setVisible(true);
        setBounds(300,300,800,300);
        inti();
        actionPerformed();
    }

    public void inti(){
        dataTitle.add("序号");
        dataTitle.add("姓名");
        dataTitle.add("性别");
        dataTitle.add("电话号码");
        dataTitle.add("Email");
        dataTitle.add("QQ");
        tableModel=new DefaultTableModel(data,dataTitle);
        jtable=new JTable(tableModel);
        jtable.setRowSorter(new TableRowSorter<TableModel>(tableModel));
        jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane.setViewportView(jtable);
        add(jPanel,BorderLayout.SOUTH);
        add(jScrollPane, BorderLayout.NORTH);
        jPanel.add(button1);
        jPanel.add(button2);
        jPanel.add(button3);
        jPanel.add(button4);
    }
    public void actionPerformed(){
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addAddress add=new addAddress(tableModel);
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                delete();
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                change change=new change(tableModel);
            }
        });
    }

    private void delete(){
        int row=jtable.getSelectedRow();
        int maxRow=jtable.getRowCount();
        if(row!=-1){
            tableModel.removeRow(row);
            number--;
            for(int i=row;i<maxRow-1;i++){
                tableModel.setValueAt(i+1,i,0);
            }
        }
    }

    class addAddress extends JFrame{
        //public static final int CANCEL_OPTION=0;
        //public static final int APPROVE_OPTION=1;
        private JButton approveButton;
        private JButton cancelButton;
        private JScrollPane jScrollPane;
        private JPanel jPanel;
        private JTextArea jTextArea;
        private JTextField name=new JTextField(15);
        private JTextField sex=new JTextField(15);
        private JTextField phone=new JTextField(15);
        private JTextField email=new JTextField(15);
        private JTextField qq=new JTextField(15);
        private Container c;

        addAddress(DefaultTableModel tableModel){
            setTitle("输入数据");
            inti();
            setVisible(true);
            setBounds(300,300,220,320);
            actionPerformed();
        }

        private void inti(){
            jPanel=new JPanel();
            add(jPanel,BorderLayout.CENTER);
            jPanel.add(new JLabel("  姓名  "));
            jPanel.add(name,BorderLayout.BEFORE_LINE_BEGINS);
            jPanel.add(new JLabel("  性别  "),BorderLayout.AFTER_LAST_LINE);
            jPanel.add(sex,BorderLayout.BEFORE_LINE_BEGINS);
            jPanel.add(new JLabel("电话号码"),BorderLayout.AFTER_LINE_ENDS);
            jPanel.add(phone,BorderLayout.BEFORE_LINE_BEGINS);
            jPanel.add(new JLabel("  Email "),BorderLayout.AFTER_LINE_ENDS);
            jPanel.add(email,BorderLayout.BEFORE_LINE_BEGINS);
            jPanel.add(new JLabel("   qq   "),BorderLayout.AFTER_LINE_ENDS);
            jPanel.add(qq,BorderLayout.BEFORE_LINE_BEGINS);
            approveButton=new JButton("确认");
            jPanel.add(approveButton);
            cancelButton=new JButton("取消");
            jPanel.add(cancelButton);
        }

        public void actionPerformed(){
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    dispose();
                }
            });
            approveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    approve(tableModel);
                }
            });
        }
        private void approve(DefaultTableModel tableModel){
            if(name.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"姓名不能为空");
            }else if(sex.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"性别不能为空");
            }else if(phone.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"电话号码不能为空");
            }else{
                String[] temp={String.valueOf(++number),name.getText(),sex.getText(),phone.getText(),email.getText(),qq.getText()};
                tableModel.addRow(temp);
                dispose();
            }
        }
    }

    class change extends JFrame{
        //public static final int CANCEL_OPTION=0;
        //public static final int APPROVE_OPTION=1;
        private int row=jtable.getSelectedRow();
        private JButton approveButton;
        private JButton cancelButton;
        private JScrollPane jScrollPane;
        private JPanel jPanel;
        private JTextArea jTextArea;
        private JTextField name=new JTextField(15);
        private JTextField sex=new JTextField(15);
        private JTextField phone=new JTextField(15);
        private JTextField email=new JTextField(15);
        private JTextField qq=new JTextField(15);
        private Container c;

        change(DefaultTableModel tableModel){
            setTitle("修改数据");
            inti();
            setVisible(true);
            setBounds(300,300,220,320);
            setup();
            actionPerformed();
        }

        private void inti(){
            jPanel=new JPanel();
            add(jPanel,BorderLayout.CENTER);
            jPanel.add(new JLabel("  姓名  "));
            jPanel.add(name,BorderLayout.BEFORE_LINE_BEGINS);
            jPanel.add(new JLabel("  性别  "),BorderLayout.AFTER_LAST_LINE);
            jPanel.add(sex,BorderLayout.BEFORE_LINE_BEGINS);
            jPanel.add(new JLabel("电话号码"),BorderLayout.AFTER_LINE_ENDS);
            jPanel.add(phone,BorderLayout.BEFORE_LINE_BEGINS);
            jPanel.add(new JLabel("  Email "),BorderLayout.AFTER_LINE_ENDS);
            jPanel.add(email,BorderLayout.BEFORE_LINE_BEGINS);
            jPanel.add(new JLabel("   qq   "),BorderLayout.AFTER_LINE_ENDS);
            jPanel.add(qq,BorderLayout.BEFORE_LINE_BEGINS);
            approveButton=new JButton("确认");
            jPanel.add(approveButton);
            cancelButton=new JButton("取消");
            jPanel.add(cancelButton);
        }
        private void setup(){
            if(row!=-1){
                name.setText((String) tableModel.getValueAt(row,1));
                sex.setText((String) tableModel.getValueAt(row,2));
                phone.setText((String) tableModel.getValueAt(row,3));
                email.setText((String) tableModel.getValueAt(row,4));
                qq.setText((String) tableModel.getValueAt(row,5));
            }
        }
        public void actionPerformed(){
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    dispose();
                }
            });
            approveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    approve(tableModel);
                }
            });
        }
        private void approve(DefaultTableModel tableModel){
            if(name.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"姓名不能为空");
            }else if(sex.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"性别不能为空");
            }else if(phone.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"电话号码不能为空");
            }else if(row!=-1){
                String[] temp={String.valueOf(row+1),name.getText(),sex.getText(),phone.getText(),email.getText(),qq.getText()};
                deleteChange();
                tableModel.insertRow(row,temp);
                dispose();
            }
        }
        private void deleteChange(){
            if(row!=-1)
            {
                tableModel.removeRow(row);
            }
        }
    }
}