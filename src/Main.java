import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/testjdbc";
        String user = "root";
        String password = "";
        Connection conn = null;

        try{
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie !");
        } catch (Exception e) {
            System.out.println("Connexion échouée !");
        }


    }
}