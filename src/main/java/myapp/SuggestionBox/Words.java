package myapp.SuggestionBox;

import myapp.API;
import myapp.SuggestionBox.WordDetails.WordDetails;

import java.sql.*;
import java.util.*;

public class Words {

    private int word_capacity;
    public static Map<String,List<String>>meaning=new HashMap<>();
    private Connection sqlite_connection=null;

    public Connection getSqlite_connection() {
        return sqlite_connection;
    }

    public void setSqlite_connection(Connection sqlite_connection) {
        this.sqlite_connection = sqlite_connection;
    }
    public Words(){
        try {

            Class.forName("org.sqlite.JDBC");
            Connection connection= DriverManager.getConnection("jdbc:sqlite:wordsApi.db");
            this.sqlite_connection=connection;
            WordDetails.sqlite_connection=connection;
            String sql="SELECT* FROM words";
            PreparedStatement statement=this.sqlite_connection.prepareStatement(sql);
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next()){

                if(!meaning.containsKey(resultSet.getString(2))){
                    meaning.put(resultSet.getString(2),new ArrayList<>());
                }
                meaning.get(resultSet.getString(2)).add(resultSet.getString(3));

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

    public List<String>auto_complete(String prefix) {

        try {
            List<String> allWordsWithPrefix = new ArrayList<>();
            String sql = "SELECT word_name FROM words WHERE word_name LIKE ? LIMIT 100";
            try (PreparedStatement statement = this.sqlite_connection.prepareStatement(sql)) {
                statement.setString(1, prefix + "%");
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        allWordsWithPrefix.add(resultSet.getString(1));
                    }
                }
            }
            return allWordsWithPrefix;
        } catch (Exception e) {
            System.out.println("Something happened!");
            e.printStackTrace(); // Print the exception for debugging purposes
            return Collections.emptyList(); // Return an empty list instead of null
        }
    }
    public void add_word(String word,String meaning){

        try {

            String sql="INSERT INTO words VALUES(?,?,?)";
            PreparedStatement statement=this.sqlite_connection.prepareStatement(sql);
            statement.setInt(1,this.word_capacity+1);
            statement.setString(2,word);
            statement.setString(3,meaning);
            statement.executeUpdate();
            if(!Words.meaning.containsKey(word)){

                Words.meaning.put(word,new ArrayList<>());
            }
            if (!Words.meaning.get(word).contains(meaning)){
                Words.meaning.get(word).add(meaning);

            }
            this.word_capacity++;
            API.addWord(word);
            statement.close();



        }catch (Exception e){
            System.out.println("Some thing happen!");
        }

    }
    public void delete_word(String content){

        String[] word=content.split(":");
        if(word.length!=2){
            System.out.println("Something wrong!");
            return;
        }
        for(int i=0;i<word.length;i++){

            word[i]=word[i].trim();

        }

        try {

            String sql="DELETE FROM words WHERE word_name= ? AND vie_meaning= ?";
            PreparedStatement statement=this.sqlite_connection.prepareStatement(sql);
            statement.setString(1,word[0]);
            statement.setString(2,word[1]);
            statement.executeUpdate();
            statement.close();
            meaning.get(word[0]).remove(word[1]);
            return;

        }catch (Exception e){
            System.out.println("Some thing happen!");
        }
        meaning.get(word[0]).remove(word[1]);



    }
    public List<String>get_4_random_words(){

        try {

            List<String>randomWords=new ArrayList<>();

            String sql = "SELECT * FROM words WHERE LENGTH(CAST(word_name AS TEXT)) <= ? ORDER BY RANDOM() LIMIT 4";
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
