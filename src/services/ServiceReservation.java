package services;
import BD.ConnectionBD;
import bserveur.Service;
import encodage.Encode;
import model.Abonne;
import model.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceReservation extends Service {
    private static ArrayList<Abonne> abonnes;
    private static ArrayList<Document> dvds;

    private ConnectionBD dbConnect = new ConnectionBD();



    public ServiceReservation(Socket s) {
        super(s);
    }

    public static void setAbonnes(ArrayList<Abonne> abonnes) {
        ServiceReservation.abonnes = abonnes;
    }
    public static void setDvds(ArrayList<Document> dvds) {
        ServiceReservation.dvds = dvds;
    }

    @Override
    public void run() {
        try {
            this.dbConnect.connectToBD();

            BufferedReader sIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter sOut = new PrintWriter(s.getOutputStream(), true);

            sOut.println("Veuillez saisir votre numéro d'abonné.");
            int numAbo = Integer.parseInt(sIn.readLine());
            sOut.println(Encode.encoder("Veuillez saisir le numéro d'un livre à réserver. \n" + this.listeDesDvdsDisponibles()));
            int numDvdChoisi = Integer.parseInt(sIn.readLine());

            réserverDvd(numAbo, numDvdChoisi);
            sOut.println("Réservation effectuée.");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private String listeDesDvdsDisponibles() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.dvds.size(); i++) {
            s.append(this.dvds.get(i).numero() + "-" + this.dvds.get(i).toString() + "\n");
        }
        return s.toString();
    }

    private void réserverDvd(int numAbo, int numDvd) throws SQLException {
        Abonne ab = this.getAbonne(numAbo);
        Document d = this.getDvd(numDvd);
        d.reservation(ab);
        this.dbConnect.reserverDvdBD(ab, d);
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
