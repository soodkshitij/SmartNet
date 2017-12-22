var markers = [];
var map;

function loadSignUpPage(){
	var address = $('#Address').val();
	jQuery.ajax({
        type : "GET",
        url : context+"/create-community/?address="+address,
        success : function(response) {
            $('#' + domId).html(response);
        }
    });  
}

function initGoogleInterPostMap(){
	map = new google.maps.Map(document.getElementById('inter-post-map'), {
	      center: {lat: 37.3382, lng: -121.8863},
	      zoom: 9
	    });
	    var latLng = new google.maps.LatLng(37.3382,-121.8863);
	    var marker = new google.maps.Marker({
	        position: latLng,
	        map: map
	    });
}

function addMarker(i,lat,long) {
    var latLng = new google.maps.LatLng(lat,long); 
    var marker = new google.maps.Marker({
      position: latLng,
      map: map
    });
    //markers.push(marker);
     markers[i] = marker;
}

function mapResize(){
	google.maps.event.trigger(map, "resize");
}


function OptionsSelected(element){
    var id = element.id;
    var neighborhood_index = parseInt(id.substring(id.lastIndexOf('_')+1));
    
    if(element.checked === true){
    	var lat = element.getAttribute('data-lat');
    	var lon = element.getAttribute('data-lon');
        //console.log(Number(neighborhood_list[neighborhood_index].lat));
        addMarker(neighborhood_index,Number(lat),Number(lon));    
    }
    else{
        markers[neighborhood_index].setMap(null);
    }
    
    console.log(neighborhood_index);
}


