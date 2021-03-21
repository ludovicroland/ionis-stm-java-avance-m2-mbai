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
      System.out.println("Saisir le nom de la playlist :");
      final String playlistName = scanner.nextLine();

      final Connection connection = DriverManager.getConnection("jdbc:sqlite:exercice6/src/main/resources/chinook.db");

      final PreparedStatement playlistStatement = connection.prepareStatement("INSERT INTO playlists (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
      playlistStatement.setString(1, playlistName);

      playlistStatement.executeUpdate();

      final ResultSet playlistResultSet = playlistStatement.getGeneratedKeys();

      if (playlistResultSet.next() == true)
      {
        final int playlistId = playlistResultSet.getInt(1);
        playlistResultSet.close();
        playlistStatement.close();

        int userChoice = 0;

        do
        {
          final Statement tracksStatement = connection.createStatement();
          final ResultSet tracksResultSet = tracksStatement.executeQuery("SELECT * FROM Tracks");

          while(tracksResultSet.next() == true)
          {
            System.out.println(tracksResultSet.getInt("TrackId") + ". " + tracksResultSet.getString("Name"));
          }

          tracksResultSet.close();
          tracksStatement.close();

          System.out.println("Quelle chanson voulez-vous ajouter à la playslist ? (0 pour arrêter) ");
          userChoice = scanner.nextInt();
          scanner.nextLine();

          if(userChoice != 0)
          {
            final PreparedStatement playlistTrackStatement = connection.prepareStatement("INSERT INTO playlist_track VALUES(?, ?)");
            playlistTrackStatement.setInt(1, playlistId);
            playlistTrackStatement.setInt(2, userChoice);

            playlistTrackStatement.executeUpdate();
          }

        }while(userChoice != 0);
      }

      connection.close();
    }
    catch (SQLException exception)
    {
      exception.printStackTrace();
    }
  }

}
