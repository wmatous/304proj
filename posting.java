import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;

class posting {
  private Connection con;
  posting(Connection c) {
    this.con = c;
  }

  SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  PreparedStatement handlePosting(Map<String, String> queryParams, String[] path, String method) throws SQLException {
    if (method == "GET"){
      return getPosting(Integer.parseInt(queryParams.get("postingId")));
    } else if (method == "PUT") {
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

  PreparedStatement handlePostingSkill(Map<String, String> queryParams, String[] path, String method) throws SQLException {
    if (method == "GET"){
      return getPostingSkills(Integer.parseInt(queryParams.get("postingId")));
    }
    return null;
  }

  PreparedStatement handleAllPostings(Map<String, String> queryParams, String[] path, String method) throws SQLException {
    if (method == "GET"){
      return getAllPostings();
    }
    return null;
  }

  PreparedStatement handleSearchPostings(Map<String, String> queryParams, String[] path, String method) throws SQLException {
    if (method == "GET"){
      return searchPostings(queryParams.get("title"), queryParams.get("cityName"), queryParams.get("state"), queryParams.get("skills"));
    }
    return null;
  }

  PreparedStatement searchPostings(String title, String cityName, String state, String skills) throws SQLException {
    PreparedStatement titlePS;
    PreparedStatement cityNamePS;
    PreparedStatement statePS;
    PreparedStatement skillsPS;

    if (title != null) {
      titlePS = getPostingsByTitle(title);
    }
    if (cityName != null) {
      cityNamePS = getPostingsByCityName(cityName);
    }
    if (state != null) {
      statePS = getPostingsByState(state);
    }
    if (skills != null) {
      skillsPS = getAllPostingsInvolvingSkill(skills);
    }

    PreparedStatement ps = con.prepareStatement("titlePS INTERSECT cityNamePS INTERSECT statePS INTERSECT skillsPS");
    // i have no idea if i can actually do this
    return ps;
  }

    /*
     * returns specified posting
     */ 
    PreparedStatement getPosting(int postingId) throws SQLException {
      PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting WHERE postingId = ?");
      ps.setInt(1, postingId);
      return ps;
    }

    /*
     * retrieves skills for specified posting
     */ 
    PreparedStatement getPostingSkills(int postingId) throws SQLException {
      PreparedStatement ps = con.prepareStatement("SELECT skillName FROM Involves WHERE postingId = ?");
      ps.setInt(1, postingId);
      return ps;
      
    }

    PreparedStatement getPostingCityAndAddress(int postingId) throws SQLException {
      PreparedStatement ps = con.prepareStatement("SELECT cityName, state FROM PostalCode " + 
        "WHERE postalCode IN (SELECT postalCode FROM Posting WHERE postingId = ?");
      ps.setInt(1, postingId);
        // no idea if i can do it this way
      return ps;
    }

    /*
     * retrieves postings with specified skill
     */ 
    PreparedStatement getAllPostingsInvolvingSkill(String skillName) throws SQLException {
      PreparedStatement ps = con.prepareStatement("SELECT P.title, P.active, P.startDate, P.address, P.postalCode, P.description, P.accountId" + 
        "FROM Involves I, Posting P WHERE I.skillName = ? AND I.postingId = P.postingId");
      ps.setString(2, skillName);
      return ps;
    }

    /*
    * returns all postings in database
    */
    PreparedStatement getAllPostings() throws SQLException {
      PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting P, PostalCode PC, Involves I " + 
        "WHERE P.postalCode = PC.postalCode AND I.postingId = P.postingId");
      return ps;
    }

    /*
    * returns a posting by title
    */
    PreparedStatement getPostingsByTitle(String title) throws SQLException {
      PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting WHERE title LIKE %title%");
      ps.setString(2, title);
      return ps;
    }

    /*
    * returns all postings in city
    */
    PreparedStatement getPostingsByCityName(String cityName) throws SQLException {
      PreparedStatement ps = con.prepareStatement("SELECT P.title, P.active, P.startDate, P.address, P.postalCode, P.description, P.accountId FROM Posting P, PostalCode PC WHERE PC.cityName = ? AND P.postalCode = PC.postalCode");
      ps.setString(2, cityName);
      return ps;
    }

    /*
    * returns all postings in a state
    */
    PreparedStatement getPostingsByState(String state) throws SQLException {
      PreparedStatement ps = con.prepareStatement("SELECT P.title, P.active, P.startDate, P.address, P.postalCode, P.description, P.accountId FROM Posting P, PostalCode PC WHERE PC.state = ? AND P.postalCode = PC.postalCode");
      ps.setString(3, state);
      return ps;
    }

    /*
     * updates posting table for specified posting
     */ 
    PreparedStatement updatePosting(int postingId, String title, String active, java.sql.Date startDate, String address, String postalCode, String description) throws SQLException {
      PreparedStatement ps = con.prepareStatement("UPDATE TABLE Posting SET title = ?, active = ?, startDate = ?, address = ?, postalCode = ?, description = ? WHERE postingId = ?");
      // do we need to be guarding against nulls here?
      ps.setString(2, title);
      ps.setString(3, active);
      ps.setDate(4, startDate);
      ps.setString(5, address);
      ps.setString(6, postalCode);
      ps.setString(7, description);

      con.setAutoCommit(false);
      ps.executeUpdate();
      con.commit();
      ps.close();

      return ps;
    }

  }





