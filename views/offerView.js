window.onload = function () {
    var url = new URL(window.location.href);
    var offerId = url.searchParams.get("offerId");
    getOffer(offerId);
};

getOffer = function (offerId) {
    let urlPath = 'http://localhost:6789/offer/?offerId=' + offerId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            populateOfferView(data);
        })
        .catch(err => console.error(err));
};

populateOfferView = function (offerData) {
    var offer = offerData[0];
    document.getElementById('o-status').innerHTML = `<h3> Status: ${offer.STATUS}</h3>`;
    document.getElementById('o-details').innerHTML = `
        <h4>Details</h4>
          <p class="lead">Information about this job offer</p>
          <hr class="my-4">
          OfferID: ${offer.OFFERID}
          <br> Employment Type: ${offer.OFFERTYPE}
          <br> Start Date: ${offer.STARTDATE}
          <script> if ($(offer.TERMINATE) == "true") {
              displayEndDate(${offer.ENDDATE});
          } </script>
          <br> Monetary Compensation: $ ${offer.COMPENSATION} per hour
          <script> if ($(offer.STATUS) != "accepted" && (offer.STATUS) != "declined") {
              displayButtons(${offer.OFFERID});
          } </script>`;
    document.getElementById('companyInfo').innerHTML = `
        <div class="jumbotron">
            <h4> Company-Name-Here </h4>
            <p class="lead">Information about your potential future employer.</p>
            <hr class="my-4">
            Number of Employees:
            <br> Industry:
            <br> Address:
            <br> Email Address:
        </div>`;
    document.getElementById('jobInfo').innerHTML = `
        <div class="jumbotron">
            <h4> ${offer.TITLE} </h4>
            <p class="lead">Information about your potential future employment.</p>
            <hr class="my-4">
            <div class="container">
                <div class="row">
                    <div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">
                        Posting Identification Number:
                        <br> Description:
                    </div>
                    <div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
                        <a href="#" class="btn btn-outline-dark btn-lg" type="submit" onsubmit="viewPost(${offer.POSTID})" role="button">View Job Posting</a>
                    </div>
                </div>
            </div>
        </div>`;
};

acceptOffer = function (offerId) {
    let urlPath = 'http://localhost:6789/offer';
    urlPath += ('/?offerId=' + offerId + '&status=' + "accepted");
    fetch(urlPath,
        {method: 'PUT'})
        .then((res) => res.json())
        .catch(err => console.error(err));
};

declineOffer = function (offerId) {
    let urlPath = 'http://localhost:6789/offer';
    urlPath += ('/?offerId=' + offerId + '&status=' + "declined");
    fetch(urlPath,
        {method: 'PUT'})
        .then((res) => res.json())
        .catch(err => console.error(err));
};

viewPost = function (postId) {
    //need some way to pass postId to page so that it loads correct data
    //need some way to parse cwl login id so that window.location points to current users public_html folder.
    //window.location = "https://www.students.cs.ubc.ca/VARIABLE_FOR_CWL_LOGIN_HERE/postingView.html"
};

displayEndDate = function (endDate) {
    `<br> End Date: ${endDate}`
};

displayButtons = function (offerId) {
    `<br> <br>
          <div class="btn-group btn-group-lg" role="button">
              <button id="acceptOffer" type="submit" onsubmit="acceptOffer(${offerId})" class="btn btn-success btn-lg" role="button">
                  Accept
              </button>
              <button id="declineOffer" type="submit" onsubmit="declineOffer(${offerId})" class="btn btn-danger btn-lg" role="button">
                  Decline
              </button>
          </div>`
};