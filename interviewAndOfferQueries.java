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
                "SELECT offerId, status, offerType, hours, compensation, terminating, startDate, endDate, expiryDate " +
                        "FROM Offer " +
                        "WHERE offerId = ?");
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
            return getAllInterviews(Integer.parseInt(queryParams.get("accountId")));
        }
        return null;
    }

    private PreparedStatement getAllInterviews(int accountId) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "SELECT I.status, I.intDate, I.address, I.applicationId AS interviewId, Z.name, Z.accountId, B.name AS company, P.postingId, P.title " +
                        "FROM Interview I, Account Z, Account B, Posting P, Application A " +
                        "WHERE A.postingId = P.postingId AND B.accountId = P.accountId AND I.applicationId = A.applicationId AND A.applicantId = Z.accountId AND Z.accountId = ?");
        ps.setInt(1, accountId);
        return ps;
    }

    PreparedStatement getAnInterview(int applicationId) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "SELECT I.status, I.intDate, I.address, I.time, I.applicationId AS interviewId, A.name, A.accountId, P.title, P.postingId " +
                        "FROM Interview I, Account A, Posting P " +
                        "WHERE I.applicationId = ?");
        ps.setInt(1, applicationId);
        return ps;
    }

    PreparedStatement updateAnInterview(int applicantId, String status) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "UPDATE Interview SET status = ? WHERE applicantId = ?");
        ps.setString(1, status);
        ps.setInt(2, applicantId);
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