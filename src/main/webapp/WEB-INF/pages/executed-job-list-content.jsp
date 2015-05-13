<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="container-fluid">
  <div class="row">
    <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      <div class="col-md-6">
        <table class="table">
          <thead>
            <tr>
              <th>#</th>
              <th>Job Name</th>
              <th>Status</th>
            </tr>
          </thead>
          <c:forEach items="${executedjoblist}" var="jobname">
              <tbody>
                <tr>
                  <td>1.</td>
                  <td>${jobname}</td>
                </tr>
              </tbody>
          </c:forEach>
        </table>
      </div>
    </div>
  </div>
</div>