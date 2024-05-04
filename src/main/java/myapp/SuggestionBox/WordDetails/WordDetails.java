package myapp.SuggestionBox.WordDetails;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WordDetails implements Word_Information{

    public boolean check=true;
    public static Connection sqlite_connection=null;


public WordDetails() throws ClassNotFoundException {

    if(sqlite_connection==null){
            try {
                Class.forName("org.sqlite.JDBC");
                Connection connection= DriverManager.getConnection("jdbc:sqlite:wordsApi.db");
                sqlite_connection=connection;


            }catch (Exception e){
                e.printStackTrace();
            }
        }




}

public WordDetails(String word){

    try {
        if(sqlite_connection==null){
            Class.forName("org.sqlite.JDBC");
            Connection connection= DriverManager.getConnection("jdbc:sqlite:wordsApi.db");
            sqlite_connection=connection;
        }


        String sql="SELECT MAX(Homonyms) FROM word_details WHERE word_name= ?";
        PreparedStatement statement=sqlite_connection.prepareStatement(sql);
        statement.setString(1,word);
        ResultSet resultSet=statement.executeQuery();
        int value = 0;
        String s=resultSet.getString(1);
        if(s==null){s="0";check=false;}
        value=Integer.parseInt(s);
        statement.close();
        for(int i=1;i<=value;i++){

            String sql1="SELECT* FROM word_details WHERE word_name = ? AND Homonyms = ?";
            String sql2="SELECT* FROM audio WHERE word_name = ? AND Homonyms = ?";
            PreparedStatement statement1=sqlite_connection.prepareStatement(sql1);
            statement1.setString(1,word);
            statement1.setString(2,String.valueOf(i));
            ResultSet resultSet1=statement1.executeQuery();
            PreparedStatement statement2=sqlite_connection.prepareStatement(sql2);
            statement2.setString(1,word);
            statement2.setString(2,String.valueOf(i));
            Homonyms homonyms=new Homonyms();
            homonyms.setHomonyms(String.valueOf(i));
            ResultSet resultSet2=statement2.executeQuery();
            List<PoS>list_pos=new ArrayList<>();
            while (resultSet1.next()){

                List<Pair>pairs=new ArrayList<>();
                PoS poS=new PoS();
                poS.setpOs(resultSet1.getString(2));
                pairs.add(new Pair(resultSet1.getString(7),resultSet1.getString(4)));
                pairs.add(new Pair(resultSet1.getString(8),resultSet1.getString(5)));
                pairs.add(new Pair(resultSet1.getString(9),resultSet1.getString(6)));

                poS.setPairs(pairs);
                list_pos.add(poS);

            }
            homonyms.setAllPoS(list_pos);
            List<Phonetic>list_phonetic=new ArrayList<>();
            while (resultSet2.next()){
                Phonetic phonetic=new Phonetic();
                phonetic.setType(resultSet2.getString(3));
                phonetic.setIPA(resultSet2.getString(4));
                phonetic.setLink(resultSet2.getString(5));
                list_phonetic.add(phonetic);

            }
            homonyms.setAllPhonetic(list_phonetic);

            allWord_details.add(homonyms);
            statement1.close();
            statement2.close();


        }




    }catch (Exception e){
        e.printStackTrace();
    }

}


}
