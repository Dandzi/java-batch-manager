<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="col-sm-9 col-md-10 main">
  <h1 class="page-header">History of jobs</h1>
  <p class="lead">List of jobs that finished in the past.</p>
  <form class="form-horizontal">
  <div class="form-group">
    <label class="col-sm-1 control-label">filter:</label>
      <div class="col-sm-4">
        <input type="text" class="form-control" id="fltr-job-name"
          placeholder="filter job names">
      </div>
   </div>
  <form class="form-horizontal">
    <div class="form-group">
      <label class="col-sm-1 control-label">start:</label>
      <div class="col-sm-2">
        <input type="text" class="form-control" id="start" placeholder="start">
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-1 control-label">count:</label>
      <div class="col-sm-2">
        <input type="text" class="form-control" id="count" placeholder="count">
      </div>
    </div>
  </form>
  <div class="col-md-12">
    <div class="col-md-4">
      <div class="panel panel-success">
        <div class="panel-heading">
          <h3 class="panel-title">Jobs</h3>
        </div>

        <div class="list-group job-names">
          <c:forEach items="${jobNames}" var="jobName">
            <a href="past-jobs/${jobName}"
              class="list-group-item job-name"
              data-job-name="${jobName}">${jobName}</a>
          </c:forEach>
        </div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="panel panel-success">
        <div class="panel-heading">
          <h3 class="panel-title">Job Instances</h3>
        </div>
        <div class="list-group job-instances">
          <c:forEach items="${jobInstance}" var="jobInstance">
            <a href="past-jobs/${jobInstance.jobName}/${jobInstance.jobInstanceId}"
              class="list-group-item job-instance" data-job-instance="${jobInstance}">${jobInstance.jobInstanceId} ${jobInstance.jobName}</a>
          </c:forEach>
        </div>
      </div>
    </div>
  </div>
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
