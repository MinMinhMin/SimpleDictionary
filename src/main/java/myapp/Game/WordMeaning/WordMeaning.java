package myapp.Game.WordMeaning;

import myapp.SuggestionBox.WordDetails.WordDetails;
import myapp.SuggestionBox.WordDetails.Word_Information;
import myapp.SuggestionBox.Words;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class WordMeaning {

    public String[] choices;
    public Boolean[] answers;
    
    public String question;

    public String audio_link_answer;

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public Boolean[] getAnswers() {
        return answers;
    }

    public void setAnswers(Boolean[] answers) {
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAudio_link_answer() {
        return audio_link_answer;
    }

    public void setAudio_link_answer(String audio_link_answer) {
        this.audio_link_answer = audio_link_answer;
    }


    public WordMeaning(String mode){

        if(mode=="no_audio"){
            String[] choice_eng=Words.getQuestionSet_meaning(getRandomLetter());
            while (choice_eng==null){

                choice_eng=Words.getQuestionSet_meaning(getRandomLetter());

            }
            String[] choice_vie=new String[choice_eng.length];
            for(int i=0;i<choice_eng.length;i++){

                choice_vie[i]=Words.meaning.get(choice_eng[i]).get((int)(Math.random()*(Words.meaning.get(choice_eng[i]).size())));

            }
            setChoices(choice_vie);
            int idx_answer=(int)(Math.random()*choice_vie.length);
            String question="Nghĩa của từ "+choice_eng[idx_answer]+" là? :";
            setQuestion(question);
            Boolean[] answer_arr=new Boolean[choice_eng.length];
            Arrays.fill(answer_arr,false);
            answer_arr[idx_answer]=true;
            setAnswers(answer_arr);
        }

        else{

            String[] choice_eng=Words.getQuestionSet_meaning(getRandomLetter());

            while (choice_eng==null){

                choice_eng=Words.getQuestionSet_meaning(getRandomLetter());

            }

            String[] choice_vie=new String[choice_eng.length];

            for(int i=0;i<choice_eng.length;i++){

                choice_vie[i]=Words.meaning.get(choice_eng[i]).get((int)(Math.random()*(Words.meaning.get(choice_eng[i]).size())));

            }
            setChoices(choice_vie);
            int idx_answer=(int)(Math.random()*choice_vie.length);
            String question="Nghĩa của từ sau là? :";
            setQuestion(question);
            Boolean[] answer_arr=new Boolean[choice_eng.length];
            Arrays.fill(answer_arr,false);
            answer_arr[idx_answer]=true;
            setAnswers(answer_arr);

            WordDetails wordDetails=new WordDetails(choice_eng[idx_answer]);
            for (WordDetails.Homonyms homonyms:WordDetails.allWord_details){

                for (Word_Information.Phonetic phonetic: homonyms.getAllPhonetic()){

                    if(phonetic.getLink()!=null&&!phonetic.getLink().contains("https://d1qx7pbj0dvboc.cloudfront.net/")){

                        setAudio_link_answer(phonetic.getLink());

                    }

                }

            }


        }


    }

    public char getRandomLetter(){

        Random r = new Random();
        char c = (char)(r.nextInt(26) + 'a');
        return c;

    }
    
    





}
