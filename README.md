### Sample CSV report
Generate a CSV listing of the results of an NXQL query

#### build using Maven
mvn install

move `target/sample-csv-report-core-1.0-SNAPSHOT.jar` to `nuxeo-server-9.10-tomcat/nxserver/bundles` 

Launch operation via cURL with nxqlQuery parameter
`curl -X POST 'http://localhost:8080/nuxeo/site/automation/Document.CSVReport' -u Administrator:Administrator -H 'content-type: application/json+nxrequest' -d '{"params":{"nxqlQuery":"select * from File"}}'`


The operation will launch a worker to create the report - this will appear in server.log:
```
2018-02-25 10:49:55,637 INFO  [CSVReportWork] HI from csv report worker                                  
2018-02-25 10:49:56,651 INFO  [CSVReportWork] 89991 
2018-02-25 10:50:23,042 INFO  [CSVReportWork] csv report worker done,  file is at /home/harlan/nuxeo-server-9.10-tomcat/tmp/report2066220076969485047.csv
```
