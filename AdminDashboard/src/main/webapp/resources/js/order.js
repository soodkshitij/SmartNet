$().ready(function() {
	$("form#cd input").each(function(){
		$(this).attr('autocomplete', 'off');
	});
});
$().ready(function() {
        // validate the comment form when it is submitted
        $('#cd').validate({
		rules:{
			name:{
				required:true
			},
			email:{
				required:true,
				email:true
			},
			line1:{
				required:true
			},
			state:{
				required:true
			},
			city:{
				required:true
			},
			pincode:{
				required:true,
				digits:true,
				minlength: 6,
                maxlength: 6,
			},
			phone:{
				required:true,
				minlength:10,
				maxlength:10,
				digits:true
			},
		},
		messages:{
			name:{
				required:"Please enter the name"
			},
			line1:{
				required:"Please enter the address"
			},
			state:{
				required: "Please select a state"
			},
			city:{
				required: "Please enter the city"
			},
			email:{
				require: "Please enter a valid email address"
			},
			pincode:{
				required: "Please enter the pincode",
				digits:"Please enter a valid pincode"
			},
			phone:{
				required: "Please enter the phone number",
				digits:"Please enter a valid number",
				minlength:"Number should be of 10 digits"
			}
		},
		submitHandler: function (form, event) {
			event.preventDefault();
			var payload = orderDetailsPayload();
			if(!validateOrderDetails()){
				alert("Please fix highlighted errors");
				return false;
			}
             $.ajax({
                 type: "POST",
                 url: "create-order",
                 data: payload,
                 contentType:'application/json',
				async: false,
				success: function (data) {
					emptyBag();
					$('#main-content').html(data);
				},
				error : function() {
					alert("OOPS!!!Failed to do changes.Try Again.",'ERROR');
				},
				cache: false,
				processData: false
             });
             return false; // required to block normal submit since you used ajax
         }
	});
    
	$( "input.unitPrice").blur(function() {
		console.log("unitPrice blur called");
		var $element = $(this);
		var unitPrice = $element.val();
		var mopPrice = parseFloat($(this).attr('mopPrice'));
		if(parseFloat(unitPrice) < mopPrice){
			//error = true;
			alert("sellingPrice must be greater than equal to mop")
			$element.addClass("border-highlight");
		}else{
			$element.removeClass("border-highlight")
			if(unitPrice == mopPrice){
				$("input.discountAmount").removeAttr("readonly");
			}
			if(unitPrice != ''){
			    $.ajax({
			         type: "GET",
			         url: "insurancePrices?price="+unitPrice,
			         //data: payload,
			         //contentType:'application/json',
					async: false,
					success: function (data) {
						console.log("response : "+JSON.stringify(data));
						var insurancePriceMap = data.response;
						for(key in insurancePriceMap){
							var dealerPrice = insurancePriceMap[key].dealerPrice;
							var sellingPrice = insurancePriceMap[key].sellingPrice;
							$element.closest('tr').find('.insuranceAmount').attr("placeholder", dealerPrice + "-" + sellingPrice);
							break;
						}
					},
					error : function() {
						alert("OOPS!!!Failed to do changes.Try Again.",'ERROR');
					},
					cache: false,
					processData: false
			    });
		    }
		}
	}).focus(function(){
		console.log("unitPrice focus called");
		var $element = $(this);
		$element.closest('tr').find('.insuranceAmount').attr("placeholder", "");
	});
	
	$( "input.phone").blur(function() {
		console.log("phone blur called");
		var mobileNumber = $(this).val();
		writeOldCustomerDetailsByMobileNumber(mobileNumber);
	});
  
});