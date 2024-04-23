package myapp.SuggestionBox.WordDetails;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WordDetails {

    public boolean check=true;
    private String word;
    private Connection sqlite_connection=null;

    public Connection getSqlite_connection() {
        return sqlite_connection;
    }


    public List<Homonyms>allWord_details=new ArrayList<>();

    public static class Homonyms{

        private String homonyms;

        private List<PoS> allPoS=new ArrayList<>();

        private List<Phonetic> allPhonetic=new ArrayList<>();


        public String getHomonyms() {
            return homonyms;
        }

        public void setHomonyms(String homonyms) {
            this.homonyms = homonyms;
        }

        public List<PoS> getAllPoS() {
            return allPoS;
        }

        public void setAllPoS(List<PoS> allPoS) {
            this.allPoS = allPoS;
        }

        public List<Phonetic> getAllPhonetic() {
            return allPhonetic;
        }

        public void setAllPhonetic(List<Phonetic> allPhonetic) {
            this.allPhonetic = allPhonetic;
        }
    }



    public static class PoS{

        private String pOs;

        public String getpOs() {
            return pOs;
        }

        public void setpOs(String pOs) {
            this.pOs = pOs;
        }



        private List<Pair> pairs=new ArrayList<>();

        public List<Pair> getPairs() {
            return pairs;
        }

        public void setPairs(List<Pair> pairs) {
            this.pairs = pairs;
        }





    }
    public static class Phonetic{

        private String type;
        private String IPA;
        private String link;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIPA() {
            return IPA;
        }

        public void setIPA(String IPA) {
            this.IPA = IPA;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
    public static class Pair{


        private String definiton;

        private String example;

        public String getDefiniton() {
            return definiton;
        }

        public void setDefiniton(String definiton) {
            this.definiton = definiton;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }
        Pair(String definiton,String example){

            setExample(example);
            setDefiniton(definiton);

        }
    }


public WordDetails(String word){

    try {

        Class.forName("org.sqlite.JDBC");
        Connection connection= DriverManager.getConnection("jdbc:sqlite:wordsApi.db");
        this.sqlite_connection=connection;

        String sql="SELECT MAX(Homonyms) FROM word_details WHERE word_name= ?";
        PreparedStatement statement=this.sqlite_connection.prepareStatement(sql);
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
            PreparedStatement statement1=this.sqlite_connection.prepareStatement(sql1);
            statement1.setString(1,word);
            statement1.setString(2,String.valueOf(i));
            ResultSet resultSet1=statement1.executeQuery();
            PreparedStatement statement2=this.sqlite_connection.prepareStatement(sql2);
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

            for (Phonetic phonetic:homonyms.allPhonetic){




            }

            for (PoS poS: homonyms.allPoS){


                for (Pair pair:poS.pairs){

                    if(pair.getDefiniton()==null){
                        break;
                    }


                }

            }

            allWord_details.add(homonyms);
            statement1.close();
            statement2.close();


        }




    }catch (Exception e){
        e.printStackTrace();
    }

}


}
