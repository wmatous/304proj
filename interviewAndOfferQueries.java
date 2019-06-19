import java.util.*;
import java.sql.*;

class interviewAndOfferQueries {
	private Connection con;

	interviewAndOfferQueries(Connection c){
	this.con = c;
	}

	PreparedStatement handleAllOffers(Map<String, String> queryParams, String[] path, String method){
		if (method == "GET") {
			return getAllOffers(Integer.parseInt(queryParams.get("accountId")));
		}
		return null;
	}

	protected PreparedStatement getAllOffers(int accountId){
		PreparedStatement ps = con.prepareStatement("SELECT O.status, O.startDate, O.expiryDate, O.offerId, C.name, C.accountId, P.position, P.postId " +
				"FROM offer O, company C, posting P " +
				"WHERE O.accountId = ?");
		ps.setInt(1, accountId);
		return ps;
	}

	PreparedStatement handleOffer(Map<String, String> queryParams, String[] path, String method){
		if (method == "GET") {
			return getOffer(Integer.parseInt(queryParams.get("offerId")));
		}
		return null;
	}

	private PreparedStatement getOffer(int offerId){
		PreparedStatement ps = con.prepareStatement(
				"SELECT offerId, status, type, hours, compensation, terminating, startDate, endDate, expiryDate "+
						"FROM offer "+
						"WHERE offerId = ?");
		ps.setInt(1, offerId);
		return ps;
	}

	PreparedStatement handleAllInterviews(Map<String, String> queryParams, String[] path, String method){
		if (method == "GET") {
			return getAllInterviews(Integer.parseInt(queryParams.get("accountId")));
		}
		return null;
	}

	private PreparedStatement getAllInterviews(int accountId){
		PreparedStatement ps = con.prepareStatement(
				"SELECT I.status, I.date, I.address, I.time, I.interviewId, C.name, C.accountId, P.position, P.postId " +
						"FROM interview I, company C, posting P " +
						"WHERE I.accountId = ?");
		ps.setInt(1, accountId);
		return ps;
	}

//removed handler for singular interview because all interview data is fetched during getAllInterview
//might not need these methods either

	private PreparedStatement getCompanyInfo(int companyId){
		PreparedStatement ps = con.prepareStatement(
		"SELECT name, size, industry, address, email"+
		"FROM company "+
		"WHERE accountId = ?");
		ps.setInt(1,companyId);
		return ps;
		}

	private PreparedStatement getPositionInfo(int postId){
		PreparedStatement ps = con.prepareStatement(
		"SELECT postId, position, description" +
		"FROM posting " +
		"WHERE postId = ?");
		ps.setInt(1, postId);
		return ps;
		}
}