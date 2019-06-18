import java.util.*;
import java.sql.*;

class posting {
  private Connection con;
  posting(Connection c) {
    this.con = c;
  }

     /*
     creates a new job posting
     */
     
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

    /*
     * returns specified posting
     */ 
    String getPosting(int postingId)
    {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting WHERE postingId = ?");
        // !!! add postalCode to get city Name + state?
        // !!! add skill to get all that too?
        // or do i just use methods below to get all that
        ps.setInt(1, postingId);
        return getRecordsAsJSON(ps);
    }

    /*
     * retrieves skills for specified posting
     */ 
    String getPostingSkills(int postingId)
    {
        PreparedStatement ps = con.createStatement("SELECT skillName FROM Involves WHERE postingId = ?");
        ps.setInt(1, postingId);
        return getRecordsAsJSON(ps);
      
    }

    String getPostingCityAndAddress(int postingId)
    {
        PreparedStatement ps = con.createStatement("SELECT cityName, state FROM PostalCode WHERE postalCode IN (SELECT postalCode FROM Posting WHERE postingId = ?");
        ps.setInt(1, postingId);
        // no idea if i can do it this way
        return getRecordsAsJSON(ps);
    }

    /*
     * retrieves postings with specified skill
     */ 
    String getAllPostingsInvolvingSkill(String skillName)
    {
        PreparedStatement ps = con.createStatement("SELECT postingId FROM Involves, Posting WHERE skillName = ? AND Involves.postingId = Posting.postingId");
        ps.setString(2, skillName);
        return getRecordsAsJSON(ps);
    }

    /*
    * returns all postings in database
    */
    String getAllPostings()
    {
        PreparedStatement ps = con.createStatement("SELECT * FROM Posting, PostalCode, Involves WHERE Posting.postalCode = PostalCode.postalCode AND Involves.postingId = Posting.postingId");
        return getRecordsAsJSON(ps);
        // natural join would probably be easier?
        // still need to get all the things here too

    }

    /*
    * returns all postings in city
    */

    String getPostingsByCityName(String cityName)
    {
        PreparedStatement ps = con.createStatement("SELECT * FROM Posting, PostalCode WHERE PostalCode.cityName = ? AND Posting.postalCode = PostalCode.postalCode");
        ps.setString(2, cityName);
        return getRecordsAsJSON(ps);

    }

    /*
    * returns all postings in a state
    */

    String getPostingsByState(String state)
    {
        PreparedStatement ps = con.createStatement("SELECT * FROM Posting, PostalCode WHERE PostalCode.state = ? AND Posting.postalCode = PostalCode.postalCode");
        ps.setString(3, state);
        return getRecordsAsJSON(ps);
    }


    /*
    * returns all recommended postings
    */
    private String getAllRecommendedPostings() {
        // ! should this be in account view or posting view??

    }



    /*
     * updates posting table for specified posting
     */ 
    int updatePosting(int postingId, String title, String active, Date startDate, String address, String postalCode, String description, int accountId)
    {
        PreparedStatement ps = con.createStatement("UPDATE TABLE Posting SET title = ?, active = ?, startDate = ?, address = ?, postalCode = ?, description = ?, accountId = ? WHERE postingId = ?");
        // !!! can we update accountId like this?
        ps.setString(2, title);
        ps.setString(3, active);
        ps.setDate(4, startDate);
        ps.setString(5, address);
        ps.setString(6, postalCode);
        ps.setString(7, description);
        ps.setInt(8, accountId);
        return executeUpdateStatement(ps);
    }
  }





