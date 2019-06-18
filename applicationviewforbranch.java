		private  String dispatch(Map<String, String> queryParams, String[] urlPath, String method){
        String response= "";
        if(urlPath.length != 0){ // urlPath[0] will be entity type eg account, skill, posting etc
            switch(urlPath[0]){
                case "account":
                    response = handleAccount(queryParams, urlPath, method); // urlPath[1] will likely be null
					break;
                case "endorsement":
					response = handleEndorsement(queryParams, urlPath, method); // urlPath[1] will likely be null
					break;
                case "skill":
					response = handleSkill(queryParams, urlPath, method); // urlPath[1] will likely be null
					break;
				case "application":
					response = handleApplication(queryParams, urlPath, method); // urlPath[1] will likely be null
					break;

                // add entries here for each database entity type
            }
        }
        return response;
    }
  
  
  
  // ANTON:
	private  String handleApplication(Map<String, String> queryParams, String[] path, String method){
		if (method == "POST"){
			return postApplication(Integer.parseInt(queryParams.get("applicationId")), queryParams.get("coverletter"), queryParams.get("resume") ,Integer.parseInt(queryParams.get("accountId")), Integer.parseInt(queryParams.get("PostingID")));
		}
		
		return "[]";
    }
	// ANTON: CREATES TABLE
	private void createApplicationTable(){
		PreparedStatement ps = con.prepareStatement("create table Application(
										ApplicationID integer primary key,
										CoverLetter char(2000),
										Resume char(2000),
										ApplicantID integer NOT NULL,
										PostingID integer NOT NULL,
										foreign key(ApplicantID) references Account(AccountID) one delete cascade,
										foreign key(PostingID) references Account(Posting) one delete cascade);");
	}
	private String postApplicationTable(int applicationId, String coverletter, String resume, int applicantID, int posting){
		PreparedStatement ps = con.prepareStatement("INSERT INTO Application (ApplicationID, CoverLetter , Resume, ApplicantID,PostingID )VALUES (?, ? , ?, ?,? ); ");
		ps.setInt(1, accountId);
		ps.setString(2, coverletter);
		ps.setString(3, resume);
		ps.setInt(4, applicantID);
		ps.setInt(5, posting);
		ps.addBatch();
		return getRecordsAsJSON(ps);
	}
/* 		//APPLICATION Insertion statement example
		ps = con.prepareStatement("INSERT INTO Application (ApplicationID, CoverLetter , Resume, ApplicantID, PostingID )VALUES (?, ? , ?, ?,? ); ");
		ps.setInt(1, 1);
		ps.setString(2, "Cover 1");
		ps.setString(3, "Resume 1");
		ps.setInt(4, 1);
		ps.setInt(5, `1);
		ps.addBatch();
		//
*/
    
