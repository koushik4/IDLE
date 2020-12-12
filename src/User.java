import src.IDLE;

import javax.swing.text.Caret;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

// NUMBER OF ROOMS AND
// NUMBERS OF USERS IN EACH ROOM AS LIST
// LIST HAS CONTENT IN THEIR RESPECTIVELY
class Text {
    HashMap<Integer,ArrayList<String>> hm;
    boolean flag;
    Text(){
        hm = new HashMap<>();
        flag = false;
    }
}
public class User implements Runnable{
    int room_id;//ID OF ROOM
    IDLE idle;//PROMPT FOR USER
    Text text;
    //START THE IDLE AND INITIATE EVERYTHING
    User(Text t,int room_id){
        this.room_id = room_id;
        idle = new IDLE();
        text = t;
        if(!text.hm.containsKey(room_id))text.hm.put(room_id,new ArrayList<>());
        text.hm.get(room_id).add("");
    }

    //COMBINE ALL CODES IN THE SPECIFIC ROOM ID
    public String get_String(int room_id){
        ArrayList<String> user_code = text.hm.get(room_id);
        int max_len = 0;
        for(String s:user_code)max_len = Math.max(s.length(),max_len);
        int i = 0;
        String str = "";
        while(i<max_len){
            for(String s:user_code){
                if(s.length() <= i)continue;
                str += s.charAt(i);
                i++;
                break;
            }
        }
        return str;
    }

    //WHENEVER USER TYPES ANYTHING UPDATE THEAT IN THE LIST
    public void add_to_text(String s,int room_id){
        int index = Integer.parseInt(Thread.currentThread().getName())-1;
        text.hm.get(room_id).set(index,s);
    }

    //UPDATES THE CODE IN LIST AND GETS THE COMBINES STRING
    //UPDATE THE CODE IN ALL IDLES
    //TAKES CARE OF BACKSPACES
    @Override
    public void run() {
        idle.textEditor.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                int i = (int)c;
                if(i == 8){
                    text.flag  = true;
                    int index = idle.textEditor.getCaretPosition();
                    ArrayList<String> user = text.hm.get(room_id);
                    for(int j=0;j<user.size();j++){
                        String s = user.get(j);
                        if(s.length()<=index)continue;
                        s = s.substring(0,index)+s.substring(index+1);
                        text.hm.get(room_id).set(j,s);
                    }
                }
                else text.flag = false;
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        while(true) {
            if(!text.flag) {
                add_to_text(idle.textEditor.getText(),this.room_id);
                String s = get_String(this.room_id);
                int p = idle.textEditor.getCaretPosition();
                idle.textEditor.setText(s);
                idle.textEditor.setCaretPosition(p);
            }
            else{
                String s = get_String(this.room_id);
                int p = idle.textEditor.getCaretPosition();
                idle.textEditor.setText(s);
                idle.textEditor.setCaretPosition(s.length());
            }
            try {
                Thread.sleep(500);
            }catch (Exception e){}
        }
    }
    public static void main(String[] args){
        Text text = new Text();
        Runnable u1 = new User(text,0);
        Runnable u2 = new User(text,0);
        Runnable u3 = new User(text,0);
        Thread user1 = new Thread(u1);
        user1.setName("1");
        Thread user2 = new Thread(u2);
        user2.setName("2");
        Thread user3 = new Thread(u3);
        user3.setName("3");
        user1.start();
        user2.start();
        user3.start();
    }
}
