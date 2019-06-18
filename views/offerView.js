populateOfferView = function(offers){
    var offer = offers[0];
    document.getElementById('status').value = offer.status;
    document.getElementById('type').value = offer.type;
    document.getElementById('offerId').value = offer.offerId;
    document.getElementById('hours').value = offer.hours;
    document.getElementById('compensation').value = offer.compensation;
    document.getElementById('terminating').value = offer.terminating;
    document.getElementById('startDate').value = offer.startDate;
    document.getElementById('endDate').value = offer.endDate;
    document.getElementById('expiryDate').value = offer.expiryDate;
};

getOffer = function(offerId){
    let urlPath = 'http://localhost:6789/offer/'+offerId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            console.log(data);
            populateOfferView(data);
        })
        .catch(err => console.error(err));
};

window.onload = function(){
    var url = new URL(window.location.href);
    var offerId = url.searchParams.get("offerId");
    getOffer(offerId);
};

