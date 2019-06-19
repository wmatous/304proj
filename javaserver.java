import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static helpers.DBVars.password;
import static helpers.DBVars.username;

public class javaserver {

    private static Connection con;

    public javaserver() {

    }

    public static void main(String argv[]) throws Exception {
        char[] clientSentence = new char[10000];
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(6789);
        try {
            // Load the Oracle JDBC driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            // may be oracle.jdbc.driver.OracleDriver as of Oracle 11g
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
            System.exit(-1);
        }

        System.out.println("connecting");
        System.out.println(connect());

        while (true) {
            try {
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                String urlPath;
                URL requestURL;
                String[] parsedPath = new String[]{""};
                Map<String, String> urlParams = new HashMap<String, String>();
                while (inFromClient.ready() && (inFromClient.read(clientSentence, 0, 10000) != -1)) {
                    urlPath = new String(clientSentence).split(" ", 3)[1];

                    requestURL = new URL("http://localhost:6789" + urlPath);
                    parsedPath = requestURL.getPath().split("/");

                    urlParams = getQueryMap(requestURL.getQuery());
                    System.out.println(urlParams);

                }

                String response = dispatch(urlParams, parsedPath);
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
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private static String dispatch(Map<String, String> queryParams, String[] urlPath) {
        String response = "";
        if (urlPath.length != 0) { // urlPath[0] will be entity type eg account, skill, posting etc
            switch (urlPath[0]) {
                case "account":
                    handleAccount(queryParams, urlPath); // urlPath[1] will be null or action like create, update, delete
                    break;

                // add entries here for each database entity type
            }
        }
        return response;
    }

    // add a handler method here for each type

    private static String handleAccount(Map<String, String> queryParams, String[] path) {

        //if (action == "update"){

        //}
        return "";
    }


    private static Map<String, String> getQueryMap(String query) {
        Map<String, String> map = new HashMap<String, String>();
        if (query == null) {
            return map;
        }
        String[] params = query.split("&");

        if (params == null) {
            return map;
        }
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    /*
     * connects to Oracle database named ug using user supplied username and password
     */
    private static boolean connect() {
        String connectURL = "jdbc:oracle:thin:@localhost:1522:stu";

        try {
            System.out.println("\nConnecting to Oracle!");
            System.out.println(username);
            System.out.println(password);

            con = DriverManager.getConnection(connectURL, username, password);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
            return false;
        }
    }


}
