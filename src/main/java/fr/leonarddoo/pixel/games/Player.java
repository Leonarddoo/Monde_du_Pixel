package fr.leonarddoo.pixel.games;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String id, word, hidden, msg;
    private int round;
    private boolean winner;
    private List<Character> use;

    public Player(String id){
        this.id = id;
        this.word = null;
        this.hidden = null;
        this.msg = null;
        this.round = 0;
        this.winner = false;
        this.use = new ArrayList<>();
    }

    public void checkChar(Character c){
        boolean find = false;
        for(int i = 0 ; i < this.word.length() ; i++){
            if(this.word.charAt(i) == c){
                StringBuilder sb = new StringBuilder(this.hidden);
                sb.setCharAt(i, c);
                this.hidden = sb.toString();
                find = true;
            }
        }
        if(!find){
            this.round++;
        }
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setHidden() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0 ; i < this.getWord().length() ; i++){
            builder.append("_");
        }
        this.hidden = builder.toString();
    }

    public String getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getHidden() {
        return hidden;
    }

    public String getMsg() {
        return msg;
    }

    public int getRound() {
        return round;
    }

    public List<Character> getUse() {
        return use;
    }
}
