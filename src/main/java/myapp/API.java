package myapp;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class API {
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

    public static class Meaning {
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

    public static class Definition {
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

    public static class Phonetic {
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

    public static class License {
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
    public static List<Meaning> fetchMeanings(String word) {
        try {
            API api = fetchWordDetails(word);
            if (api != null) {
                return api.getMeanings();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<Phonetic> fetchPhonetics(String word) {
        try {
            API api = fetchWordDetails(word);
            if (api != null) {
                return api.getPhonetics();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

            // Provided JSON data
            String jsonData = content.toString();

            // Create a Gson instance
            Gson gson = new Gson();

            // Parse the JSON data into an array of API objects
            API[] definitions = gson.fromJson(jsonData, API[].class);

            // Return the first element of the array (assuming there's only one result)
            if (definitions.length > 0) {
                return definitions[0];
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Return null in case of an error
    }

    public static void findWord(String word){

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
            //String jsonData = "[{\"word\":\"hello\",\"phonetics\":[{\"audio\":\"https://api.dictionaryapi.dev/media/pronunciations/en/hello-au.mp3\",\"sourceUrl\":\"https://commons.wikimedia.org/w/index.php?curid=75797336\",\"license\":{\"name\":\"BY-SA 4.0\",\"url\":\"https://creativecommons.org/licenses/by-sa/4.0\"}},{\"text\":\"/həˈləʊ/\",\"audio\":\"https://api.dictionaryapi.dev/media/pronunciations/en/hello-uk.mp3\",\"sourceUrl\":\"https://commons.wikimedia.org/w/index.php?curid=9021983\",\"license\":{\"name\":\"BY 3.0 US\",\"url\":\"https://creativecommons.org/licenses/by/3.0/us\"}},{\"text\":\"/həˈloʊ/\",\"audio\":\"\"}],\"meanings\":[{\"partOfSpeech\":\"noun\",\"definitions\":[{\"definition\":\"\\\"Hello!\\\" or an equivalent greeting.\",\"synonyms\":[],\"antonyms\":[]}],\"synonyms\":[\"greeting\"],\"antonyms\":[]},{\"partOfSpeech\":\"verb\",\"definitions\":[{\"definition\":\"To greet with \\\"hello\\\".\",\"synonyms\":[],\"antonyms\":[]}],\"synonyms\":[],\"antonyms\":[]},{\"partOfSpeech\":\"interjection\",\"definitions\":[{\"definition\":\"A greeting (salutation) said when meeting someone or acknowledging someone’s arrival or presence.\",\"synonyms\":[],\"antonyms\":[],\"example\":\"Hello, everyone.\"},{\"definition\":\"A greeting used when answering the telephone.\",\"synonyms\":[],\"antonyms\":[],\"example\":\"Hello? How may I help you?\"},{\"definition\":\"A call for response if it is not clear if anyone is present or listening, or if a telephone conversation may have been disconnected.\",\"synonyms\":[],\"antonyms\":[],\"example\":\"Hello? Is anyone there?\"},{\"definition\":\"Used sarcastically to imply that the person addressed or referred to has done something the speaker or writer considers to be foolish.\",\"synonyms\":[],\"antonyms\":[],\"example\":\"You just tried to start your car with your cell phone. Hello?\"},{\"definition\":\"An expression of puzzlement or discovery.\",\"synonyms\":[],\"antonyms\":[],\"example\":\"Hello! What’s going on here?\"}],\"synonyms\":[],\"antonyms\":[\"bye\",\"goodbye\"]}],\"license\":{\"name\":\"CC BY-SA 3.0\",\"url\":\"https://creativecommons.org/licenses/by-sa/3.0\"},\"sourceUrls\":[\"https://en.wiktionary.org/wiki/hello\"]}]";

            Gson gson = new Gson();

            API[] definitions = gson.fromJson(jsonData, API[].class);



            for (API definition : definitions) {
                System.out.println("Từ: " + definition.getWord());


                System.out.println("Nghĩa    :");
                for (API.Meaning meaning : definition.getMeanings()) {
                    System.out.println("  Loại từ: " + meaning.getPartOfSpeech());
                    System.out.println();
                    for (API.Definition def : meaning.getDefinitions()) {

                        System.out.println("    Định nghĩa: " + def.getDefinition());
                        System.out.println("    Ví dụ: " + (def.getExample() != null ? def.getExample() : "N/A"));
                        System.out.println();

                    }
                }


                System.out.println("Phát âm:");
                System.out.println();
                for (API.Phonetic phonetic : definition.getPhonetics()) {

                    System.out.println("  IPA: " + (phonetic.getText() != null ? phonetic.getText() : "N/A"));
                    System.out.println("  Âm thanh: " + (phonetic.getAudio() != null ? phonetic.getAudio() : "N/A"));
                    System.out.println();

                }



            }

        }catch (MalformedURLException e){
            System.out.println("Some thing wrongs!");
        } catch (IOException e) {
            System.out.println("Connection failed");        }



    }


}
