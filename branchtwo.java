import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;
import oracle.jdbc.driver.OracleDriver;
import javax.json.stream.*;


// for the login window
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
	
			ps = con.prepareStatement("DROP TABLE ?");
			ps.setString(1,tableName);
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
		// disable auto commit mode
		con.setAutoCommit(false);

		ps = con.prepareStatement("select table_name from user_tables");
		ResultSet rs = ps.executeQuery();
		String s;
		while(rs.next())
		{
			s = rs.getString("table_name");
			System.out.println("Drop Table? y/n " + s);
			String choice = in.readLine();
			if (choice == "y"){
				dropTable(s);
			}
			
		}
		
	  con.commit();

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
     * retrieves endorsement count for account
     */ 
    private String getAccountEndorsements(int accountId)
    {
		PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as count FROM Endorses WHERE endorsedId = ?");
		ps.setInt(1, accountId);
		return getRecordsAsJSON(ps);
	  
	}

	/*
     * retrieves skills for account
     */ 
    private String getAccountSkills(int accountId)
    {
		PreparedStatement ps = con.prepareStatement("SELECT name FROM ExperiencedAt WHERE accountId = ?");
		ps.setInt(1, accountId);
		return getRecordsAsJSON(ps);
	  
	}

	/*
     * retrieves specified account
     */ 
    private String getAccount(int accountId)
    {
		PreparedStatement ps = con.prepareStatement("SELECT * FROM Account WHERE accountId = ?");
		ps.setInt(1, accountId);
		return getRecordsAsJSON(ps);
	  
	}

	/*
     * updates account table for specified account
     */ 
    private String updateAccount(int accountId, String name, String email, String postalCode)
    {
		// create postalcode?
		PreparedStatement ps = con.prepareStatement("UPDATE TABLE Account SET name = ?, email = ?, postalCode = ? WHERE accountId = ?");
		ps.setString(1, name);
		ps.setString(2, email);
		ps.setString(3, postalCode);
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
    private String getRecordsAsJSON(PreparedStatement ps)
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
		String choice = in.readLine();
		if (choice == "y"){
			ps = con.prepareStatement("CREATE TABLE "+
			"CountryLanguage "+
			"(country char(30) PRIMARY KEY,"+
			"primaryLanguage char(30))");
			System.out.println(ps.executeUpdate());
			con.commit();
		}

		System.out.println("Add Table City? y/n ");
		choice = in.readLine();
		if (choice == "y"){
			ps = con.prepareStatement("CREATE TABLE City "+
			"(cityName char(30), state char(30), country char(30), population int,"+
			"PRIMARY KEY (cityName, state),"+
			"FOREIGN KEY (country) REFERENCES CountryLanguage(country))");
			System.out.println(ps.executeUpdate());
			con.commit();
		}
		System.out.println("Add Table PostalCode? y/n ");
		choice = in.readLine();
		if (choice == "y"){
			ps = con.prepareStatement("CREATE TABLE "+
			"PostalCode "+
			"(postalCode char(10), cityName char(30) not null, state char(30) not null, "+
			"PRIMARY KEY (postalCode)," +
			"FOREIGN KEY (cityName, state) REFERENCES City(cityName, state))");

			System.out.println(ps.executeUpdate());
			con.commit();
		}
		
		System.out.println("Add Table Account? y/n ");
		choice = in.readLine();
		if (choice == "y"){
			ps = con.prepareStatement("CREATE TABLE Account "+
			"(accountId int, name char(30), email char(30), postalCode char(10) not null, "+
			"PRIMARY KEY (accountId),"+
			"FOREIGN KEY (postalCode) references PostalCode(postalCode))");

			System.out.println(ps.executeUpdate());
			con.commit();
		}

		System.out.println("Add Table Skill? y/n ");
		choice = in.readLine();
		if (choice == "y"){
			ps = con.prepareStatement("CREATE TABLE Skill "+
			"(name char(30) PRIMARY KEY)");

			System.out.println(ps.executeUpdate());
			con.commit();
		}

		System.out.println("Add Table ExperiencedAt? y/n ");
		choice = in.readLine();
		if (choice == "y"){
			ps = con.prepareStatement("CREATE TABLE ExperiencedAt ("+
				"accountID integer, "+
				"name char(30), "+
				"PRIMARY KEY (accountID, name), "+
				"FOREIGN KEY (accountID) REFERENCES Account(accountID) ON DELETE CASCADE, "+
				"FOREIGN KEY (name) REFERENCES Skill (name) ON DELETE CASCADE)");

			System.out.println(ps.executeUpdate());
			con.commit();
		}

		System.out.println("Add Table ExperiencedAt? y/n ");
		choice = in.readLine();
		if (choice == "y"){
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

		


		ps = con.prepareStatement("");
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
        String response= "";
        if(urlPath.length != 0){ // urlPath[0] will be entity type eg account, skill, posting etc
            switch(urlPath[0]){
                case "account":
                    response = handleAccount(queryParams, urlPath, method); // urlPath[1] will likely be null
                break;
                case "endorsement":
					response = handleEndorsement(queryParams, urlPath, method); // urlPath[1] will likely be null
                break;
                case "skill":
					response = handleSkill(queryParams, urlPath, method); // urlPath[1] will likely be null
                break;

                // add entries here for each database entity type
            }
        }
        return response;
    }

    // add a handler method here for each type

    private  String handleAccount(Map<String, String> queryParams, String[] path, String method){
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
    private  String handleEndorsement(Map<String, String> queryParams, String[] path, String method){
		if (method == "GET"){
			return getAccountEndorsements(Integer.parseInt(queryParams.get("accountId")));
		}
		return "[]";
    }
    private  String handleSkill(Map<String, String> queryParams, String[] path, String method){
		if (method == "GET"){
			return getAccountSkills(Integer.parseInt(queryParams.get("accountId")));
		}
		return "[]";
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
    private boolean connect(String username, String password)
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
			System.out.print("5.  Quit\n>> ");
	
			choice = Integer.parseInt(in.readLine());
			
			System.out.println(" ");
	
			switch(choice)
			{
			   case 1:  checkTables(); break;
			   case 2:  createTablesJobSite(); break;
			   case 3:  populateTablesJobSite(); break;
			   case 4:  runServer(); break;
			   case 5:  quit = true;
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
	try{
		welcomeSocket = new ServerSocket(6789);
		Socket connectionSocket = welcomeSocket.accept();
		BufferedReader inFromClient =
			new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		String urlPath;
		String method;
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


    /*
     * inserts a branch
     
    private void insertBranch()
    {
	int                bid;
	String             bname;
	String             baddr;
	String             bcity;
	int                bphone;
	PreparedStatement  ps;
	  
	try
	{
	  ps = con.prepareStatement("INSERT INTO branch VALUES (?,?,?,?,?)");
	
	  System.out.print("\nBranch ID: ");
	  bid = Integer.parseInt(in.readLine());
	  ps.setInt(1, bid);

	  System.out.print("\nBranch Name: ");
	  bname = in.readLine();
	  ps.setString(2, bname);

	  System.out.print("\nBranch Address: ");
	  baddr = in.readLine();
	  
	  if (baddr.length() == 0)
          {
	      ps.setString(3, null);
	  }
	  else
	  {
	      ps.setString(3, baddr);
	  }
	 
	  System.out.print("\nBranch City: ");
	  bcity = in.readLine();
	  ps.setString(4, bcity);

	  System.out.print("\nBranch Phone: ");
	  String phoneTemp = in.readLine();
	  if (phoneTemp.length() == 0)
	  {
	      ps.setNull(5, java.sql.Types.INTEGER);
	  }
	  else
	  {
	      bphone = Integer.parseInt(phoneTemp);
	      ps.setInt(5, bphone);
	  }

	  ps.executeUpdate();

	  // commit work 
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
		// undo the insert
		con.rollback();	
	    }
	    catch (SQLException ex2)
	    {
		System.out.println("Message: " + ex2.getMessage());
		System.exit(-1);
	    }
	}
    } */


    /*
     * deletes a branch
      
    private void deleteBranch()
    {
	int                bid;
	PreparedStatement  ps;
	  
	try
	{
	  ps = con.prepareStatement("DELETE FROM branch WHERE branch_id = ?");
	
	  System.out.print("\nBranch ID: ");
	  bid = Integer.parseInt(in.readLine());
	  ps.setInt(1, bid);

	  int rowCount = ps.executeUpdate();

	  if (rowCount == 0)
	  {
	      System.out.println("\nBranch " + bid + " does not exist!");
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
	*/
    

    /*
     * updates the name of a branch
     
    private void updateBranch()
    {
	int                bid;
	String             bname;
	PreparedStatement  ps;
	  
	try
	{
	  ps = con.prepareStatement("UPDATE branch SET branch_name = ? WHERE branch_id = ?");
	
	  System.out.print("\nBranch ID: ");
	  bid = Integer.parseInt(in.readLine());
	  ps.setInt(2, bid);

	  System.out.print("\nBranch Name: ");
	  bname = in.readLine();
	  ps.setString(1, bname);

	  int rowCount = ps.executeUpdate();
	  if (rowCount == 0)
	  {
	      System.out.println("\nBranch " + bid + " does not exist!");
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
	*/

    
    /*
     * display information about branches
     
    private void showBranch()
    {
	String     bid;
	String     bname;
	String     baddr;
	String     bcity;
	String     bphone;
	Statement  stmt;
	ResultSet  rs;
	   
	try
	{
	  stmt = con.prepareStatement();

	  rs = stmt.executeQuery("SELECT * FROM branch");

	  // get info on ResultSet
	  ResultSetMetaData rsmd = rs.getMetaData();

	  // get number of columns
	  int numCols = rsmd.getColumnCount();

	  System.out.println(" ");
	  
	  // display column names;
	  for (int i = 0; i < numCols; i++)
	  {
	      // get column name and print it

	      System.out.printf("%-15s", rsmd.getColumnName(i+1));    
	  }

	  System.out.println(" ");

	  while(rs.next())
	  {
	      // for display purposes get everything from Oracle 
	      // as a string

	      // simplified output formatting; truncation may occur

	      bid = rs.getString("branch_id");
	      System.out.printf("%-10.10s", bid);

	      bname = rs.getString("branch_name");
	      System.out.printf("%-20.20s", bname);

	      baddr = rs.getString("branch_addr");
	      if (rs.wasNull())
	      {
	    	  System.out.printf("%-20.20s", " ");
              }
	      else
	      {
	    	  System.out.printf("%-20.20s", baddr);
	      }

	      bcity = rs.getString("branch_city");
	      System.out.printf("%-15.15s", bcity);

	      bphone = rs.getString("branch_phone");
	      if (rs.wasNull())
	      {
	    	  System.out.printf("%-15.15s\n", " ");
              }
	      else
	      {
	    	  System.out.printf("%-15.15s\n", bphone);
	      }      
	  }
 
	  // close the statement; 
	  // the ResultSet will also be closed
	  stmt.close();
	}
	catch (SQLException ex)
	{
	    System.out.println("Message: " + ex.getMessage());
	}	
	}
	*/
    
 
    public static void main(String args[])
    {
		branchtwo b = new branchtwo();
    }
}

