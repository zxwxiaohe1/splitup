<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base
	href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<script type="text/javascript" src="webuploader/jquery-1.7.2.js"></script>
<script type="text/javascript" src="webuploader/webuploader.min.js"></script>
<link href="webuploader/webuploader.css" type="css/text" />
<script type="text/javascript" src="admin/bootstrap/jquery-2.0.0.min.js"></script>
<script type="text/javascript" src="admin/bootstrap/jquery-ui.js"></script>
<link href="admin/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<script type="text/javascript" src="admin/bootstrap/bootstrap.min.js"></script>
</head>
<body>
	<h2>Hello World!</h2>
	<div id="thelist" class="uploader-list"></div>
	<div style="margin: 20px 20px 20px 0;">
		<div id="picker" class="form-control-focus">选择文件</div>
	</div>
	<button id="btnSync" type="button" class="btn btn-warning">开始同步</button>
	
	
	<div class="progress">
	  <div id="progress" class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">
	    <span class="sr-only">60% Complete</span>
	  </div>
	</div>
	
	
	<script>
    var fileMd5;  //文件唯一标识  
    
    /******************下面的参数是自定义的*************************/  
    var fileName;//文件名称  
    var oldJindu;//如果该文件之前上传过 已经上传的进度是多少  
    var count=0;//当前正在上传的文件在数组中的下标，一次上传多个文件时使用  
    var filesArr=new Array();//文件数组：每当有文件被添加进队列的时候 就push到数组中  
    var map={};//key存储文件id，value存储该文件上传过的进度  
	 WebUploader.Uploader.register({    
	        "before-send-file":"beforeSendFile",//整个文件上传前  
	        "before-send":"beforeSend",  //每个分片上传前  
	        "after-send-file":"afterSendFile",  //分片上传完毕  
	    },  
	    {    
	        //时间点1：所有分块进行上传之前调用此函数    
	        beforeSendFile:function(file){  
	        	//alert('----');
	            var deferred = WebUploader.Deferred();    
	            //1、计算文件的唯一标记fileMd5，用于断点续传  如果.md5File(file)方法里只写一个file参数则计算MD5值会很慢 所以加了后面的参数：10*1024*1024  
	            (new WebUploader.Uploader()).md5File(file,0,10*1024*1024).progress(function(percentage){  
	                $('#'+file.id ).find('p.state').text('正在读取文件信息...');  
	            })    
	            .then(function(val){    
	                $('#'+file.id ).find("p.state").text("成功获取文件信息...");    
	                fileMd5=val;    
	                uploader.options.formData.guid = fileMd5;
	                console.log("fileMd5:"+fileMd5);
	                //获取文件信息后进入下一步    
	                deferred.resolve();    
	            });    
	              
	            fileName=file.name; //为自定义参数文件名赋值  
	            return deferred.promise();    
	        },    
	        //时间点2：如果有分块上传，则每个分块上传之前调用此函数    
	        beforeSend:function(block){  
	        	//alert('-******-');
	            var deferred = WebUploader.Deferred();    
	            $.ajax({    
	                type:"POST",    
	                url:"${ctx}/upload/CheckChumServlet",  //ajax验证每一个分片  
	                data:{    
	                    fileName : fileName,  
	                    fileMd5:fileMd5,  //文件唯一标记    
	                    chunk:block.chunk,  //当前分块下标    
	                    chunkSize:block.end-block.start,//当前分块大小  
	                    guid: uploader.options.formData.guid
	                },    
	                cache: false,  
	                async: false,  // 与js同步  
	                timeout: 1000, //todo 超时的话，只能认为该分片未上传过  
	                dataType:"json",    
	                success:function(response){    
	                	console.log(block.chunk+"--"+response.ifExist);
	                    if(response.ifExist){  
	                        //分块存在，跳过    
	                        deferred.reject();    
	                    }else{    
	                        //分块不存在或不完整，重新发送该分块内容    
	                        deferred.resolve();    
	                    }    
	                }    
	            });    
	                               
	            this.owner.options.formData.fileMd5 = fileMd5;    
	            deferred.resolve();    
	            return deferred.promise();    
	        },    
	        //时间点3：所有分块上传成功后调用此函数    
	        afterSendFile:function(){  
	        	//alert('-***2222**-');
	            //如果分块上传成功，则通知后台合并分块    
	            $.ajax({    
	                type:"POST",    
	                url:"${ctx}/mergeOrCheckChunks.do?param=mergeChunks",  //ajax将所有片段合并成整体  
	                data:{    
	                    fileName : fileName,  
	                    fileMd5:fileMd5,  
	                },    
	                success:function(data){  
	                    count++; //每上传完成一个文件 count+1  
	                    if(count<=filesArr.length-1){  
	                        uploader.upload(filesArr[count].id);//上传文件列表中的下一个文件  
	                    }  
	                     //合并成功之后的操作  
	                }    
	            });    
	        }    
	    }); 
		var uploader = WebUploader.create({

			// swf文件路径
			swf : 'webuploader/Uploader.swf',
			// 文件接收服务端。
			server : 'UploadVideoServlet',
			// 选择文件的按钮。可选。
			// 内部根据当前运行是创建，可能是input元素，也可能是flash.
			pick : '#picker',
			chunked: true,  //分片处理
            chunkSize: 10 * 1024 * 1024, //每片5M  
            threads:3,//上传并发数。允许同时最大上传进程数。
			// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
			resize : false
		});

		// 当有文件被添加进队列的时候
		uploader.on('fileQueued', function(file) {
			//alert(123);
			$("#thelist").append(
					'<div id="' + file.id + '" class="item">'
							+ '<h4 class="info">' + file.name + '</h4>'
							+ '<p class="state">等待上传...</p>' + '</div>');
		});

		uploader.on('uploadProgress', function(file,percentage) {
			$("#progress").css("width",parseInt(percentage*100)+"%");
		});
		
		uploader.on('uploadSuccess', function(file) {
			$('#' + file.id).find('p.state').text('已上传');
			$.post("UploadSuccessServlet", { "guid": uploader.options.formData.guid,fileName:file.name},
			   function(data){
		   	}, "json");
		});

		uploader.on('uploadError', function(file) {
			$('#' + file.id).find('p.state').text('上传出错');
		});

		uploader.on('uploadComplete', function(file) {
			$('#' + file.id).find('.progress').fadeOut();
		});
		
        uploader.on( 'beforeFileQueued', function( file ) {
            
         // alert(file.size);

        }); 

		$("#btnSync").on('click', function() {
			if ($(this).hasClass('disabled')) {
				return false;
			}
			console.log("get fileMd5:"+fileMd5);
			
			uploader.upload();

		});
	</script>
</body>
</html>
