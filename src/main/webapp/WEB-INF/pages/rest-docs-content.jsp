<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="col-md-10 col-md-offset-1 main">
 <h3 class="page-header">REST api documentation</h3>
 <div class="well">
  <div class="table-responsive">
   <table class="table table-striped">
    <thead>
     <tr>
      <th>#</th>
      <th>Method Name</th>
      <th>URI</th>
      <th>Method Type</th>
      <th>Method Parameters</th>
      <th>Return Parameter</th>
      <th>Description</th>
     </tr>
    </thead>
    <tbody>

     <tr>
      <td>1</td>
      <td>launch</td>
      <td>/rest/launch</td>
      <td>POST</td>
      <td>JobInstRest</td>
      <td>JobExecutionDto</td>
      <td>This method launches job execution.</td>
     </tr>

     <tr>
      <td>2</td>
      <td>stop</td>
      <td>/rest/stop</td>
      <td>POST</td>
      <td>JobWrapperForRest</td>
      <td>JobExecutionDto</td>
      <td>This method stops job execution.</td>
     </tr>
     
     <tr>
      <td>3</td>
      <td>restart</td>
      <td>/rest/restart</td>
      <td>POST</td>
      <td>JobWrapperForRest</td>
      <td>JobExecutionDto</td>
      <td>This method restart job execution.</td>
     </tr>
     
    <tr>
      <td>4</td>
      <td>getJobNames</td>
      <td>/rest/jobnames</td>
      <td>GET</td>
      <td>JobWrapperForRest</td>
      <td>Set&ltString&gt</td>
      <td>This method returns all job names of registered jobs.</td>
     </tr>
     
     <tr>
      <td>5</td>
      <td>getJobExecById</td>
      <td>/rest/execution</td>
      <td>GET</td>
      <td>JobWrapperForRest</td>
      <td>JobExecutionDto</td>
      <td>This method returns JobExecutionDto by id.</td>
     </tr>
     
     <tr>
      <td>6</td>
      <td>getRunningJobExecutions</td>
      <td>/rest/running/all</td>
      <td>GET</td>
      <td>JobWrapperForRest</td>
      <td>List&ltJobExecutionDto&gt</td>
      <td>This method returns List of all running JobExecutionDto's.</td>     
     </tr>

     <tr>
      <td>7</td>
      <td>getRunningJobExecutionsByJobName</td>
      <td>/rest/running/name</td>
      <td>GET</td>
      <td>JobWrapperForRest</td>
      <td>Set&ltJobExecutionDto&gt</td>
      <td>This method returns Set of all running JobExecutionDto of one Job.</td>     
     </tr>
     
     <tr>
      <td>8</td>
      <td>getJobInstancesDto</td>
      <td>/rest/instances</td>
      <td>GET</td>
      <td>JobInstRest</td>
      <td>List&ltJobInstanceDto&gt</td>
      <td>This method returns Instances of given jobName from id start. The maximum size of list is defined by count.</td>     
     </tr>
     
    </tbody>
   </table>
  </div>

  <h3>JSON objects formats examples.</h3>

  <div class="row">
   <div class="col-md-8">
    <h4>JobInstRest</h4>
    <div class="json">{"jobName":"test", "parameters":"param1=value1", "start":"0", "count":"10","jobType":"SPRINGBATCH"}</div>
   </div>
  </div>

  <div class="row">
   <div class="col-md-8">
    <h4>JobWrapperForRest</h4>
    <div class="json">{"jobName":"test","jobType":"SPRINGBATCH", "idOfJob":"0"}</div>
   </div>
  </div>

  <div class="row">
   <div class="col-md-8">
    <h4>JobExecutionDto</h4>
    <div class="json">{"jobExecutionId":0, "jobName":"test","parameters":"{param1=value1}", "createTime":1450798766433,"startTime":null,"status":"STARTING",
     "stop":false,"isRestartable":false,"jobType":"spring","idAndType":null, "stepExecutionsDto":null}</div>
   </div>
  </div>

  <div class="row">
   <div class="col-md-8">
    <h4>List&ltJobExecutionDto&gt</h4>
    <div class="json">[{"jobExecutionId":0,"jobName":"test","parameters":"{param1=value1}","createTime":1450802449162,"startTime":1450802449220,"status":"STARTED",
    "stop":false,"isRestartable":false,"jobType":"spring","idAndType":null,
    "stepExecutionsDto":[{"id":2,"stepName":null,"persistentUserData":null,"readerCheckpointInfo":null,"writerCheckpointInfo":null,"exception":null,
     "startTime":1450802808605,"endTime":null,"exitStatus":"EXECUTING"},{"id":1,"stepName":null,"persistentUserData":null,"readerCheckpointInfo":null,
     "writerCheckpointInfo":null,"exception":null,"startTime":1450802451760,"endTime":1450802808587,"exitStatus":"COMPLETED"}]}]
    </div>
   </div>
  </div>


 </div>
</div>