package src;
import javax.swing.*;
import javax.swing.text.Caret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import src.Suggestion;
public class IDLE extends JFrame{
    JFrame frame = new JFrame("Online Coding");
    JTextArea textEditor; //CODE
    JPanel text_settings  = new JPanel();//ALL FONT AND TEXT SETTINGS
    JPanel left_line_counter = new JPanel();//FOR THE LINE NUMBER
    JPanel lines_and_code = new JPanel();//LINE NUMBER + CODE
    JComboBox text_settings_font_size;//FONT SIZE
    JComboBox text_settings_font;//FONT
    JComboBox text_settings_language;//PROGRAMMING LANGUAGE
    JComboBox text_settings_mode;//DARK MODE
    JTextField text_settings_find;//TEXT FIELD FOR FIND WORD
    int intendation = 0;
    String intendation_spaces = "    ";
    Suggestion suggestion = new Suggestion();
    String file_name = "";

    String buffer = ""; //COPY,PASTE
    JLabel[] lines = new JLabel[1000];//LINES
    IDLE() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        lines_and_code.setLayout(new BorderLayout());
        file_name = "";

        //SCROLL PANE FOR LINES AND CODE
        JScrollPane scrollPane = new JScrollPane(lines_and_code);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        Add_Text_Editor();
        Add_Text_Settings();
        Add_Line_Counter();

        get_Suggestion();

        this.frame.getContentPane().add(scrollPane,BorderLayout.CENTER);
        frame.setSize(800,500);
        frame.setVisible(true);
    }

    //SUGGESTIONS
    public void get_Suggestion() {
        textEditor.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyChar()=='\n' || e.getKeyChar()==' ')return;
                String[] words = textEditor.getText().split("\n");
                String[] word = words[words.length-1].split(" ");
                ArrayList<String> list = new ArrayList<>(suggestion.autoComplete(word[word.length-1]));
                JPopupMenu popupMenu = new JPopupMenu();
                ArrayList<JMenuItem> l = new ArrayList<>();
                for(String s:list) {
                    l.add(new JMenuItem(s));
                    l.get(l.size()-1).addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String[] words = textEditor.getText().split(" ");
                            words[words.length-1] += s;
                            String str = "";
                            for(String w:words)str += w+" ";
                            textEditor.setText(str);
                        }
                    });
                    popupMenu.add(l.get(l.size()-1));
                }
                Caret c = textEditor.getCaret();
                Point p = c.getMagicCaretPosition();
                if(l.size() !=0 )popupMenu.show(textEditor,p.x,p.y+20);
            }
        });
    }
    //FIND THE WORD
    public void findText(String text){
        String code = textEditor.getText().toLowerCase();
        for(int i=0;i<=code.length()-text.length();i++) {
            if(code.substring(i,i+text.length()).equals(text))
                try {
                    textEditor.getHighlighter().addHighlight(i, i + text.length(), new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
                } catch (Exception e){}
        }
    }
    public String Save_As(){
        JFileChooser fileChooser = new JFileChooser();
        String name = "";
        int option = fileChooser.showSaveDialog(this.frame);
        if(option == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            System.out.println(this.file_name);
            try {
                name = file.getAbsolutePath();
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(this.textEditor.getText());
                fileWriter.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
        return name;
    }
    //TEXT EDITOR + RIGHT CLICK POP MENU + INTENDATION
    public void Add_Text_Editor() {
        this.textEditor = new JTextArea();
        lines_and_code.add(this.textEditor,BorderLayout.CENTER);
        this.textEditor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getButton() != 3)return;
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem save = new JMenuItem("Save");
                JMenuItem selectall = new JMenuItem("Select All");
                JMenuItem copy = new JMenuItem("Copy");
                JMenuItem cut = new JMenuItem("Cut");
                JMenuItem paste = new JMenuItem("Paste");
                JMenuItem save_as = new JMenuItem("Save As");
                JMenuItem new_file = new JMenuItem("New");
                JMenuItem open = new JMenuItem("Open");
                open.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        int option = fileChooser.showDialog(frame, "open");
                        if(option == JFileChooser.APPROVE_OPTION){
                            File file = fileChooser.getSelectedFile();
                            try {
                                FileReader fileReader = new FileReader(file);
                                int i = 0;String s = "";
                                while((i = fileReader.read())!=-1){
                                    s += (char)i;
                                }
                                textEditor.setText(s);
                                frame.setTitle(file.getAbsolutePath());
                            } catch (Exception fileNotFoundException) {

                            }
                        }
                    }
                });
                new_file.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = Save_As();
                        IDLE idle = new IDLE();
                        idle.file_name = name ;
                        idle.setTitle(name);
                    }
                });
                save.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(file_name.equals("")) {
                            Save_As();
                            return;
                        }
                        File file = new File(file_name);
                        FileWriter fileWriter = null;
                        try {
                            fileWriter = new FileWriter(file);
                            fileWriter.write(textEditor.getText());
                            fileWriter.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                    }
                });
                save_as.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String s = Save_As();
                        frame.setTitle(s);
                    }
                });
                copy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        buffer = textEditor.getSelectedText();
                    }
                });
                paste.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String s = textEditor.getText();
                        s += buffer;
                        textEditor.setText(s);
                    }
                });
                selectall.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        textEditor.select(0,textEditor.getText().length());
                    }
                });

                popupMenu.add(new_file);popupMenu.add(open);
                popupMenu.add(save_as);popupMenu.add(save);popupMenu.add(selectall);
                popupMenu.add(copy);popupMenu.add(cut);popupMenu.add(paste);

                popupMenu.show(textEditor, e.getX(),e.getY());
            }
        });
        this.textEditor.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == '{'){
                    String s = textEditor.getText();
                    int n = textEditor.getCaretPosition();
                    s = s.substring(0,textEditor.getCaretPosition())+'}'+s.substring(textEditor.getCaretPosition());
                    textEditor.setText(s);
                    textEditor.setCaretPosition(n);
                    intendation++;
                }
                if(e.getKeyChar() == '('){
                    String s = textEditor.getText();
                    s = s.substring(0,textEditor.getCaretPosition())+')'+s.substring(textEditor.getCaretPosition());
                    textEditor.setText(s);
                    textEditor.setCaretPosition(s.indexOf(')'));
                }
                if(e.getKeyChar() == '['){
                    String s = textEditor.getText();
                    s = s.substring(0,textEditor.getCaretPosition())+']'+s.substring(textEditor.getCaretPosition());
                    textEditor.setText(s);
                    textEditor.setCaretPosition(s.indexOf(']'));
                }
                if(e.getKeyChar() == '\n'){
                    String s = textEditor.getText();
                    if(s.length() != textEditor.getCaretPosition() && s.charAt(textEditor.getCaretPosition()-1) == '}') {
                        String sub = s.substring(0,textEditor.getCaretPosition());
                        int x = 1,count = 0;
                        for(int i=sub.length()-2;i>=0;i--){
                            if(sub.charAt(i) == '}'){x++;count++;}
                            if(sub.charAt(i) == '{')x--;
                        }
                        intendation -= count+1;
                        if(intendation<0)intendation = 0;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyChar() == '\n'){
                    String s = textEditor.getText().substring(0,textEditor.getCaretPosition());
                    for(int i=0;i<intendation;i++)s += intendation_spaces;
                    int n = s.length();
                    int index = (intendation)* intendation_spaces.length()+2;
                    if(s.length()>=index && s.charAt(s.length()-index)=='{') {
                        s += '\n';
                        for (int i = 0; i < intendation - 1; i++) s += intendation_spaces;
                    }
                    s += textEditor.getText().substring(textEditor.getCaretPosition());
                    textEditor.setText(s);
                    textEditor.setCaretPosition(n);
                }
            }
        });
    }

    //FONT + FONT SIZE + LANGUAGE + MODE
    public void Add_Text_Settings() {
        String[] font_size = new String[100];
        text_settings.setLayout(new FlowLayout());
        for(int i=0;i<100;i++)font_size[i] = ""+(i+1);
        text_settings_font_size = new JComboBox(font_size);

        String[] font = {"Arial", "Arial Narrow", "Times New Roman", "Sans Serrif", "Comic Sans MS","Geneva", "Helvetica","Lucida Sans","Kai",
        "Book Antiqua", "Century","Monaco", "Desdemona", "Serif", "Onyx", "MS Gothic", "Zapfino"};
        Arrays.sort(font);
        text_settings_font = new JComboBox(font);
        text_settings_font.setSelectedIndex(0);
        text_settings_font_size.setSelectedIndex(11);

        String[] language = {"Java8", "CPP", "Python3"};
        text_settings_language = new JComboBox(language);

        String[] mode = {"Light","Dark"};
        text_settings_mode = new JComboBox(mode);

        text_settings_find = new JTextField();
        text_settings_find.setColumns(20);
        text_settings_find.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                textEditor.getHighlighter().removeAllHighlights();
                String text = text_settings_find.getText();
                findText(text.toLowerCase());
            }
        });

        text_settings.add(text_settings_font);
        text_settings.add(text_settings_font_size);
        text_settings.add(text_settings_language);
        text_settings.add(text_settings_mode);
        text_settings.add(text_settings_find);

        text_settings_font.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String font = ""+text_settings_font.getSelectedItem();
                int size = text_settings_font_size.getSelectedIndex()+1;
                for(int i=0;i<lines.length;i++)lines[i].setFont(new Font(font,Font.PLAIN,size));
                textEditor.setFont(new Font(font,Font.PLAIN,size));
            }
        });
        text_settings_font_size.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String font = ""+text_settings_font.getSelectedItem();
                int size = text_settings_font_size.getSelectedIndex()+1;
                for(int i=0;i<lines.length;i++)lines[i].setFont(new Font(font,Font.PLAIN,size));
                textEditor.setFont(new Font(font,Font.PLAIN,size));
            }
        });
        text_settings_mode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(text_settings_mode.getSelectedIndex()==1) {
                    textEditor.setBackground(Color.BLACK);
                    textEditor.setForeground(Color.WHITE);
                    textEditor.setCaretColor(Color.WHITE);
                }
                else{
                    textEditor.setBackground(Color.WHITE);
                    textEditor.setForeground(Color.BLACK);
                    textEditor.setCaretColor(Color.BLACK);
                }
            }
        });

        this.frame.add(text_settings, BorderLayout.NORTH);
    }

    //LINE COUNT(UP TO 1000 LOC)
    public void Add_Line_Counter() {
        left_line_counter.setLayout(new BoxLayout(left_line_counter, BoxLayout.Y_AXIS));
        for(int i=0;i<lines.length;i++){
            lines[i] = new JLabel("\t");
            left_line_counter.add(lines[i]);
        }
        String spaces = "        \t";
        lines[0].setText("1"+spaces);
        lines_and_code.add(left_line_counter,BorderLayout.WEST);
        textEditor.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                for(int i=0;i<textEditor.getLineCount();i++)lines[i].setText(""+(i+1)+spaces);
                for(int i=textEditor.getLineCount();i<lines.length;i++)lines[i].setText(spaces);
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        new IDLE();
    }
}
