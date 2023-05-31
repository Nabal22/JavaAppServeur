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
import java.util.GregorianCalendar;

public class ConnectionBD {
    private Connection conn;

    /**
     * Établit une connexion à la base de données.
     * @throws ClassNotFoundException si le pilote de base de données n'est pas trouvé.
     */
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

    /**
     * Récupère la liste des abonnés à partir de la base de données.
     * @return la liste des abonnés.
     * @throws SQLException en cas d'erreur SQL lors de la récupération des données.
     * @throws ParseException si la conversion de la date de naissance échoue.
     */
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

    /**
     * Récupère la liste des documents à partir de la base de données.
     * @param abonnes la liste des abonnés.
     * @return la liste des documents.
     * @throws SQLException en cas d'erreur SQL lors de la récupération des données.
     */
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
                        Timestamp timestamp = rs.getTimestamp(8);
                        Date date = new Date(timestamp.getTime());
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime(date);
                        switch (type) {
                            case "dvd":
                                document = new DVD(rs.getInt(1), rs.getString(2), Etat.RESERVE, this.getAbonneById(abonnes,rs.getInt(6)) , calendar, rs.getBoolean(3));
                                break;
                        }
                }

                documents.add(document);
            }
        }
        return documents;
    }

    /**
     * Récupère un abonné à partir de sa liste d'abonnés et de son numéro d'adhérent.
     * @param abonnes la liste des abonnés.
     * @param numero le numéro d'adhérent de l'abonné recherché.
     * @return l'abonné correspondant au numéro d'adhérent, ou null si aucun abonné n'est trouvé.
     */
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

    /**
     * Réserve un document pour un abonné dans la base de données.
     * @param abonne l'abonné qui effectue la réservation.
     * @param document le document à réserver.
     * @throws SQLException en cas d'erreur SQL lors de l'insertion des données.
     */
    public void reserverDocumentBD(Abonne abonne, IDocument document) throws SQLException {
        int idDocument = document.numero();
        Date date = new Date();
        Statement reqInsert = this.conn.createStatement();
        int rsInsert = reqInsert.executeUpdate("INSERT INTO RESERVATION VALUES (" + abonne.getNumeroAdhérent() + ", " + idDocument + ", '" + formatGregorianCalendar(getCurrentGregorianCalendar()) + "')");
    }

    /**
     * Supprime l'entrée correspondant au retour d'un document dans la base de données.
     * @param document le document à retourner.
     * @throws SQLException en cas d'erreur SQL lors de la suppression des données.
     */
    public void retournerDocument(IDocument document) throws SQLException {
        Statement req3 = this.conn.createStatement();
        int rsDelete = req3.executeUpdate("DELETE FROM EMPRUNT WHERE idDocument = " + document.numero());
    }

    /**
     * Effectue l'emprunt d'un document par un abonné dans la base de données.
     * @param document le document à emprunter.
     * @param abonne l'abonné qui effectue l'emprunt.
     * @throws SQLException en cas d'erreur SQL lors de l'insertion ou de la suppression des données.
     */
    public void emprunterDocument(IDocument document, Abonne abonne) throws SQLException {
        int idDocument = document.numero();
        Date date = new Date();
        if(document.reservePar() == null || document.reservePar().getNumeroAdhérent() == abonne.getNumeroAdhérent()) {
            //on insert le numeroAbonne et l'idDocument dans EMPRUNT
            Statement req4 = this.conn.createStatement();
            int rsInsert = req4.executeUpdate("INSERT INTO EMPRUNT VALUES (" + abonne.getNumeroAdhérent() +"," + idDocument + ", '" + formatGregorianCalendar(getCurrentGregorianCalendar()) + "')");

            //on supprime la reservation
            Statement req5 = this.conn.createStatement();
            int rsDelete = req5.executeUpdate("DELETE FROM RESERVATION WHERE idDocument = " + idDocument);
            System.out.println("Emprunt effectué");
        }
        else {
            System.out.println("Ce document est réservé par un autre abonné");
        }
    }

    /**
     * Annule la réservation d'un document dans la base de données.
     * @param document le document dont la réservation doit être annulée.
     * @throws SQLException en cas d'erreur SQL lors de la suppression des données.
     */
    public void annulerReservation(IDocument document) throws SQLException {
        int idDocument = document.numero();
        Statement req6 = this.conn.createStatement();
        int rsDelete = req6.executeUpdate("DELETE FROM RESERVATION WHERE idDocument = " + idDocument );
        System.out.println("Réservation annulée pour le document " + idDocument);
    }

    /**
     * Récupère l'objet GregorianCalendar correspondant à la date et l'heure actuelles.
     * @return l'objet GregorianCalendar.
     */
    private GregorianCalendar getCurrentGregorianCalendar(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(timestamp.getTime());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Formate un objet GregorianCalendar en une chaîne de caractères au format "yyyy-MM-dd HH:mm:ss".
     * @param calendar l'objet GregorianCalendar à formater.
     * @return la chaîne de caractères formatée.
     */
    private String formatGregorianCalendar(GregorianCalendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }
}
