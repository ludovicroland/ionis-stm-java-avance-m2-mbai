import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
      System.out.println("Saisir votre identifiant personnel :");
      final int customerId = scanner.nextInt();
      scanner.nextLine();

      final Connection connection = DriverManager.getConnection("jdbc:sqlite:exercice4/src/main/resources/chinook.db");

      final PreparedStatement customer = connection.prepareStatement("SELECT * FROM customers WHERE CustomerId = ?");
      customer.setInt(1, customerId);

      final ResultSet resultSet = customer.executeQuery();

      //nom
      //prenom
      //phone
      //mail

      if (resultSet.next() == true)
      {
        final String firstName = resultSet.getString("FirstName");
        final String lastName = resultSet.getString("LastName");
        final String phone = resultSet.getString("Phone");
        final String email = resultSet.getString("Email");

        String newFirstName = null;
        String newLastName = null;
        String newPhone = null;
        String newEmail = null;
        int userChoice = 0;

        do
        {
          System.out.println("Informations clientes trouvées !");
          System.out.println("Quelle information souhaitez-vous mettre à jour ?");
          System.out.println("1. Prénom");
          System.out.println("2. Nom");
          System.out.println("3. Téléphone");
          System.out.println("4. Email");

          userChoice = scanner.nextInt();
          scanner.nextLine();
        } while (userChoice < 1 || userChoice > 4);

        System.out.println("Saisir la nouvelle valeur :");
        final String newValue = scanner.nextLine();

        System.out.println("Valeur a insérer : " + newValue);

        switch (userChoice)
        {
          case 1:
            newFirstName = newValue;
            break;

          case 2:
            newLastName = newValue;
            break;

          case 3:
            newPhone = newValue;
            break;

          case 4:
            newEmail = newValue;
            break;
        }

        final PreparedStatement updateStatement = connection.prepareStatement("UPDATE customers " +
            "SET FirstName = ?," +
            "LastName = ?," +
            "Phone = ?," +
            "Email = ? " +
            "WHERE CustomerId = ?");

        updateStatement.setString(1, newFirstName == null ? firstName : newFirstName);
        updateStatement.setString(2, newLastName == null ? lastName : newLastName);
        updateStatement.setString(3, newPhone == null ? phone : newPhone);
        updateStatement.setString(4, newEmail == null ? email : newEmail);
        updateStatement.setInt(5, customerId);

        final int count = updateStatement.executeUpdate();

        System.out.println(count + " ligne a bien été MAJ");

        updateStatement.close();
      }

      resultSet.close();
      customer.close();

      connection.close();
    }
    catch (SQLException exception)
    {
      exception.printStackTrace();
    }
  }

}
