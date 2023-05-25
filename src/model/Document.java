package model;

import exceptions.*;

import java.util.GregorianCalendar;

public abstract class Document implements IDocument {

    protected int idDocument;
    protected String titre;

    protected Enum etat;//reservé ou emprunté ou libre

    protected Abonne abonne;

    protected GregorianCalendar dateReservation;

    public Document(int idDocument, String titre, Etat etat) {
        this.idDocument = idDocument;
        this.titre = titre;
        this.etat = etat;
    }

    public Document(int idDocument, String titre, Etat etat, Abonne abonne) {
        this.idDocument = idDocument;
        this.titre = titre;
        this.etat = etat;
        this.abonne = abonne;
    }

    public Document(int idDocument, String titre, Etat etat, Abonne abonne, GregorianCalendar dateReservation) {
        this.idDocument = idDocument;
        this.titre = titre;
        this.etat = etat;
        this.abonne = abonne;
        this.dateReservation = dateReservation;
    }

    public int getIdDocument() {
        return idDocument;
    }

    // return null si pas emprunté ou pas réservé
    public Abonne empruntePar(){
        if(this.etat.equals(Etat.EMPRUNTE)) {
            return this.abonne;
        }
        else {
            return null;  // Abonné qui a emprunté ce document
        }
    }

    public Abonne reservePar() {
        if (this.etat.equals(Etat.RESERVE)) {
            return this.abonne;
        } else {
            return null;
        }
    } ; // Abonné qui a réservé ce document


    // precondition ni réservé ni emprunté
    public void reservation(Abonne ab) throws documentDejaEmprunteException, documentDejaReserveException, documentPourAdulteException {
        if (this.etat.equals(Etat.LIBRE) && this.estAutorise(ab)){
            this.etat = Etat.RESERVE;
            this.abonne = ab;
            this.dateReservation = new GregorianCalendar();
        } else if (this.etat.equals(Etat.EMPRUNTE)) {
            throw new documentDejaEmprunteException();
        } else if (this.etat.equals(Etat.RESERVE)) {
            throw new documentDejaReserveException();
        } else if (!this.estAutorise(ab)) {
            throw new documentPourAdulteException();
        }
    }


    // precondition libre ou réservé par l’abonné qui vient emprunter
    public void emprunt(Abonne ab) throws documentNonLibreException, documentPourAdulteException {
        if ((this.etat.equals(Etat.RESERVE) && this.abonne == ab) ||
                (this.etat.equals(Etat.LIBRE) && this.abonne == null)
                && this.estAutorise(ab)) {
            this.etat = Etat.EMPRUNTE;
            this.abonne = ab;
        } else if ((this.etat.equals(Etat.RESERVE) && this.abonne == ab) ||
                (this.etat.equals(Etat.LIBRE) && this.abonne == null)
                        && !this.estAutorise(ab)){
            throw new documentPourAdulteException();
        } else {
            throw new documentNonLibreException();
        }
    }


    // retour d’un document ou annulation d‘une réservation
    public void retour() throws documentNonEmpruntéException {
        if(this.etat.equals(Etat.EMPRUNTE)) {
            this.etat = Etat.LIBRE;
            this.abonne = null;
        } else {
            throw new documentNonEmpruntéException();
        }
    };

    public abstract boolean estAutorise(Abonne ab);

    public String toString() {
        String etatString = this.etat.equals(Etat.EMPRUNTE) ? "emprunté" : this.etat.equals(Etat.LIBRE) ? "libre" : "réservé";
        String abonneString = this.abonne == null ? "aucun" : this.abonne.toString();

        return "Document [idDocument=" + idDocument + ", titre=" + titre + ", état=" + etatString + ", abonné=" + abonneString + ", date de réservation=" + dateReservation + "]";
    }


    public Abonne getAbonne() {
        return this.abonne;
    }

    public GregorianCalendar getDateReservation() {
        return this.dateReservation;
    }

}
