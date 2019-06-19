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


    /*
     * returns specified posting
     */ 
    PreparedStatement getPosting(int postingId)
    {
      PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting WHERE postingId = ?");
        // !!! add postalCode to get city Name + state?
        // !!! add skill to get all that too?
        // or do i just use methods below to get all that
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
      PreparedStatement ps = con.prepareStatement("SELECT postingId FROM Involves, Posting WHERE skillName = ? AND Involves.postingId = Posting.postingId");
      ps.setString(2, skillName);
      return ps;
    }

    /*
    * returns all postings in database
    */
    PreparedStatement getAllPostings()
    {
      PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting, PostalCode, Involves WHERE Posting.postalCode = PostalCode.postalCode AND Involves.postingId = Posting.postingId");
      return ps;
        // natural join would probably be easier?
        // still need to get all the things here too

    }


    /*
    * returns all postings in city


    String getPostingsByCityName(String cityName)
    {
      PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting, PostalCode WHERE PostalCode.cityName = ? AND Posting.postalCode = PostalCode.postalCode");
      ps.setString(2, cityName);
      return getRecordsAsJSON(ps);

    }

    /*
    * returns all postings in a state


    String getPostingsByState(String state)
    {
      PreparedStatement ps = con.prepareStatement("SELECT * FROM Posting, PostalCode WHERE PostalCode.state = ? AND Posting.postalCode = PostalCode.postalCode");
      ps.setString(3, state);
      return getRecordsAsJSON(ps);
    }


    /*
    * returns all recommended postings

    private String getAllRecommendedPostings() {
        // ! should this be in account view or posting view??
      return "";
    }

*/

    /*
     * updates posting table for specified posting
     */ 
    PreparedStatement updatePosting(int postingId, String title, String active, java.sql.Date startDate, String address, String postalCode, String description)
    {
      PreparedStatement ps = con.prepareStatement("UPDATE TABLE Posting SET title = ?, active = ?, startDate = ?, address = ?, postalCode = ?, description = ? WHERE postingId = ?");
        // !!! can we update accountId like this?

      ps.setString(2, title);
      ps.setString(3, active);
      ps.setDate(4, startDate);
      ps.setString(5, address);
      ps.setString(6, postalCode);
      ps.setString(7, description);
      return ps;
    }

  }





