<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="col-md-10 col-md-offset-1 main">
  <h3 class="page-header">Job Execution Detail</h3>
<div class="col-md-8 well">
    <ul class="list-group">
      <li class="list-group-item">Job Execution Id:<span class="br">${jobExecution.jobExecutionId}</span></li>
      <li class="list-group-item">Job Name:<span class="br">${jobExecution.jobName}</span></</li>
      <li class="list-group-item">Job Parameters:<span class="br">${jobExecution.parameters}</span></li>
      <li class="list-group-item">Created:<span class="br">${jobExecution.createTime}</span></li>
      <li class="list-group-item">Started:<span class="br">${jobExecution.startTime}</span></li>
      <li class="list-group-item">Status:<span class="br">${jobExecution.status}</span></li>
    </ul>
      <c:if test="${jobExecution.isRestartable == true}">
   <form:form  modelAttribute="jobExecution" method="POST">
     <button type="submit" class="btn btn-default disconnect">Restart</button>
   </form:form>
  </c:if>
  </div>

</div>
