package myapp.SuggestionBox.WordDetails;

import java.util.ArrayList;
import java.util.List;

public interface Word_Information {

    List<Homonyms> allWord_details=new ArrayList<>();

    class Homonyms{

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



    class PoS{

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
    class Phonetic{

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
    class Pair{


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
        public Pair(String definiton, String example){

            setExample(example);
            setDefiniton(definiton);

        }
    }

}
