<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="col-sm-9 col-md-10 main">
 <h1 class="page-header">Running Jobs List</h1>
 <p class="lead">List of jobs that are currently running.</p>
 <div class="row">
  <div class="col-md-10">
   <form:form modelAttribute="jobExecutionsList" method="POST" action="/java-batch-manager/running-jobs/stop" class="form-horizontal">
    <c:forEach items="${jobExecutionsList.allJobExecutions}" var="job" varStatus="status">
     <div class="form-group col-md-12">
      <div class="row">
       <div class="col-md-1">
        <label class="control-label text-center pad-r-20">${status.index+1}.</label>
       </div>
       <div class="col-md-2">
        <label class="control-label text-center pad-r-20 job-name" data-toggle="tooltip" data-placement="top" data-job-name="${job.jobName}"
         title="Job name: ${job.jobName}">${job.jobName}</label>
       </div>
       <div class="col-md-2">
        <label class="control-label text-center pad-r-20 job-name" data-toggle="tooltip" data-placement="top" data-job-name="${job.parameters}"
         title="Job parameters: ${job.parameters}">${job.parameters}</label>
       </div>
       <div class="col-md-2">
        <label class="control-label text-center pad-r-20 job-name" data-toggle="tooltip" data-placement="top" data-job-name="${job.status}"
         title="Job status: ${job.status}">${job.status} </label>
       </div>
       <div class="btn-group" data-toggle="buttons">
        <label class="btn btn-default">
         <form:checkbox path="allJobExecutions[${status.index}].stop" value="true" />
         stop
        </label>
       </div>
      </div>
      <form:errors path="allJobExecutions[${status.index}]" cssClass="alert alert-danger" />
     </div>
    </c:forEach>
    <div>
     <button type="submit" class="disconnect btn btn-default">Stop selected jobs</button>
     <form:errors cssClass="alert alert-danger" />
    </div>
    <form:errors path="allJobExecutions[0].stop" cssClass="alert alert-danger" />
   </form:form>
  </div>
 </div>
</div>

