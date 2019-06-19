import java.io.*;
import java.net.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;
import oracle.jdbc.driver.OracleDriver;

import javax.json.*;

import static helpers.DBVars.username;
import static helpers.DBVars.password;


// for the login window
import javax.json.stream.JsonGenerator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * This class implements a graphical login window and a simple text
 * interface for interacting with the branch table 
 */ 
public class branchtwo implements ActionListener 

{
    // command line reader 
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	char[] clientSentence= new char[10000];
	String capitalizedSentence;
	ServerSocket welcomeSocket;

    private Connection con;
    private posting posting = new posting(con);
    private interviewAndOfferQueries intAndOff = new interviewAndOfferQueries(con);
	SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // user is allowed 3 login attempts
    private int loginAttempts = 0;

    // components of the login window
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JFrame mainFrame;


    /*
     * constructs login window and loads JDBC driver
     */ 
    public branchtwo() {
      mainFrame = new JFrame("User Login");

      JLabel usernameLabel = new JLabel("Enter username: ");
      JLabel passwordLabel = new JLabel("Enter password: ");

      usernameField = new JTextField(10);
      passwordField = new JPasswordField(10);
      passwordField.setEchoChar('*');

      JButton loginButton = new JButton("Log In");

      JPanel contentPane = new JPanel();
      mainFrame.setContentPane(contentPane);


      // layout components using the GridBag layout manager

      GridBagLayout gb = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();

      contentPane.setLayout(gb);
      contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      // place the username label 
      c.gridwidth = GridBagConstraints.RELATIVE;
      c.insets = new Insets(10, 10, 5, 0);
      gb.setConstraints(usernameLabel, c);
      contentPane.add(usernameLabel);

      // place the text field for the username 
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.insets = new Insets(10, 0, 5, 10);
      gb.setConstraints(usernameField, c);
      contentPane.add(usernameField);

      // place password label
      c.gridwidth = GridBagConstraints.RELATIVE;
      c.insets = new Insets(0, 10, 10, 0);
      gb.setConstraints(passwordLabel, c);
      contentPane.add(passwordLabel);

      // place the password field 
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.insets = new Insets(0, 0, 10, 10);
      gb.setConstraints(passwordField, c);
      contentPane.add(passwordField);

      // place the login button
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.insets = new Insets(5, 10, 10, 10);
      c.anchor = GridBagConstraints.CENTER;
      gb.setConstraints(loginButton, c);
      contentPane.add(loginButton);

      // register password field and OK button with action event handler
      passwordField.addActionListener(this);
      loginButton.addActionListener(this);

      // anonymous inner class for closing the window
      mainFrame.addWindowListener(new WindowAdapter() 
      {
	public void windowClosing(WindowEvent e) 
	{ 
	  System.exit(0); 
	}
      });

      // size the window to obtain a best fit for the components
      mainFrame.pack();

      // center the frame
      Dimension d = mainFrame.getToolkit().getScreenSize();
      Rectangle r = mainFrame.getBounds();
      mainFrame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

      // make the window visible
      mainFrame.setVisible(true);

      // place the cursor in the text field for the username
      usernameField.requestFocus();

      try 
      {
	// Load the Oracle JDBC driver
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		// may be oracle.jdbc.driver.OracleDriver as of Oracle 11g
      }
      catch (SQLException ex)
      {
	System.out.println("Message: " + ex.getMessage());
	System.exit(-1);
      }
	}
	
	// job site methods

	/*
     * lists tables in DB
     */ 
    private void checkTables()
    {
	
	PreparedStatement  ps;
	  
	try
	{
		// disable auto commit mode
		con.setAutoCommit(false);

		ps = con.prepareStatement("select table_name from user_tables");
		ResultSet rs = ps.executeQuery();
		while(rs.next())
	  {
		System.out.println(rs.getString("table_name"));
	  }
		
	  con.commit();

	  ps.close();
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
	
	public void dropTable(String tableName){
		{
	
		PreparedStatement  ps;
			
		try
		{
			// disable auto commit mode
			con.setAutoCommit(false);
			// may have to include schema eg ORA_WMATOUS.tableName
			ps = con.prepareStatement("DROP TABLE "+ tableName+"  CASCADE CONSTRAINTS PURGE ");
			ps.executeUpdate();
			
			con.commit();
	
			ps.close();
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

	/*
     * deletes all tables in DB
     */ 
    private void clearAllTables()
    {
	
	PreparedStatement  ps;
	  
	try
	{
		
		String tableName = "CountryLanguage";
		System.out.println("Drop Table? y/n " + tableName);
		Integer choice = Integer.parseInt(in.readLine());
		if (choice != 0){
			dropTable(tableName);
		}
		tableName = "City";
		System.out.println("Drop Table? y/n " + tableName);
		 choice = Integer.parseInt(in.readLine());
		if (choice != 0){
			dropTable(tableName);
		}
		tableName = "PostalCode";
		System.out.println("Drop Table? y/n " + tableName);
		 choice = Integer.parseInt(in.readLine());
		if (choice != 0){
			dropTable(tableName);
		}
		tableName = "Account";
		System.out.println("Drop Table? y/n " + tableName);
		 choice = Integer.parseInt(in.readLine());
		if (choice != 0){
			dropTable(tableName);
		}
		tableName = "Skill";
		System.out.println("Drop Table? y/n " + tableName);
		 choice = Integer.parseInt(in.readLine());
		if (choice != 0){
			dropTable(tableName);
		}
		tableName = "Posting";
		System.out.println("Drop Table? y/n " + tableName);
		 choice = Integer.parseInt(in.readLine());
		if (choice != 0){
			dropTable(tableName);
		}
		tableName = "ExperiencedAt";
		System.out.println("Drop Table? y/n " + tableName);
		 choice = Integer.parseInt(in.readLine());
		if (choice != 0){
			dropTable(tableName);
		}
		tableName = "Endorses";
		System.out.println("Drop Table? y/n " + tableName);
		 choice = Integer.parseInt(in.readLine());
		if (choice != 0){
			dropTable(tableName);
		}
		tableName = "Involves";
		System.out.println("Drop Table? y/n " + tableName);
		 choice = Integer.parseInt(in.readLine());
		if (choice != 0){
			dropTable(tableName);
		}
		tableName = "Interview";
		System.out.println("Drop Table? y/n " + tableName);
		 choice = Integer.parseInt(in.readLine());
		if (choice != 0){
			dropTable(tableName);
		}
		tableName = "Offer";
		System.out.println("Drop Table? y/n " + tableName);
		 choice = Integer.parseInt(in.readLine());
		if (choice != 0){
			dropTable(tableName);
		}
		
		
	}
	catch (IOException e)
		{
			System.out.println("IOException!");
	
			try
			{
			con.close();
			System.exit(-1);
			}
			catch (SQLException ex)
			{
			 System.out.println("Message: " + ex.getMessage());
			}
		}
    }

	/*
     * retrieves recommended postings for account
     */ 
    private String getRecommendedPostings(int accountId) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT Involves.postingId "+
		"FROM (((Account "+
		"INNER JOIN ExperiencedAt ON ExperiencedAt.accountId = Account.accountId) "+
		"INNER JOIN Skill ON Skill.name = ExperiencedAt.name) "+
		"INNER JOIN Involves ON Involves.name = Skill.name ) WHERE Account.accountId = ?");
		ps.setInt(1, accountId);
		return getRecordsAsJSON(ps);
	  
	}

	/*
     * retrieves endorsement count for account
     */ 
    private String getAccountEndorsements(int accountId) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as count FROM Endorses WHERE endorsedId = ?");
		ps.setInt(1, accountId);
		return getRecordsAsJSON(ps);
	  
	}

	/*
     * retrieves skills for account
     */ 
    private String getAccountSkills(int accountId) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT name FROM ExperiencedAt WHERE accountId = ?");
		ps.setInt(1, accountId);
		return getRecordsAsJSON(ps);
	  
	}

	/*
     * retrieves specified account
     */ 
    private String getAccount(int accountId) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * FROM Account WHERE accountId = ?");
		ps.setInt(1, accountId);
		return getRecordsAsJSON(ps);
	  
	}

	/*
     * updates account table for specified account
     */ 
    private String updateAccount(int accountId, String name, String email, String postalCode) throws SQLException {
		// create postalcode?
		PreparedStatement ps = con.prepareStatement("UPDATE TABLE Account SET name = ?, email = ?, postalCode = ? WHERE accountId = ?");
		ps.setString(1, name);
		ps.setString(2, email);
		ps.setString(3, postalCode);
		ps.setInt(4, accountId);
		return executeUpdateStatement(ps);
	}


	/*
	* executes preparedstatement update
	*/
	public String executeUpdateStatement(PreparedStatement ps){
		try
		{
			// disable auto commit mode
			con.setAutoCommit(false);
			String updatedRecords = "{ status: 'success', updatedRecords : " + ps.executeUpdate() + "}";
			con.commit();
			ps.close();
			return updatedRecords;
			
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
			return "{ status: 'failure'}";
		}
	}


	
	/*
     * takes preparedstatement select query and executes query, returning JSON string
     */ 
    String getRecordsAsJSON(PreparedStatement ps)
    {
	  
	try
	{
		// disable auto commit mode
		con.setAutoCommit(false);

		ResultSet rs = ps.executeQuery();

		// get info on ResultSet
		ResultSetMetaData rsmd = rs.getMetaData();
		// get number of columns
		int numCols = rsmd.getColumnCount();

		ArrayList<String> fields = new ArrayList<String>();
		// get column names;
		for (int i = 0; i < numCols; i++)
		{
			fields.add(rsmd.getColumnName(i+1));    
		}

		StringWriter sw = new StringWriter();
		JsonGenerator gen = Json.createGenerator(sw);

		gen.writeStartArray();
		
		
		while(rs.next())
		{
			gen.writeStartObject();
			for (String s : fields){
				gen.write(s, rs.getString(s));
			}
			gen.writeEnd();
			
		}
		gen.close();

		con.commit();

	  ps.close();

		return sw.toString();
	  
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
	return "[]";
	}
	



	/*
     * creates and populates tables
     */ 
    private void createTablesJobSite()
    {
	
	PreparedStatement  ps;
	  
	try
	{
		// disable auto commit mode
		con.setAutoCommit(false);
	  
		System.out.println("Add Table CountryLanguage? y/n ");
		Integer choice = Integer.parseInt(in.readLine());
		if (choice !=0){
			ps = con.prepareStatement("CREATE TABLE "+
			"CountryLanguage "+
			"(country char(30) PRIMARY KEY,"+
			"primaryLanguage char(30))");
			System.out.println(ps.executeUpdate());
			con.commit();
		}

		System.out.println("Add Table City? y/n ");
		choice = Integer.parseInt(in.readLine());
		if (choice !=0){
			ps = con.prepareStatement("CREATE TABLE City "+
			"(cityName char(30), state char(30), country char(30), population int,"+
			"PRIMARY KEY (cityName, state),"+
			"FOREIGN KEY (country) REFERENCES CountryLanguage(country))");
			System.out.println(ps.executeUpdate());
			con.commit();
		}
		System.out.println("Add Table PostalCode? y/n ");
		choice = Integer.parseInt(in.readLine());
		if (choice !=0){
			ps = con.prepareStatement("CREATE TABLE "+
			"PostalCode "+
			"(postalCode char(10), cityName char(30) not null, state char(30) not null, "+
			"PRIMARY KEY (postalCode)," +
			"FOREIGN KEY (cityName, state) REFERENCES City(cityName, state))");

			System.out.println(ps.executeUpdate());
			con.commit();
		}
		
		System.out.println("Add Table Account? y/n ");
		choice = Integer.parseInt(in.readLine());
		if (choice !=0){
			ps = con.prepareStatement("CREATE TABLE Account "+
			"(accountId int, name char(30), email char(30), postalCode char(10) not null, "+
			"PRIMARY KEY (accountId),"+
			"FOREIGN KEY (postalCode) references PostalCode(postalCode))");

			System.out.println(ps.executeUpdate());
			con.commit();
		}

		System.out.println("Add Table Skill? y/n ");
		choice = Integer.parseInt(in.readLine());
		if (choice !=0){
			ps = con.prepareStatement("CREATE TABLE Skill "+
			"(name char(30) PRIMARY KEY)");

			System.out.println(ps.executeUpdate());
			con.commit();
		}

		System.out.println("Add Table Posting? y/n ");
		choice = Integer.parseInt(in.readLine());
		if (choice !=0){
			ps = con.prepareStatement("CREATE TABLE Posting "+
			"(postingId int, title char(30) not null, active char(10), startDate date, address char(30), postalCode char(10) not null, description char(200), accountId int, "+
			"PRIMARY KEY (postingId),"+
			"FOREIGN KEY (accountId) references Account(accountId),"+
			"FOREIGN KEY (postalCode) references PostalCode(postalCode))");

			System.out.println(ps.executeUpdate());
			con.commit();
		}

		System.out.println("Add Table ExperiencedAt? y/n ");
		choice = Integer.parseInt(in.readLine());
		if (choice !=0){
			ps = con.prepareStatement("CREATE TABLE ExperiencedAt ("+
				"accountId integer, "+
				"name char(30), "+
				"PRIMARY KEY (accountId, name), "+
				"FOREIGN KEY (accountId) REFERENCES Account(accountId) ON DELETE CASCADE, "+
				"FOREIGN KEY (name) REFERENCES Skill (name) ON DELETE CASCADE)");

			System.out.println(ps.executeUpdate());
			con.commit();
		}

		System.out.println("Add Table Endorses? y/n ");
		choice = Integer.parseInt(in.readLine());
		if (choice !=0){
			ps = con.prepareStatement("CREATE TABLE "+
				"Endorses "+
				"(endorserID int, endorsedID int,  "+
				"PRIMARY KEY (endorserID, endorsedID),  "+
				"FOREIGN KEY (endorserID) REFERENCES Account (accountId)  "+
				"ON DELETE CASCADE,  "+
				"FOREIGN KEY (endorsedID) REFERENCES Account (accountId)  "+
				"ON DELETE CASCADE)");

			System.out.println(ps.executeUpdate());
			con.commit();
		}

		System.out.println("Add Table Involves? y/n ");
		choice = Integer.parseInt(in.readLine());
		if (choice !=0){
			ps = con.prepareStatement("CREATE TABLE Involves"+
				"(postingId int, name char(30), yearsExperience int,  "+
				"PRIMARY KEY (postingId, name),  "+
				"FOREIGN KEY (name) REFERENCES Skill(name)  "+
				"ON DELETE CASCADE,  "+
				"FOREIGN KEY (postingId) REFERENCES Posting(postingId)  "+
				"ON DELETE CASCADE)");

			System.out.println(ps.executeUpdate());
			con.commit();
		}

		System.out.println("Add Table application? y/n ");
		choice = Integer.parseInt(in.readLine());
		if (choice !=0){
			ps = con.prepareStatement("create table Application("+
				"applicationID integer primary key, "+
				"coverLetter char(2000), "+
				"resume char(2000), "+
				"applicantId integer NOT NULL, "+
				"postingID integer NOT NULL, "+
				"foreign key (applicantId) references Account (accountId) on delete cascade,"+ 
				"foreign key (postingID) references Posting (postingid) on delete cascade)");

			System.out.println(ps.executeUpdate());
			con.commit();
		}

		System.out.println("Add Table Interview? y/n ");
		choice = Integer.parseInt(in.readLine());
		if (choice !=0){
			ps = con.prepareStatement("CREATE TABLE "+
					"Interview "+
					"(PRIMARY KEY applicantId integer, status char(20), intDate date, address char(20), "+
					"FOREIGN KEY (applicantId) REFERENCES Account (accountId) ON DELETE CASCADE)");
			System.out.println(ps.executeUpdate());
			con.commit();
		}

		System.out.println("Add Table Offer? y/n ");
		choice = Integer.parseInt(in.readLine());
		if (choice !=0){
			ps = con.prepareStatement("CREATE TABLE "+
					"Offer "+
					"( offerId int PRIMARY KEY, status char(20), offerType char(20), hours int, compensation real, " +
					"terminating char(20), startDate date, endDate date, expiryDate date, accountId int, postingId int, "+
					"FOREIGN KEY (accountId) REFERENCES Account (accountId) ON DELETE CASCADE,"+
					"FOREIGN KEY (postingId) REFERENCES Posting (postingId) ON DELETE CASCADE)");

			System.out.println(ps.executeUpdate());
			con.commit();
		}

		ps = con.prepareStatement("CREATE TABLE test (name char(30) PRIMARY KEY)");
		ps.close();
		
	}
	catch (IOException e)
		{
			System.out.println("IOException!");
	
			try
			{
			con.close();
			System.exit(-1);
			}
			catch (SQLException ex)
			{
			 System.out.println("Message: " + ex.getMessage());
			}
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
     * populates tables
     */ 
    private void populateTablesJobSite()
    {
	
	PreparedStatement  ps;
	  
	try
	{
		// disable auto commit mode
		con.setAutoCommit(false);

		String query = "INSERT INTO CountryLanguage (country, primaryLanguage) VALUES (?, ?)";
		ps = con.prepareStatement(query); 
		//
		ps.setString(1, "Canada");
		ps.setString(2, "English");
		ps.addBatch();
		//
		ps.setString(1, "USA");
		ps.setString(2, "English");
		ps.addBatch();
		ps.setString(1, "Mexico");
		ps.setString(2, "Spanish");
		ps.addBatch();
		
		ps.executeBatch();
		con.commit();
		//
		query = "INSERT INTO City (cityName, state, country, population) VALUES (?, ?, ?, ?)";
		ps = con.prepareStatement(query); 
		//
		ps.setString(1, "Vancouver");
		ps.setString(2, "BC");
		ps.setString(3, "Canada");
		ps.setInt(4, 650000);
		ps.addBatch();
		//
		ps.setString(1, "San Francisco");
		ps.setString(2, "CA");
		ps.setString(3, "USA");
		ps.setInt(4, 1200000);
		ps.addBatch();
		ps.setString(1, "Toronto");
		ps.setString(2, "ON");
		ps.setString(3, "Canada");
		ps.setInt(4, 1700000);
		ps.addBatch();
		ps.setString(1, "Mexico City");
		ps.setString(2, "Mexico");
		ps.setString(3, "Mexico");
		ps.setInt(4, 8900000);
		ps.addBatch();

		ps.executeBatch();
		con.commit();
		//

		query = "INSERT INTO Account (accountId, name, email, postalCode) VALUES (?, ?, ?, ?)";
		ps = con.prepareStatement(query);
		ps.setInt(1, 1);
		ps.setString(2, "Will Matous");
		ps.setString(3, "willmatous@gmail.com");
		ps.setString(4, "V9M3Z3");
		ps.addBatch();
		//
		ps.setInt(1, 2);
		ps.setString(2, "Max Hnatiuk");
		ps.setString(3, "maximilian.hnatiuk@gmail.com");
		ps.setString(4, "V6T1Z4");
		ps.addBatch();
		//
		ps.setInt(1, 3);
		ps.setString(2, "Kiera Peters");
		ps.setString(3, "kierapeters14@gmail.com");
		ps.setString(4, "V6T1Z4");
		ps.addBatch();
		//
		ps.setInt(1, 4);
		ps.setString(2, "Anton Zubchenko");
		ps.setString(3, "zubara@inbox.ru");
		ps.setString(4, "V6T1Z4");
		ps.addBatch();
		//
		ps.setInt(1, 5);
		ps.setString(2, "Joe Smith");
		ps.setString(3, "joesmith@gmail.com");
		ps.setString(4, "94103");
		ps.addBatch();
		//
		ps.setInt(1, 6);
		ps.setString(2, "Microsoft Vancouver");
		ps.setString(3, "vancouver@microsoft.com");
		ps.setString(4, "V6B1C1");
		ps.addBatch();
		//
		ps.setInt(1, 7);
		ps.setString(2, "Uber");
		ps.setString(3, "info@uber.com");
		ps.setString(4, "94103");
		ps.addBatch();
		//
		ps.setInt(1, 8);
		ps.setString(2, "Airbnb");
		ps.setString(3, "info@airbnb.com");
		ps.setString(4, "94103");
		ps.addBatch();
		//
		ps.setInt(1, 9);
		ps.setString(2, "SAP Vancouver");
		ps.setString(3, "vancouver@sap.com");
		ps.setString(4, "94103");
		ps.addBatch();
		//
		ps.setInt(1, 9);
		ps.setString(2, "Airbnb");
		ps.setString(3, "info@airbnb.com");
		ps.setString(4, "V6B1C1");
		ps.addBatch();
		//
		ps.setInt(1, 9);
		ps.setString(2, "Zoom Carpets");
		ps.setString(3, "info@zoomcarpets.com");
		ps.setString(4, "V6S1H7");
		ps.addBatch();

		ps.executeBatch();
		con.commit();
		//

		query = "INSERT INTO Skill (name) VALUES (?)";
		ps = con.prepareStatement(query);
		//
		ps.setString(1, "javascript");
		ps.addBatch();
		//
		ps.setString(1, "python");
		ps.addBatch();
		ps.setString(1, "database");
		ps.addBatch();
		ps.setString(1, "frontend");
		ps.addBatch();
		ps.setString(1, "server");
		ps.addBatch();
		ps.setString(1, "bloodwork");
		ps.addBatch();
		ps.setString(1, "icu");
		ps.addBatch();
		ps.setString(1, "inpatient");
		ps.addBatch();
		ps.setString(1, "forklift");
		ps.addBatch();
		ps.setString(1, "mechanic");
		ps.addBatch();
		ps.setString(1, "cad");
		ps.addBatch();
		ps.setString(1, "mockups");
		ps.addBatch();
		
		ps.executeBatch();
		con.commit();
		//

		query = "INSERT INTO ExperiencedAt (accountId, name) VALUES (?, ?)";
		ps = con.prepareStatement(query);
		ps.setInt(1, 1);
		ps.setString(2, "javascript");
		ps.addBatch();
		//
		ps.setString(2, "java");
		ps.addBatch();
		//
		ps.setInt(1, 2);
		ps.setString(2, "javascript");
		ps.addBatch();
		ps.setString(2, "java");
		ps.addBatch();
		ps.setString(2, "server");
		ps.addBatch();
		ps.setString(2, "python");
		ps.addBatch();
		//
		ps.setInt(1, 3);
		ps.setString(2, "bloodwork");
		ps.addBatch();
		ps.setString(2, "icu");
		ps.addBatch();
		//
		ps.setInt(1, 4);
		ps.setString(2, "forklift");
		ps.addBatch();
		ps.setString(2, "mechanic");
		ps.addBatch();
		//
		ps.setInt(1, 5);
		ps.setString(2, "cad");
		ps.addBatch();
		ps.setString(2, "mockups");
		ps.addBatch();
		ps.setString(2, "javascript");
		ps.addBatch();
		ps.setString(2, "database");
		ps.addBatch();
		//

		ps.executeBatch();
		con.commit();
		//

		query = "INSERT INTO Endorses (endorserID, endorsedID) VALUES (?, ?)";
		ps = con.prepareStatement(query);
		ps.setInt(1,1);
		ps.setInt(2,2);
		ps.addBatch();
		ps.setInt(2,3);
		ps.addBatch();
		ps.setInt(2,4);
		ps.addBatch();
		//
		ps.setInt(1,2);
		ps.setInt(2,1);
		ps.addBatch();
		ps.setInt(2,3);
		ps.addBatch();
		//
		ps.setInt(1,3);
		ps.setInt(2,2);
		ps.addBatch();
		ps.setInt(2,1);
		ps.addBatch();
		ps.setInt(2,4);
		ps.addBatch();
		//
		ps.setInt(1,4);
		ps.setInt(2,2);
		ps.addBatch();
		//

		ps.executeBatch();
		con.commit();
		//

		query = "INSERT INTO Involves (postingId, skillName, yearsExperience) VALUES (?, ?, ?)";
		ps = con.prepareStatement(query);
		ps.setInt(1, 51);
		ps.setString(2, "javascript");
		ps.setInt(3, 3);
		ps.addBatch();

		ps.setInt(1, 51);
		ps.setString(2, "java");
		ps.setInt(3, 3);
		ps.addBatch();

		ps.setInt(1, 51);
		ps.setString(2, "database");
		ps.setInt(3, 3);
		ps.addBatch();
		//
		ps.setInt(1, 83);
		ps.setString(2, "forklift");
		ps.setInt(3, 1);
		ps.addBatch();

		ps.setInt(1, 45);
		ps.setString(2, "bloodwork");
		ps.setInt(3, 2);
		ps.addBatch();

		ps.setInt(1, 37);
		ps.setString(2, "icu");
		ps.setInt(3, 4);
		ps.addBatch();

		ps.executeBatch();
		con.commit();


		query = "INSERT INTO Offer (offerId, status, offerType, hours, compensation, terminating, startDate, endDate, " +
				"expiryDate, accountId, postingId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		ps = con.prepareStatement(query);

		//Junior Software Developer
		ps.setInt(1, 0);
		ps.setString(2, "Active");
		ps.setString(3, "Full Time");
		ps.setInt(4, 40);
		ps.setFloat(5, 23.50f);
		ps.setString(6, "false");
		ps.setDate(7, java.sql.Date.valueOf("2019-09-04"));
		ps.setNull(8, 0);
		ps.setDate(9, java.sql.Date.valueOf("2019-08-04"));
		ps.setInt(10, 1);
		ps.setInt(11, 51);
		ps.addBatch();

		//Construction
		ps.setInt(1, 1);
		ps.setFloat(5, 28.00f);
		ps.setDate(9, java.sql.Date.valueOf("2019-08-28"));
		ps.setInt(10, 83);
		ps.addBatch();

		//Architect
		ps.setInt(1, 2);
		ps.setFloat(5, 32.13f);
		ps.setString(6, "true");
		ps.setDate(8, java.sql.Date.valueOf("2021-09-04"));
		ps.setDate(9, java.sql.Date.valueOf("2019-07-22"));
		ps.setInt(10, 96);
		ps.addBatch();

		//Lab Technician
		ps.setInt(1, 3);
		ps.setString(2, "Expired");
		ps.setFloat(5, 18.50f);
		ps.setString(6, "true");
		ps.setDate(8, java.sql.Date.valueOf("2020-01-13"));
		ps.setDate(9, java.sql.Date.valueOf("2019-06-17"));
		ps.setInt(10, 45);
		ps.addBatch();

		//Nurse
		ps.setInt(1, 4);
		ps.setFloat(5, 26.45f);
		ps.setDate(9, java.sql.Date.valueOf("2019-08-15"));
		ps.setInt(10, 37);
		ps.addBatch();

		ps.executeBatch();
		con.commit();


		query = "INSERT INTO Posting (postingId, title, active, startDate, address, postalCode, description, accountId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		ps = con.prepareStatement(query);
		ps.setInt(1, 51);
		ps.setString(2, "Junior Software Developer");
		ps.setString(3, "true");
		ps.setDate(4, java.sql.Date.valueOf("2019-09-04"));
		ps.setString(5, "187 Main Street");
		ps.setString(6, "94103");
		ps.setString(7, "Seeking full time agile software developer with experience in Java.");
		ps.setInt(8, 1);
		ps.addBatch();

		ps.setInt(1, 83);
		ps.setString(2, "Construction");
		ps.setString(3, "false");
		ps.setDate(4, java.sql.Date.valueOf("2019-09-04"));
		ps.setString(5, "1080 Hamilton St.");
		ps.setString(6, "V6S1H7");
		ps.setString(7, "Looking for a construction worker to start in the fall doing physical labor.");
		ps.setInt(8, 2);

		ps.addBatch();

		ps.setInt(1, 96);
		ps.setString(2, "Architect");
		ps.setString(3, "true");
		ps.setDate(4, java.sql.Date.valueOf("2019-09-04"));
		ps.setString(5, "123 Granville St.");
		ps.setString(6, "V6T1Z4");
		ps.setString(7, "Small firm looking to hire experienced architect.");
		ps.setInt(8, 3);
		ps.addBatch();

		ps.setInt(1, 45);
		ps.setString(2, "Lab technician");
		ps.setString(3, "true");
		ps.setDate(4, java.sql.Date.valueOf("2019-09-04"));
		ps.setString(5, "955 Hornby St.");
		ps.setString(6, "V6S1H7");
		ps.setString(7, "Medical lab needs technician to analyze results and draw blood.");
		ps.setInt(8, 4);
		ps.addBatch();

		ps.setInt(1, 37);
		ps.setString(2, "Nurse");
		ps.setString(3, "true");
		ps.setDate(4, java.sql.Date.valueOf("2019-12-08"));
		ps.setString(5, "11 Hamilton St.");
		ps.setString(6, "V6T1Z4");
		ps.setString(7, "Hospital is looking for more OR nurses, experience preferred.");
		ps.setInt(8, 2);
		ps.addBatch();

		ps.executeBatch();
		con.commit();
		

//		"(applicantId int, status char(20), date date, time time, address char(20), "
		query = "INSERT INTO Interview (applicantId, status , intDate, address) VALUES (?, ?, ?, ?, ?)";
		ps = con.prepareStatement(query);

		//Junior Software Developer
		ps.setInt(1, 0);
		ps.setString(2, "Active");
		ps.setDate(3, java.sql.Date.valueOf("2019-07-01"));
		ps.setString(4, "187 Main Street");
		ps.addBatch();

		//Construction
		ps.setInt(1, 1);
		ps.setString(2, "Expired");
		ps.setDate(3, java.sql.Date.valueOf("2019-06-05"));
		ps.setString(4, "1080 Hamilton St.");
		ps.addBatch();

		//Architect
		ps.setInt(1, 2);
		ps.setString(2, "Accepted");
		ps.setDate(3, java.sql.Date.valueOf("2019-07-12"));
		ps.setString(4, "123 Granville St.");
		ps.addBatch();

		//Lab Technician
		ps.setInt(1, 3);
		ps.setString(2, "Accepted");
		ps.setDate(3, java.sql.Date.valueOf("2019-04-17"));
		ps.setString(4, "955 Hornby St.");
		ps.addBatch();

		//Nurse
		ps.setInt(1, 4);
		ps.setString(2, "Declined");
		ps.setDate(3, java.sql.Date.valueOf("2019-06-21"));
		ps.setString(4, "11 Hamilton St.");
		ps.addBatch();

		ps.executeBatch();
		con.commit();

		ps.close();
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

	private  String dispatch(Map<String, String> queryParams, String[] urlPath, String method){
    	try{
        String response= "";
        if(urlPath.length != 0) { // urlPath[0] will be entity type eg account, skill, posting etc
			switch (urlPath[0]) {
				case "account":
					response = handleAccount(queryParams, urlPath, method); // urlPath[1] will likely be null
					break;
				case "recommended":
					response = handleRecommended(queryParams, urlPath, method); // urlPath[1] will likely be null
					break;
				case "endorsement":
					response = handleEndorsement(queryParams, urlPath, method); // urlPath[1] will likely be null
					break;
				case "skill":
					response = handleSkill(queryParams, urlPath, method); // urlPath[1] will likely be null
					break;
				case "posting":
					response = getRecordsAsJSON(posting.handlePosting(queryParams, urlPath, method));
					break;
				case "searchPostings":
					response = getRecordsAsJSON(posting.handleSearchPostings(queryParams, urlPath, method));
					break;
				case "postingSkill":
					response = getRecordsAsJSON(posting.handlePostingSkill(queryParams, urlPath, method));
					break;
				case "allInterviews":
					response = getRecordsAsJSON(intAndOff.handleAllInterviews(queryParams, urlPath, method));
					break;
				case "interview":
					response = getRecordsAsJSON(intAndOff.handleAnInterview(queryParams, urlPath, method));
					break;
				case "allOffers":
					response = getRecordsAsJSON(intAndOff.handleAllOffers(queryParams, urlPath, method));
					break;
				case "offer":
					response = getRecordsAsJSON(intAndOff.handleAnOffer(queryParams, urlPath, method));
					break;
				case "application":
					response = handleApplication(queryParams, urlPath, method);
					break;

				// add entries here for each database entity type
			}
		}
			return response;
        }
        catch(Exception e){
			System.out.println(e);
		}
        return "[]";
    }

	// add a handler method here for each type
	
	private  String handleRecommended(Map<String, String> queryParams, String[] path, String method) throws SQLException {
		if (method == "GET"){
			return getRecommendedPostings(Integer.parseInt(queryParams.get("accountId")));
		}
		return "[]";
    }

    private  String handleAccount(Map<String, String> queryParams, String[] path, String method) throws SQLException {
		if (method == "GET"){
			return getAccount(Integer.parseInt(queryParams.get("accountId")));
		} else if (method == "PUT") {
			return updateAccount(Integer.parseInt(queryParams.get("accountId")),
				queryParams.get("name"), 
				queryParams.get("email"), 
				queryParams.get("postalCode"));
		}
		return "[]";
    }
    private  String handleEndorsement(Map<String, String> queryParams, String[] path, String method) throws SQLException {
		if (method == "GET"){
			return getAccountEndorsements(Integer.parseInt(queryParams.get("accountId")));
		}
		return "[]";
    }
    private  String handleSkill(Map<String, String> queryParams, String[] path, String method) throws SQLException {
		if (method == "GET"){
			return getAccountSkills(Integer.parseInt(queryParams.get("accountId")));
		}
		return "[]";
    }

	// Anton 
private String postApplicationTable(int applicationId, String coverletter, String resume, int applicantID, int posting) throws SQLException {
		PreparedStatement ps = con.prepareStatement("INSERT INTO Application (ApplicationID, CoverLetter , Resume, ApplicantID,PostingID )VALUES (?, ? , ?, ?,? ); ");
		ps.setInt(1, applicationId);
		ps.setString(2, coverletter);
		ps.setString(3, resume);
		ps.setInt(4, applicantID);
		ps.setInt(5, posting);
		return getRecordsAsJSON(ps);
	}
	private  String handleApplication(Map<String, String> queryParams, String[] path, String method) throws SQLException {
		if (method == "POST"){
			return postApplicationTable(Integer.parseInt(queryParams.get("applicationId")), queryParams.get("coverletter"), queryParams.get("resume") ,Integer.parseInt(queryParams.get("accountId")), Integer.parseInt(queryParams.get("PostingID")));
		}
		if (method == "GET"){
			return getApplication(Integer.parseInt(queryParams.get("applicationId")));
		}
		
		return "[]";
    }
    	private String getApplicationTable(int applicationId){
		reparedStatement ps = con.prepareStatement("select (*) from Application where applicationID = ?;");
		ps.setInt(1, accountId);
		return getRecordsAsJSON(ps);
	}
    

    
    private  Map<String, String> getQueryMap(String query)  
{  
    Map<String, String> map = new HashMap<String, String>(); 
    if (query == null){
        return map;
    }
    String[] params = query.split("&");  
    
    if (params == null){
        return map;
    }
    for (String param : params)  
    {  
        String name = param.split("=")[0];  
        String value = param.split("=")[1];  
        map.put(name, value);  
    }  
    return map;  
}
/*
     * connects to Oracle database named ug using user supplied 




    /*
     * connects to Oracle database named ug using user supplied username and password
     */ 
    private boolean connect(String username1, String password1)
    {
      String connectURL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu"; 

      try 
      {
	con = DriverManager.getConnection(connectURL,username,password);

	System.out.println("\nConnected to Oracle!");
	return true;
      }
      catch (SQLException ex)
      {
	System.out.println("Message: " + ex.getMessage());
	return false;
      }
    }


    /*
     * event handler for login window
     */ 
    public void actionPerformed(ActionEvent e) 
    {
	if ( connect(usernameField.getText(), String.valueOf(passwordField.getPassword())) )
	{
	  // if the username and password are valid, 
	  // remove the login window and display a text menu 
	  mainFrame.dispose();
		showMenu();
		    
	}
	else
	{
	  loginAttempts++;
	  
	  if (loginAttempts >= 3)
	  {
	      mainFrame.dispose();
	      System.exit(-1);
	  }
	  else
	  {
	      // clear the password
	      passwordField.setText("");
	  }
	}             
                    
    }


    /*
     * displays simple text interface
     */ 
    private void showMenu() 
    {
		int choice;
		boolean quit;
	
		quit = false;
		
		try 
		{
			// disable auto commit mode
			con.setAutoCommit(false);
	
			while (!quit)
			{
			System.out.print("\n\nPlease choose one of the following: \n");
			System.out.print("1.  list tables\n");
			System.out.print("2.  create job site tables\n");
			System.out.print("3.  populate job site tables\n");
			System.out.print("4.  Run the server\n");
			System.out.print("5.  Delete tables\n");
			
			System.out.print("6.  Quit\n>> ");
	
			choice = Integer.parseInt(in.readLine());
			
			System.out.println(" ");
	
			switch(choice)
			{
			   case 1:  checkTables(); break;
			   case 2:  createTablesJobSite(); break;
			   case 3:  populateTablesJobSite(); break;
			   case 4:  runServer(); break;
			   case 5:  clearAllTables(); break;
			   case 6:  quit = true;
			}
			}
	
			con.close();
			in.close();
			System.out.println("\nGood Bye!\n\n");
			System.exit(0);
		}
		catch (IOException e)
		{
			System.out.println("IOException!");
	
			try
			{
			con.close();
			System.exit(-1);
			}
			catch (SQLException ex)
			{
			 System.out.println("Message: " + ex.getMessage());
			}
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
		}
		}

	public int runServer(){
		while(true){
		try{
			welcomeSocket = new ServerSocket(6789);
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient =
				new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			String urlPath;
			String method = "";
			URL requestURL;
			String[] parsedPath = new String[]{""};
			Map<String, String> urlParams = new HashMap<String, String>();
			while (inFromClient.ready() && (inFromClient.read(clientSentence, 0, 10000) != -1)) {
				urlPath = new String(clientSentence).split(" ", 3)[1];
				method = new String(clientSentence).split(" ", 3)[0];
				requestURL = new URL("http://localhost:6789"+urlPath);
				parsedPath = requestURL.getPath().split("/");
				urlParams = getQueryMap(requestURL.getQuery());
				System.out.println(clientSentence);
			}
			
			String response="";
			try{
				response = dispatch(urlParams, parsedPath, method);
			} catch (Exception e){
				System.out.println(e);
				response = "[]";
			}
			PrintWriter pw = new PrintWriter(outToClient);
					pw.print("HTTP/1.1 200 \r\n"); // Version & status code
					pw.print("Content-Type: application/json\r\n"); // The type of data
					pw.print("Access-Control-Allow-Origin: *\r\n");
					pw.print("X-Content-Type-Options: nosniff\r\n");
					pw.print("Access-Control-Allow-Headers: *\r\n");
					pw.print("Allow: OPTIONS, GET, HEAD, POST\r\n");
					pw.print("Connection: close\r\n"); // Will close stream
					pw.print("\r\n"); // End of headers
					pw.println(response);
					pw.flush();
					pw.close();
			// fetch('http://localhost:6789/path/?params').then((res) => res.json()).then(data => console.log(data)).catch(err => console.error(err));
			}catch(Exception e){
				System.out.println(e);
				return 1;
			}
		}
	}

    public static void main(String args[])
    {
		branchtwo b = new branchtwo();
    }
}

