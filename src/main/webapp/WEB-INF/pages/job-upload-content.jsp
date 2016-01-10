<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="col-md-10 col-md-offset-1 main">
  <h3 class="page-header">Job Execution Detial</h3>
  <div class="col-md-10 well">
    <p class="lead">Upload your own Job contexts. Uploaded jobs then will be ready for execution.</p>
    <form:form modelAttribute="file" action="/java-batch-manager/file-upload" method="post"
      enctype="multipart/form-data">
      <div class="col-lg-8 col-sm-8 col-12">
        <div class="input-group">
          <span class="input-group-btn"> <span
            class="btn btn-default btn-file"> Browse&hellip;<form:input
                path="filePart" type="file" name="file" />
          </span>
          </span>
          <form:input path="name" type="text" class="form-control"
            readonly="true" />
        </div>
      </div>
      <button class="btn btn-default" type="submit">Save file</button>
    </form:form>
  </div>
</div>