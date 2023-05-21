package services;

import BD.ConnectionBD;
import bserveur.Service;
import exceptions.abonneNonTrouveException;
import exceptions.documentNonTrouveException;
import model.Abonne;
import model.IDocument;

import java.net.Socket;
import java.util.ArrayList;

public class ServiceCommun extends Service {

    private static ArrayList<Abonne> abonnes;
    private static ArrayList<IDocument> documents;

    private ConnectionBD dbConnect = new ConnectionBD();

    public static void setAbonnes(ArrayList<Abonne> abonnes) {
        ServiceCommun.abonnes = abonnes;
    }
    public static void setDocuments(ArrayList<IDocument> documents) {
        ServiceCommun.documents = documents;
    }


    public ServiceCommun(Socket s) {
        super(s);
    }

    @Override
    public void run() {}

    protected IDocument getDocument(int numDvd) throws documentNonTrouveException {
        for (int i = 0; i < this.documents.size(); i++) {
            if(this.documents.get(i).numero() == numDvd) {
                return this.documents.get(i);
            }
        }
        throw new documentNonTrouveException();
    }

    protected boolean isAbonne(int numAbo) {
        for (int i = 0; i < this.abonnes.size(); i++) {
            if(this.abonnes.get(i).getNumeroAdhérent() == numAbo) {
                return true;
            }
        }
        return false;
    }

    protected Abonne getAbonne(int numAbo) throws abonneNonTrouveException {
        for (int i = 0; i < this.abonnes.size(); i++) {
            if(this.abonnes.get(i).getNumeroAdhérent() == numAbo) {
                return this.abonnes.get(i);
            }
        }
        throw new abonneNonTrouveException();
    }

    protected String listeDesDocuments() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.documents.size(); i++) {
            s.append(this.documents.get(i).numero() + "-" + this.documents.get(i).toString() + "\n");
        }
        return s.toString();
    }
}
