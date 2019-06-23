import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

class interviewAndOfferQueries {
    private Connection con;

    interviewAndOfferQueries(Connection c) {
        this.con = c;
    }

    PreparedStatement handleAllOffers(Map<String, String> queryParams, String[] path, String method) throws SQLException {
        if (method.equals("GET")) {
            return getAllOffers(Integer.parseInt(queryParams.get("accountId")));
        }
        return null;
    }

    private PreparedStatement getAllOffers(int accountId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT O.status, O.startDate, O.expiryDate, O.offerId, B.name AS company, A.accountId, P.postingId, P.title " +
                "FROM Offer O, Account A, Account B, Posting P " +
                "WHERE  O.postingId = P.postingId AND B.accountId = P.accountId AND O.accountId = A.accountId AND O.accountId = ?");
        ps.setInt(1, accountId);
        return ps;
    }


    public PreparedStatement getAnOffer(int offerId) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "SELECT O.offerId, O.status, O.offerType, O.hours, O.compensation, O.terminating, O.startDate, O.endDate, O.expiryDate, P.title, P.postingId, P.description, B.name AS companyname, C.csize AS companysize, C.industry AS companyindustry, C.address, B.email AS companyemail " +
                        "FROM Offer O,  Account B, Company C, Posting P " +
                        "WHERE P.postingId = O.postingId AND B.accountId = P.accountId AND B.accountId = C.accountId AND O.offerId = ?");
        ps.setInt(1, offerId);
        return ps;
    }

    public PreparedStatement updateAnOffer(int offerId, String status) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "UPDATE Offer SET status = ? WHERE offerId = ?");
        ps.setString(1, status);
        ps.setInt(2, offerId);
        return ps;
    }

    PreparedStatement handleAllInterviews(Map<String, String> queryParams, String[] path, String method) throws SQLException {
        if (method.equals("GET")) {
            PreparedStatement ps = getAllInterviews(Integer.parseInt(queryParams.get("applicantId")));
            return ps;
        }
        return null;
    }

    private PreparedStatement getAllInterviews(int applicantId) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "SELECT I.status, I.intDate, I.address, I.applicationId AS interviewId, Z.name, Z.accountId, B.name AS company, P.postingId, P.title " +
                        "FROM Interview I, Account Z, Account B, Posting P, Application A " +
                        "WHERE A.postingId = P.postingId AND B.accountId = P.accountId AND I.applicationId = A.applicationId AND A.applicantId = Z.accountId AND Z.accountId = ?");
        ps.setInt(1, applicantId);
        return ps;
    }

    public PreparedStatement getAnInterview(int applicationId) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "SELECT I.status, I.intDate, I.address, I.applicationId AS interviewId, "+
                " A.name as companyname, A.email as companyemail, P.title, P.description, P.postingId , C.Industry as companyindustry, C.cSize as companysize, app.applicantId as applicantID " +
                        " FROM Interview I, Account A, Posting P, Company C, Application APP " +
                        " WHERE app.applicationId =  I.applicationId AND app.postingId = p.postingId and "+
                        " p.accountId = A.accountId and a.accountId = c.accountId AND I.applicationId = ? ");
        ps.setInt(1, applicationId);
        return ps;
    }

    public PreparedStatement updateAnInterview(int applicationId, String status) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "UPDATE Interview SET status = ? WHERE applicationId = ?");
        ps.setString(1, status);
        ps.setInt(2, applicationId);
        return ps;
    }

//removed handler for singular interview because all interview data is fetched during getAllInterview
//might not need these methods either

    private PreparedStatement getCompanyInfo(int companyId) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "SELECT A.name, C.csize, C.industry, C.address, A.email " +
                        "FROM Company C, Account A " +
                        "WHERE C.accountId = ? AND A.accountId=C.accountId");
        ps.setInt(1, companyId);
        return ps;
    }

    private PreparedStatement getPositionInfo(int postingId) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "SELECT postingId, title, description " +
                        "FROM Posting " +
                        "WHERE postingId = ?");
        ps.setInt(1, postingId);
        return ps;
    }
}