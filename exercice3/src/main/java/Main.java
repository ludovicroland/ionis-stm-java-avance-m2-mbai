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
      final Connection connection = DriverManager.getConnection("jdbc:sqlite:exercice3/src/main/resources/chinook.db");
      final Statement playlistStatement = connection.createStatement();
      final ResultSet playlistResultSet = playlistStatement.executeQuery("SELECT * FROM playlists");

      while (playlistResultSet.next() == true)
      {
        final int playlistId = playlistResultSet.getInt("PlaylistId");
        final String name = playlistResultSet.getString("Name");

        System.out.println(playlistId + ". " + name);
      }

      playlistResultSet.close();
      playlistStatement.close();

      final Scanner scanner = new Scanner(System.in);
      System.out.println("Saisir le numéro de la playlist à afficher :");
      final int playlistId = scanner.nextInt();

      final PreparedStatement statement = connection.prepareStatement("SELECT tracks.Name as Name " +
          "FROM tracks " +
          "INNER JOIN playlist_track ON tracks.TrackId = playlist_track.TrackId " +
          "WHERE playlist_track.PlaylistId = ? ");

      statement.setInt(1, playlistId);

      final ResultSet resultSet = statement.executeQuery();

      while (resultSet.next() == true)
      {
        final String track = resultSet.getString("Name");
        System.out.println(track);
      }

      resultSet.close();
      statement.close();
      connection.close();
    }
    catch (SQLException exception)
    {
      exception.printStackTrace();
    }
  }

}
