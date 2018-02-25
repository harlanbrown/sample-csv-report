package org.nuxeo.sample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang3.StringUtils;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;

import org.nuxeo.ecm.core.work.api.WorkManager;

import org.nuxeo.runtime.api.Framework;

/**
 *
 */
@Operation(id=CSVReport.ID, category=Constants.CAT_DOCUMENT, label="CSVReport", description="Describe here what your operation does.")
public class CSVReport {

    private static final Log log = LogFactory.getLog(CSVReport.class);

    public static final String ID = "Document.CSVReport";

    public static final String DEFAULT_NXQL_QUERY = "SELECT * FROM Document WHERE ecm:mixinType = 'Picture' AND picture:views/*/title IS NULL";

    public String getNxqlQuery() {
        return nxqlQuery;
    }

    public void setNxqlQuery(String nxqlQuery) {
        this.nxqlQuery = nxqlQuery;
    }

    @Context
    protected CoreSession session;

    @Param(name = "nxqlQuery", required = false)
    protected String nxqlQuery = DEFAULT_NXQL_QUERY;

    @OperationMethod
    public void run() {

        WorkManager workManager = Framework.getService(WorkManager.class);

        if (workManager == null) {
            throw new RuntimeException("No WorkManager available");
        }

        if (!StringUtils.isBlank(nxqlQuery)) {
            CSVReportWork work = new CSVReportWork(session.getRepositoryName(), nxqlQuery);
            workManager.schedule(work);
        }

    }
}
