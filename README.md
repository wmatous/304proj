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
``` http://localhost:6789/account/?accountId=123&name=will&email=will@example.com&postalCode=V9M3Z3 method = 'PUT'
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

# jsongenerator
json is included by default in enterprise edition java, not our standard edition. we have to add the classes similar to how we added database support. I have all my zip and jar files in the same directory as my java source files, and use the following  bashsrc lines
```
export ORACLE_HOME=~/304courseproj/classes12.zip 
export CLASSPATH=.:./hb15.zip:$ORACLE_HOME:$CLASSPATH
export CLASSPATH=.:/usr/lib/oracle/12.2/client64/lib/ojdbc8.jar:$CLASSPATH
export CLASSPATH=.:./javax.json-api-1.1.jar:$CLASSPATH
```


# steps

1. put all the repo files in a folder called /javasrc directly inside your home folder (~/javasrc)
2. add the export lines to your bashsrc file
  a. if you dont have one already:
  ```
  vi ~/.bashsrc
  ```
  press i to edit, copy and paste the lines, press escape, type :wq and press enter, then:
  ```
  source ~/.bashsrc
  ```
3. cd into javasrc
4. enter your database u/p in helpers/DBVars.java
5. javac branchtwo.java
6. java branchtwo

if you entered your u/p in helpers, you dont need to enter them in the popup

# the server is alive
but at some cost

make sure you have the javax.json-1.1.jar file

add the following line to your ~/.bashrc and dont forget to ``` source ~/.bashrc ```
```
CLASSPATH=.:./javax.json-1.1.jar:$CLASSPATH
```
then compile and run branchtwo

add tables to your database, when prompted for y/n just choose any number except 0 for yes

you might get an error that indicates you dont have enough space - in this case choose option 5 and enter 0 for all of the prompts - 
itll clean up tutorial tables afterwards
then choose option 3 to populate the database 

# known issues
some of the insert statements are going to fail. mainly because of constraint violations due to FK referencing a nonexistent record

i added print statements to debug this. if a failure occurs, comment out everything above the last print statement, fix whatever 
the bug is, recompile and try again

also it is likely that your requests will all fail because i used str1 == str2 instead of str1.equals(str2) in a bunch of places.
 look for that first as it is almost certainly the cause.

lastly, because we used char() instead of varchar() in our tables, all those fields come back a fixed size with a bunch of whitespace.
we can trim the whitespace in js, or go back and fix the tables
