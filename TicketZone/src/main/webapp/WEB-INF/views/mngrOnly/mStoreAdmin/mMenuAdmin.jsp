<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>메뉴 관리</title>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<link href="resources/css/mMenu.css" rel="stylesheet">
<script src="resources/js/mngrOnly/mMenu.js"></script>
</head>

<body>
 <%@include file="/WEB-INF/views/include/Mngrheader.jsp"%> 
 <%@include file="/WEB-INF/views/include/side.jsp" %> 

<div class="store_form">	
   <h1>메뉴 리스트 관리</h1>
   <div class="left">
      <div id="cate">
      
         <div class="cate_add"> 
            <input type="text" id="setMenu" name="setMenu">
            <button id="addSetMenu">
            <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24"><path d="M0 0h24v24H0z" fill="none"/><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm5 11h-4v4h-2v-4H7v-2h4V7h2v4h4v2z"/></svg></button>  
            <button id="CateInputUpdate">
            <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24"><path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/><path d="M0 0h24v24H0z" fill="none"/></svg></button>
         </div>  
         
         <div class="cate_text">
            <h5>카테고리</h5>
            <c:if test="${empty tab}">
            <ul>
               <li></li>
            </ul>
            </c:if>
            
            <c:if test="${!empty tab}">
               <c:forEach var="c" items="${tab}" varStatus = "status">
               <ul>
                  <li>
                  <button class="catebtn" name="menu_cate" value="${c.menu_cate}">${c.menu_cate}</button></li>
               </ul>
               </c:forEach>
            </c:if>
            
            <c:if test="${!empty add}">
               <p class="Tab">
               <button class="addcate" name="menu_cate" value="${add}">${add}</button></p>
            </c:if>
         </div> <!-- cate_text -->
         
      </div> <!-- cate -->
   </div> <!-- left -->
   
   <!-- 메뉴표 -->
   <div class="right">
      <!-- 메뉴 버튼 -->
      <div class="menu_btn">
          <button id="addMenu">메뉴 추가</button>
         <button id="UpdateInputMenu">메뉴 수정</button>
         <button id="DeleteMenu">메뉴 삭제</button>
      </div> <!-- menu_btn -->
      
      <c:if test="${empty menu}">
         <table id="customAdmin">
            <tbody>
               <tr>
                 <th width="50px"><input type="checkbox" id="selectAll"></th> 
                  <th width="250px">메뉴 이름</th>
                  <th width="50px;">가격</th>
                  <th width="450px;">설명</th>
               </tr>
               
               <tr>
                  <td></td> 
                  <td></td>
                  <td></td>
                  <td></td>
               </tr>
            </tbody>
         </table>
      </c:if>
      
      <c:if test="${!empty menu}">
         <table id="customAdmin">
            <tbody>
               <tr>
                  <th width="50px"><input type="checkbox" id="selectAll"></th>
                  <th width="250px">메뉴 이름</th>
                  <th width="50px;">가격</th>
                  <th width="450px;">설명</th>
               </tr>
               <c:forEach var="m" items="${menu}">
                  <tr>
                     <td><input type="checkbox" class="selectMenu" name="selectMenu" value="${m.menu_code}"></td>
                     <td id="menu_name">${m.menu_name}</td>
                     <td id="menu_price">${m.menu_price}</td>
                     <td id="store_note">${m.store_note}</td>
                  </tr>
               </c:forEach>
            </tbody>
         </table>
      </c:if>
      
      <button id="TraddMenu" style="display:none">추가하기</button>      
      <button id="UpdateMenu" style="display:none">수정하기</button>
      <button id="UpdateCate" style="display:none">탭 수정하기</button>
      
   </div> <!-- right -->

   <c:if test="${!empty param.license_number}">         
   <input type="hidden" name="license_number" value="${param.license_number}">
   </c:if>
   <c:if test="${empty param.license_number}">         
   <input type="hidden" name="license_number" value="${tab[0].license_number}">
   </c:if>
   <c:if test="${!empty param.menu_cate}">
   <input type="hidden" name="menu_cate" value="${param.menu_cate}">
   </c:if>
   <c:if test="${empty param.menu_cate}">
   <input type="hidden" name="menu_cate" value="${tab[0].menu_cate}">
   </c:if>  
   
</div> <!-- store_form -->

</body>
</html>