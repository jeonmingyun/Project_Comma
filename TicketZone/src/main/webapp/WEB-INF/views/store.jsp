<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>Document</title>
<link href="resources/css/store.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="resources/js/store.js"></script>
 
</head>
<body>

	<div id="area_form">
		<%@include file="include/header.jsp"%>

		<section>
			
			<!--search_box-->
			<input type="hidden" id="sido_key" value="12685d425f1af0872d756c" />
			<input type="hidden" id="sigoon_key" value="b0888bae39fbd0463a9252" /> 
    		<input type="hidden" id="dong_key" value="91afccaa8d7f499151ee3b" /> <!--  아직 key 인증을 받지 못함... -->
    		<input type="hidden" name="apiKey" value="E4A59B05-0CF4-3654-BD0C-A169F70CCB34" />
    		<div>
        		<select id="sido_code">
            		<option>선택</option>
        		</select>		
        		<select id="sigoon_code">
            		<option>선택</option>
        		</select>
        		<select id="dong_code">
            		<option>선택</option>
				</select>
				<input type="text" id="detail">
				<button id="test">선택확인</button>
    		</div>
			<!-- 제휴매장 -->
			<div id="store_form">
				<ul class="store_img">
					<c:forEach var="store" items="${list}">
						<li>
							<button class="license" value="${store.license_number}">
								<img class="s_img" src="resources/img/miyago.jpg">
							</button>
							<div>
								<h3>${store.store_name}</h3>
								<p>연락처: ${store.store_tel}</p>
								<p>주소: ${store.address_name}</p>
								<p>영업시간: ${store.store_time}</p>
							</div>
						</li>
					</c:forEach>
				</ul>

				<!-- pagination -->
				<div class="pagination">
					<a href="#"><</a> 
					<a href="#" class="active"> 1 </a> 
					<a href="#"> 2 </a> 
					<a href="#"> 3 </a> 
					<a href="#">></a>
				</div>
			</div>
		</section>

		<%@include file="include/footer.jsp"%>
	</div>

</body>
</html>

