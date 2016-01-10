<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="col-sm-9 col-md-10 main">
 <h1 class="page-header">Job List</h1>
 <p class="lead">List of jobs that are ready to launch. You can write optional parameters to input next to job name.</p>
 <div class="col-md-8">
  <form:form modelAttribute="job" method="POST" action="launchable-jobs" class="form-horizontal">

   <div class="row">
    <div class="col-md-12">
     <h3>Select Job Name:</h3>
     <div class="btn-group" data-toggle="buttons">
      <c:forEach items="${jsrjobs}" var="j">
       <label class="btn btn-default">
        <form:radiobutton path="jobName" value="${j}_jsr" />
        ${j}
       </label>
      </c:forEach>
      <c:forEach items="${springjobs}" var="j">
       <label class="btn btn-default">
        <form:radiobutton path="jobName" value="${j}_spring" />
        ${j}
       </label>
      </c:forEach>
     </div>
     <form:errors path="jobName" cssClass="alert-danger illeg-attr" element="span" />
    </div>
   </div>

   <div class="row">
    <div class="col-md-12">
     <h3>Write Down Parameters:</h3>
     <div class="col-md-6">
      <div class="form-group">
       <form:input class="form-control" path="parameters" />
      </div>
     </div>
    </div>
   </div>

   <div class="row">
    <div class="col-md-12">
     <h3>Set Start Time:</h3>
     <div class="col-md-6">
      <div class="form-group">
       <div class='input-group date' id='datetimepicker1'>
        <form:input type='text' class="form-control" path="startTime" />
        <span class="input-group-addon">
         <span class="glyphicon glyphicon-calendar"></span>
        </span>
       </div>
      </div>
     </div>
    </div>
   </div>
   <div class="row">
    <div class="col-md-12">
     <button type="submit" class="btn btn-default disconnect">Launch</button>
    </div>
   </div>

  </form:form>
 </div>
 <div class="col-md-4">
  <h3>Jobs that starts in future:</h3>
  <div class="future_jobs_height">
   <c:forEach items="${futurejobs}" var="j">
    <div class="row ">
     <div class="col-md-4">${j.jobName}</div>
     <div class="col-md-4 params" data-value="${j.parameters}" data-toggle="tooltip" data-placement="top" title="${j.parameters}"></div>
     <div class="col-md-4">${j.startTime}</div>
    </div>
   </c:forEach>
  </div>
 </div>
</div>