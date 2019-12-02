import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import com.sxt.work.util.*;
import java.nio.charset.StandardCharsets;

public class work extends JFrame implements ActionListener{
    public static final String LINE_SEPARATOR=System.getProperty("line.separator");
    private int flag=0;
    String currentPath=null;
    String currentFileName=null;

    private JFileChooser fileChooser;
    private JMenuBar mb=new JMenuBar();

    private FgMenu mFile;
    private FgMenu mWork;
    private FgMenu mList;

    private JMenuItem miNew;
    private JMenuItem miOpen;
    private JMenuItem miSave;
    private JMenuItem miFont;
    private JMenuItem miQuit;

    private JTextArea ta=new JTextArea();
    private JScrollPane sp=new JScrollPane(ta);
    //private JTextPane tp=new JTextPane();
    private JToolBar mtb=new JToolBar();
    work(String sTitle){
        super(sTitle);
        fileChooser = new JFileChooser();
        addMenus();
        testLine view=new testLine();
        sp.setRowHeaderView(view);
        add(sp);
        setSize(800,600);
        addToolBar();
        centerWindow();
        this.setResizable(true);
        isChanged();
        MainFrameWindowListener();
    }

    public static void main(String args[]){
        EventQueue.invokeLater(()->{
            work fr=new work("201806061332——左佑——Java程序设计综合实验");
            fr.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            fr.setVisible(true);
        });
    }
    private void addMenus(){
        setJMenuBar(mb);
        mFile=new FgMenu("文件（F）",KeyEvent.VK_F);
        mWork=new FgMenu("Java上机题目");
        mList=new FgMenu("通讯录",KeyEvent.VK_L);

        miNew=new JMenuItem("新建（N）",KeyEvent.VK_H);
        miOpen =new JMenuItem("打开（O）...",KeyEvent.VK_O);
        miSave =new JMenuItem("保存（S）",KeyEvent.VK_S);
        miFont =new JMenuItem("字体与颜色（F）",KeyEvent.VK_F);
        miQuit =new JMenuItem("退出（X）...",KeyEvent.VK_X);

        miNew.addActionListener((ActionListener) this);
        miOpen.addActionListener((ActionListener)this);
        miSave.addActionListener((ActionListener)this);
        miFont.addActionListener((ActionListener)this);
        miQuit.addActionListener((ActionListener)this);

        miNew.setActionCommand("newFile");
        miOpen.setActionCommand("openFile");
        miSave.setActionCommand("saveFile");
        miFont.setActionCommand("font");
        miQuit.setActionCommand("exit");

        mFile.add(miNew);
        mFile.add(miOpen);
        mFile.add(miSave);
        mFile.addSeparator();
        mFile.add(miFont);
        mFile.addSeparator();
        mFile.add(miQuit);

        mb.add(mFile);
        mb.add(mWork);
        mb.add(mList);
    }
    private void addToolBar(){
        Container c=getContentPane();
        c.add(BorderLayout.NORTH,mtb);
        mtb.setLayout(new FlowLayout(FlowLayout.LEFT));
        FgButton[] btn= {
                new FgButton(new ImageIcon(getClass().getResource("1.png")),"新建文件"),
                new FgButton(new ImageIcon(getClass().getResource("1.png")),"打开文件"),
                new FgButton(new ImageIcon(getClass().getResource("1.png")),"保存文件")
        };
        for(int i=0;i<btn.length;i++){
            btn[i].setBorder(BorderFactory.createEmptyBorder());
            mtb.add(btn[i]);
        }
        mtb.setFloatable(false);
    }
    public void centerWindow(){
        Toolkit tk=getToolkit();
        Dimension dm=tk.getScreenSize();
        setLocation((int)(dm.getWidth()-getWidth())/2,(int)(dm.getHeight()-getHeight())/2);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("newFile")){
            newFile();
        }else if(e.getActionCommand().equals("openFile")){
            try {
                openFile();
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }else if(e.getActionCommand().equals("saveFile")){
            save();
        }else if(e.getActionCommand().equals("exit")){
            exit();
        }else if(e.getActionCommand().equals("font")){
            font fontChooser=new font(ta);
            fontChooser.showFontDialog(this);
            Font font=fontChooser.getSelectFont();
            ta.setFont(font);
            ta.setForeground(fontChooser.rFColor());
            ta.setBackground(fontChooser.rBColor());
            testLine view=new testLine(font);
            sp.setRowHeaderView(view);
        }
    }
    private void MainFrameWindowListener(){
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(flag==2&&currentPath==null){
                    int result=JOptionPane.showConfirmDialog(work.this,"是否保存更改？","记事本",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
                    if(result==JOptionPane.OK_OPTION){
                        work.this.saveAs();
                        int result2=JOptionPane.showConfirmDialog(work.this,"确定关闭？","系统提示",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
                        if(result2==JOptionPane.OK_OPTION){
                            work.this.dispose();
                            work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                        }
                    }else if(result==JOptionPane.NO_OPTION){
                        work.this.dispose();
                        work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    }
                }else if(flag==2&&currentPath!=null){
                    int result=JOptionPane.showConfirmDialog(work.this,"是否将更改保存到"+currentPath+"?","记事本",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
                    if(result==JOptionPane.OK_OPTION){
                        work.this.save();
                        int result2=JOptionPane.showConfirmDialog(work.this,"确定关闭？","系统提示",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
                        if(result2==JOptionPane.OK_OPTION){
                            work.this.dispose();
                            work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                        }
                    }else if(result==JOptionPane.NO_OPTION){
                        work.this.dispose();
                        work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    }
                }else{
                    int result=JOptionPane.showConfirmDialog(work.this,"确定关闭？","系统提示",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
                    if(result==JOptionPane.OK_OPTION){
                        work.this.dispose();
                        work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    }
                }
            }
        });
    }

    private void isChanged() {
        ta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped (KeyEvent e) {
            Character c=e.getKeyChar();
            if(c != null && !ta.getText().toString().equals("")){
                flag=2;
            }
        }
        });
    }
    private void newFile(){
        if(flag==0||flag==1){
            return;
        }else if(flag==2&&this.currentPath==null){
            int result=JOptionPane.showConfirmDialog(work.this,"是否将更改保存","记事本",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.saveAs();
            }else if(result==JOptionPane.NO_OPTION){
                this.ta.setText("");
                this.setTitle("201806061332——左佑——Java程序设计综合实验");
                flag=1;
            }
            return;
        }else if(flag==2&&this.currentPath!=null){
            int result=JOptionPane.showConfirmDialog(work.this,"是否将更改保存到",this.currentPath,JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.saveAs();
            }else if(result==JOptionPane.NO_OPTION){
                this.ta.setText("");
                this.setTitle("201806061332——左佑——Java程序设计综合实验");
                flag=1;
            }
        }else if(flag==3){
            this.ta.setText("");
            this.setTitle("201806061332——左佑——Java程序设计综合实验");
            flag=1;
        }
    }

    private void save(){
        if(this.currentPath==null){
            this.saveAs();
            if(this.currentPath==null){
                return;
            }
        }
        FileWriter fw=null;
        try{
            fw=new FileWriter(new File(currentPath));
            fw.write(ta.getText());
            fw.flush();
            flag=3;
            this.setTitle("201806061332——左佑——Java程序设计综合实验"+this.currentPath);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(fw!=null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAs(){
        JFileChooser choose=new JFileChooser();
        int result=choose.showSaveDialog(this);
        if(result==JFileChooser.APPROVE_OPTION){
            File file=choose.getSelectedFile();
            FileWriter fw=null;
            try{
                fw=new FileWriter(file);
                fw.write(ta.getText());
                currentFileName=file.getName();
                currentPath=file.getAbsolutePath();
                fw.flush();
                this.flag=3;
                this.setTitle("201806061332——左佑——Java程序设计综合实验"+currentPath);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try{
                    if(fw!=null)
                        fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void openFile() throws FileNotFoundException, UnsupportedEncodingException {
        if(flag==2&&this.currentPath==null){
            int result=JOptionPane.showConfirmDialog(work.this,"是否将更改保存","记事本",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.saveAs();
            }
        }else if(flag==2&&this.currentPath!=null){
            int result=JOptionPane.showConfirmDialog(work.this,"是否将更改保存","记事本",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.save();
            }
        }
        JFileChooser choose=new JFileChooser();
        int result=choose.showOpenDialog(this);
        if(result==JFileChooser.APPROVE_OPTION){
            File file=choose.getSelectedFile();
            currentFileName=file.getName();
            currentPath=file.getAbsolutePath();
            flag=3;
            this.setTitle("201806061332——左佑——Java程序设计综合实验"+this.currentPath);
            BufferedReader br=null;
            try{
                InputStreamReader isr=new InputStreamReader(new FileInputStream(file),"GBK");
                br= new BufferedReader(isr);
                StringBuilder sb=new StringBuilder();
                String line=null;
                while((line=br.readLine())!=null){
                    sb.append(line).append('\n');
                }
                ta.setText(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void exit(){
        if(flag==2&&currentPath==null){
            int result=JOptionPane.showConfirmDialog(work.this,"是否保存更改？","记事本",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                work.this.saveAs();
                int result2=JOptionPane.showConfirmDialog(work.this,"确定关闭？","系统提示",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
                if(result2==JOptionPane.OK_OPTION){
                    work.this.dispose();
                    work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
            }else if(result==JOptionPane.NO_OPTION){
                work.this.dispose();
                work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }else if(flag==2&&currentPath!=null){
            int result=JOptionPane.showConfirmDialog(work.this,"是否将更改保存到"+currentPath+"?","记事本",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                work.this.save();
                int result2=JOptionPane.showConfirmDialog(work.this,"确定关闭？","系统提示",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
                if(result2==JOptionPane.OK_OPTION){
                    work.this.dispose();
                    work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
            }else if(result==JOptionPane.NO_OPTION){
                work.this.dispose();
                work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }else{
            int result=JOptionPane.showConfirmDialog(work.this,"确定关闭？","系统提示",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                work.this.dispose();
                work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }
    }

    class FgMenu extends JMenu{
        public FgMenu(String label){
            super(label);
        }
        public FgMenu (String label,int nAccelerator){
            super(label);
            setMnemonic(nAccelerator);
        }
    }
    class FgButton extends JButton{
        public FgButton(){
            super() ;
        }
        public FgButton(Icon icon){
            super(icon);
        }
        public FgButton(Icon icon,String strToolTipText){
            super(icon);
            setToolTipText(strToolTipText);
        }
        public FgButton(String text){
            super(text);
        }
        public FgButton(String text,Icon icon,String strToolTipText){
            super(text,icon);
            setToolTipText(strToolTipText);
        }
    }
}
