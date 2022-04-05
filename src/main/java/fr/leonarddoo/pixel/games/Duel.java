package fr.leonarddoo.pixel.games;

import fr.leonarddoo.pixel.Membre;

public class Duel {

    public static String getErreur(String j1, String j2, int mise, String auteur, String adversaire){

        StringBuilder res = new StringBuilder();

        if(j1 != null || j2 != null) {
            res.append("Désolé mais un duel est déjà en cours.");

        }else if(auteur.equals(adversaire)){
            res.append("Vous ne pouvez pas faire un duel contre vous-même.");

        }else if(mise < 1 || mise > 3){
            res.append("La mise doit être comprise entre 1 et 3 (inclus).");

        }else if(Membre.getMembre(auteur).getPoints() < mise){
            res.append("Vous ne possedez pas autant de <:jeu:959785480227020800> **Point de Jeu**.");

        }else if(Membre.getMembre(adversaire).getPoints() < mise){
            res.append("Cette personne n'a pas assez de <:jeu:959785480227020800> **Point de Jeu** pour accepter le duel.");
        }

        System.out.println(res);

        return res.toString();



    }
}
