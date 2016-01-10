$(document).ready(function () {
	
	$('[data-toggle="tooltip"]').tooltip();
	
	/*
	 * Popover functionality
	 */
	/*var popoverTemplate = ['<div class="popover">',
	                       	  '<h3 class="popover-title"></h3>',
	                          '<div class="arrow"></div>',
	                          '<div class="popover-content">',
	                          '</div>',
	                          '</div>'].join('');
	var title=['Launch job?'].join('');
		
	$('[data-toggle="popover"]').popover({
        trigger: 'click',
        content: function(){
    		var jobname=$(this).closest(".form-group").find(".job-name").data("job-name");		
    		var content = ['<div><p>Launch job '+jobname+' with given parameters?</p></div>'+
    	                   '<div class="text-center">'+
    	                   '<button class="btn btn-default disconnect" data-start="launch" type="submit">Launch</button>'+
    		               '</div>'].join('');
    		return content;
        },
        title: title,
        template: popoverTemplate,
        placement: "right",
        html: true,
	});*/

	
	/*
	 * Websocket 
	 */
	
	var socket;
	var stompClient;
	var connected;
	
	connect();
	
	function connect(){
		try{
			
			var host = getRootUri() + "/java-batch-manager/notification";
 
            function getRootUri() {
                return "ws://" + (document.location.hostname == "" ? "localhost" : document.location.hostname) + ":" +
                        (document.location.port == "" ? "8080" : document.location.port);
            }
            
			socket = new WebSocket(host);
			stompClient = Stomp.over(socket);
			stompClient.connect({}, function(frame){
				connected=true;
				var date=new Date();
				console.log(date + date.getMilliseconds());
				stompClient.send("/java-batch-manager/notification",{},"connected");
				stompClient.subscribe('/launchable-jobs',function(msg){					
					parseNotification(msg);
				});
			});

		}catch(exception){
			alert("error");
		}
		
	}
	$(document).on("click",".disconnect",function(){
		if(stompClient !== undefined){
			stompClient.send("/java-batch-manager/notification",{},"disconnected");
			stompClient.disconnect();
		}
	});
	
	function parseNotification(msg){		
		var jsonMsg = JSON.parse(msg.body);
		if(jsonMsg.hasOwnProperty('action')){
			location.reload();

		}
		if(jsonMsg.hasOwnProperty('jobName')){
			buildNotification(jsonMsg.jobName, jsonMsg.parameters, jsonMsg.status);
			var date=new Date();
			date.setSeconds(date.getSeconds()+5);
			Cookies.set('notification',jsonMsg,{expires:date});
		}
	}
	
	function buildNotification(jobName,parameters,status){
		$(".notification").removeAttr("style");
		$(".notification").append("<div class=\"alert alert-success col-md-offset-8 col-md-3\" role=\"alert\">"+ 
				"Job execution of job " + jobName +" with parameters "+ parameters+ " now has status "+status+
				"</div>").animate({ bottom:50, opacity:"show"}, 1000)
                			.delay(5000)
                			.fadeOut(function(){
                				$(".alert").remove();
                			});
	
	}
	
	
	$(document).on("click",'[data-start="launch"]',function(event){
		stompClient.send("/java-batch-manager/notification",{},JSON.stringify({"test":"test"}));
	});
	
	
	/*
	 * modifications
	 */
	
	var cookieVal=Cookies.get('notification');
	if(cookieVal){
		var jsonMsg = JSON.parse(cookieVal);
		buildNotification(jsonMsg.jobName, jsonMsg.parameters, jsonMsg.status);
	}
	

    $('.btn-file :file').on('fileselect', function(event, numFiles, label) {
        
        var input = $(this).parents('.input-group').find(':text'),
            log = numFiles > 1 ? numFiles + ' files selected' : label;
        
        if( input.length ) {
            input.val(log);
        } else {
            if( log ) alert(log);
        }
        
    });
    
    $(document).ready(function () {
    	  $('[data-toggle="offcanvas"]').click(function () {
    	    $('.row-offcanvas').toggleClass('active')
    	  });
    	});
    
    /*
     * job type selection
     */
    $('.job-type-btn').on("change",function() {        
    	$(".job-type-form").submit();
		if(stompClient !== undefined){
			stompClient.send("/java-batch-manager/notification",{},"reload");
		}
    });
    
	setActiveJobServiceRadioButton();
    function setActiveJobServiceRadioButton(){
    	var activeJobServ = $('.active-job-service').val();
    	$('input[id][value='+activeJobServ+']').parent().addClass("active");    	
    }
    
    /*
     * autocomplete
     */
    var jobNames = new Array();
    fillJobNames();
    function fillJobNames(){
    	$(".job-name").each(function(i){
    		jobNames.push($(this).attr("data-job-name"));
    	})
    }
    
    var jobNameSelect = function () {
    	
    } 

    /*$('#fltr-job-name').on('select',function(event){
    	$(this).val('');
    	$(".job-name").show();

    })*/
    
    $('#fltr-job-name').on('keyup',function(event){
    	if(event.which == 13){
    		var str2 = $(this).val();
    		$(".job-name").each(function(i){
    			var str1 = $(this).data('job-name');
    			if(str1.indexOf(str2)==-1){
    				$(this).hide();
    			}
    		})
    	}
    	$(this).autocomplete({
        	source: jobNames,
        	select: function(){
        		var str2 = $(this).val();
        		$(".job-name").each(function(i){
        			var str1 = $(this).data('job-name');
        			if(str1.indexOf(str2)==-1){
        				$(this).hide();
        			}
        		})
        	},        	
        })
    })
    
    /*
     * Date Time picker
     */
    
    $('#datetimepicker1').datetimepicker();
    
	/*
	 * Ajax calls for past job content update
	 */
	
	$(".list-group-item").on("click", function(event){
		$(".job-name.active").removeClass("active");
		var jobName=$(this).data("job-name");
		$('[data-job-name ='+jobName+']').addClass("active");
		event.preventDefault();
		var url=$(this).attr('href');
		var start=$("#start").val();
		var count=$("#count").val();
		getInstances(event,url,start,count);
	});
	
	function getInstances(event,url,start,count){
		$.ajax({
			type: "GET",
			url: ""+url,
			dataType: "json",
			data: {
					"start": start,
					"count": count
				 },
			success: function(response){
				$(".job-instance").remove();
				var jobInstances=$(".job-instances");
				$.each(response,function(key,job){
					$("<a href=\"past-jobs/"+job.jobName+"/"+job.jobInstanceId+"\" class=\"list-group-item job-instance\">"+job.jobInstanceId+" "+job.jobName+"</a>").appendTo(jobInstances);
				});	
			},
			error: function(err){
				console.log(err);
			}
		});
	}
	
	$(document).on("click", ".job-instance",function(event){
		$(".job-instance.active").removeClass("active");
		$(this).addClass("active");
		event.preventDefault();
		var url=$(this).attr('href');
		getExecutions(event,url);
	});
	
	function getExecutions(event,url){
		$.ajax({
			type: "GET",
			url:""+url,
			dataType: "json",
			success: function(response){
				$("tbody").empty();
				$.each(response,function(key,execution){
					$("<tr class=\"job-execution\">" +
							"<td class=\"t-width-min\">1.</td>" +
							"<td class=\"width-min\">"+execution.jobName+"</td>" +
							"<td class=\"params width-min\">"+modifyParams(execution.parameters)+"</td>" +
							"<td class=\"width-min\">"+execution.status+"</td>" +
							"<td class=\"width-min\">"+converTime(execution.createTime)+"</td>" +
							"<td class=\"width-min\">"+converTime(execution.startTime)+"</td>" +
							"<td class=\"width-min\"><a href=\"/java-batch-manager/job-execution/"+execution.jobExecutionId+"\" class=\"glyphicon glyphicon-eye-open\"></a></td>" +
						"<tr/>").appendTo(".job-executions");
				});
			},
			error: function(err){
				console.log(err);
		}
		})
	}
	
	function converTime(date){
		var d = new Date(date);
		var month = d.getMonth()+1;
		return ""+d.getDate()+"."+month+"."+d.getFullYear()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
	}
	
	function modifyParams(params){
		if(params.length > 10){
			params=params.substring(0,10)+"&hellip;";
			return params;
		}
		return params;
	}
	
	function testRest(){
		$.ajax({
			url:"/java-batch-manager/rest/execution/0",
			type: 'GET',
			dataType: 'json',
		    contentType: 'application/json',
		    mimeType: 'application/json',
			success: function(response){
				alert(response);
			},
			error: function(err){
				alert(err);
			}
		});
	}
	
	//testRest();
});
	
$(document).on('change', '.btn-file :file', function() {
	  var input = $(this),
	      numFiles = input.get(0).files ? input.get(0).files.length : 1,
	      label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
	  input.trigger('fileselect', [numFiles, label]);
	});