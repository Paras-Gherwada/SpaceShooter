import java.sql.*;

class DatabaseConnector {
    Connection conn = null;
    String temp = null;
    int points = 0, asteroids = 0, death_count = 0;

    DatabaseConnector() throws Exception {
        createConnection();
    }

    void createConnection() throws Exception {
	Class.forName("oracle.jdbc.OracleDriver");
        this.conn = DriverManager.getConnection(
            "jdbc:oracle:thin:@localhost:1521/xepdb1",
            "apophis",
            "apophis"
        );
    }

    int addPlayer(String username, String password) throws Exception {
        PreparedStatement pstmt=conn.prepareStatement("insert into Space_Shooter values(?, ?, ?, ?)");
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setInt(3, 0);
        pstmt.setInt(4, 0);
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected;
    }

    boolean validateUsername(String username) throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from space_shooter where username='" + username + "'");

        if(!rs.next())
            return false;

        temp = rs.getString("password");
        return true;
    }

    boolean validatePassword(String password) throws Exception {
        return temp.equals(password);
    }

    int getPoints(String username) throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from space_shooter where username='" + username + "'");
        if(rs.next())
            points = rs.getInt("points");
        return points;
    }

    int getAsteroids(String username) throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from space_shooter where username='" + username + "'");

        if(rs.next())
            asteroids = rs.getInt("asteroids");

        return asteroids;
    }

    int getDeathCount(String username) throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from space_shooter where username='" + username + "'");

        if(rs.next())
            death_count = rs.getInt("death_count");
        return death_count;
    }

    void updatePoints(String uname, int points, int asteroids, int death_count) throws Exception {
        PreparedStatement pstmt = conn.prepareStatement("update space_shooter set points = ?, asteroids = asteroids + ?, death_count = ? where username = ?");
        pstmt.setInt(1, points);
        pstmt.setInt(2, asteroids);
        pstmt.setInt(3, death_count);
        pstmt.setString(4, uname);
        pstmt.executeUpdate();
    }

    ResultSet getStatistics() throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from space_shooter order by points desc");
        return rs;
    }

    void closeConnection() throws Exception {
        conn.close();
    }
}