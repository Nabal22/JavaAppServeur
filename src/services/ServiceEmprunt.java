package services;

import BD.ConnectionBD;
import bserveur.Service;

import encodage.Encode;
import exceptions.abonneNonTrouveException;
import exceptions.documentNonEmpruntéException;

import exceptions.documentNonLibreException;
import exceptions.documentNonTrouveException;
import model.Abonne;
import model.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceEmprunt extends Service {
    private static ArrayList<Abonne> abonnes;
    private static ArrayList<Document> dvds;

    private ConnectionBD dbConnect = new ConnectionBD();

    public ServiceEmprunt(Socket s) {
        super(s);
    }

    public static void setAbonnes(ArrayList<Abonne> abonnes) {
        ServiceEmprunt.abonnes = abonnes;
    }
    public static void setDvds(ArrayList<Document> dvds) {
        ServiceEmprunt.dvds = dvds;
    }

    @Override
    public void run() {

        try {
            this.dbConnect.connectToBD();

            BufferedReader sIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter sOut = new PrintWriter(s.getOutputStream(), true);

            sOut.println("Veuillez saisir votre numéro d'abonné.");
            int numAbo = Integer.parseInt(sIn.readLine());
            sOut.println(Encode.encoder("Veuillez saisir le numéro d'un livre à emprunter. \n" + this.listeDesDvds()));
            int numDvdChoisi = Integer.parseInt(sIn.readLine());

            try {
                this.emprunterDvd(numAbo, numDvdChoisi);
                sOut.println(Encode.encoder("Vous avez emprunté ce document."));
            } catch (SQLException e) {
                sOut.println(Encode.encoder("Le document n'est pas libre."));
            } catch (documentNonTrouveException e) {
                sOut.println(Encode.encoder("Le document n'existe pas en base de données."));
            } catch (abonneNonTrouveException e) {
                sOut.println(Encode.encoder("Votre numéro abonné n'est pas reconnu."));
            } catch (documentNonLibreException e) {
                sOut.println(Encode.encoder("Le document n'est pas libraae."));
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Document getDvd(int numDvd) throws documentNonTrouveException {
        for (int i = 0; i < this.dvds.size(); i++) {
            if(this.dvds.get(i).numero() == numDvd) {
                return this.dvds.get(i);
            }
        }
        throw new documentNonTrouveException();
    }

    private Abonne getAbonne(int numAbo) throws abonneNonTrouveException {
        for (int i = 0; i < this.abonnes.size(); i++) {
            if(this.abonnes.get(i).getNumeroAdhérent() == numAbo) {
                return this.abonnes.get(i);
            }
        }
        throw new abonneNonTrouveException();
    }

    private void emprunterDvd(int numAbo, int numDvd) throws abonneNonTrouveException, documentNonTrouveException, SQLException, documentNonLibreException {
        Abonne ab = this.getAbonne(numAbo);
        Document d = this.getDvd(numDvd);
        if((d.empruntePar() == null && d.reservePar() == null) || (d.empruntePar() == null && d.reservePar() == ab)) {
            d.emprunt(ab);
            this.dbConnect.emprunterDvd(d, ab);
        } else {
            throw new documentNonLibreException();
        }

    }

    private String listeDesDvds() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.dvds.size(); i++) {
            s.append(this.dvds.get(i).numero() + "-" + this.dvds.get(i).toString() + "\n");
        }
        return s.toString();
    }

}
