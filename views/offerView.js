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
          <br> <br>
          <div class="btn-group btn-group-lg" role="button">
              <button id="acceptOffer" onclick="acceptOffer(${offer.OFFERID})" class="btn btn-success btn-lg" role="button">
                  Accept
              </button>
              <button id="declineOffer" onclick="declineOffer(${offer.OFFERID})" class="btn btn-danger btn-lg" role="button">
                  Decline
              </button>
          </div>`
    document.getElementById('companyInfo').innerHTML = `
        <div class="jumbotron">
            <h4> ${offer.COMPANYNAME} </h4>
            <p class="lead">Information about your potential future employer.</p>
            <hr class="my-4">
            Number of Employees: ${offer.COMPANYSIZE}
            <br> Industry: ${offer.COMPANYINDUSTRY}
            <br> Address: ${offer.ADDRESS}
            <br> Email Address: ${offer.COMPANYEMAIL}
        </div>`;
    document.getElementById('jobInfo').innerHTML = `
        <div class="jumbotron">
            <h4> ${offer.TITLE} </h4>
            <p class="lead">Information about your potential future employment.</p>
            <hr class="my-4">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                        Posting Identification Number: ${offer.POSTINGID}
                        <br> Description: ${offer.DESCRIPTION}
                    </div>
                </div>
            </div>
        </div>`;
};

acceptOffer = function (offerId) {
    let urlPath = 'http://localhost:6789/offer';
    urlPath += ('/?offerId=' + offerId + '&status=Accepted');
    fetch(urlPath,
        {method: 'POST'})
        .then((res) => res.json())
        .catch(err => console.error(err));
};

declineOffer = function (offerId) {
    let urlPath = 'http://localhost:6789/offer';
    urlPath += ('/?offerId=' + offerId + '&status=Declined');
    fetch(urlPath,
        {method: 'POST'})
        .then((res) => res.json())
        .catch(err => console.error(err));
};

displayEndDate = function (endDate) {
    `<br> End Date: ${endDate}`
};