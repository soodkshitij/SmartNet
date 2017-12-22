$(function() {
	
	$('#myModal').on('shown.bs.modal', function (e) {
		$( "li.post-type" ).removeClass('post-type-list-hover');
		$( "li.post-type" ).first().addClass('post-type-list-hover');
		$("#message-container").show();
		$("#alert-container").hide();
		$("#inter-container").hide();
		$(".postbox-radio-options-container").hide();
		$("#inter-container .post-section").show();
		$("#inter-container .inter-post-map-container").hide();
		initGoogleInterPostMap();
		mapResize();
	})
	
	$( "li.post-type" ).on('click',function() {
		$( "li.post-type" ).removeClass('post-type-list-hover');
		$(this).addClass('post-type-list-hover');
		if($(this).hasClass("alerts")){
			$("#inter-container").hide(500);
			$("#message-container").hide(500);
			$("#alert-container").show(500);
		}
		else if($(this).hasClass("inter-post")){
			$("#message-container").hide(500);
			$("#alert-container").hide(500);
			$("#inter-container").show(500);
		}
		else{
			$("#alert-container").hide(500);
			$("#inter-container").hide(500);
			$("#message-container").show(500);
		}
	});
	
	$(".category-list-block").on('click',function(){
		$(".postbox-radio-options-container").toggle();
	});
	
	$(".inter-post-form-button").on('click',function(){
		var category = $('#inter-container input[name=category]:checked').val();
		console.log(category);
		var subject = $('#inter-container input[name=subject]').val();
		console.log(subject);
		var msg = $('textarea#inter-message').val();
		console.log(msg);
		
		if (!category){
			alert("Please select category from category list.");
			return false;
		}
		
		if (!subject){
			alert("Please enter subject for the post.");
			return false;
		}
		
		if (!msg){
			alert("Please enter the message.");
			return false;
		}
		
		$("#inter-container .post-section").hide();
		$("#inter-container .inter-post-map-container").show();
		mapResize();
	});
	
	$(".inter-post-form-button-submit").on('click',function(){
		var category = $('#inter-container input[name=category]:checked').val();
		console.log(category);
		var subject = $('#inter-container input[name=subject]').val();
		console.log(subject);
		var msg = $('textarea#inter-message').val();
		console.log(msg);
		var c_id= [];
		$("#neighborhood-list :input:checked").each(function() {
			c_id.push($(this).attr('data-c-id'));
		});
		if (c_id.length==0){
			alert("Please select communities to post your message");
			return false;
		}
		console.log(c_id);
		
		var imgs = $("#uploaded-public-keys").val();
		$("#uploaded-public-keys").val("");
		jQuery.ajax({
			url: context+"/post-inter-post/?subject="+subject+"&message="+msg+"&category="+category+"&communityIds="+c_id+"&image_ids="+imgs,
			type: 'POST',
			async: false,
			success: function (data) {
				$('#myModal').modal('toggle');
				$('#alert-post-form')[0].reset();
				$('#message-post-form')[0].reset();
				$('#inter-post-form')[0].reset();
			},
			error : function() {
				alert("OOPS!!!Failed to do changes.Try Again.",'ERROR');
			},
			cache: false,
			contentType: false,
			processData: false
		});
		
	});
	
	$(".message-post-form-button").on('click',function(){
		var category = $('#message-container input[name=category]:checked').val();
		console.log(category);
		var subject = $('#message-container input[name=subject]').val();
		console.log(subject);
		var msg = $('textarea#message').val();
		console.log(msg);
		
		if (!category){
			alert("Please select category from category list.");
			return false;
		}
		
		if (!subject){
			alert("Please enter subject for the post.");
			return false;
		}
		
		if (!msg){
			alert("Please enter the message.");
			return false;
		}
		
		//post form
		
		var imgs = $("#uploaded-public-keys").val();
		$("#uploaded-public-keys").val("");
		
		jQuery.ajax({
			url: context+"/post-update/?subject="+subject+"&message="+msg+"&category="+category+"&image_ids="+imgs,
			type: 'POST',
			async: false,
			success: function (data) {
				$('#myModal').modal('toggle');
				$('#alert-post-form')[0].reset();
				$('#message-post-form')[0].reset();
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
	
	$(".alert-post-form-button").on('click',function(){
		//alertmessage
		var msg = $('textarea#alertmessage').val();
		console.log(msg);
		if (!msg){
			alert("Please enter the alert message.");
			return false;
		}
		
		jQuery.ajax({
			url: context+"/post-update/?subject="+"Urgent Alert"+"&message="+msg+"&category="+"URGENT_ALERT",
			type: 'POST',
			async: false,
			success: function (data) {
				$('#alert-post-form')[0].reset();
				$('#message-post-form')[0].reset();
				$('#myModal').modal('toggle');
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
	
	$(".user-message-post-form").on('click',function(){
		//alertmessage
		var msg = $('textarea#message').val();
		var to_user_id = $(".modal-header input.user_id").val();
		console.log(msg);
		if (!msg){
			alert("Please enter the alert message.");
			return false;
		}
		
		jQuery.ajax({
			url: context+"/post-message/?content="+msg+"&to_user_id="+to_user_id,
			type: 'POST',
			async: false,
			success: function (data) {
				console.log(data);
				$('#messageModal').modal('toggle');
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
	
	$(".user-message").on('click',function(){
		//alertmessage
		var from_user=$(this).attr('data-user-id');
		var to_user=$(this).attr('data-me-user');
		
		jQuery.ajax({
			url: context+"/messages-from-user/?to_user_id="+to_user+"&from_user_id="+from_user,
			type: 'GET',
			async: false,
			success: function (data) {
				document.getElementById("id-messages-from-user").innerHTML=data;
				$(".send-box").attr("data-user-id",from_user);
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
	
	$(".ajax-category").on('click',function(){
		var cat = $(this).attr('data-cat');
		window.open(context+"/category-feed/?category="+cat,"_self")
	});
	
	$(".neighbor-button-right").on('click',function(){
		var user_name = $(this).attr('data-user-name');
		var user_id = $(this).attr('data-user-id');
		$(".modal-header > h4").text("Send a message to "+user_name);
		$(".modal-header input.user_id").val(user_id);
	});
	
	$(document).on('click', ".button-container" , function() {
		var post_id = $(this).attr("data-post-id");
		var reply_class = ".comment-container-"+post_id;
		var expanded = $(this).attr("expanded");
		
		
		if (expanded=="false"){
			jQuery.ajax({
				url: context+"/get-comment?post_id="+post_id,
				type: 'GET',
				async: false,
				success: function (data) {
					$(".comments-"+post_id).append(data);
				},
				error : function() {
					alert("OOPS!!!Failed to do changes.Try Again.",'ERROR');
				},
				cache: false,
				contentType: false,
				processData: false
			});
		}
		$(this).attr("expanded","true");
		$(reply_class).slideToggle();
	});
	
	$(document).on('keydown', ".comment-add-reply-text-input" , function(e) {
	   var code = e.keyCode ? e.keyCode : e.which;
	   if (code == 13 && !e.shiftKey) {  // Enter keycode
		 var post_id = $(this).attr("data-post-id");
		 var cmt = $(this).val();
		 $(this).val("");
		 e.preventDefault();
		 
		 jQuery.ajax({
				url: context+"/post-comment/?post_id="+post_id+"&cmt="+cmt,
				type: 'POST',
				async: false,
				success: function (data) {
					$(".comments-"+post_id).append(data);
				},
				error : function() {
					alert("OOPS!!!Failed to do changes.Try Again.",'ERROR');
				},
				cache: false,
				contentType: false,
				processData: false
			});
		 
	     return true;
	   }
	});
	
	$(document).on('keydown', ".chat-reply-textarea" , function(e) {
		   var code = e.keyCode ? e.keyCode : e.which;
		   if (code == 13 && !e.shiftKey) {  // Enter keycode
			 var to_user = $(".send-box").attr("data-user-id");
			 var from_user = $(".send-box").attr("data-me-user");
			 var msg = $(this).val();
			 $(this).val("");
			 e.preventDefault();
			 
			 jQuery.ajax({
					url: context+"/post-message-ajax/?content="+msg+"&to_user_id="+to_user,
					type: 'POST',
					async: false,
					success: function (data) {
						console.log(data);
						$("#id-messages-from-user").append(data);
					},
					error : function() {
						alert("OOPS!!!Failed to do changes.Try Again.",'ERROR');
					},
					cache: false,
					contentType: false,
					processData: false
				});
			 
		     return true;
		   }
		});
	
	
	$(".add-photo").on('click',function(){
		console.log("add-photo");
		$("#uploadModal").modal('show');
	});
	
});