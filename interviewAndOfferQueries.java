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

    PreparedStatement handleAnOffer(Map<String, String> queryParams, String[] path, String method) throws SQLException {
        if (method.equals("GET")) {
            return getAnOffer(Integer.parseInt(queryParams.get("offerId")));
        } else if (method.equals("PUT")) {
            return updateAnOffer(Integer.parseInt(queryParams.get("offerId")),
                    queryParams.get("status"));
        }
        return null;
    }

    private PreparedStatement getAnOffer(int offerId) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "SELECT offerId, status, offerType, hours, compensation, terminating, startDate, endDate, expiryDate " +
                        "FROM Offer " +
                        "WHERE offerId = ?");
        ps.setInt(1, offerId);
        return ps;
    }

    private PreparedStatement updateAnOffer(int offerId, String status) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "UPDATE TABLE (Offer) SET status = ? WHERE offerId = ?");
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
                "SELECT I.status, I.intDate, I.address, I.time, I.interviewId, Z.name, Z.accountId, P.postId " +
                        "FROM Interview I, Account Z, Posting P, Application A " +
                        "WHERE A.postingId = P.postingId AND I.applicantId = A.applicantId AND A.applicantId = Z.accountId AND A.applicantId = ?");
        ps.setInt(1, accountId);
        return ps;
    }

    PreparedStatement handleAnInterview(Map<String, String> queryParams, String[] path, String method) throws SQLException {
        if (method.equals("GET")) {
            return getAnInterview(Integer.parseInt(queryParams.get("applicantId")));
        } else if (method.equals("POST")) {
            return updateAnInterview(Integer.parseInt(queryParams.get("applicantId")),
                    queryParams.get("status"));
        }
        return null;
    }

    private PreparedStatement getAnInterview(int applicationId) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "SELECT I.status, I.intDate, I.address, I.time, I.interviewId, A.name, A.accountId, P.position, P.title, P.postId " +
                        "FROM Interview I, Account A, Posting P " +
                        "WHERE I.applicantId = ?");
        ps.setInt(1, applicationId);
        return ps;
    }

    private PreparedStatement updateAnInterview(int applicantId, String status) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "UPDATE TABLE (Interview) SET status = ? WHERE applicantId = ?");
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

    private PreparedStatement getPositionInfo(int postId) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "SELECT postId, title, description " +
                        "FROM Posting " +
                        "WHERE postId = ?");
        ps.setInt(1, postId);
        return ps;
    }
}