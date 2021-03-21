import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * @author Ludovic Roland
 * @since 2021.03.21
 */
final class Main
{

  public static void main(String[] args)
  {
    try
    {
      final Scanner scanner = new Scanner(System.in);

      System.out.println("Saisir le nom de l'artiste à ajouter :");
      final String artist = scanner.nextLine();

      System.out.println("Ajout de l'artist : " + artist);

      final Connection connection = DriverManager.getConnection("jdbc:sqlite:exercice5/src/main/resources/chinook.db");

      //Alternative possible : faire une requête de sélection pour retrouver le nouvel identifiant de notre artist
      final PreparedStatement artistStatement = connection.prepareStatement("INSERT INTO artists (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
      artistStatement.setString(1, artist);
      artistStatement.executeUpdate();

      final ResultSet artistResultSet = artistStatement.getGeneratedKeys();

      if(artistResultSet.next() == true)
      {
        final int artistId = artistResultSet.getInt(1);

        System.out.println("Saisir le nom de l'album à ajouter :");
        final String album = scanner.nextLine();

        System.out.println("Ajout de l'album : " + album);

        final PreparedStatement albumStatement = connection.prepareStatement("INSERT INTO albums (Title, ArtistId) VALUES (?, ?)");
        albumStatement.setString(1, album);
        albumStatement.setInt(2, artistId);
        albumStatement.executeUpdate();

        albumStatement.close();
      }
      else
      {
        //issue
      }

      artistResultSet.close();
      artistStatement.close();

      connection.close();
    }
    catch (SQLException exception)
    {
      exception.printStackTrace();
    }
  }

}
