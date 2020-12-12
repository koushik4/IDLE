import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JFrame {
    JFrame frame;
    JButton join, create;
    Text text = new Text();
    int room = 0;
    HashMap<Integer,User> hm = new HashMap<>();
    Main(){
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
                        int room_id = Integer.parseInt(textField.getText());
                        if(!text.hm.containsKey(room_id)){
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
                            return;
                        }
                        Runnable user = new User(text,room_id);
                        Thread t = new Thread(user);
                        t.setName(String.valueOf(text.hm.getOrDefault(room_id,new ArrayList<>()).size()));
                        t.start();
                        f.setVisible(false);
                    }
                });
            }
        });
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int room_id = room;
                Runnable user = new User(text,room_id);
                Thread t = new Thread(user);
                t.setName(String.valueOf(text.hm.getOrDefault(room_id,new ArrayList<>()).size()));
                t.start();
                room++;
            }
        });

        frame.add(join);
        frame.add(create);


        frame.setVisible(true);
    }
    public static void main(String[] args){
        new Main();
    }

}
