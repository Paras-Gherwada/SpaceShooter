package spaceshooter;

import java.sql.*;

public class DatabaseConnector {
    Connection connection;
    Statement statement;
    PreparedStatement prepared_statement;
    ResultSet result_set;
    
    DatabaseConnector()throws Exception{
        createConnection();
    }
    
    void createConnection() throws Exception{
        Class.forName("oracle.jdbc.OracleDriver");
        this.connection = DriverManager.getConnection   (
                                                            "jdbc:oracle:thin:@localhost:1521/xepdb1",
                                                            "apophis",
                                                            "apophis"
                                                        );
        statement = connection.createStatement();
    }
    
    int addPlayer(String username, String password) throws Exception{
        prepared_statement = connection.prepareStatement("INSERT INTO SPACE_SHOOTER VALUES(?, ?, ?, ?, ?)");
        prepared_statement.setString(1, username);
        prepared_statement.setString(2, password);
        prepared_statement.setInt(3, 0);
        prepared_statement.setInt(4, 0);
        prepared_statement.setInt(5, 0);
        
        return prepared_statement.executeUpdate();
    }
    
    boolean validateUsername(String username) throws Exception{
        result_set = statement.executeQuery("SELECT * FROM SPACE_SHOOTER WHERE USERNAME = '"+username+"'");
        if(!result_set.next())
            return false;
        return true;
    }
    
    boolean validatePassword(String password) throws Exception{
        return result_set.getString("password").equals(password);
    }
    
    String [] fetchDetails() throws Exception{
        String[] user_data =   {
                                result_set.getString("username"),
                                result_set.getString("points"),
                                result_set.getString("asteroids"),
                                result_set.getString("death_count")
                            };
        return user_data;
    }
    
    void updatePoints(String uname, int points, int asteroids, int death_count) throws Exception{
        prepared_statement = connection.prepareStatement    (
                                                                "UPDATE SPACE_SHOOTER SET  POINTS = ?,"+
                                                                "ASTEROIDS = ASTEROIDS+?,"+
                                                                "DEATH_COUNT = ?"+
                                                                "WHERE USERNAME = ?"
                                                            );
        prepared_statement.setInt(1, points);
        prepared_statement.setInt(2, asteroids);
        prepared_statement.setInt(3, death_count);
        prepared_statement.setString(4, uname);
        prepared_statement.executeUpdate();
    }
    
    ResultSet getStatistics() throws Exception{
        return statement.executeQuery("SELECT * FROM SPACE_SHOOTER ORDER BY POINTS DESC");
    }
    
    void closeConnection() throws Exception{
        connection.close();
    }
}