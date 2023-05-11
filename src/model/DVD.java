package model;

public class DVD implements Document{
    int numero;
    String titre;
    Boolean estPourAdulte;
    Abonne abonne;

    String etat; //reservé ou emprunté ou libre

    public int numero() {
        return this.numero;
    }
    // return null si pas emprunté ou pas réservé
    public Abonne empruntePar(){
        if(this.etat != "emprunté" || this.etat != "réservé") {
            return null;
        }
        else {
            return this.abonne;
        }
    } ; // Abonné qui a emprunté ce document


    public Abonne reservePar() {
        return this.abonne;
    } ; // Abonné qui a réservé ce document


    // precondition ni réservé ni emprunté
    public void reservation(Abonne ab){
        if (this.etat == "libre") {
            this.etat = "emprunté";
            this.abonne = ab;
        }
    }


    // precondition libre ou réservé par l’abonné qui vient emprunter
    public void emprunt(Abonne ab) {
        if (this.etat == "réservé" && this.abonne == ab) {
            this.etat = "emprunté";
        } else if (this.abonne == null && this.etat == "libre") {
            this.etat = "emprunté";
            this.abonne = ab;
        }
    }


    // retour d’un document ou annulation d‘une réservation
    public void retour() {
        this.etat = "libre";
        this.abonne = null;
    };

    public String toString() {
        return this.titre;
    }


}
