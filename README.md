# 304proj
database project

oracle is set up in such a way that we can only access it while running jdbc on the remote server

which is fine except that im not entirely sure how to make our java tcp rest server available to the internet
without a ton of mucking around

# the solution

serve the js and html from the remote public_html folder, and make the rest api request to localhost on the
local machine that we are demoing on

ssh into the remote server and start the java server there

ssh tunnel your local machine to the remote machine
```
ssh -L 6789:localhost:6789 remote.students.cs.ubc.ca
```
tada! requests to localhost on the demo machine will be sent to the remote machine and the database will work (probably)

# helper method for returning results as JSON 

Use the helper method  
``` 
private String getRecordsAsJSON(PreparedStatement ps) 
```
to retrieve the results of a query as a String representing a JSON list . PreparedStatement can be created like this:
```
ps = con.prepareStatement("select * from Account");
```
or to insert the value of variables:
```
ps = con.prepareStatement("select * from Account WHERE accountId = ? AND age > ?");
ps.setString(1, acctID);
ps.setInt(2, minAge);
```
using 1-based index

# dispatch

all requests will come through dispatch with a path like /account, a set of query params, and an HTTP method verb like GET or PUT or POST. the path should determine what handler takes the request to build a response/update the db, the params are your variables, and the method describes what the action does for example:
``` 
http://localhost:6789/account/?accountId=123&name=will&email=will@example.com&postalCode=V9M3Z3 method = 'PUT'
```
this will update an existing account record with the supplied values. HTTP verbs describe what sort of action you are doing:
GET = retrieval - no changes should be made to anything on the backend
POST = creation - make a new record
PUT = update - update an existing record.
POST and PUT can be mixed for example if you dont care if there is already an existing account with accountId=123, update if it exists or create if not
but dont mix GET with anything else


# executeUpdateStatement helper
```
public int executeUpdateStatement(PreparedStatement ps)
```
deals with all the exception handling etc - returns the number of db rows created/updated - for insert, alter, create, drop statements
