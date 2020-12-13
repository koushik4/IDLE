import src.IDLE;

import javax.swing.text.Caret;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import src.JDBC;

// NUMBER OF ROOMS AND
// NUMBERS OF USERS IN EACH ROOM AS LIST
// LIST HAS CONTENT IN THEIR RESPECTIVELY
//CHALO ITS SEE
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
    int user_id;
    IDLE idle;//PROMPT FOR USER
    JDBC jdbc;
    //START THE IDLE AND INITIATE EVERYTHING
    User(JDBC jdbc,int room_id) throws Exception{
        this.room_id = room_id;
        idle = new IDLE();
        this.jdbc = jdbc;
        int id = jdbc.get_id()+1;
        this.user_id = id;
        jdbc.insertUser(room_id,"",id);

    }

    //COMBINE ALL CODES IN THE SPECIFIC ROOM ID
    public String get_String(int room_id) throws Exception{
        HashMap<Integer,String> hm = jdbc.getCode(room_id);
        ArrayList<String> user_code = new ArrayList<>();
        for(String s:hm.values())user_code.add(s);
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
    public void add_to_text(String s,int room_id) throws Exception{
        jdbc.updatetUser(room_id,s,user_id);
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
                    jdbc.flag  = true;
                    int index = idle.textEditor.getCaretPosition();
                    try {
                        HashMap<Integer, String> user = jdbc.getCode(room_id);
                        for(int j:user.keySet()){
                            String s = user.get(j);
                            if(s.length()<=index)continue;
                            s = s.substring(0,index)+s.substring(index+1);
                            try{jdbc.updatetUser(room_id,s,j);}catch (Exception exception1){}
                        }
                    }catch (Exception exception){}
                }
                else jdbc.flag = false;
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        try {
            while (true) {
                if (!jdbc.flag) {
                    add_to_text(idle.textEditor.getText(), this.room_id);
                    String s = get_String(this.room_id);
                    int p = idle.textEditor.getCaretPosition();
                    idle.textEditor.setText(s);
                    idle.textEditor.setCaretPosition(s.length());
                } else {
                    String s = get_String(this.room_id);
                    int p = idle.textEditor.getCaretPosition();
                    idle.textEditor.setText(s);
                    idle.textEditor.setCaretPosition(s.length());
                }
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }catch (Exception e){e.printStackTrace();}

    }
}
