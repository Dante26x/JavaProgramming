import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.*;

public class work extends JFrame {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private int flag = 0;
    String currentPath = null;
    String currentFileName = null;

    private JFileChooser fileChooser;
    private JMenuBar mb = new JMenuBar();

    private FgMenu mFile;
    private FgMenu mWork;
    private JMenu mList;

    private JMenuItem miNew;
    private JMenuItem miOpen;
    private JMenuItem miSave;
    private JMenuItem miFont;
    private JMenuItem miQuit;

    private JMenuItem huiWen;
    private JMenuItem translate;
    private JMenuItem staticWord;
    private JMenuItem sum;

    private JTextArea ta = new JTextArea();
    private JScrollPane sp = new JScrollPane(ta);
    //private JTextPane tp=new JTextPane();
    private JToolBar mtb = new JToolBar();

    work(String sTitle) throws IOException, ClassNotFoundException {
        super(sTitle);
        fileChooser = new JFileChooser();
        addMenus();
        add(sp);
        setSize(800, 600);
        addToolBar();
        centerWindow();
        readTA();
        //setup();
        this.setResizable(true);
        isChanged();
        addAction();
        MainFrameWindowListener();
    }

    public static void main(String args[]) {
        EventQueue.invokeLater(() -> {
            work fr = null;
            try {
                fr = new work("201806061332——左佑——Java程序设计综合实验");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            fr.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            fr.setVisible(true);
        });
    }

    private void addMenus() {
        setJMenuBar(mb);
        mFile = new FgMenu("文件（F）", KeyEvent.VK_F);
        mWork = new FgMenu("Java上机题目");
        mList = new FgMenu("通讯录");

        miNew = new JMenuItem("新建（N）", KeyEvent.VK_H);
        miOpen = new JMenuItem("打开（O）...", KeyEvent.VK_O);
        miSave = new JMenuItem("保存（S）", KeyEvent.VK_S);
        miFont = new JMenuItem("字体与颜色（F）", KeyEvent.VK_F);
        miQuit = new JMenuItem("退出（X）...", KeyEvent.VK_X);

        huiWen = new JMenuItem("回文数");
        translate = new JMenuItem("数字与英语互译");
        staticWord = new JMenuItem("统计英语数据");
        sum = new JMenuItem("文本文件求和");

        mFile.add(miNew);
        mFile.add(miOpen);
        mFile.add(miSave);
        mFile.addSeparator();
        mFile.add(miFont);
        mFile.addSeparator();
        mFile.add(miQuit);

        mWork.add(huiWen);
        mWork.add(translate);
        mWork.add(staticWord);
        mWork.add(sum);

        mb.add(mFile);
        mb.add(mWork);
        mb.add(getJMenu());
    }

    private void addToolBar() {
        Container c = getContentPane();
        c.add(BorderLayout.NORTH, mtb);
        mtb.setLayout(new FlowLayout(FlowLayout.LEFT));
        FgButton[] btn = {
                new FgButton(new ImageIcon(getClass().getResource("1.png")), "新建文件"),
                new FgButton(new ImageIcon(getClass().getResource("1.png")), "打开文件"),
                new FgButton(new ImageIcon(getClass().getResource("1.png")), "保存文件")
        };
        for (int i = 0; i < btn.length; i++) {
            btn[i].setBorder(BorderFactory.createEmptyBorder());
            mtb.add(btn[i]);
        }
        mtb.setFloatable(false);
    }

    private void centerWindow() {
        Toolkit tk = getToolkit();
        Dimension dm = tk.getScreenSize();
        setLocation((int) (dm.getWidth() - getWidth()) / 2, (int) (dm.getHeight() - getHeight()) / 2);
    }

    private JMenu getJMenu() {
        mList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    try {
                        addressList aL = new addressList();
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        return mList;
    }

    private void addAction() {
        sum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                TestJProgressBar testJProgressBar=new TestJProgressBar(ta);
            }
        });
        huiWen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                huiwen huiwen = new huiwen();
            }
        });
        staticWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Statistics statistics = new Statistics();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        translate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Word word = new Word();
            }
        });
        miNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                newFile();
            }
        });
        miOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    openFile();
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        miSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                save();
            }
        });
        miFont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setFont();
            }
        });
        miQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                exit();
            }
        });
    }

    private void MainFrameWindowListener() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (flag == 2 && currentPath == null) {
                    int result = JOptionPane.showConfirmDialog(work.this, "是否保存更改？", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        work.this.saveAs();
                        int result2 = JOptionPane.showConfirmDialog(work.this, "确定关闭？", "系统提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result2 == JOptionPane.OK_OPTION) {
                            work.this.dispose();
                            work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                        }
                    } else if (result == JOptionPane.NO_OPTION) {
                        work.this.dispose();
                        work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    }
                } else if (flag == 2 && currentPath != null) {
                    int result = JOptionPane.showConfirmDialog(work.this, "是否将更改保存到" + currentPath + "?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        work.this.save();
                        int result2 = JOptionPane.showConfirmDialog(work.this, "确定关闭？", "系统提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result2 == JOptionPane.OK_OPTION) {
                            work.this.dispose();
                            work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                        }
                    } else if (result == JOptionPane.NO_OPTION) {
                        work.this.dispose();
                        work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    }
                } else {
                    int result = JOptionPane.showConfirmDialog(work.this, "确定关闭？", "系统提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        work.this.dispose();
                        work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    }
                }
                try {
                    saveTA();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void isChanged() {
        ta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                Character c = e.getKeyChar();
                if (c != null && !ta.getText().toString().equals("")) {
                    flag = 2;
                }
            }
        });
    }

    private void setFont() {
        font fontChooser = new font(ta);
        fontChooser.showFontDialog(this);
        Font font = fontChooser.getSelectFont();
        ta.setFont(font);
        ta.setForeground(fontChooser.rFColor());
        ta.setBackground(fontChooser.rBColor());
        testLine view = new testLine(font);
        sp.setRowHeaderView(view);
    }

    private void newFile() {
        if (flag == 0 || flag == 1) {
            return;
        } else if (flag == 2 && this.currentPath == null) {
            int result = JOptionPane.showConfirmDialog(work.this, "是否将更改保存", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                this.saveAs();
            } else if (result == JOptionPane.NO_OPTION) {
                this.ta.setText("");
                this.setTitle("201806061332——左佑——Java程序设计综合实验");
                flag = 1;
            }
            return;
        } else if (flag == 2 && this.currentPath != null) {
            int result = JOptionPane.showConfirmDialog(work.this, "是否将更改保存到", this.currentPath, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                this.saveAs();
            } else if (result == JOptionPane.NO_OPTION) {
                this.ta.setText("");
                this.setTitle("201806061332——左佑——Java程序设计综合实验");
                flag = 1;
            }
        } else if (flag == 3) {
            this.ta.setText("");
            this.setTitle("201806061332——左佑——Java程序设计综合实验");
            flag = 1;
        }
    }

    private void save() {
        if (this.currentPath == null) {
            this.saveAs();
            if (this.currentPath == null) {
                return;
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(new File(currentPath));
            fw.write(ta.getText());
            fw.flush();
            flag = 3;
            this.setTitle("201806061332——左佑——Java程序设计综合实验" + this.currentPath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAs() {
        JFileChooser choose = new JFileChooser();
        int result = choose.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = choose.getSelectedFile();
            FileWriter fw = null;
            try {
                fw = new FileWriter(file);
                fw.write(ta.getText());
                currentFileName = file.getName();
                currentPath = file.getAbsolutePath();
                fw.flush();
                this.flag = 3;
                this.setTitle("201806061332——左佑——Java程序设计综合实验" + currentPath);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fw != null)
                        fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void openFile() throws FileNotFoundException, UnsupportedEncodingException {
        if (flag == 2 && this.currentPath == null) {
            int result = JOptionPane.showConfirmDialog(work.this, "是否将更改保存", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                this.saveAs();
            }
        } else if (flag == 2 && this.currentPath != null) {
            int result = JOptionPane.showConfirmDialog(work.this, "是否将更改保存", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                this.save();
            }
        }
        JFileChooser choose = new JFileChooser();
        int result = choose.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = choose.getSelectedFile();
            currentFileName = file.getName();
            currentPath = file.getAbsolutePath();
            flag = 3;
            this.setTitle("201806061332——左佑——Java程序设计综合实验" + this.currentPath);
            BufferedReader br = null;
            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GBK");
                br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                ta.setText(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void exit() {
        if (flag == 2 && currentPath == null) {
            int result = JOptionPane.showConfirmDialog(work.this, "是否保存更改？", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                work.this.saveAs();
                int result2 = JOptionPane.showConfirmDialog(work.this, "确定关闭？", "系统提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result2 == JOptionPane.OK_OPTION) {
                    work.this.dispose();
                    work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
            } else if (result == JOptionPane.NO_OPTION) {
                work.this.dispose();
                work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        } else if (flag == 2 && currentPath != null) {
            int result = JOptionPane.showConfirmDialog(work.this, "是否将更改保存到" + currentPath + "?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                work.this.save();
                int result2 = JOptionPane.showConfirmDialog(work.this, "确定关闭？", "系统提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result2 == JOptionPane.OK_OPTION) {
                    work.this.dispose();
                    work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
            } else if (result == JOptionPane.NO_OPTION) {
                work.this.dispose();
                work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        } else {
            int result = JOptionPane.showConfirmDialog(work.this, "确定关闭？", "系统提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                work.this.dispose();
                work.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }
    }

    private void saveTA() throws IOException {
        File file = new File("TA_install.dat");
        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
        TA_set set = new TA_set();
        set.back = ta.getBackground();
        set.fore = ta.getForeground();
        set.font = ta.getFont();
        oos.writeObject(set);
        oos.close();
    }

    private void readTA() throws IOException, ClassNotFoundException {
        File file = new File("TA_install.dat");
        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            TA_set set;
            set = (TA_set) ois.readObject();
            //System.out.println(set.back);
            ta.setBackground(set.back);
            ta.setForeground(set.fore);
            ta.setFont(set.font);
            testLine view = new testLine(set.font);
            sp.setRowHeaderView(view);
        } else {
            testLine view = new testLine();
            sp.setRowHeaderView(view);
        }
    }

    class FgMenu extends JMenu {
        public FgMenu(String label) {
            super(label);
        }

        public FgMenu(String label, int nAccelerator) {
            super(label);
            setMnemonic(nAccelerator);
        }
    }

    class FgButton extends JButton {
        public FgButton() {
            super();
        }

        public FgButton(Icon icon) {
            super(icon);
        }

        public FgButton(Icon icon, String strToolTipText) {
            super(icon);
            setToolTipText(strToolTipText);
        }

        public FgButton(String text) {
            super(text);
        }

        public FgButton(String text, Icon icon, String strToolTipText) {
            super(text, icon);
            setToolTipText(strToolTipText);
        }
    }

    class TA_set implements Serializable {
        private Color back;
        private Color fore;
        private Font font;
    }

    class Word extends JFrame {
        private JPanel jPanel;
        private JTextField jTextField1;
        private JTextField jTextField2;
        private JButton close;
        private JLabel jLabel1;
        private JLabel jLabel2;
        private final String x[] = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        private final String y[] = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
        private final String z[] = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

        public Word() {
            setTitle("数字与英文互译");
            setVisible(true);
            setBounds(300, 300, 200, 170);
            inti();
            addAction();
            centerWindow();
        }

        private void inti() {
            jLabel1 = new JLabel("输入100以内的数字或数字的英语:");
            jPanel = new JPanel();
            jPanel.add(jLabel1);
            jTextField1 = new JTextField(15);
            jTextField1.setEditable(true);
            jPanel.add(jTextField1);
            jLabel2 = new JLabel("结果:");
            jPanel.add(jLabel2);
            jTextField2 = new JTextField(15);
            jTextField2.setEditable(false);
            jPanel.add(jTextField2);
            close = new JButton("关闭");
            jPanel.add(close, BorderLayout.SOUTH);
            add(jPanel);
        }

        public String format(int i) {
            StringBuilder sb = new StringBuilder();
            if (i != 0) {
                if (i >= 20) {
                    sb.append(z[i / 10 - 2]).append(" ");
                    if (i % 10 != 0)
                        sb.append(x[i % 10]);
                } else if (i >= 10 && i < 20) {
                    sb.append(y[i % 10]).append(" ");
                } else {
                    sb.append(x[i]);
                }
            } else
                sb.append(x[0]);
            return sb.toString();
        }

        public int parse(String str) {
            HashMap<String, Integer> hm = new HashMap<String, Integer>();
            hm.put("zero", 0);
            hm.put("one", 1);
            hm.put("two", 2);
            hm.put("three", 3);
            hm.put("four", 4);
            hm.put("five", 5);
            hm.put("six", 6);
            hm.put("seven", 7);
            hm.put("eight", 8);
            hm.put("nine", 9);
            hm.put("ten", 10);
            hm.put("eleven", 11);
            hm.put("twelve", 12);
            hm.put("thirteen", 13);
            hm.put("fourteen", 14);
            hm.put("fifteen", 15);
            hm.put("sixteen", 16);
            hm.put("seventeen", 17);
            hm.put("eighteen", 18);
            hm.put("nineteen", 19);
            hm.put("twenty", 20);
            hm.put("thirty", 30);
            hm.put("forty", 40);
            hm.put("fifty", 50);
            hm.put("sixty", 60);
            hm.put("seventy", 70);
            hm.put("eighty", 80);
            hm.put("ninety", 90);
            int i = 0, j = 0;
            Boolean x = true;
            String[] k = str.split(" ");
            for (String string : k) {
                if (i < 10 && j > 0) {
                    if (hm.get(string) > 0)
                        x = false;
                } else if (i >= 20 && j > 0) {
                    if (hm.get(string) > 9)
                        x = false;
                } else if (i >= 10 && i < 20 && j > 0) {
                    if (hm.get(string) > 0)
                        x = false;
                }
                if (j > 2)
                    x = false;
                i += hm.get(string);
                j++;
            }
            if (x)
                return i;
            else
                return -1;
        }

        private void addAction() {
            jTextField1.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent documentEvent) {
                    translate();
                }

                @Override
                public void removeUpdate(DocumentEvent documentEvent) {
                    translate();
                }

                @Override
                public void changedUpdate(DocumentEvent documentEvent) {
                    translate();
                }
            });
            close.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    dispose();
                }
            });
        }

        private void translate() {
            String input = jTextField1.getText();
            input = input.toLowerCase();
            String input2 = input;
            if (input.equals("exit") || input.equals("quit")) {
                System.out.println("Exit");
            }
            if (input.matches("\\d+")) {
                //System.out.println("please enter a number");
                if (input.matches("\\s+|\\s?"))
                    System.out.println("Wrong input, please input again");
                else {
                    int num = Integer.parseInt(input);
                    if (num >= 100) {
                        System.out.println("Wrong input, please input again");
                    } else {
                        jTextField2.setText(format(num));
                    }
                    //if(num >= 100)
                    //throw new NumberFormatException();
                }
            } else if (input2.matches("[A-Z,a-z,\\s]+")) {
                //System.out.println("Please enter format");
                //input = sc.nextLine();
                int x;
                if (!input2.matches("\\s+")) {
                    try {
                        x = parse(input2);
                        if (x != -1)
                            jTextField2.setText(String.valueOf(x));
                        else
                            System.out.println("Wrong input, please input again");
                    } catch (Exception WordException) {
                        System.out.println("Wrong input, please input again");
                    }
                } else
                    System.out.println("Wrong input, please input again");
            } else {
                System.out.println("Wrong input, please input again");
            }
            if (input.equals("")) {
                jTextField2.setText("");
            }
        }
    }

    public class Statistics extends JFrame {//定义一个统计（Statistics）类
        private JTextField jTextField1;
        private JTextField jTextField2;
        private Box box1;
        private Box box2;
        private Box box3;
        private Integer count_or;
        private Integer count_3;
        private String str;//读取文本的每一行
        private char letter[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};//含52个字符的数组
        private int total[] = new int[26];//动态申请数组，用于统计26个字母每个字母开头的单词数

        public Statistics() throws IOException {
            setTitle("统计英文数据");
            readIn();
            chart();
            inti();
            centerWindow();
        }

        private void inti() throws IOException {
            setBounds(300, 300, 900, 400);
            setVisible(true);
            File file = new File("chart.JPEG");
            BufferedImage image = ImageIO.read(file);
            box1 = Box.createHorizontalBox();
            box1.add(new JLabel(new ImageIcon(image)));
            box2 = Box.createHorizontalBox();
            box2.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            box2.add(new JLabel("单词中含“or”字符串的单词数量："));
            box2.add(new JLabel(String.valueOf(count_or)));
            box3 = Box.createHorizontalBox();
            box3.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            box3.add(new JLabel("长度为3的单词数量："));
            box3.add(new JLabel(String.valueOf(count_3)));
            Box box;
            box = Box.createHorizontalBox();
            box.add(box1);
            box.add(box2);
            box.add(box3);
            getContentPane().add(box);
        }

        private void readIn() {
            str = ta.getText();
            System.out.println("*******");
            System.out.println(str);//输出读入的数据
            System.out.println(str.replace(',', ' '));
            str = str.replace(',', ' ');//将逗号替换成空格，输出改变后的结果
            System.out.println(str.replace('.', ' '));
            str = str.replace('.', ' ');//将句号替换成空格，输出改变后的结果
            System.out.println(str.replace('?', ' '));
            str = str.replace('?', ' ');//将问号替换成空格，输出改变后的结果
            System.out.println(str.replace('!', ' '));
            str = str.replace('!', ' ');//将感叹号替换成空格，输出改变后的结果
            System.out.println(str.replace(':', ' '));
            str = str.replace(':', ' ');//将封号替换成空格，输出改变后的结果
            System.out.println(str.replace('(', ' '));
            str = str.replace('(', ' ');//将左括号替换成空格，输出改变后的结果
            System.out.println(str.replace(')', ' '));
            str = str.replace(')', ' ');//将右括号替换成空格，输出改变后的结果
            System.out.println(str.replace(" – ", " "));
            str = str.replace(" – ", " ");//将连接符替换成空格，输出改变后的结果
            //注意：此时还未处理单引号
            System.out.println(str);//输出拼接后的结果
            System.out.println();
            String a = str;//类型转换
            String[] split = a.split(" ");//按空格进行拆分a
            int len = split.length;//len为单词总数
            for (int i = 0; i < len; i++) {
                if (split[i].length() > 0 && split[i].charAt(0) == '\'') {//判断每个单词首部字母是否是单引号
                    split[i] = split[i].replace("'", "");    //如果是，将其去掉
                    System.out.println("        dadasdas");
                }
            }
            //---------------------------------------------------------------------------------
            System.out.println();
            int newline = 1;//控制每行输出的单词数，方便查看输出结果
            for (String s : split) {
                if (newline % 30 == 0) {
                    System.out.println(s);//输出每个单词，并且换行
                } else
                    System.out.print(s + " ");//输出每个单词，并且输出一个空格
                newline++;
            }
            System.out.println();
            System.out.println("-----------------------------------***********--------------------------------------------------");
            //---------------------------------单词总数------------------------------------------
            System.out.println();
            int num = 0;
            for (int i = 0; i < len; i++) {//统计单词数
                if (split[i].length() > 0)//判断单词长度是否大于0
                    num++;//若是，则加1
            }
            System.out.println("The total number of words is " + num);//输出结果
            //---------------------------------首字母--------------------------------------------
            for (int i = 0; i < len; i++) {//检验所有单词
                for (int j = 0; j < 52; j++)//52个字母（大小写），每一个字母都进行一次匹配
                    if (split[i].length() > 0 && split[i].charAt(0) == letter[j])//单词长度大于0且首字母等于某个字母
                        total[j % 26]++;//该字母数量加一，注意：a与A是同一个字母
            }
            System.out.println();
            for (int i = 0; i < 26; i++) {//输出所有字母的数量
                System.out.println("The number of words at the beginning of " + letter[i] + " letter is " + total[i]);
            }
            //---------------------------------长度为3的单词数--------------------------------------
            int count1 = 0;
            for (int i = 0; i < len; i++) {
                if (split[i].length() == 3)//判断单词长度是否为3
                    count1++;//计数器加1
            }
            count_3 = count1;
            System.out.println();//输出结果
            System.out.println("The total number of words with length 3 is " + count1);
            //-----------------------------------or的数量----------------------------------------------
            int length = str.length();//用未拆分前的的str的长度，方便使用
            int count = 0;
            for (int i = 0; i < length - 1; i++) {//对所有字母进行判断
                if (str.charAt(i) == 'o' && str.charAt(i + 1) == 'r')//前一个字母为'o'且后一个字母为'r'
                    count++;      //计数器加1
            }
            count_or = count;
            System.out.println("The total number of 'or' is " + count);//输出结果
        }

        private void chart() throws IOException {
            DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();
            for (int i = 0; i < total.length; i++) {
                defaultCategoryDataset.setValue(total[i], ""/*String.valueOf(letter[26+i])*/, String.valueOf(letter[26 + i]));
            }
            JFreeChart chart = ChartFactory.createBarChart("26个字母开头的单词数统计", "字母", "单词数量", defaultCategoryDataset, PlotOrientation.VERTICAL, true, false, false);
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.getDomainAxis().setLabelFont(ta.getFont());
            plot.getDomainAxis().setTickLabelFont(ta.getFont());
            plot.getRangeAxis().setLabelFont(ta.getFont());
            /*------这句代码解决了底部汉字乱码的问题-----------*/
            chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));
            /*------这句代码解决了标题汉字乱码的问题-----------*/
            chart.getTitle().setFont(new Font("宋体", Font.PLAIN, 12));
            ChartUtils.saveChartAsJPEG(new File("chart.JPEG"), chart, 500, 300);
        }

    }

    private class huiwen extends JFrame {
        private JPanel jPanel;
        private JTextField jTextField;
        private JButton judge;
        private JButton close;

        public huiwen() {
            inti();
            action();
        }

        private void inti() {
            setVisible(true);
            setBounds(300, 300, 300, 100);
            jPanel = new JPanel();
            jPanel.add(new JLabel("请输入1~99999之间的整数"));
            jTextField = new JTextField(5);
            jPanel.add(jTextField);
            judge = new JButton("判断是否为回文数");
            close = new JButton("关闭");
            jPanel.add(judge);
            jPanel.add(close);
            add(jPanel);
        }

        private void action() {
            judge.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    judge();
                }
            });
            close.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    dispose();
                }
            });
        }

        private void judge() {
            System.out.print("Please enter string:");
            String line = jTextField.getText();
            if (line.matches("^[0-9]*$") && Integer.parseInt(line) <= 99999) {
                int i = Integer.parseInt(line);
                if (line == Manacher(line)) {
                    if (line.matches("0"))
                        JOptionPane.showMessageDialog(null, "不是回文数");
                    else if (i >= 0)
                        JOptionPane.showMessageDialog(null, "是回文数");
                    else
                        JOptionPane.showMessageDialog(null, "不是回文数");
                }
            } else {
                JOptionPane.showMessageDialog(null, "输入错误");
            }
        }

        private String Manacher(String s) {
            // Insert '#'
            String t = "$#";
            for (int i = 0; i < s.length(); ++i) {
                t += s.charAt(i);
                t += "#";
            }
            t += "@";
            // Process t
            int[] p = new int[t.length()];
            ;
            int mx = 0, id = 0, resLen = 0, resCenter = 0;
            for (int i = 1; i < t.length() - 1; ++i) {
                p[i] = mx > i ? Math.min(p[2 * id - i], mx - i) : 1;
                while (((i - p[i]) >= 0) && ((i + p[i]) < t.length() - 1) && (t.charAt(i + p[i]) == t.charAt(i - p[i])))
                    ++p[i];
                if (mx < i + p[i]) {
                    mx = i + p[i];
                    id = i;
                }
                if (resLen < p[i]) {
                    resLen = p[i];
                    resCenter = i;
                }
            }
            return s.substring((resCenter - resLen) / 2, (resCenter - resLen) / 2 + resLen - 1);
        }
    }

    private class TestJProgressBar{
        BarThread stepper;
        private class BarThread extends Thread{
            private int DELAY=500;
            JProgressBar progressBar;
            private boolean m_bStopped;
            private boolean m_bPaused=false;
            public BarThread(JProgressBar bar) {
                progressBar = bar;
                m_bStopped=false;
                m_bPaused=false;
            }
            public void run() {
                String str=ta.getText();
                String[] Arr;
                Arr=str.split("\n");
                double answer=0;
                int minimum = 0;
                int maximum = Arr.length;
                progressBar.setMinimum(minimum);
                progressBar.setMaximum(maximum);
                for (int i = minimum; i < maximum; i++) {
                    if(m_bStopped){
                        progressBar.setValue(0);
                        break;
                    }
                    try {
                        while(m_bPaused)
                            Thread.sleep(DELAY);
                        String[] Array = Arr[i].split("=");
                        int k=0;
                        for(String Var: Array){
                            if(k==0)
                                k++;
                            else{
                                double j = Double.parseDouble( Var );
                                answer+=j;
                            }
                        }
                        progressBar.setValue(i);
                        progressBar.setString("当前计算到第"+(i+1)+"个，剩余"+(maximum-i-1)+"个");
                    }
                    catch (InterruptedException ignoredException) {
                    }
                }
                if(progressBar.getValue()==maximum-1)
                    JOptionPane.showMessageDialog(null,"结果："+answer);
            }
            public void Pause(boolean bPaused){
                m_bPaused=bPaused;
            }
            public void Stop(boolean bStopped){
                m_bStopped=bStopped;
            }
        }

        public TestJProgressBar(JTextArea ta) {
            JFrame frm= new JFrame("文本文件求和");
            final JProgressBar aJProgressBar = new JProgressBar();
            aJProgressBar.setStringPainted(true);
            aJProgressBar.setBackground(Color.white);
            aJProgressBar.setForeground(Color.blue);
            final JButton btnStart = new JButton("开始");
            final JButton btnStop = new JButton("取消");
            frm.setBounds(300,300,300,100);

            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource()==btnStart){
                        if(stepper==null){
                            stepper=new BarThread(aJProgressBar);
                            btnStop.setEnabled(true);
                            stepper.start();
                        }
                        if(btnStart.getText().equals("开始") | btnStart.getText().equals("继续")){
                            btnStart.setText("暂停");
                            stepper.Pause(false);
                        }
                        else{
                            btnStart.setText("继续");
                            stepper.Pause(true);
                        }
                    }else{
                        if(stepper!=null) {
                            stepper.Pause(false);
                            btnStart.setText("开始");
                            btnStop.setEnabled(false);
                            stepper.Stop(true);
                            stepper = null;
                        }else{
                            stepper=new BarThread(aJProgressBar);
                        }
                    }

                }
            };
            btnStart.addActionListener(actionListener);
            btnStop.addActionListener(actionListener);
            frm.add(aJProgressBar, BorderLayout.NORTH);
            JPanel jp=new JPanel();
            jp.setLayout(new FlowLayout(FlowLayout.RIGHT));
            jp.add(btnStart);
            jp.add(btnStop);
            btnStop.setEnabled(false);
            frm.add(jp, BorderLayout.SOUTH);
            frm.setSize(300, 100);
            //frm.pack();
            frm.setVisible(true);
        }
    }

    public class addressList extends JFrame implements Serializable{
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
        private data data=new data();
        private Object addressList;

        class data implements Serializable{
            public data(){
                data=new Vector<Vector<String>>();
            }
            Vector<Vector<String>> data;
        }
        public addressList() throws IOException, ClassNotFoundException {
            setTitle("通讯录");
            setVisible(true);
            setBounds(300,300,800,300);
            read();
            actionPerformed();
            MainFrameWindowListener();
        }

        public void inti(){
            dataTitle.add("序号");
            dataTitle.add("姓名");
            dataTitle.add("性别");
            dataTitle.add("电话号码");
            dataTitle.add("Email");
            dataTitle.add("QQ");
            tableModel=new DefaultTableModel(data.data,dataTitle){
                public boolean isCellEditable(int row,int column){
                    return false;
                }
            };
            jtable=new JTable(tableModel);
            jtable.setRowSorter(new TableRowSorter<TableModel>(tableModel));
            jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jScrollPane.setViewportView(jtable);
            add(jPanel,BorderLayout.SOUTH);
            add(jScrollPane, BorderLayout.NORTH);
            //jtable.setEnabled(false);
            jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jPanel.add(button1);
            jPanel.add(button2);
            jPanel.add(button3);
            jPanel.add(button4);
        }
        private void MainFrameWindowListener(){
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        save();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
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
            button4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    search search=new search(tableModel);
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

        private void read() throws IOException, ClassNotFoundException {
            File file=new File("address.dat");
            if(file.exists()) {
                data newData=new data();
                FileInputStream fileInputStream=new FileInputStream(file);
                ObjectInputStream ois=null;
                ois=new ObjectInputStream(fileInputStream);
                newData= (data) ois.readObject();
                //System.out.println(newData.data);
                data=newData;
                inti();
                number=tableModel.getRowCount();
                //System.out.println(number);
            }else {
                inti();
                number=0;
            }
        }

        private void save() throws IOException {
            File file=new File("address.dat");
            if(!file.exists()){
                file.createNewFile();
            }else{
                file.delete();
                file.createNewFile();
            }
            ObjectOutputStream oos=null;
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            oos=new ObjectOutputStream(fileOutputStream);
            oos.writeObject(data);
            oos.close();
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

        class search extends JFrame{
            //public static final int CANCEL_OPTION=0;
            //public static final int APPROVE_OPTION=1;
            private JButton searchButton;
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

            search(DefaultTableModel tableModel){
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
                searchButton=new JButton("查找");
                jPanel.add(searchButton);
                cancelButton=new JButton("关闭");
                jPanel.add(cancelButton);
            }

            public void actionPerformed(){
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        dispose();
                    }
                });
                searchButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        search(tableModel);
                    }
                });
            }
            private void search(DefaultTableModel tableModel){
                JFrame jFrame=new JFrame();
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Dimension dimension = toolkit.getScreenSize();
                jFrame.setSize((int) (dimension.width * 0.5), (int) (dimension.height * 0.5));
                jFrame.setLocationRelativeTo(null);
                JTable table=new JTable();
                table.setModel(tableModel);
                table.setAutoCreateRowSorter(true);
                JScrollPane jScrollPane=new JScrollPane();
                jScrollPane.setViewportView(table);
                jFrame.add(jScrollPane);
                jFrame.setVisible(true);
                //TableRowSorter<DefaultTableModel> sorter=new TableRowSorter<DefaultTableModel>(tableModel);
                TableRowSorter<DefaultTableModel> nameSorter=new TableRowSorter<DefaultTableModel>(tableModel);
                TableRowSorter<DefaultTableModel> sexSorter=new TableRowSorter<DefaultTableModel>(tableModel);
                TableRowSorter<DefaultTableModel> phoneSorter=new TableRowSorter<DefaultTableModel>(tableModel);
                TableRowSorter<DefaultTableModel> emailSorter=new TableRowSorter<DefaultTableModel>(tableModel);
                TableRowSorter<DefaultTableModel> qqSorter=new TableRowSorter<DefaultTableModel>(tableModel);
                //table.setRowSorter(sorter);
                if(!name.getText().equals("")){
                    nameSorter.setRowFilter(RowFilter.regexFilter(name.getText()));
                    table.setRowSorter(nameSorter);
                }
                if(!sex.getText().equals("")){
                    sexSorter.setRowFilter(RowFilter.regexFilter(sex.getText()));
                    table.setRowSorter(sexSorter);
                }
                if(!phone.getText().equals("")){
                    phoneSorter.setRowFilter(RowFilter.regexFilter(phone.getText()));
                    table.setRowSorter(phoneSorter);
                }
                if(!email.getText().equals("")){
                    emailSorter.setRowFilter(RowFilter.regexFilter(email.getText()));
                    table.setRowSorter(emailSorter);
                }
                if(!qq.getText().equals("")){
                    qqSorter.setRowFilter(RowFilter.regexFilter(qq.getText()));
                    table.setRowSorter(qqSorter);
                }
                //RowFilter andFilter=RowFilter.andFilter(nameSorter,sexSorter)
                //sorter.setRowFilter(RowFilter.regexFilter());
            }
        }
    }

    public class font extends JDialog {
        public static final int CANCEL_OPTION = 0;
        public static final int APPROVE_OPTION = 1;
        private static final String CHINA_STRING = "程序设计综合实验";
        private static final String ENGLISH_STRING = "JAVA";
        private static final String NUMBER_STRING = "201806061332";
        private Font font;
        private Box box;
        private JTextField fontText = null;
        private JTextField styleText = null;
        private JTextField previewText = null;
        private JTextField sizeText = null;
        private JRadioButton chinaButton = null;
        private JRadioButton englishButton = null;
        private JRadioButton numberButton = null;
        private JList fontList = null;
        private JList styleList = null;
        private JList sizeList = null;
        private JButton cancelButton = null;
        private JButton approveButton = null;
        private JButton colorButton = null;
        private JButton bgButton = null;
        private String[] fontArray = null;
        private String[] styleArray = {"常规", "粗体", "斜体", "粗斜体"};
        private String[] sizeArray = {"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "初号", "小初", "一号", "小一", "二号", "小二", "三号", "小三", "四号", "小四", "五号", "小五", "六号", "小六", "七号", "八号"};
        private int[] sizeIntArray = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 42, 36, 26, 24, 22, 18, 16, 15, 14, 12, 10, 9, 8, 7, 6, 5};
        private int returnValue = CANCEL_OPTION;

        public font(JTextArea ta) {
            setTitle("字体选择");
            Font font = ta.getFont();
            this.font = font;
            init();
            addListener();
            setup();
            setPT(ta);
            setModal(true);
            setResizable(false);
            pack();
        }

        private void setPT(JTextArea ta) {
            previewText.setForeground(ta.getForeground());
            previewText.setBackground(ta.getBackground());
        }

        private void setup() {
            String fontName = font.getFamily();
            int fontStyle = font.getStyle();
            int fontSize = font.getSize();
            boolean b = false;
            for (int i = 0; i < sizeArray.length; i++) {
                if (sizeArray[i].equals(String.valueOf(fontSize))) {
                    b = true;
                    break;
                }
            }
            if (b) {
                sizeList.setSelectedValue(String.valueOf(fontSize), true);
            } else {
                sizeText.setText(String.valueOf(fontSize));
            }
            fontList.setSelectedValue(fontName, true);
            styleList.setSelectedIndex(fontStyle);
            chinaButton.doClick();
            setPreview();
        }

        private void setPreview() {
            Font f = groupFont();
            previewText.setFont(f);
        }

        private Font groupFont() {
            String fontName = fontText.getText();
            int fontStyle = styleList.getSelectedIndex();
            String sizeStr = sizeText.getText().trim();
            if (sizeStr.length() == 0) {
                showErrorDialog("字体大小必须是有效数值");
                return null;
            }
            int fontSize = 0;
            for (int i = 0; i < sizeArray.length; i++) {
                if (sizeStr.equals(sizeArray[i])) {
                    fontSize = sizeIntArray[i];
                    break;
                }
            }
            if (fontSize == 0) {
                try {
                    fontSize = Integer.parseInt(sizeStr);
                    if (fontSize < 1) {
                        showErrorDialog("字体（大小）必须是有效数值");
                        return null;
                    }
                } catch (NumberFormatException nfe) {
                    showErrorDialog("字体（大小必须是有效数值）");
                    return null;
                }
            }
            return new Font(fontName, fontStyle, fontSize);
        }

        private void init() {
            GraphicsEnvironment eq = GraphicsEnvironment.getLocalGraphicsEnvironment();
            fontArray = eq.getAvailableFontFamilyNames();
            box = Box.createVerticalBox();
            box.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            fontText = new JTextField();
            fontText.setEditable(false);
            fontText.setBackground(Color.WHITE);
            styleText = new JTextField();
            styleText.setEditable(false);
            styleText.setBackground(Color.WHITE);
            sizeText = new JTextField("12");
            Document doc = new PlainDocument() {
                public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                    if (str == null) {
                        return;
                    }
                    if (getLength() >= 3) {
                        return;
                    }
                    if (!str.matches("[0-9]+") && !str.equals("初号") && !str.equals("小初") && !str.equals("一号") && !str.equals("小一") && !str.equals("二号") && !str.equals("小二") && !str.equals("三号") && !str.equals("小三") && !str.equals("四号") && !str.equals("小四") && !str.equals("五号") && !str.equals("小五") && !str.equals("六号") && !str.equals("小六") && !str.equals("七号") && !str.equals("八号")) {
                        return;
                    }
                    super.insertString(offs, str, a);
                    sizeList.setSelectedValue(sizeText.getText(), true);
                }
            };
            sizeText.setDocument(doc);
            previewText = new JTextField(20);
            previewText.setHorizontalAlignment(JTextField.CENTER);
            previewText.setBackground(Color.WHITE);
            chinaButton = new JRadioButton("中文预览", true);
            englishButton = new JRadioButton("英文预览");
            numberButton = new JRadioButton("数字预览");
            ButtonGroup bg = new ButtonGroup();
            bg.add(chinaButton);
            bg.add(englishButton);
            bg.add(numberButton);
            fontList = new JList(fontArray);
            styleList = new JList(styleArray);
            sizeList = new JList(sizeArray);
            approveButton = new JButton("确定");
            cancelButton = new JButton("取消");
            colorButton = new JButton("字体颜色");
            bgButton = new JButton("背景颜色");
            Box box1 = Box.createHorizontalBox();
            JLabel l1 = new JLabel("字体");
            JLabel l2 = new JLabel("字形");
            JLabel l3 = new JLabel("大小");
            l1.setPreferredSize(new Dimension(165, 14));
            l1.setMaximumSize(new Dimension(165, 14));
            l1.setMinimumSize(new Dimension(165, 14));
            l2.setPreferredSize(new Dimension(165, 14));
            l2.setMaximumSize(new Dimension(1165, 14));
            l2.setMinimumSize(new Dimension(165, 14));
            l3.setPreferredSize(new Dimension(165, 14));
            l3.setMaximumSize(new Dimension(1165, 14));
            l3.setMinimumSize(new Dimension(165, 14));
            box1.add(l1);
            box1.add(l2);
            box1.add(l3);
            Box box2 = Box.createHorizontalBox();
            fontText.setPreferredSize(new Dimension(160, 20));
            fontText.setMaximumSize(new Dimension(160, 20));
            fontText.setMinimumSize(new Dimension(1160, 20));
            box2.add(fontText);
            box2.add(Box.createHorizontalStrut(5));
            styleText.setPreferredSize(new Dimension(90, 20));
            styleText.setMinimumSize(new Dimension(90, 20));
            styleText.setMaximumSize(new Dimension(90, 20));
            box2.add(styleText);
            box2.add(Box.createHorizontalStrut(5));
            sizeText.setPreferredSize(new Dimension(80, 20));
            sizeText.setMaximumSize(new Dimension(80, 20));
            sizeText.setMinimumSize(new Dimension(80, 20));
            box2.add(sizeText);
            Box box3 = Box.createHorizontalBox();
            JScrollPane sp1 = new JScrollPane(fontList);
            sp1.setPreferredSize(new Dimension(160, 100));
            sp1.setMaximumSize(new Dimension(160, 100));
            sp1.setMinimumSize(new Dimension(160, 100));
            box3.add(sp1);
            box3.add(Box.createHorizontalStrut(5));
            JScrollPane sp2 = new JScrollPane(styleList);
            sp1.setPreferredSize(new Dimension(90, 100));
            sp1.setMaximumSize(new Dimension(90, 100));
            sp1.setMinimumSize(new Dimension(90, 100));
            box3.add(sp2);
            box3.add(Box.createHorizontalStrut(5));
            JScrollPane sp3 = new JScrollPane(sizeList);
            sp1.setPreferredSize(new Dimension(80, 100));
            sp1.setMaximumSize(new Dimension(80, 100));
            sp1.setMinimumSize(new Dimension(80, 100));
            box3.add(sp3);
            Box box4 = Box.createHorizontalBox();
            Box box5 = Box.createVerticalBox();
            JPanel box6 = new JPanel(new BorderLayout());
            box5.setBorder(BorderFactory.createTitledBorder("字符集"));
            box6.setBorder(BorderFactory.createTitledBorder("实例"));
            box5.add(chinaButton);
            box5.add(englishButton);
            box5.add(numberButton);
            box5.setPreferredSize(new Dimension(90, 95));
            box5.setMaximumSize(new Dimension(90, 95));
            box5.setMinimumSize(new Dimension(90, 95));
            box6.add(previewText);
            box6.setPreferredSize(new Dimension(250, 95));
            box6.setMaximumSize(new Dimension(250, 95));
            box6.setMinimumSize(new Dimension(250, 95));
            box4.add(box5);
            box4.add(Box.createHorizontalStrut(4));
            box4.add(box6);
            Box box7 = Box.createHorizontalBox();
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
                    if (!listSelectionEvent.getValueIsAdjusting()) {
                        fontText.setText(String.valueOf(fontList.getSelectedValue()));
                        setPreview();
                    }
                }
            });
            styleList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent listSelectionEvent) {
                    if (!listSelectionEvent.getValueIsAdjusting()) {
                        styleText.setText(String.valueOf(styleList.getSelectedValue()));
                        setPreview();
                    }
                }
            });
            sizeList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent listSelectionEvent) {
                    if (!listSelectionEvent.getValueIsAdjusting()) {
                        if (!sizeText.isFocusOwner()) {
                            sizeText.setText(String.valueOf(sizeList.getSelectedValue()));
                        }
                        setPreview();
                    }
                }
            });
            EncodeAction ea = new EncodeAction();
            colorButton.addActionListener(ea);
            bgButton.addActionListener(ea);
            chinaButton.addActionListener(ea);
            englishButton.addActionListener(ea);
            numberButton.addActionListener(ea);
            approveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    font = groupFont();
                    returnValue = APPROVE_OPTION;
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

        public final int showFontDialog(JFrame owner) {
            setLocationRelativeTo(owner);
            setVisible(true);
            return returnValue;
        }

        public final Font getSelectFont() {
            return font;
        }

        private void disposeDialog() {
            font.this.removeAll();
            font.this.dispose();
        }

        private void showErrorDialog(String errorMessage) {
            JOptionPane.showMessageDialog(this, errorMessage, "错误", JOptionPane.ERROR_MESSAGE);
        }

        public Color rFColor() {
            return previewText.getForeground();
        }

        public Color rBColor() {
            return previewText.getBackground();
        }

        class EncodeAction extends Component implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(chinaButton)) {
                    previewText.setText(CHINA_STRING);
                } else if (e.getSource().equals(englishButton)) {
                    previewText.setText(ENGLISH_STRING);
                } else if (e.getSource().equals(numberButton)) {
                    previewText.setText(NUMBER_STRING);
                } else if (e.getSource().equals(colorButton)) {
                    JColorChooser jcc = new JColorChooser();
                    JOptionPane.showMessageDialog(this, jcc, "选择字体颜色", JOptionPane.PLAIN_MESSAGE);
                    Color color = jcc.getColor();
                    previewText.setForeground(color);
                } else if (e.getSource().equals(bgButton)) {
                    JColorChooser jcc1 = new JColorChooser();
                    JOptionPane.showMessageDialog(this, jcc1, "选择背景颜色", JOptionPane.PLAIN_MESSAGE);
                    Color color = jcc1.getColor();
                    previewText.setBackground(color);
                }
            }
        }
    }

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
}