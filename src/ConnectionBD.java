import java.sql.*;

public class ConnectionBD {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://db4free.net:3306/java_app_serveur?characterEncoding=UTF-8&useSSL=false";
        String user = "albantalagrand";
        String password = "rootpassword";
        Connection conn = null;

        try{
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie !");
        } catch (Exception e) {
            System.out.println("Connexion échouée !"+ e);
        }

    }
}