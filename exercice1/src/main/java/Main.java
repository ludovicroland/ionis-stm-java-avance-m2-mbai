import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Ludovic Roland
 * @since 2021.03.06
 */
final class Main
{

  public static void main(String[] args)
  {
    try
    {
      final Connection connection = DriverManager.getConnection("jdbc:sqlite:exercice1/src/main/resources/chinook.db");

      final Statement statement = connection.createStatement();
      final ResultSet resultSet = statement.executeQuery("SELECT * FROM artists");

      final ResultSetMetaData metaData = resultSet.getMetaData();

      //CREATE TABLE "artists"
      //(
      //    [ArtistId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
      //    [Name] NVARCHAR(120)
      //)

      final StringBuilder stringBuilder = new StringBuilder();

      stringBuilder.append("CREATE TABLE \"").append(metaData.getTableName(1)).append("\"\n");
      stringBuilder.append("(\n");

      for (int index = 1; index <= metaData.getColumnCount(); index++)
      {
        stringBuilder.append("\t[").append(metaData.getColumnName(index)).append("] ");
        stringBuilder.append(metaData.getColumnTypeName(index));

        if (metaData.getPrecision(index) > 0)
        {
          stringBuilder.append("(").append(metaData.getPrecision(index)).append(")");
        }

        if (metaData.isAutoIncrement(index))
        {
          stringBuilder.append(" PRIMARY KEY AUTOINCREMENT ");
        }

        if (metaData.isNullable(index) == ResultSetMetaData.columnNoNulls)
        {
          stringBuilder.append("NOT NULL");
        }

        if (index < metaData.getColumnCount())
        {
          stringBuilder.append(",\n");
        }
      }

      stringBuilder.append("\n)");

      System.out.println(stringBuilder.toString());

      resultSet.close();
      statement.close();
      connection.close();
    }
    catch (SQLException throwables)
    {
      throwables.printStackTrace();
    }
  }

}
