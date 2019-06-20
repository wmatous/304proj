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
        if (method.equals("GET")) {
            return getPosting(Integer.parseInt(queryParams.get("postingId")));
        } else if (method.equals("POST")) {
            return updatePosting(Integer.parseInt(queryParams.get("postingId")),
                    queryParams.get("title"),
                    queryParams.get("active"),
                    java.sql.Date.valueOf(queryParams.get("startDate")),
                    queryParams.get("address"),
                    queryParams.get("postalCode"),
                    queryParams.get("description"));
        }
        return null;
    }

    // PreparedStatement handlePostingSkill(Map<String, String> queryParams, String[] path, String method) throws SQLException {
    //     if (method.equals("GET")) {
    //         return getPostingSkills(Integer.parseInt(queryParams.get("postingId")));
    //     }
    //     return null;
    // }

    PreparedStatement handleAllPostings(Map<String, String> queryParams, String[] path, String method) throws SQLException {
        if (method.equals("GET")) {
            return getAllPostings();
        }
        return null;
    }

    PreparedStatement handleSearchPostings(Map<String, String> queryParams, String[] path, String method) throws SQLException {
        if (method.equals("GET")) {
            System.out.println("get method");
            return searchPostings(queryParams.get("title"), queryParams.get("cityName"), queryParams.get("state"), queryParams.get("skills"));
        }
        return null;
    }

    private PreparedStatement searchPostings(String title, String cityName, String state, String skills) throws SQLException {
        String query = "SELECT DISTINCT P.postingId, DISTINCT P.title, P.active, P.startDate, P.address, P.postalCode, P.description, P.accountId " +
            "FROM Posting P, PostalCode PC WHERE P.postalCode = PC.postalCode";
        if (title != null){
           query += " OR P.title LIKE "+title;
        } 
        if(cityName != null ){
            query += " OR PC.cityName LIKE "+ cityName;
        }
        if (state != null){
            query += "OR PC.state LIKE "+state;
        }
        PreparedStatement ps = con.prepareStatement(query);
        System.out.println(title);
        return ps;
    }


    /*
     * returns specified posting
     */
    private PreparedStatement getPosting(int postingId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting WHERE postingId = ?");
        ps.setInt(1, postingId);
        return ps;
    }

    // /*
    //  * retrieves skills for specified posting
    //  */
    // private PreparedStatement getPostingSkills(int postingId) throws SQLException {
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
        PreparedStatement ps = con.prepareStatement("SELECT P.title, P.active, P.startDate, P.address, P.postalCode, P.description, P.accountId" +
                "FROM Involves I, Posting P WHERE I.name = ? AND I.postingId = P.postingId");
        ps.setString(2, skillName);
        return ps;
    }

    /*
     * returns all postings in database
     */
    private PreparedStatement getAllPostings() throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting P, PostalCode PC, Involves I " +
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
    private PreparedStatement updatePosting(int postingId, String title, String active, java.sql.Date startDate, String address, String postalCode, String description) throws SQLException {
        PreparedStatement ps = con.prepareStatement("UPDATE TABLE Posting SET title = ?, active = ?, startDate = ?, address = ?, postalCode = ?, description = ? WHERE postingId = ?");
        // do we need to be guarding against nulls here?
        ps.setString(1, title);
        ps.setString(2, active);
        ps.setDate(3, startDate);
        ps.setString(4, address);
        ps.setString(5, postalCode);
        ps.setString(6, description);

        con.setAutoCommit(false);
        ps.executeUpdate();
        con.commit();
        ps.close();

        return ps;
    }

}





