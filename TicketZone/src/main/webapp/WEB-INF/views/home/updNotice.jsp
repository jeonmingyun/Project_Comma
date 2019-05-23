<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>번호요</title>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<link href="../resources/css/updNotice.css" rel="stylesheet" />
<script src="../resources/js/updNotice.js"></script>
<style media="screen">
</style>
<script>
	
</script>
</head>

<body>
	<h2>공지사항 수정</h2>
	<!-- <form method="post" action="/update"> -->
		<input type="hidden" value="${noticeUpd[0].notice_no}" name="notice_no">
		<div>
			제목 <input name="notice_title" id="title" size="80" value="${noticeUpd[0].notice_title}">
		</div>
		<div>
			내용
			<textarea name="notice_content" id="content" rows="4" cols="80">${noticeUpd[0].notice_content}</textarea>
		</div>
		<div>
		<span id="date">첨부파일</span>
			<div id="uploadFile">
			<c:if test="${file[0] != null}"><a href="#">${file[0].fileName}</a><button id="deleteFile">삭제하기</button></c:if><c:if test="${file[0] == null}"></c:if>
			<br>
			</div>
			<input type="file" name="uploadFile">
		</div>
		<div>
			<button id="update">수정</button>
			<button id="cancel">취소</button>
		</div>
	<!-- </form> -->
		<div class="modifyDiv"></div>
		<c:if test="${file[0] != null}">
		<input id="uuid" type="hidden" name="uuid" value="${file[0].uuid}">
		<input id="uploadPath" type="hidden" name="uploadPath" value="${file[0].uploadPath}">
		<input id="fileName" type="hidden" name="fileName" value="${file[0].fileName}">
		<input id="notice_status" type="hidden" name="notice_status" value="${noticeUpd[0].notice_status}">
		</c:if>
</body>
</html>