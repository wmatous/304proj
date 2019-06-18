import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;
import oracle.jdbc.driver.OracleDriver;
import javax.json.stream.*;

class interviewAndOfferQueries {
	private Connection con;

	interviewAndOfferQueries(Connection c){
	this.con = c;
	}

	String handleAllOffers(Map<String, String> queryParams, String[] path, String method) throws SQLException{
		if (method == "GET") {
			return getAllOffers(queryParams.get("accountId"));
		}
		return "[]";
	}

	protected String getAllOffers(int accountId) throws SQLException{
		PreparedStatement ps = con.prepareStatement("SELECT O.status, O.type, O.offerId, C.name, C.accountId, P.position, P.postId " +
				"FROM offer O, company C, posting P " +
				"WHERE O.accountId = ?");
		ps.setInt(1, accountId);
		return getRecordsAsJSON(ps);
	}

	String handleOffer(Map<String, String> queryParams, String[] path, String method) throws SQLException{
		if (method == "GET") {
			return getOffer(queryParams.get("accountId"));
		}
		return "[]";
	}

	private String getOffer(int offerId) throws SQLException{
		PreparedStatement ps = con.prepareStatement(
				"SELECT offerId, status, type, hours, compensation, terminating, startDate, endDate, expiryDate "+
						"FROM offer "+
						"WHERE offerId = ?");
		ps.setInt(1, offerId);
		return getRecordsAsJSON(ps);
	}

	String handleAllInterviews(Map<String, String> queryParams, String[] path, String method) throws SQLException{
		if (method == "GET") {
			return getAllInterviews(queryParams.get("accountId"));
		}
		return "[]";
	}

	private String getAllInterviews(int accountId) throws SQLException{
		PreparedStatement ps = con.prepareStatement(
				"SELECT I.status, I.date, I.address, I.time, I.interviewId, C.name, C.accountId, P.position, P.postId " +
						"FROM interview I, company C, posting P " +
						"WHERE I.accountId = ?");
		ps.setInt(1, accountId);
		return getRecordsAsJSON(ps);
	}

	String handleInterview(Map<String, String> queryParams, String[] path, String method) throws SQLException{
		if (method == "GET") {
			return getInterview(queryParams.get("accountId"));
		}
		return "[]";
	}

	private String getInterview(int applicantId) throws SQLException{
		PreparedStatement ps = con.prepareStatement(
				"SELECT status, date, time, address " +
						"FROM interview " +
						"WHERE applicantId = ?");
		ps.setInt(1, applicantId);
		return getRecordsAsJSON(ps);
	}

	private String getCompanyInfo(int companyId) throws SQLException{
		PreparedStatement ps = con.prepareStatement(
		"SELECT name, size, industry, address, email"+
		"FROM company "+
		"WHERE accountId = ?");
		ps.setInt(1,companyId);
		return getRecordsAsJSON(ps);
		}

	private String getPositionInfo(int postId) throws SQLException{
		PreparedStatement ps = con.prepareStatement(
		"SELECT postId, position, description" +
		"FROM posting " +
		"WHERE postId = ?");
		ps.setInt(1, postId);
		return getRecordsAsJSON(ps);
		}
}