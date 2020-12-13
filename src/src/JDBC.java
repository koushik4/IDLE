package src;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class JDBC {
    Connection connection;
    String get_code;
    String insert;
    String update;
    String get_id;
    public boolean flag;

    public JDBC() throws Exception{
        String url = "jdbc:mysql://localhost:3306/OnlineCoding1";
        String username = "root";
        String pass = "lcspick_123";
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(url,username,pass);
        get_code = "SELECT * FROM CODE WHERE room_id = ?";
        insert = "INSERT INTO CODE VALUES (?,?,?)";
        update = "UPDATE CODE SET code = ? WHERE room_id = ? and id = ?";
        get_id = "SELECT MAX(ID) FROM CODE";
        flag = false;
    }
    public HashMap<Integer,String> getCode(int room_id) throws Exception{
        PreparedStatement preparedStatement = connection.prepareStatement(get_code);
        preparedStatement.setInt(1, room_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        HashMap<Integer,String> hm = new HashMap<>();
        while(resultSet.next()){
            String s = resultSet.getString("code");
            int id = resultSet.getInt("id");
            hm.put(id,s);
        }
        return hm;
    }
    public void insertUser(int room_id, String code, int id) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setInt(1, room_id);
        preparedStatement.setString(2, code);
        preparedStatement.setInt(3, id);
        preparedStatement.executeUpdate();
    }
    public void updatetUser(int room_id, String code, int id) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement(update);
        preparedStatement.setString(1, code);
        preparedStatement.setInt(2, room_id);
        preparedStatement.setInt(3, id);
        preparedStatement.executeUpdate();
    }
    public int get_id() throws Exception{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(get_id);
        resultSet.next();
        return resultSet.getInt(1);
    }
    public int get_room_id() throws Exception{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(get_id);
        resultSet.next();
        return resultSet.getInt(1);
    }
    public static void main(String[] args) throws Exception{
        JDBC jdbc = new JDBC();
    }
}
