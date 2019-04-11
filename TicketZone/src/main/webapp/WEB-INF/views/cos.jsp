<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">

<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script>


	
	$(document).ready(function() {
		$("#input_img").on("change", handleImgFileSelect);
		
		
		
		
		var uploadResult = $(".uploadResult ul");
		function showUploadedFile(uploadResultArr) {
			var str = "";

			$(uploadResultArr).each(
					function(i, obj) {
						
						if (!obj.image) {
						//다운로드	
							var fileCallPath = encodeURIComponent(obj.uploadPath+"/" + obj.uuid +"_"+obj.fileName);
							
							var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
							
							str += "<li><a href='/download?fileName="+fileCallPath+"'>"+"<img src='/resources/img/attach.png'>"
									+ obj.fileName + "</a>"+"<span data-file=\'"+fileCallPath+"\' data-type='file'>x</span>"+"<div></li>";
						} else {
							console.log("false");
							var fileCallPath = encodeURIComponent(obj.uploadPath
									+ "/s_" + obj.uuid + "_" + obj.fileName);
							var originPath = obj.uploadPath+ "\\"+obj.uuid +"_"+obj.fileName;
							originPath = originPath.replace(new RegExp(/\\/g),"/");
							str += "<li><a href=\"javascript:showImage(\'"+originPath+"\')\"><img src='/display?fileName="
									+ fileCallPath + "'></a>"+"<span data-file=\'"+fileCallPath+"\' data-type='image'> x </span>" + "<li>";
						}

					});
			uploadResult.append(str);
		}
		var cloneObj = $(".uploadDiv").clone();
		$("#uploadBtn").on("click", function(e) {
			var formData = new FormData();
			var inputFile = $("input[name='uploadFile']");
			var files = inputFile[0].files;

			console.log(files);

			for (var i = 0; i < files.length; i++) {
				if (!checkExtension(files[i].name, files[i].size)) {
					return false;
				}
				formData.append("uploadFile", files[i]);
				console.log(files[i]);
			}

			$.ajax({
				url : '/uploadAjaxAction',
				processData : false,
				contentType : false,
				data : formData,
				type : 'POST',
				dataType : 'json',
				success : function(result) {
					console.log(result);
					showUploadedFile(result);
					$(".uploadDiv").html(cloneObj.html());
				}
			});
		});
		$(".uploadResult").on("click","span",function(e){
			var targetFile = $(this).data("file");
			var type = $(this).data("type");
			console.log(targetFile);
			
			$.ajax({
				url: '/deleteFile',
				data: {fileName: targetFile, type:type},
				dataType: 'text',
				type: 'POST',
					success: function(result){
						alert(result);
					}
			});
		});
	});
	var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
	var maxSize = 5242880; //5MB

	function checkExtension(fileName, fileSize) {
		if (fileSize >= maxSize) {
			alert("파일 사이즈 초과");
			return false;
		}

		if (regex.test(fileName)) {
			alert("해당 종류의 파일은 업로드 할 수 없습니다.");
			return false;
		}
		return true;
	}
	function showImage(fileCallPath){
		$(".bigPictureWrapper").css("display", "flex").show();
		
		$(".bigPicture").html("<img src='/display?fileName=" +encodeURI(fileCallPath)+"'>").animate({width:'100%', height:'100%'},1000);
		$(".bigPicture").on("click", function(e){
			$(".bigPicture").animate({width: '0%', height: '0%'}, 1000);
			setTimeout(function(){
				$(".bigPictureWrapper").hide();
			}, 1000);
		});
		
	}
	function handleImgFileSelect(e){
		var files = e.target.files;
		var filesArr = Array.prototype.slice.call(files);
		
		filesArr.forEach(function(f){
			if(!f.type.match("image.*")){
				alert("확장자는 이미지 확장자만 가능합니다.");
				return
			}
			sel_file = f;
			
			var reader = new FileReader();
			reader.onload = function(e){
				$("#img").attr("src", e.target.result);
			}
			reader.readAsDataURL(f);
		});
	}
</script>
<style>
.uploadResult {
	width: 100%;
	background-color: gray;
}

.uploadResult ul {
	display: flex;
	flex-flow: row;
	justify-content: center;
	align-items: center;
}

.uploadResult ul li {
	list-style: none;
	padding: 10px;
}

.uploadResult ul li img {
	width: 100px;
}
.bigPictureWrapper{
	position: absolute;
	display: none;
	jsutify-content: center;
	align-items: center;
	top:0%;
	width:100%;
	height:100%;
	background-color: gray;
	z-index: 100;
	background:rgba(255,255,255,0.5);
}
.bigPicture{
	position: relative;
	display:flex;
	justify-content: center;
	align-items: center;
}
.bigPicture img{
	width:600px;
}
.img_wrap{
	width:300px;
	margin-top:50px;
}
.img_wrap img{
	max-width: 100%;
}
</style>
<title>Document</title>
</head>
<body>
	<%@include file="include/header.jsp"%>

	<h1>업로드 테스트</h1>
	<!-- <form action="uploadFormAction" method="post"
		enctype="multipart/form-data">
		<input type="file" name="uploadFile" multiple>
		<button>Submit</button>
	</form>
	<br />
	<br /> -->
	<div class="uploadDiv">
		<input id="input_img" type="file" name="uploadFile" multiple>
	</div>

	<div class="uploadResult">
		<ul>

		</ul>
	</div>
	
	<div class="bigPictureWrapper">
		<div class="bigPicture">
		</div>
	</div>
	<div>
		<div class="img_wrap">
			<img id="img"/>
		</div>
	</div>
	<button id="uploadBtn">Upload</button>


	<%@include file="include/footer.jsp"%>
</body>
</html>
