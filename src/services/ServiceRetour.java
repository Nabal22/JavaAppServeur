package services;

import BD.ConnectionBD;
import bserveur.Service;

import exceptions.documentNonEmpruntéException;

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

public class ServiceRetour extends Service {
    private static ArrayList<Abonne> abonnes;
    private static ArrayList<Document> dvds;

    private ConnectionBD dbConnect = new ConnectionBD();



    public ServiceRetour(Socket s) {
        super(s);
    }

    public static void setAbonnes(ArrayList<Abonne> abonnes) {
        ServiceRetour.abonnes = abonnes;
    }
    public static void setDvds(ArrayList<Document> dvds) {
        ServiceRetour.dvds = dvds;
    }

    @Override
    public void run() {

        try {
            this.dbConnect.connectToBD();

            BufferedReader sIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter sOut = new PrintWriter(s.getOutputStream(), true);

            sOut.println("Veuillez saisir le numéro du document à retourner.");
            int numDvdChoisi = Integer.parseInt(sIn.readLine());

            System.out.println("test");

            for (int i = 0; i < dvds.size(); i++) {
                System.out.println(dvds.get(i).toString() + " " + dvds.get(i).getAbonne());
            }

            try {
                this.retournerDVD(getDvd(numDvdChoisi));
                sOut.println("Le document a bien été retourné.");

            } catch (documentNonEmpruntéException e) {
                sOut.println("Le document n'est pas emprunté.");
            } catch (documentNonTrouveException e) {
                sOut.println("Le document n'existe pas dans notre base de données.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
    public void retournerDVD(Document dvd) throws documentNonEmpruntéException, SQLException {
        dvd.retour();
        this.dbConnect.retournerDvd(dvd);
    }
}
