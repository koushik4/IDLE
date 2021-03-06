import src.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JFrame  {
    JFrame frame;
    JButton join, create;
    JDBC jdbc;
    int room = 0;
    HashMap<Integer,User> hm = new HashMap<>();
    Main(){
        try{
            jdbc = new JDBC();
        }catch (Exception e){}
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setLayout(new FlowLayout());
        join = new JButton("Join");
        create = new JButton("Create");

        join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField textField = new JTextField();
                JButton button = new JButton("OK");
                textField.setColumns(10);
                JFrame f = new JFrame();
                f.setSize(200,200);
                f.setLayout(new FlowLayout());
                f.add(textField);
                f.add(button);
                f.setVisible(true);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            int room_id = Integer.parseInt(textField.getText());
                            System.out.println(room_id);
                            HashMap<Integer,String> hm = jdbc.getCode(room_id);
                            System.out.println(hm);
                            if(hm.isEmpty()){
                                OpenDialog();
                                return;
                            }
                            Runnable user = new User(jdbc,room_id);
                            Thread t = new Thread(user);
                           // t.setName(String.valueOf(jdbc.hm.getOrDefault(room_id,new ArrayList<>()).size()));
                            t.start();
                            f.setVisible(false);
                        }catch (Exception exception){OpenDialog();}
                    }
                });
            }
        });
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int room_id = jdbc.get_room_id()+1;
                    System.out.println(room_id);
                    Runnable user = new User(jdbc, room_id);
                    Thread t = new Thread(user);
                    t.start();
                    room++;
                }catch(Exception exception){}
            }
        });

        frame.add(join);
        frame.add(create);


        frame.setVisible(true);
    }
    public void OpenDialog(){
        JDialog dialog = new JDialog(frame,"Error",true);
        dialog.setLayout(new FlowLayout());
        JLabel label = new JLabel("Room Number does not exist");
        JButton button1 = new JButton("ok");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        dialog.add(label);
        dialog.add(button1);
        dialog.setSize(200,200);
        dialog.setVisible(true);
    }
    public static void main(String[] args){
        new Main();
    }
}
