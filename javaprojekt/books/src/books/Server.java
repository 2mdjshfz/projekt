/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package books;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Admin
 */
public class Server {
    private static final String ServerAddress = "jdbc:postgresql://http://127.0.0.1:50388/lol";
    private static final String Username = "postgres";
    private static final String Password = "admin";
    
    public static ServerResponse GetBooks()
    {
        try (Connection connection = DriverManager.getConnection(ServerAddress, Username, Password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT b.id as ID, b.tytul as Tytuł, b.tom as Tom, b.rok_wydania as Rok_wydania, b.rodzaj as Rodzaj, b.isbn as ISBN, a.imie || ' ' || a.nazwisko as Autor FROM Books b join Authors a on b.author_id = a.id");
            return new ServerResponse(0, resultSet);
 
        } catch (SQLException e) {
            return new ServerResponse(1, null);
        }
        
    }
    public static ServerResponse GetAuthors()
    {
        try (Connection connection = DriverManager.getConnection(ServerAddress, Username, Password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, imie, nazwisko, kraj_pochodzenia FROM Authors");
            return new ServerResponse(0, resultSet);
 
        } catch (SQLException e) {
            return new ServerResponse(1, null);
        }
        
    }
    public static ServerResponse InsertBook(String tytul, int tom, Date rokWydania, String rodzaj, String isbn, int autorId)
    {
        try (Connection connection = DriverManager.getConnection(ServerAddress, Username, Password)){
            String query = "Insert into Books(tytul, tom, rok_wydania, rodzaj, isbn, author_id)"
                    + " values(?, ?, ?, ?, ?, ?)";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, tytul);
            st.setInt(2, tom);
            st.setDate(3, rokWydania);
            st.setString(4, rodzaj);
            st.setString(5, isbn);
            st.setInt(6, autorId);

            st.executeUpdate();
            st.close();
            return new ServerResponse(0, null);
        } 
        catch (SQLException se)
        {
          return new ServerResponse(1, null);
        }
    }
    public static ServerResponse InsertAutor(String imie, String nazwisko, String krajPochodzenia)
    {
        try (Connection connection = DriverManager.getConnection(ServerAddress, Username, Password)){
            String query = "Insert into Authors(imie, nazwisko, kraj_pochodzenia)"
                    + " values(?, ?, ?)";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, imie);
            st.setString(2, nazwisko);
            st.setString(3, krajPochodzenia);

            st.executeUpdate();
            st.close();
            return new ServerResponse(0, null);
          } 
          catch (SQLException se)
          {
            return new ServerResponse(1, null);
          }
    }
    public static ServerResponse DeleteBook(int id)
    {
        try (Connection connection = DriverManager.getConnection(ServerAddress, Username, Password)){
            String query = "Delete from Books"
                    + " where id = ?";
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1, id);

            st.executeUpdate();
            st.close();
            return new ServerResponse(0, null);
          } 
          catch (SQLException se)
          {
            return new ServerResponse(1, null);
          }
    }
    
    public static ServerResponse DeleteAutor(int id)
    {
        try (Connection connection = DriverManager.getConnection(ServerAddress, Username, Password)){
            String query = "Delete from Authors"
                    + " where id = ?";
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1, id);

            st.executeUpdate();
            st.close();
            return new ServerResponse(0, null);
          } 
          catch (SQLException se)
          {
            if(se.getMessage().contains("books_authors_fk")) return new ServerResponse(1, null);
            return new ServerResponse(-1, null);
          }
    }
}
