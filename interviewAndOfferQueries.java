import java.util.*;
import java.sql.*;

class interviewAndOfferQueries {
	private Connection con;

	interviewAndOfferQueries(Connection c){
	this.con = c;
	}

	PreparedStatement handleAllOffers(Map<String, String> queryParams, String[] path, String method) throws SQLException {
		if (method == "GET") {
			return getAllOffers(Integer.parseInt(queryParams.get("accountId")));
		}
		return null;
	}

	protected PreparedStatement getAllOffers(int accountId) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT O.status, O.startDate, O.expiryDate, O.offerId, C.name, C.accountId, P.position, P.postId " +
				"FROM offer O, company C, posting P " +
				"WHERE  O.postingId = P.postingID AND P.accountId = C.accountID AND  O.accountId = ?");
		ps.setInt(1, accountId);
		return ps;
	}

	PreparedStatement handleAnOffer(Map<String, String> queryParams, String[] path, String method) throws SQLException {
		if (method == "GET") {
			return getAnOffer(Integer.parseInt(queryParams.get("offerId")));
		}
		return null;
	}

	private PreparedStatement getAnOffer(int offerId) throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"SELECT offerId, status, type, hours, compensation, terminating, startDate, endDate, expiryDate "+
						"FROM offer "+
						"WHERE offerId = ?");
		ps.setInt(1, offerId);
		return ps;
	}

	PreparedStatement handleAllInterviews(Map<String, String> queryParams, String[] path, String method) throws SQLException {
		if (method == "GET") {
			return getAllInterviews(Integer.parseInt(queryParams.get("accountId")));
		}
		return null;
	}

	private PreparedStatement getAllInterviews(int applicantId) throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"SELECT I.status, I.date, I.address, I.time, I.interviewId, C.name, C.accountId, P.position, P.postId " +
						"FROM interview I, company C, posting P, application A" +
						"WHERE A.postingId = P.postingId AND I.applicantId = A.applicantId AND P.accountId = C.accountId AND A.applicantId = ?");
		ps.setInt(1, applicantId);
		return ps;
	}

	PreparedStatement handleAnInterview(Map<String, String> queryParams, String[] path, String method) throws SQLException {
		if (method == "GET") {
			return getAnInterview(Integer.parseInt(queryParams.get("applicationId")));
		}
		return null;
	}

	private PreparedStatement getAnInterview(int applicationId) throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"SELECT I.status, I.date, I.address, I.time, I.interviewId, C.name, C.accountId, P.position, P.postId " +
						"FROM interview I, company C, posting P " +
						"WHERE I.applicationId = ?");
		ps.setInt(1, applicationId);
		return ps;
	}

//removed handler for singular interview because all interview data is fetched during getAllInterview
//might not need these methods either

	private PreparedStatement getCompanyInfo(int companyId) throws SQLException {
		PreparedStatement ps = con.prepareStatement(
		"SELECT name, size, industry, address, email"+
		"FROM company "+
		"WHERE accountId = ?");
		ps.setInt(1,companyId);
		return ps;
		}

	private PreparedStatement getPositionInfo(int postId) throws SQLException {
		PreparedStatement ps = con.prepareStatement(
		"SELECT postId, position, description" +
		"FROM posting " +
		"WHERE postId = ?");
		ps.setInt(1, postId);
		return ps;
		}
}