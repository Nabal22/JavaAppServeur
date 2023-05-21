package BD;

import model.Abonne;
import model.DVD;
import model.Etat;
import model.IDocument;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    public ArrayList<Abonne> getAbonnesFromDB() throws SQLException, ParseException {
        ArrayList<Abonne> abonnes = new ArrayList<>();
        String requete = "SELECT * from ABONNE";
        Statement req1 = this.conn.createStatement();
        ResultSet rs = req1.executeQuery(requete);
        //convertir la date de naissance au format  YYYY-MM-DD en Date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        while(rs.next()) {
            String dateNaissanceStr = rs.getString(3);
            Date dateNaissance = dateFormat.parse(dateNaissanceStr);
            abonnes.add(new Abonne(rs.getInt(1), rs.getString(2), dateNaissance));
        }
        return abonnes;
    }

    public ArrayList<IDocument> getDocumentsFROMDB(ArrayList<Abonne> abonnes) throws SQLException {
        ArrayList<IDocument> documents = new ArrayList<>();
        ArrayList<String> documentsType = new ArrayList<>();
        documentsType.add("dvd");

        for (String type : documentsType) {
            String request = "Select * from vue_etat_" + type;
            Statement req2 = this.conn.createStatement();
            ResultSet rs = req2.executeQuery(request);
            while(rs.next()) {
                IDocument document = null;
                // set etat
                switch (rs.getString(4)) {
                    case "LIBRE":
                        switch (type) {
                            case "dvd":
                                document = new DVD(rs.getInt(1), rs.getString(2), Etat.LIBRE, rs.getBoolean(3));
                                break;
                        }
                    break;

                    case "EMPRUNTE":
                        switch (type) {
                            case "dvd":
                                document = new DVD(rs.getInt(1), rs.getString(2), Etat.EMPRUNTE, this.getAbonneById(abonnes,rs.getInt(5)), rs.getBoolean(3));
                        }
                    break;

                    case "RESERVE":
                        switch (type) {
                            case "dvd":
                                document = new DVD(rs.getInt(1), rs.getString(2), Etat.RESERVE, this.getAbonneById(abonnes,rs.getInt(6)) , new Date(), rs.getBoolean(3));
                                break;
                        }
                }

                documents.add(document);
            }
        }
        return documents;
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

    public void reserverDocumentBD(Abonne abonne, IDocument document) throws SQLException {
        int idDocument = document.numero();
        Date date = new Date();
        Statement reqInsert = this.conn.createStatement();
        int rsInsert = reqInsert.executeUpdate("INSERT INTO RESERVATION VALUES (" + abonne.getNumeroAdhérent() + ", " + idDocument + ", '" + date + "')");
    }

    public void retournerDocument(IDocument document) throws SQLException {
        Statement req3 = this.conn.createStatement();
        int rsDelete = req3.executeUpdate("DELETE FROM EMPRUNT WHERE idDocument = " + document.numero());
    }

    public void emprunterDocument(IDocument document, Abonne abonne) throws SQLException {
        int idDocument = document.numero();
        Date date = new Date();
        if(document.reservePar() == null || document.reservePar().getNumeroAdhérent() == abonne.getNumeroAdhérent()) {
            //on insert le numeroAbonne et l'idDocument dans EMPRUNT
            Statement req4 = this.conn.createStatement();
            int rsInsert = req4.executeUpdate("INSERT INTO EMPRUNT VALUES (" + abonne.getNumeroAdhérent() +"," + idDocument + ", '" + java.sql.Date.valueOf(java.time.LocalDate.now()) + "')");
            if (document.reservePar() != null) {
                //on supprime la reservation
                Statement req5 = this.conn.createStatement();
                int rsDelete = req5.executeUpdate("DELETE FROM RESERVATION WHERE idDocument = " + idDocument);
            }
            System.out.println("Emprunt effectué");
        }
        else {
            System.out.println("Ce document est réservé par un autre abonné");
        }
    }
}