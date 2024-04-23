package myapp;
import com.google.gson.Gson;
import javafx.geometry.Pos;
import myapp.SuggestionBox.WordDetails.WordDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class API implements Serializable{
    private List<Meaning> meanings;
    private List<Phonetic> phonetics;
    private String word;
    private License license;
    private List<String> sourceUrls;

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<Meaning> meanings) {
        this.meanings = meanings;
    }

    public List<Phonetic> getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(List<Phonetic> phonetics) {
        this.phonetics = phonetics;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public List<String> getSourceUrls() {
        return sourceUrls;
    }

    public void setSourceUrls(List<String> sourceUrls) {
        this.sourceUrls = sourceUrls;
    }

    public static class Meaning implements Serializable {
        private String partOfSpeech;
        private List<Definition> definitions;

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public void setPartOfSpeech(String partOfSpeech) {
            this.partOfSpeech = partOfSpeech;
        }

        public List<Definition> getDefinitions() {
            return definitions;
        }

        public void setDefinitions(List<Definition> definitions) {
            this.definitions = definitions;
        }
    }

    public static class Definition implements Serializable{
        private String definition;
        private List<String> synonyms;
        private List<String> antonyms;
        private String example;

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        public List<String> getSynonyms() {
            return synonyms;
        }

        public void setSynonyms(List<String> synonyms) {
            this.synonyms = synonyms;
        }

        public List<String> getAntonyms() {
            return antonyms;
        }

        public void setAntonyms(List<String> antonyms) {
            this.antonyms = antonyms;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }
    }

    public static class Phonetic implements Serializable{
        private String text;
        private String audio;
        private String sourceUrl;
        private License license;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public License getLicense() {
            return license;
        }

        public void setLicense(License license) {
            this.license = license;
        }
    }

    public static class License implements Serializable{
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
    public static List<Meaning> fetchMeanings(API api,String word) {
        if (api!=null){
            return api.getMeanings();
        }
        return new ArrayList<>();
    }

    public static List<Phonetic> fetchPhonetics(API api,String word) {
        if (api!=null){
            return api.getPhonetics();
        }
        return new ArrayList<>();
    }

    public static API fetchWordDetails(String word) {
        try {
            URL dict_url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + word);
            URLConnection urlConnection = dict_url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }

            String jsonData = content.toString();

            Gson gson = new Gson();

            API[] definitions = gson.fromJson(jsonData, API[].class);

            if (definitions.length > 0) {
                return definitions[0];
            }

        } catch (IOException e) {
            // Print this when there is a problem with the connection
            System.out.println("Unable to connect to the URL");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Return null in case of an error
    }
    public static String getAudioCountry(String url){

        if (url.isEmpty())return null;
        int last_index=url.length()-1;
        String country="";
        country+=url.charAt(last_index-5);
        country+=url.charAt(last_index-4);
        return country;

    }
    public static String get_audio_list(String url,int homonyms,String IPA){

        StringBuilder stringBuilder=new StringBuilder();
        for(int i=url.length()-1;i>=0;i--){

            if(url.charAt(i)=='/'){
                break;
            }
            stringBuilder.append(url.charAt(i));

        }
        String string=stringBuilder.reverse().toString();
        String[] strings=string.split("\\.");
        string=strings[0];
        string=String.valueOf(homonyms)+"-"+string;
        String[] data=string.split("-");
        String s="";
        for (int i=2;i<data.length;i++){

            s+=data[i];
            if(i+1<data.length){
                s+="-";
            }
        }
        if(!s.isEmpty()){
            return s;
        }
        return null;

    }
    public static void addWord(String word){

        //connect to online dictionary
        try {
            URL dict_url=new URL("https://api.dictionaryapi.dev/api/v2/entries/en/"+word);
            URLConnection urlConnection=dict_url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line,content="";
            while ((line = bufferedReader.readLine()) != null)
            {
                content=line;
            }


            String jsonData=content;

            Gson gson = new Gson();

            API[] definitions = gson.fromJson(jsonData, API[].class);
            int i=1;
            WordDetails wordDetails=new WordDetails();

            String sqlite="SELECT * FROM word_details WHERE word_name= ?";
            PreparedStatement preparedstatement=WordDetails.sqlite_connection.prepareStatement(sqlite);
            preparedstatement.setString(1,word);
            ResultSet resultSet=preparedstatement.executeQuery();
            if(!resultSet.next()){
                preparedstatement.close();
                return;
            }

            for (API definition : definitions) {

                WordDetails.Homonyms homonyms=new WordDetails.Homonyms();
                homonyms.setHomonyms(String.valueOf(i));


                List<WordDetails.PoS>list_pos=new ArrayList<>();
                for (API.Meaning meaning : definition.getMeanings()) {
                    System.out.println("  Loại từ: " + meaning.getPartOfSpeech());
                    WordDetails.PoS pos=new WordDetails.PoS();
                    System.out.println();
                    List<WordDetails.Pair>pairList=new ArrayList<>();
                    int count=0;
                    for (API.Definition def : meaning.getDefinitions()) {

                        WordDetails.Pair pair=new WordDetails.Pair(def.getDefinition(),def.getExample());
                        pairList.add(pair);
                        count++;
                        if(count==3){
                            break;
                        }


                    }
                    pos.setPairs(pairList);
                    list_pos.add(pos);

                }
                homonyms.setAllPoS(list_pos);

                List<WordDetails.Phonetic>list_phonetic=new ArrayList<>();

                for (API.Phonetic phonetic : definition.getPhonetics()) {
                    WordDetails.Phonetic phoneticWord=new WordDetails.Phonetic();
                    phoneticWord.setIPA(phonetic.getText());
                    phoneticWord.setLink(phonetic.getAudio());
                    phoneticWord.setType(get_audio_list(phonetic.getAudio(),i, phonetic.getText()));
                    list_phonetic.add(phoneticWord);
                }
                homonyms.setAllPhonetic(list_phonetic);
                wordDetails.allWord_details.add(homonyms);

            }

            for(WordDetails.Homonyms homonyms: wordDetails.allWord_details){

                for (WordDetails.PoS poS:homonyms.getAllPoS()){
                    String sql="INSERT INTO word_details (word_name,PoS,Homonyms,Example1,Example2,Example3,Definition1,Definition2,Definition3) VALUES (?,?,?,?,?,?,?,?,?)";
                    PreparedStatement statement=WordDetails.sqlite_connection.prepareStatement(sql);
                    statement.setString(1,word);
                    statement.setString(3,homonyms.getHomonyms());
                    statement.setString(2,poS.getpOs());
                    int j=4,k=7;
                    for(WordDetails.Pair pair:poS.getPairs()){

                        statement.setString(j,pair.getExample());
                        statement.setString(k,pair.getDefiniton());
                        j++;
                        k++;

                    }
                    statement.executeUpdate();
                    statement.close();

                }
                for (WordDetails.Phonetic phonetic: homonyms.getAllPhonetic()){
                    String sql="INSERT INTO audio (Homonyms,word_name,type,IPA,link) VALUES (?,?,?,?,?)";
                    PreparedStatement statement= WordDetails.sqlite_connection.prepareStatement(sql);
                    statement.setString(1, homonyms.getHomonyms());
                    statement.setString(2,word);
                    statement.setString(3,phonetic.getType());
                    statement.setString(4,phonetic.getIPA());
                    statement.setString(5,phonetic.getLink());
                    statement.executeUpdate();
                    statement.close();

                }


            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Connection failed"); } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }


}