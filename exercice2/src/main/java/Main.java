import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

      System.out.println("Quel est votre prénom ?");
      final String firstname = scanner.nextLine();

      System.out.println("Quel est votre nom ?");
      final String lastname = scanner.nextLine();

      final Connection connection = DriverManager.getConnection("jdbc:sqlite:exercice1/src/main/resources/chinook.db");

      //numéro de la facture
      //montant de la facture
      //date de la facture
      //détail des chansons achetées
      //-->nom chanson
      //-->artiste
      //-->format
      //-->nom album

      final PreparedStatement statement = connection.prepareStatement("SELECT invoices.InvoiceId as InvoiceId, invoices.Total as Total, invoices.InvoiceDate as Date, tracks.Name as Track, media_types.Name as MediaType, artists.Name as Artist, albums.Title as Album " +
          "FROM invoices " +
          "INNER JOIN customers ON invoices.CustomerId = customers.CustomerId " +
          "INNER JOIN invoice_items ON invoices.InvoiceId = invoice_items.InvoiceId " +
          "INNER JOIN tracks ON invoice_items.TrackId = tracks.TrackId " +
          "INNER JOIN media_types ON tracks.MediaTypeId = media_types.MediaTypeId " +
          "INNER JOIN albums ON tracks.AlbumId = albums.AlbumId " +
          "INNER JOIN artists ON albums.ArtistId = artists.ArtistId " +
          "WHERE customers.FirstName = ? " +
          "AND customers.LastName = ?");

      statement.setString(1, firstname);
      statement.setString(2, lastname);

      final ResultSet resultSet = statement.executeQuery();
      int currentInvoiceId = -1;

      while (resultSet.next() == true)
      {
        final int invoiceId = resultSet.getInt("InvoiceId");
        final double total = resultSet.getDouble("Total");
        final String date = resultSet.getString("Date");
        final String track = resultSet.getString("Track");
        final String mediaType = resultSet.getString("MediaType");
        final String artist = resultSet.getString("Artist");
        final String album = resultSet.getString("Album");

        if (currentInvoiceId != invoiceId)
        {
          currentInvoiceId = invoiceId;

          final Date realDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);

          System.out.println("*****************************");
          System.out.println("FACTURE N°" + invoiceId);
          System.out.println("*****************************");
          System.out.println("Le " + new SimpleDateFormat("dd/MM/yyyy").format(realDate));
          System.out.println("*****************************");
          System.out.println("PRIX TOTAL :" + total + "€");
          System.out.println("*****************************");
          System.out.println("DETAIL DE LA FACTURE :");
        }

        System.out.println(track + "(" + mediaType + ") issue de " + album + " de " + artist);
      }

      resultSet.close();
      statement.close();
      connection.close();
    }
    catch (SQLException | ParseException exception)
    {
      exception.printStackTrace();
    }
  }

}
