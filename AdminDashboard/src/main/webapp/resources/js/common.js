function loadNewCommunity(domId){
	jQuery.ajax({
        type : "GET",
        url : context+"/create-community",
        success : function(response) {
            $('#' + domId).html(response);
        }
    });  
}

function loadCommunityList(domId, status){
	jQuery.ajax({
        type : "GET",
        url : context+"/community-list/?status="+status,
        success : function(response) {
            $('#' + domId).html(response);
        }
    }); 
}

function loadPendingUsers(domId){
	jQuery.ajax({
        type : "GET",
        url : context+"/pending-users",
        success : function(response) {
            $('#' + domId).html(response);
        }
    }); 
}

function loadIp(domId){
	jQuery.ajax({
        type : "GET",
        url : context+"/pending-inter-post",
        success : function(response) {
            $('#' + domId).html(response);
        }
    }); 
}