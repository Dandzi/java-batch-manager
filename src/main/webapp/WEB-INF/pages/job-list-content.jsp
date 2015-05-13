<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="container-fluid">
  <div class="row">
    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      <div class="col-md-6">
        <table class="table">
          <thead>
            <tr>
              <th>#</th>
              <th>Job Name</th>
              <th>Parameters</th>
              <th></th>
            </tr>
          </thead>
          <c:forEach items="${joblist}" var="job">
            <form:form modelAttribute="${job.jobName}" method="POST"
              action="job-list/${job.jobName}">
              <tbody>
                <tr>
                  <td>1.</td>
                  <td>${job.jobName}</td>
                  <td><form:input path="parameters"/></td>
                  <td><input type="submit" value="Launch" /></td>
                </tr>
              </tbody>
            </form:form>
          </c:forEach>
        </table>
      </div>
    </div>
  </div>
</div>