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
