package org.nuxeo.sample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.io.Writer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.work.AbstractWork;
import org.nuxeo.runtime.api.Framework;

public class CSVReportWork extends AbstractWork {

    private static final Log log = LogFactory.getLog(CSVReportWork.class);
//    public static final int DEFAULT_BATCH_SIZE = 20;

    protected String repositoryName;
    protected String nxqlQuery;
    protected File outputFile;
//    protected int batchSize = DEFAULT_BATCH_SIZE;

    public CSVReportWork(String repositoryName, String nxqlQuery) {
        this.repositoryName = repositoryName;
        this.nxqlQuery = nxqlQuery;
    }

    @Override 
    public String getTitle() {
        return "CSV Report work";
    }

    @Override
    public void work() {
        log.info("HI from csv report worker");
        setProgress(Progress.PROGRESS_INDETERMINATE);

        openSystemSession();
        IterableQueryResult result = session.queryAndFetch(nxqlQuery, "NXQL");

        setStatus("Generating csv report");
        log.info("csv report will contain " + result.size() + " records");
        try {
            outputFile = Framework.createTempFile("report",".csv");
        } catch (IOException e) {
        }

        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("id", "name", "type","parentId").withRecordSeparator('\n');

        if (outputFile != null) {
            try ( 
                  Writer writer = new BufferedWriter(new FileWriter(outputFile));
                  CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)
                ) {

                for (Map<String, Serializable> map : result) {
                    String docId = (String) map.get("ecm:uuid");
                    DocumentModel d = session.getDocument(new IdRef(docId));

                    String docName = d.getProperty("dc:title").toString();
                    String docType = d.getType();
                    String parentId = d.getParentRef().toString();

                    csvPrinter.printRecord(docId,docName,docType,parentId);
                }
                csvPrinter.flush();
                csvPrinter.close();

            } catch (IOException e) {
            }
            result.close();
            setStatus("Done");
            log.info("csv report worker done,  file is at " + outputFile);
        }

    }

}

