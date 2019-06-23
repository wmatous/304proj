import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;

class posting {
    private Connection con;

    posting(Connection c) {
        this.con = c;
    }

    SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    PreparedStatement handlePosting(Map<String, String> queryParams, String[] path, String method) throws SQLException {
        System.out.println("handle posting");
        if (method.equals("GET")) {
            return getPosting(Integer.parseInt(queryParams.get("postingId")));
        } else if (method.equals("PUT")) {
            return updatePosting(Integer.parseInt(queryParams.get("postingId")),
                    queryParams.get("title"),
                    queryParams.get("active"),
                    java.sql.Date.valueOf(queryParams.get("startDate")),
                    queryParams.get("address"),
                    queryParams.get("postalCode"),
                    queryParams.get("cityName"),
                    queryParams.get("state"),
                    queryParams.get("description"),
                    queryParams.get("skills"));
        }
        return null;
    }

    // PreparedStatement handlePostingSkill(Map<String, String> queryParams, String[] path, String method) throws SQLException {
    //     System.out.println("handle posting skill");
    //     if (method.equals("GET")) {
    //         return getPostingSkills(Integer.parseInt(queryParams.get("postingId")));
    //     }
    //     return null;
    // }

    PreparedStatement handleAllPostings(Map<String, String> queryParams, String[] path, String method) throws SQLException {
        System.out.println("handle all");
        if (method.equals("GET")) {
            return getAllPostings();
        }
        return null;
    }

    PreparedStatement handleSearchPostings(Map<String, String> queryParams, String[] path, String method) throws SQLException {
        if (method.equals("GET")) {
            System.out.println("get method");
            System.out.println(queryParams.get("title"));
            return searchPostings(queryParams.get("title"), queryParams.get("cityName"), queryParams.get("state"));
        }
        return null;
    }

    private PreparedStatement searchPostings(String title, String cityName, String state) throws SQLException {
        System.out.println("searching postings");

        String query = "SELECT P.postingId, P.title, P.active, P.startDate, P.address, P.postalCode, P.description FROM Posting P, PostalCode PC " +
            "WHERE P.postalCode = PC.postalCode AND";
        if (title != null){
           query += " P.title LIKE ?";
        } 
        if(cityName != null ){
            query += " OR PC.cityName LIKE ?";
        }
        if (state != null){
            query += " OR PC.state LIKE ?";
        }

        //         if (title != null){
        //    query += " OR P.title LIKE " + "\"%" + "?" + "\"";
        // } 
        // if(cityName != null ){
        //     query += " OR PC.cityName LIKE "+ "\"%" + "?" + "\"";
        // }
        // if (state != null){
        //     query += " OR PC.state LIKE " + "\"%" + "?" + "\"";
        // }

        System.out.println(query);

        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, title);
        ps.setString(2, cityName);
        ps.setString(3, state);
        System.out.println(title + cityName + state);
        System.out.println(ps);
        return ps;
    }


    /*
     * returns specified posting
     */
    private PreparedStatement getPosting(int postingId) throws SQLException {
        System.out.println("getposting");
        PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting WHERE postingId = ?");
        ps.setInt(1, postingId);
        return ps;
    }

    // /*
    //  * retrieves skills for specified posting
    //  */
    // private PreparedStatement getPostingSkills(int postingId) throws SQLException {
    //     System.out.println("getps");
    //     PreparedStatement ps = con.prepareStatement("SELECT name FROM Involves WHERE postingId = ?");
    //     ps.setInt(1, postingId);
    //     return ps;
    // }

    PreparedStatement getPostingCityAndAddress(int postingId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT cityName, state FROM PostalCode " +
                "WHERE postalCode IN (SELECT postalCode FROM Posting WHERE postingId = ?)");
        ps.setInt(1, postingId);
        // no idea if i can do it this way
        return ps;
    }

    /*
     * retrieves postings with specified skill
     */
    private PreparedStatement getAllPostingsInvolvingSkill(String skillName) throws SQLException {
        System.out.println("gapis");
        PreparedStatement ps = con.prepareStatement("SELECT P.title, P.active, P.startDate, P.address, P.postalCode, P.description, P.accountId" +
                "FROM Involves I, Posting P WHERE I.name = ? AND I.postingId = P.postingId");
        ps.setString(2, skillName);
        return ps;
    }

    /*
     * returns all postings in database
     */
    private PreparedStatement getAllPostings() throws SQLException {
        System.out.println("get all postings");
        PreparedStatement ps = con.prepareStatement("SELECT P.postingId, P.title, P.active, P.startDate, P.address, P.postalCode, PC.cityName, PC.state, P.description, P.accountId, I.name FROM Posting P, PostalCode PC, Involves I " +
                "WHERE P.postalCode = PC.postalCode AND I.postingId = P.postingId");
        return ps;
    }

    /*
     * returns a posting by title
     */
    private PreparedStatement getPostingsByTitle(String title) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting WHERE title LIKE %title%");
        ps.setString(2, title);
        return ps;
    }

    /*
     * returns all postings in city
     */
    private PreparedStatement getPostingsByCityName(String cityName) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT P.title, P.active, P.startDate, P.address, P.postalCode, P.description, P.accountId FROM Posting P, PostalCode PC WHERE PC.cityName = ? AND P.postalCode = PC.postalCode");
        ps.setString(1, cityName);
        return ps;
    }

    /*
     * returns all postings in a state
     */
    private PreparedStatement getPostingsByState(String state) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT P.title, P.active, P.startDate, P.address, P.postalCode, P.description, P.accountId FROM Posting P, PostalCode PC WHERE PC.state = ? AND P.postalCode = PC.postalCode");
        ps.setString(1, state);
        return ps;
    }

    /*
     * updates posting table for specified posting
     */
    private PreparedStatement updatePosting(int postingId, String title, String active, java.sql.Date startDate, String address, String postalCode, String cityName, String state, String description, String skills) throws SQLException {
        PreparedStatement ps = con.prepareStatement("UPDATE TABLE Posting SET title = ?, active = ?, startDate = ?, address = ?, postalCode = ?, description = ? WHERE postingId = ?");
        // do we need to be guarding against nulls here?
        System.out.println("updaaaate");
        ps.setString(1, title);
        ps.setString(2, active);
        ps.setDate(3, startDate);
        ps.setString(4, address);
        ps.setString(5, postalCode);
        ps.setString(6, description);
        ps.executeUpdate();

        ps = con.prepareStatement("UPDATE TABLE PostalCode SET postalCode = ?, cityName = ?, state = ?");
        ps.setString(1, postalCode);
        ps.setString(2, cityName);
        ps.setString(3, state);
        ps.executeUpdate();

        ps = con.prepareStatement("UPDATE TABLE Involves SET postingId = ?, name = ?");
        ps.setInt(1, postingId);
        ps.setString(2, skills);

        con.setAutoCommit(false);

        ps.executeUpdate();

        con.commit();
        ps.close();

        return ps;
    }

}





