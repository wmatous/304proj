import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

class posting {
  private Connection con;
  posting(Connection c) {
    this.con = c;
  }

  SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  PreparedStatement handlePosting(Map<String, String> queryParams, String[] path, String method){
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

  PreparedStatement handlePostingSkill(Map<String, String> queryParams, String[] path, String method){
    if (method == "GET"){
      return getPostingSkills(Integer.parseInt(queryParams.get("postingId")));
    }
    return null;
  }

  PreparedStatement handleSearchPostings(Map<String, String> queryParams, String[] path, String method){
    if (method == "GET"){
      return searchPostings(queryParams.get("title"), queryParams.get("cityName"), queryParams.get("state"), queryParams.get("skills"));
    }
    return null;
  }

  PreparedStatement searchPostings(String title, String cityName, String state, String skills)
  {
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
    PreparedStatement getPosting(int postingId)
    {
      PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting WHERE postingId = ?");
        // !!! add postalCode to get city Name + state?
        // !!! add skill to get all that too?
      ps.setInt(1, postingId);
      return ps;
    }

    /*
     * retrieves skills for specified posting
     */ 
    PreparedStatement getPostingSkills(int postingId)
    {
      PreparedStatement ps = con.prepareStatement("SELECT skillName FROM Involves WHERE postingId = ?");
      ps.setInt(1, postingId);
      return ps;
      
    }

    PreparedStatement getPostingCityAndAddress(int postingId)
    {
      PreparedStatement ps = con.prepareStatement("SELECT cityName, state FROM PostalCode WHERE postalCode IN (SELECT postalCode FROM Posting WHERE postingId = ?");
      ps.setInt(1, postingId);
        // no idea if i can do it this way
      return ps;
    }

    /*
     * retrieves postings with specified skill
     */ 
    PreparedStatement getAllPostingsInvolvingSkill(String skillName)
    {
      PreparedStatement ps = con.prepareStatement("SELECT P.title, P.active, P.startDate, P.address, P.postalCode, P.description, P.accountId FROM Involves I, Posting P WHERE I.skillName = ? AND I.postingId = P.postingId");
      ps.setString(2, skillName);
      return ps;
    }

    /*
    * returns all postings in database
    */
    PreparedStatement getAllPostings()
    {
      PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting P, PostalCode PC, Involves I WHERE P.postalCode = PC.postalCode AND I.postingId = P.postingId");
      return ps;
        // natural join would probably be easier?
        // still need to get all the things here too
    }

    PreparedStatement getPostingsByTitle(String title)
    {
      PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting WHERE title like %title%");
      ps.setString(2, title);
      return ps;
    }

    /*
    * returns all postings in city
    */
    PreparedStatement getPostingsByCityName(String cityName)
    {
      PreparedStatement ps = con.prepareStatement("SELECT P.title, P.active, P.startDate, P.address, P.postalCode, P.description, P.accountId FROM Posting P, PostalCode PC WHERE PC.cityName = ? AND P.postalCode = PC.postalCode");
      ps.setString(2, cityName);
      return ps;
    }

    /*
    * returns all postings in a state
    */
    PreparedStatement getPostingsByState(String state)
    {
      PreparedStatement ps = con.prepareStatement("SELECT P.title, P.active, P.startDate, P.address, P.postalCode, P.description, P.accountId FROM Posting P, PostalCode PC WHERE PC.state = ? AND P.postalCode = PC.postalCode");
      ps.setString(3, state);
      return ps;
    }

    /*
     * updates posting table for specified posting
     */ 
    PreparedStatement updatePosting(int postingId, String title, String active, java.sql.Date startDate, String address, String postalCode, String description)
    {
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

    /*
     creates a new job posting
     */
     /*
    private void createPosting()
    {
    int                postingId;
    String             title;
    String             active;
    // !!! active should be boolean or string?
    Date               startDate;       
    String             address;
    String             postalCode;
    String             cityName;
    String             state;
    String             description;
    int                accountId;
    String             skillName;
    int                yearsExperience;
    PreparedStatement  ps;
    PreparedStatement  ps2;
    PreparedStatement  ps3;
      
    try
    {
      ps = con.prepareStatement("INSERT INTO Posting VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
      ps2 = con.prepareStatement("INSERT INTO PostalCode VALUES (?, ?, ?)");
      ps3 = con.prepareStatement("INSERT INTO Involves VALUES (?, ?, ?");
      // !!! do we still need just a Skill table
    
      System.out.print("\nPosting ID: ");
      postingId = Integer.parseInt(in.readLine());
      ps.setInt(1, postingId);
      ps3.setInt(1, postingId);

      System.out.print("\nJob Title: ");
      title = in.readLine();
      ps.setString(2, title);

      System.out.print("\nActive: ");
      active = in.readLine();
      ps.setString(3, active);
     
      System.out.print("\nStart Date: ");
      startDate = in.readLine();
      ps.setDate(4, startDate);

      System.out.print("\nAddress: ");
      address = in.readLine();
      ps.setString(5, address);

      System.out.print("\nPostal Code: ");
      postalCode = in.readLine();
      ps.setString(6, postalCode);
      ps2.setString(1, postalCode);

      System.out.print("\nCity Name: ");
      cityName = in.readLine();
      ps2.setString(2, cityName);

      System.out.print("\nState: ");
      state = in.readLine();
      ps2.setString(3, state);

      System.out.print("\nDescription: ");
      description = in.readLine();
      ps.setString(7, description);

      System.out.print("\nAccountID: ");
      accountId = in.readLine();
      ps.setString(8, accountId);

      System.out.print("\nSkill Name: ");
      skillName = in.readLine();
      ps3.setString(2, skillName);

      System.out.print("\nYears Experience: ");
      yearsExperience = in.readLine();
      ps3.setInt(3, yearsExperience);

      ps.executeUpdate();
      ps2.executeUpdate();
      ps3.executeUpdate();

      // commit work 
      con.commit();

      ps.close();
      ps2.close();
    }
    catch (IOException e)
    {
        System.out.println("IOException!");
    }
    catch (SQLException ex)
    {
        System.out.println("Message: " + ex.getMessage());
        try 
        {
        // undo the insert
        con.rollback(); 
        }
        catch (SQLException ex2)
        {
        System.out.println("Message: " + ex2.getMessage());
        System.exit(-1);
        }
    }
    }
    */

    /*
     * deletes a job posting
     */

    private void deletePosting()
    {
      int                postingId;
      PreparedStatement  ps;
      
      try
      {
        ps = con.prepareStatement("DELETE FROM Posting WHERE postingId = ?");

        System.out.print("\nPostingID: ");
        postingId = Integer.parseInt(in.readLine());
        ps.setInt(1, postingId);

        int rowCount = ps.executeUpdate();

        if (rowCount == 0)
        {
          System.out.println("\nPosting " + postingId + " does not exist!");
        }

        con.commit();

        ps.close();
      }
      catch (IOException e)
      {
        System.out.println("IOException!");
      }
      catch (SQLException ex)
      {
        System.out.println("Message: " + ex.getMessage());

        try 
        {
          con.rollback(); 
        }
        catch (SQLException ex2)
        {
          System.out.println("Message: " + ex2.getMessage());
          System.exit(-1);
        }
      }
    }
  }





