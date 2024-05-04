package myapp.SuggestionBox;

import myapp.LogController;
import myapp.SuggestionBox.WordDetails.API;
import myapp.Main;
import myapp.MainController;
import myapp.SuggestionBox.WordDetails.WordDetails;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.*;

public class Words {
    public static Map<String, List<String>> meaning = new HashMap<>();
    private static int word_capacity;
    static private Connection sqlite_connection = null;

    public Words() {
        try {

            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:wordsApi.db");
            sqlite_connection = connection;
            WordDetails.sqlite_connection = connection;
            String sql = "SELECT* FROM words";
            PreparedStatement statement = sqlite_connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                if (!meaning.containsKey(resultSet.getString(2))) {
                    meaning.put(resultSet.getString(2), new ArrayList<>());
                }
                meaning.get(resultSet.getString(2)).add(resultSet.getString(3));

            }
            word_capacity = meaning.size();
            statement.close();
            PreparedStatement statement1 = sqlite_connection.prepareStatement("SELECT MAX(word_id) FROM words");
            ResultSet resultSet1 = statement1.executeQuery();
            while (resultSet1.next()) {
                word_capacity = resultSet1.getInt(1);
                break;
            }


        } catch (Exception e) {
            System.out.println("Some thing happen!");
        }


    }

    public static void add_word(String word, String meaning,boolean called) {

        try {

            String sql = "INSERT INTO words VALUES(?,?,?,?)";
            PreparedStatement statement = sqlite_connection.prepareStatement(sql);
            statement.setInt(1, word_capacity + 1);
            statement.setString(2, word);
            statement.setString(3, meaning);
            statement.executeUpdate();
            if (!Words.meaning.containsKey(word)) {

                Words.meaning.put(word, new ArrayList<>());
            }
            if (!Words.meaning.get(word).contains(meaning)) {
                Words.meaning.get(word).add(meaning);

            }
            word_capacity++;
            API.addWord(word);
            statement.close();


        } catch (Exception e) {
            System.out.println("Some thing happen!");
        }

    }

    public static  void add_to_favourite(String word,String meaning){

        try{

            String sql="UPDATE words SET FAVORITE= ? WHERE word_name = ? AND vie_meaning = ?";
            PreparedStatement preparedStatement=sqlite_connection.prepareStatement(sql);
            preparedStatement.setInt(1,1);
            preparedStatement.setString(2,word);
            preparedStatement.setString(3,meaning);
            preparedStatement.executeUpdate();


        }catch (Exception e){

            e.printStackTrace();

        }



    }

    public static  void add_to_unfavourite(String word,String meaning){

        try{

            String sql="UPDATE words SET FAVORITE= ? WHERE word_name = ? AND vie_meaning = ?";
            PreparedStatement preparedStatement=sqlite_connection.prepareStatement(sql);
            preparedStatement.setNull(1, Types.INTEGER);
            preparedStatement.setString(2,word);
            preparedStatement.setString(3,meaning);
            preparedStatement.executeUpdate();


        }catch (Exception e){

            e.printStackTrace();

        }



    }

    public static void delete_word(String content,boolean called) {

        String[] word = content.split(":");
        if (word.length != 2) {
            if(called){Main.mainController.POPUP("Invalid deleting action!",false);}
            System.out.println("Something wrong!");
            return;
        }
        for (int i = 0; i < word.length; i++) {

            word[i] = word[i].trim();

        }

        try {

            String sql = "DELETE FROM words WHERE word_name= ? AND vie_meaning= ?";
            PreparedStatement statement = sqlite_connection.prepareStatement(sql);
            statement.setString(1, word[0]);
            statement.setString(2, word[1]);
            statement.executeUpdate();
            statement.close();
            meaning.get(word[0]).remove(word[1]);
            if(called){Main.mainController.POPUP("Deleted success",true);Main.mainController.getLog().Update(Main.mainController.getDate().getText() + " " + Main.mainController.getTime().getText() + " ( " + "Delete " + word[0] + ": " + word[1] + ")");}
            return;

        } catch (Exception e) {
            if(called){Main.mainController.POPUP("Invalid deleting action!",false);}
            System.out.println("Some thing happen!");
        }
        meaning.get(word[0]).remove(word[1]);


    }

    public static void update_word(String word, String meaning, String new_meaning) {

        delete_word(word + ": " + meaning,false);
        add_word(word, new_meaning,false);
        //Main.mainController.POPUP("Change word success!",true);
    }

    public static void update_from_txt() {
        try {
            FileReader fileReader = new FileReader("data/List.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Map<String, String> word = new HashMap<>();

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {

                String[] words = line.split("\\t");
                if (words.length < 2) {
                    continue;
                }
                word.put(words[0], words[1]);

            }
            bufferedReader.close();
            fileReader.close();
            Set<String> set = meaning.keySet();
            for (String s : set) {
                if (!word.containsKey(s)) {

                    List<String> list = new ArrayList<>();
                    for (String mean : meaning.get(s)) {

                        list.add(mean);
                    }

                    for (String mean : list) {

                        String content = s + ": " + mean;
                        delete_word(content,true);

                    }

                }


            }


        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    public Connection getSqlite_connection() {
        return sqlite_connection;
    }

    public List<String> auto_complete(String prefix,String mode) {

        if(mode=="favorite"){
            try {
                List<String> allWordsWithPrefix = new ArrayList<>();
                if(prefix!="*"){
                    String sql = "SELECT word_name FROM words WHERE word_name LIKE ? AND favorite = 1 LIMIT 100";
                    try (PreparedStatement statement = sqlite_connection.prepareStatement(sql)) {
                        statement.setString(1, prefix + "%");
                        try (ResultSet resultSet = statement.executeQuery()) {
                            while (resultSet.next()) {
                                allWordsWithPrefix.add(resultSet.getString(1));
                            }
                        }
                    }
                    return allWordsWithPrefix;
                }
                else{
                    String sql = "SELECT word_name FROM words WHERE favorite = 1 ";
                    try (PreparedStatement statement = sqlite_connection.prepareStatement(sql)) {
                        try (ResultSet resultSet = statement.executeQuery()) {
                            while (resultSet.next()) {
                                allWordsWithPrefix.add(resultSet.getString(1));
                            }
                        }
                    }
                    return allWordsWithPrefix;
                }


            } catch (Exception e) {
                System.out.println("Something happened!");
                e.printStackTrace(); // Print the exception for debugging purposes
                return Collections.emptyList(); // Return an empty list instead of null
            }
        }
        else{

            try {
                List<String> allWordsWithPrefix = new ArrayList<>();
                String sql = "SELECT word_name FROM words WHERE word_name LIKE ? LIMIT 100";
                try (PreparedStatement statement = sqlite_connection.prepareStatement(sql)) {
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


    }

    public List<String> get_4_random_words() {

        try {

            List<String> randomWords = new ArrayList<>();

            String sql = "SELECT * FROM words WHERE LENGTH(CAST(word_name AS TEXT)) <= ? ORDER BY RANDOM() LIMIT 4";
            PreparedStatement statement = sqlite_connection.prepareStatement(sql);
            statement.setInt(1, 8);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                randomWords.add(resultSet.getString(2));

            }
            statement.close();
            return randomWords;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String get_a_random_word(String mode){

        try{
            if(mode.equals("easy")){
                String sql = "SELECT * FROM words WHERE LENGTH(CAST(word_name AS TEXT)) >= 3 AND LENGTH(CAST(word_name AS TEXT)) <= 8 ORDER BY RANDOM() LIMIT 1";
                PreparedStatement preparedStatement=sqlite_connection.prepareStatement(sql);
                ResultSet resultSet=preparedStatement.executeQuery();
                while (resultSet.next()){
                    return resultSet.getString(2);
                }
                return null;
            }

            if(mode.equals("normal")){
                String sql = "SELECT * FROM words WHERE LENGTH(CAST(word_name AS TEXT)) >= 9 AND LENGTH(CAST(word_name AS TEXT)) <= 11 ORDER BY RANDOM() LIMIT 1";
                PreparedStatement preparedStatement=sqlite_connection.prepareStatement(sql);
                ResultSet resultSet=preparedStatement.executeQuery();
                while (resultSet.next()){
                    return resultSet.getString(2);
                }
                return null;
            }
            if(mode.equals("hard")){
                String sql = "SELECT * FROM words WHERE LENGTH(CAST(word_name AS TEXT)) >= 12 ORDER BY RANDOM() LIMIT 1";
                PreparedStatement preparedStatement=sqlite_connection.prepareStatement(sql);
                ResultSet resultSet=preparedStatement.executeQuery();
                while (resultSet.next()){
                    return resultSet.getString(2);
                }
                return null;
            }
            return null;


        }catch (Exception e){
            e.printStackTrace();
        }
        return null;





    }




}
