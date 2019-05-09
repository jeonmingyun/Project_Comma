<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>


<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>Insert title here</title>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=2a24234e20fd78957bd509e4c423610f&libraries=services,clusterer,drawing"></script>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="../resources/js/store_info.js"></script>
<link href="../resources/css/store_info.css" rel="stylesheet">
</head>
<body>	
	<%@include file="/WEB-INF/views/include/header.jsp"%>
		
		<!-- 가게 정보 -->
		<div id="store_name" style="display:none;">${storeList[0].store_name}</div>
		
	<div id="store_form">
		<c:forEach var="s" items="${storeList}">
			<div id="store_info" class="${s.address_name}">
				<h1 id="store_name">${s.store_name}</h1>
				<div id="divider_sh">
					<div class="line"></div>
				</div>

				<p>
					<img name="store_img">
				</p>
				
				<p>매장연락처: ${s.store_tel}</p>
				<p>매장주소: ${s.address_name}</p>
				<p>영업시간: ${s.store_time}</p>
				<p>매장소개: ${s.store_intro}</p>
			</div>
			<input type="hidden" id="uuid" value="${s.img_uuid}">
			<input type="hidden" id="uploadpath" value="${s.img_uploadpath}">
			<input type="hidden" id="filename" value="${s.img_filename}">
		</c:forEach>
	
		<!-- 지도 -->
		<div id="map"></div>

</div><!-- store_form -->

	
	<%-- <%@include file="/WEB-INF/views/include/footer.jsp"%> --%>

</body>
</html>