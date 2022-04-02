package fr.leonarddoo.pixel.reputation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Membre implements Serializable {

    private static List<Membre> membreList = new ArrayList<>();

    private String id;
    private int reputation;
    private int messages;
    private int invitations;

    public Membre(String id, int r, int m, int i){
        this.id = id;
        this.reputation = r;
        this.messages = m;
        this.invitations = i;
        writerList();
    }

    public void removeReputation(int m){
        if(this.reputation - m < 0){
            this.reputation = 0;
        }else{
            this.reputation -= m;
        }
    }

    public void addReputation(int m){
        this.reputation += m;
        writerList();
    }

    public boolean addMessage(){
        this.messages++;
        if(this.messages%50 == 0){
            this.reputation++;
            return true;
        }
        writerList();
        return false;
    }

    public void addInvitation(){
        invitations++;
        writerList();
    }

    public static boolean contains(String id){
        for(Membre m : membreList){
            if(m.getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public static Membre retrieve(String id){
        for(Membre m : membreList){
            if(m.getId().equals(id)){
                return m;
            }
        }
        return null;
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

}
