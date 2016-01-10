<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<jsp:include page="head.jsp" />
<body role="document">
  <jsp:include page="page-header.jsp" />
  <div class="container-fluid">
    <div class="row">
      <jsp:include page="${param.content}.jsp" />
      <c:if test="">
      
      </c:if>
    </div>
  </div>
  <jsp:include page="notification.jsp" />
  <jsp:include page="footer.jsp" />
  <jsp:include page="scripts.jsp" />
</body>
</html>
