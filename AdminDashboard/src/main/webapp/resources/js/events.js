
$(function() {
	$(".sidepannel_new_community").live('click', function() {
		loadNewCommunity("main-content");
	});
	
	$(".sidepannel_pending_community").live('click', function() {
		loadCommunityList("main-content","0");
	});
	
	$(".sidepannel_active_community").live('click', function() {
		loadCommunityList("main-content","1");
	});
	
	$(".sidepannel_paused_community").live('click', function() {
		loadCommunityList("main-content","2");
	});
	
	$(".view_pending_user").live('click', function() {
		loadPendingUsers("main-content");
	});
	
	$(".approve_inter_post").live('click', function() {
		loadIp("main-content");
	});
	
	$(".activate-pending-users").live('click',function(){
		var user_id = $(this).attr('data-id');
		jQuery.ajax({
			url: context+"/activate-user?user_id="+user_id,
			type: 'POST',
			contentType:'application/json',
			async: false,
			success: function (data) {
				var obj = JSON.parse(data);
				alert(obj.message);
//				$("#scanModel").on("hidden.bs.modal", function () {
//					$("#purchase-reference-submit").submit();
//				});
			},
			error : function() {
				alert("OOPS!!!Failed to do changes.Try Again.",'ERROR');
			},
			cache: false,
			processData: false
		});
		return false;
	});
		
	$(".activate-ip").live('click',function(){
		var p_id = $(this).attr('data-id');
		jQuery.ajax({
			url: context+"/activate-ip?id="+p_id,
			type: 'POST',
			contentType:'application/json',
			async: false,
			success: function (data) {
				var obj = JSON.parse(data);
				alert(obj.message);
//				$("#scanModel").on("hidden.bs.modal", function () {
//					$("#purchase-reference-submit").submit();
//				});
			},
			error : function() {
				alert("OOPS!!!Failed to do changes.Try Again.",'ERROR');
			},
			cache: false,
			processData: false
		});
		return false;
	});
	
	$("#create_new_community_form").live('submit', function(){
		
		var formData = new FormData($(this)[0]);

		jQuery.ajax({
			url: context+"/create-community",
			type: 'POST',
			data: formData,
			async: false,
			success: function (data) {
				var obj = JSON.parse(data);
				alert(obj.message)
				if (obj.result){
					window.location.href= context+"/dashboard";
				}
			},
			error : function() {
				alert("OOPS!!!Failed to do changes.Try Again.",'ERROR');
			},
			cache: false,
			contentType: false,
			processData: false
		});
		return false;
	});
	
	$(".mark-community-as-active").live('click', function() {
		var communityId = $(this).attr('community-id');
		var communityName = $(this).attr('community-name');
		var target = $(this).attr('data-target');
		console.log(communityId+" "+communityName+" "+target);
		console.log($(target+" h4.modelHeaderCustom.cname"));
		$(target+ " h4.modelHeaderCustom.cname>span").text(communityName);
		$(target+" input.communityId").val(communityId);
	});
	
	$(".mark-community-as-paused").live('click', function() {
		var communityId = $(this).attr('community-id');
		var communityName = $(this).attr('community-name');
		var target = $(this).attr('data-target');
		console.log(communityId+" "+communityName+" "+target);
		console.log($(target+" h4.modelHeaderCustom.cname"));
		$(target+ " h4.modelHeaderCustom.cname>span").text(communityName);
		$(target+" input.communityId").val(communityId);
	});
	
	$(".mark-community-as-active-again").live('click', function() {
		var communityId = $(this).attr('community-id');
		var communityName = $(this).attr('community-name');
		var target = $(this).attr('data-target');
		console.log(communityId+" "+communityName+" "+target);
		console.log($(target+" h4.modelHeaderCustom.cname"));
		$(target+ " h4.modelHeaderCustom.cname>span").text(communityName);
		$(target+" input.communityId").val(communityId);
	});
	
	
	$("#submit-mark-community-as-active").live('click', function(){
		var communityId = $("#pendingModel .communityId").val();
		var communityAdminEmail = $("#pendingModel .modal-body .community_admin_email").val();
		jQuery.ajax({
			url: context+"/community-admin/mark-as-active/?communityId="+communityId+"&communityAdminEmail="+communityAdminEmail,
			type: 'POST',
			contentType:'application/json',
			async: false,
			success: function (data) {
				var obj = JSON.parse(data);
				alert(obj.message);
				$('#scanModel').modal('hide');
//				$("#scanModel").on("hidden.bs.modal", function () {
//					$("#purchase-reference-submit").submit();
//				});
			},
			error : function() {
				alert("OOPS!!!Failed to do changes.Try Again.",'ERROR');
			},
			cache: false,
			processData: false
		});
		return false;
	});
	
	$("#submit-mark-community-as-paused").live('click', function(){
		var communityId = $("#pendingModel .communityId").val();
		jQuery.ajax({
			url: context+"/community-admin/mark-as-paused/?communityId="+communityId,
			type: 'POST',
			contentType:'application/json',
			async: false,
			success: function (data) {
				alert("Data updated successfully");
				$('#scanModel').modal('hide');
//				$("#scanModel").on("hidden.bs.modal", function () {
//					$("#purchase-reference-submit").submit();
//				});
			},
			error : function() {
				alert("OOPS!!!Failed to do changes.Try Again.",'ERROR');
			},
			cache: false,
			processData: false
		});
		return false;
	});
	
	$("#submit-mark-community-as-active-again").live('click', function(){
		var communityId = $("#pausedModel .communityId").val();
		jQuery.ajax({
			url: context+"/community-admin/mark-as-active-again/?communityId="+communityId,
			type: 'POST',
			contentType:'application/json',
			async: false,
			success: function (data) {
				var obj = JSON.parse(data);
				alert(obj.message);
				$('#pausedModel').modal('hide');
//				$("#scanModel").on("hidden.bs.modal", function () {
//					$("#purchase-reference-submit").submit();
//				});
			},
			error : function() {
				alert("OOPS!!!Failed to do changes.Try Again.",'ERROR');
			},
			cache: false,
			processData: false
		});
		return false;
	});
	
});
