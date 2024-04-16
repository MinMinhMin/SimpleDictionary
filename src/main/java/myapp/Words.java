package myapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Words {

    private int word_capacity;
    public static Map<String,String>meaning=new HashMap<>();
    private Connection sqlite_connection=null;

    public Connection getSqlite_connection() {
        return sqlite_connection;
    }

    public void setSqlite_connection(Connection sqlite_connection) {
        this.sqlite_connection = sqlite_connection;
    }
    Words(){
        try {

            Class.forName("org.sqlite.JDBC");
            Connection connection= DriverManager.getConnection("jdbc:sqlite:wordsApi.db");
            this.sqlite_connection=connection;

            String sql="SELECT* FROM words";
            PreparedStatement statement=this.sqlite_connection.prepareStatement(sql);
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next()){

                meaning.put(resultSet.getString(2), resultSet.getString(3));

            }
            this.word_capacity=meaning.size();
            statement.close();
            PreparedStatement statement1=this.sqlite_connection.prepareStatement("SELECT MAX(word_id) FROM words");
            ResultSet resultSet1=statement1.executeQuery();
            while (resultSet1.next()){
                this.word_capacity=resultSet1.getInt(1);
                break;
            }



        }catch (Exception e){
            System.out.println("Some thing happen!");
        }


    }

    public List<String>auto_complete(String prefix){

        try {
            List<String>all_words_with_prefix=new ArrayList<>();

            String sql="SELECT * FROM words WHERE word_name LIKE ?";
            PreparedStatement statement=this.sqlite_connection.prepareStatement(sql);
            statement.setString(1,prefix+"%");
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next()){
                all_words_with_prefix.add(resultSet.getString(2));
            }
            statement.close();
            return all_words_with_prefix;


        }catch (Exception e){
            System.out.println("Some thing happen!");
        }
        return null;
    }
    public void add_word(String word,String meaning){

        try {

            if(Words.meaning.containsKey(word)){
                return;
            }
            String sql="INSERT INTO words VALUES(?,?,?)";
            PreparedStatement statement=this.sqlite_connection.prepareStatement(sql);
            statement.setInt(1,this.word_capacity+1);
            statement.setString(2,word);
            statement.setString(3,meaning);
            statement.executeUpdate();
            Words.meaning.put(word,meaning);
            this.word_capacity++;
            statement.close();



        }catch (Exception e){
            System.out.println("Some thing happen!");
        }

    }
    public void delete_word(String word){

        try {

            String sql="DELETE FROM words WHERE word_name= ?";
            PreparedStatement statement=this.sqlite_connection.prepareStatement(sql);
            statement.setString(1,word);
            statement.executeUpdate();
            statement.close();
            meaning.remove(word);
            return;

        }catch (Exception e){
            System.out.println("Some thing happen!");
        }
        meaning.remove(word);


    }
    public List<String>get_3_random_words(){

        try {

            List<String>randomWords=new ArrayList<>();

            String sql = "SELECT * FROM words WHERE LENGTH(CAST(word_name AS TEXT)) <= ? ORDER BY RANDOM() LIMIT 3";
            PreparedStatement statement=this.sqlite_connection.prepareStatement(sql);
            statement.setInt(1,8);
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next()){

                randomWords.add(resultSet.getString(2));

            }
            statement.close();
            return randomWords;


        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }


}
