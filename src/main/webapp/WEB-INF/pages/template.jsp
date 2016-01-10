<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="head.jsp" />
<body role="document">
  <input type="hidden" class="active-job-service" value="${activeJobService}" />
  <jsp:include page="page-header.jsp" />
  <div class="container-fluid">
   <div class="row row-offcanvas row-offcanvas-left">      
     <jsp:include page="nav-side-bar.jsp" />
      <form:form class="job-type-form col-md-offset-2" modelAttribute="globVar" method="POST"
        action="/java-batch-manager/job-type">      
        <div class="btn-group col-md-4" data-toggle="buttons">
          <c:forEach items="${jobTypeList}" var="type">
            <label class="btn btn-default job-type-btn"> 
              <form:radiobutton path="jobType" value="${type}"/>
              ${type.stringTypeRepr}
            </label>
          </c:forEach> 
        </div>
      </form:form>
      <jsp:include page="${param.content}.jsp" />
    </div>
  </div>
  
  <jsp:include page="notification.jsp" />
  <jsp:include page="footer.jsp" />
  <jsp:include page="scripts.jsp" />
</body>
</html>
