package fr.leonarddoo.pixel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Membre implements Serializable {

    private static List<Membre> membreList = new ArrayList<>();

    private String id;
    private int reputation;
    private int messages;
    private int invitations;
    private int points;

    public Membre(String id){
        this.id = id;
        this.reputation = 0;
        this.messages = 0;
        this.invitations = 0;
        this.points = 0;
        writerList();
    }

    public void removeReputation(int m){
        if(this.reputation - m < 0){
            this.reputation = 0;
        }else{
            this.reputation -= m;
        }
        writerList();
    }

    public void addReputation(int m){
        this.reputation += m;
        writerList();
    }

    public void addMessage(){
        this.messages++;
        writerList();
    }

    public void addInvitation(){
        invitations++;
        writerList();
    }

    public void addPoints(int m){
        this.points += m;
        writerList();
    }

    public void removePoints(int m){
        if(this.points - m < 0){
            this.points = 0;
        }else{
            this.points -= m;
        }
        writerList();
    }

    public static Membre getMembre(String id){
        for(Membre m : membreList){
            if(m.getId().equals(id)){
                return m;
            }
        }
        Membre membre = new Membre(id);
        Membre.getMembreList().add(membre);
        return membre;
    }

    public static void readList(){
        try(ObjectInputStream oos = new ObjectInputStream(new FileInputStream("save.ser"))){
            membreList = new ArrayList<>((List<Membre>)oos.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writerList(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.ser"))){
            oos.writeObject(membreList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Membre> getMembreList(){
        return membreList;
    }

    public String getId(){
        return id;
    }

    public int getReputation(){
        return reputation;
    }

    public int getMessages(){
        return messages;
    }

    public int getInvitations(){
        return invitations;
    }

    public int getPoints(){
        return points;
    }

}
