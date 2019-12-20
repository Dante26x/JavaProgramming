import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;

import com.sxt.work.util.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.nio.charset.StandardCharsets;
import java.text.AttributedCharacterIterator;
import java.util.*;
import java.util.List;

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
}