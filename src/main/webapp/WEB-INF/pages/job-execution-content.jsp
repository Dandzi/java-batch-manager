<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="col-md-10 col-md-offset-1 main">
  <h3 class="page-header">Job Execution Detail</h3>
<div class="col-md-8 well">
   <form:form  modelAttribute="jobExecution" method="POST">
    <ul class="list-group">
      <li class="list-group-item">Job Execution Id:<span class="br">${jobExecution.jobExecutionId}</span></li>
      <input type="hidden"  value="${jobExecution.jobExecutionId}" />
      <li class="list-group-item">Job Name:<span class="br">${jobExecution.jobName}</span></</li>
      <input type="hidden"  value="${jobExecution.jobName}" />
      <li class="list-group-item">Job Parameters:<span class="br">${jobExecution.parameters}</span></li>
      <input type="hidden"  value="${jobExecution.parameters}" />
      <li class="list-group-item">Created:<span class="br">${jobExecution.createTime}</span></li>
      <li class="list-group-item">Started:<span class="br">${jobExecution.startTime}</span></li>
      <li class="list-group-item">Finished:<span class="br">${jobExecution.startTime}</span></li>
      <li class="list-group-item">Status:<span class="br">${jobExecution.status}</span></li>
      <li class="list-group-item">Job type:<span class="br">${jobExecution.jobType}</span></li>
      <li class="list-group-item">Job exit description:<span class="br">${jobExecution.exitDescription}</span></li>
      <c:forEach items="${jobExecution.stepExecutionsDto}" var="step">
       <li class="list-group-item">Step id:<span class="br">${step.id}</span></li>
       <li class="list-group-item">Step name:<span class="br">${step.stepName}</span></li>
       <li class="list-group-item">Step start time:<span class="br">${step.startTime}</span></li>
       <li class="list-group-item">Step end time:<span class="br">${step.endTime}</span></li>       
       <li class="list-group-item">Step exit description:<span class="br">${step.exitDescription}</span></li>
      </c:forEach>
    </ul>
      <c:if test="${jobExecution.isRestartable == true}">
        <button type="submit" class="btn btn-default disconnect">Restart</button>
      </c:if>
      <form:errors path="jobName" cssClass="alert-danger illeg-attr" element="span"/>   
  </form:form>
  </div>

</div>
