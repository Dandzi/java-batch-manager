<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="col-sm-9 col-md-10 main">
 <h1 class="page-header">History of jobs</h1>
 <p class="lead">List of jobs that finished in the past.</p>

 <div class="row">
  <div class="col-md-12">
   <div class="col-md-4">
    <div class="input-group">
     <span class="input-group-addon" id="sizing-addon2">filter</span>
     <input type="text" class="form-control " id="fltr-job-name" placeholder="filter job names">
     <div class="input-group-btn">
      <button type="button" class="btn btn-default filter">
       <i class="glyphicon glyphicon-search"></i>
      </button>
      <button type="button" class="btn btn-default reset">reset</button>
     </div>
    </div>
   </div>
  </div>
 </div>
 <br />

 <form class="form-horizontal">
  <div class="row">
   <div class="col-md-12">
    <div class="col-md-2">
     <div class="input-group">
      <span class="input-group-addon" id="sizing-addon2">start</span>
      <input type="text" id="start" class="form-control" placeholder="start">
     </div>
    </div>
   </div>
  </div>

  <br />

  <div class="row">
   <div class="col-md-12">
    <div class="col-md-2">
     <div class="input-group">
      <span class="input-group-addon" id="sizing-addon2">count</span>
      <input type="text" id="count" class="form-control" placeholder="count">
     </div>
    </div>
   </div>
  </div>
 </form>

 <br />
 <div class="row">
  <div class="col-md-12">
   <div class="col-md-4">
    <div class="panel panel-success">
     <div class="panel-heading">
      <h3 class="panel-title">Jobs</h3>
     </div>
     <div class="past_jobs_height">
      <div class="list-group job-names">
       <c:forEach items="${jsrjobs}" var="jobName">
        <a href="past-jobs/${jobName}_jsr" class="list-group-item job-name" data-job-name="${jobName}">${jobName}</a>
       </c:forEach>
       <c:forEach items="${springjobs}" var="jobName">
        <a href="past-jobs/${jobName}_spring" class="list-group-item job-name" data-job-name="${jobName}">${jobName}</a>
       </c:forEach>
      </div>
     </div>
    </div>
   </div>
   <div class="col-md-4">
    <div class="panel panel-success">
     <div class="panel-heading">
      <h3 class="panel-title">Job Instances</h3>
     </div>
     <div class="past_jobs_height">
      <div class="list-group job-instances">
       <c:forEach items="${jobInstance}" var="jobInstance">
        <a href="past-jobs/${jobInstance.jobName}/${jobInstance.jobInstanceId}" class="list-group-item job-instance" data-job-instance="${jobInstance}">${jobInstance.jobInstanceId}
         ${jobInstance.jobName}</a>
       </c:forEach>
      </div>
     </div>
    </div>
   </div>
  </div>
 </div>

 <div class="row">
  <div class="col-md-12">
   <div class="col-md-12">
    <table class="table">
     <thead>
      <tr>
       <th class="t-width-min">#</th>
       <th class="t-width">Job Name</th>
       <th class="t-width">Job Parameters</th>
       <th class="t-width">Job Status</th>
       <th class="t-width">Created</th>
       <th class="t-width">Started</th>
       <th class="t-width">Detail</th>
      </tr>
     </thead>
     <tbody class="job-executions">
      <tr>
       <td class="t-width-min"></td>
       <td class="t-width"></td>
       <td class="t-width"></td>
       <td class="t-width"></td>
       <td class="t-width"></td>
       <td class="t-width"></td>
       <td class="t-width"></td>
      </tr>
     </tbody>
    </table>
   </div>
  </div>
 </div>
</div>
