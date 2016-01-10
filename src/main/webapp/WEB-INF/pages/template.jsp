<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" />
<body role="document">
  <jsp:include page="page-header.jsp" />
  <div class="container-fluid">
   <div class="row row-offcanvas row-offcanvas-left">      
     <jsp:include page="nav-side-bar.jsp" />
      <jsp:include page="${param.content}.jsp" />
    </div>
  </div>
  
  <jsp:include page="notification.jsp" />
  <jsp:include page="footer.jsp" />
  <jsp:include page="scripts.jsp" />
</body>
</html>
