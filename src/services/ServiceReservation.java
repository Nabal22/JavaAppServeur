package services;
import bserveur.Service;
import encodage.Encode;
import model.Abonne;
import model.DVD;
import model.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServiceReservation extends Service {
    ArrayList<Abonne> abonnes;
    ArrayList<Document> dvds;

    public ServiceReservation(Socket s, ArrayList<Abonne> abonnes, ArrayList<Document> dvds) {
        super(s);
    }

    public void setAbonnes(ArrayList<Abonne> abonnes) {
        //this.abonnes = abonnes;
    }
    public void setDvds(ArrayList<Document> dvds) {
        //this.dvds = dvds;
    }

    @Override
    public void run() {
        try {
            BufferedReader sIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter sOut = new PrintWriter(s.getOutputStream(), true);

            sOut.println("Veuillez saisir votre numéro d'abonné.");
            int numAbo = sIn.read();
            sOut.println("Veuillez saisir le numéro d'un livre à réserver.");
            sOut.println("test");
            sOut.println(Encode.encoder(this.listeDesDvdsDisponibles()));
            int numDvdChoisi = sIn.read();

            réserverDvd(numAbo, numDvdChoisi);
            sOut.println("Réservation effectuée.");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String listeDesDvdsDisponibles() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.dvds.size(); i++) {
            if(this.dvds.get(i).empruntePar() == null && this.dvds.get(i).reservePar() == null)
            s.append(this.dvds.get(i).numero() + "-" + this.dvds.get(i).toString());
        }
        return s.toString();
    }

    private void réserverDvd(int numAbo, int numDvd) {
        Abonne ab = this.getAbonne(numAbo);
        Document d = this.getDvd(numDvd);
        d.reservation(ab);
    }

    //retourne l'abonne qui a le numéro
    private Abonne getAbonne(int numAbo) {
        for (int i = 0; i < this.abonnes.size(); i++) {
            if(this.abonnes.get(i).getNumeroAdhérent() == numAbo) {
                return this.abonnes.get(i);
            }
        }
        return null;
    }
    private Document getDvd(int numDvd) {
        for (int i = 0; i < this.dvds.size(); i++) {
            if(this.dvds.get(i).numero() == numDvd) {
                return this.dvds.get(i);
            }
        }
        return null;
    }
}
