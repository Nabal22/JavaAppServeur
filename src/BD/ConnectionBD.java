package BD;

import model.Abonne;
import model.DVD;
import model.Document;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class ConnectionBD {
    private Connection conn;

    public void connectToBD() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://db4free.net:3306/java_app_serveur?characterEncoding=UTF-8&useSSL=false";
        String user = "albantalagrand";
        String password = "rootpassword";
        Connection conn = null;

        try{
            this.conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie !");
        } catch (Exception e) {
            System.out.println("Connexion échouée !"+ e);
        }
    }

    public ArrayList<Abonne> getAbonnesFromDB() throws SQLException {
        ArrayList<Abonne> abonnes = new ArrayList<>();
        String requete = "SELECT * from ABONNE";
        Statement req1 = this.conn.createStatement();
        ResultSet rs = req1.executeQuery(requete);

        while(rs.next()) {
            abonnes.add(new Abonne(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        return abonnes;
    }

    public ArrayList<Document> getDvdsFROMDB(ArrayList<Abonne> abonnes) throws SQLException {
        ArrayList<Document> dvds = new ArrayList<>();
        String requete = "Select * from vue_etat_dvd";
        Statement req2 = this.conn.createStatement();
        ResultSet rs = req2.executeQuery(requete);




        while(rs.next()) {
            Integer numAb = rs.getInt(5);
            if(numAb == null) {
                numAb = rs.getInt(6);
            }

            Document dvd = new DVD(rs.getInt(2), rs.getString(1), rs.getBoolean(3), this.getAbonneById(abonnes, numAb), rs.getString(4).toLowerCase());
            dvds.add(dvd);
        }

        return dvds;

    }

    private Abonne getAbonneById(ArrayList<Abonne> abonnes, Integer numero) {
        if (numero == null) {
            return null;
        }
        for (int i = 0; i < abonnes.size(); i++) {
            if (abonnes.get(i).getNumeroAdhérent() == numero) {
                return abonnes.get(i);
            }
        }
        return null;
    }

    public void reserverDvdBD(Abonne abonne, Document dvd) throws SQLException {
        Statement req2 = this.conn.createStatement();
        ResultSet rs = req2.executeQuery("Select idDocument from DVD where idDvd = " + dvd.numero());
        rs.next();
        int idDocument = rs.getInt(1);

        //Pour l'instant date null
        Statement reqInsert = this.conn.createStatement();
        int rsInsert = req2.executeUpdate("INSERT INTO RESERVATION VALUES (" + abonne.getNumeroAdhérent() + ", " + idDocument + ", null)");


    }
}