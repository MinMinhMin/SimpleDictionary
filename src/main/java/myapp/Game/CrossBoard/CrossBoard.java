package myapp.Game.CrossBoard;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrossBoard {

    char[][] board=new char[10][8];
    boolean[][] visit=new boolean[10][8];
    CrossBoard(){

        for(int i=0;i<10;i++){
            for (int j=0;j<8;j++){
                this.board[i][j]=(char)((int)((Math.random())*26+97));
                this.visit[i][j]=false;
            }

        }

    }

    public char[][] getBoard() {
        return board;
    }
    public boolean allVisit(int x,int y,int endX,int endY,String word){
        if(endX<0||endX>=10||endY<0||endY>=8){
            return false;
        }
        if(x==endX){

            for(int i=Math.min(y,endY);i<=Math.max(y,endY);i++){

                if(this.visit[x][i]){
                    return false;
                }


            }
            int j=0;
            for(int i=Math.min(y,endY);i<=Math.max(y,endY);i++){

                this.board[x][i]=word.charAt(j);
                this.visit[x][i]=true;
                j++;

            }

            return true;

        }
        else{
            for(int i=Math.min(x,endX);i<=Math.max(x,endX);i++){

                if(this.visit[i][y]){
                    return false;
                }


            }
            int j=0;
            for(int i=Math.min(x,endX);i<=Math.max(x,endX);i++){

                this.board[i][y]=word.charAt(j);
                this.visit[i][y]=true;
                j++;

            }
            return true;
        }


    }
    public boolean canBePlaced(int x,int y,String word){
        List<List<Integer>> list= new ArrayList<>();
        list.add(Arrays.asList(new Integer[]{0, word.length() - 1}));
        list.add(Arrays.asList(new Integer[]{0, -word.length() + 1}));
        list.add(Arrays.asList(new Integer[]{ word.length() - 1,0}));
        list.add(Arrays.asList(new Integer[]{ -word.length() + 1,0}));
        List<Integer>small_list=new ArrayList<>();
        for(int i=3;i>=0;i--){
            small_list=list.get((int)(Math.random()*i));
            if(allVisit(x,y,x+small_list.get(0),y+small_list.get(1),word)){
                return true;
            }
            list.remove(small_list);
        }
        return false;


    }
    public void addWord(String word){

        if(word.length()>10)return;
        int x=(int)(Math.random()*10);
        int y=(int)(Math.random()*8);
        while (!canBePlaced(x,y,word)){
             x=(int)(Math.random()*10);
             y=(int)(Math.random()*8);
        }

    }
    public static String getHint(String word){

        StringBuilder hint=new StringBuilder(word);
        int cnt=0;
        while(cnt!=(word.length())/2){

            int index=(int)(word.length()*Math.random());
            if(hint.charAt(index)=='_')continue;
            hint.setCharAt(index,'_');
            cnt++;

        }
        return hint.toString();



    }
}
