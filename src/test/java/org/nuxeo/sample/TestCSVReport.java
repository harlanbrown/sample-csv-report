package org.nuxeo.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(AutomationFeature.class)
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
//@Deploy("org.nuxeo.sample.sample-csv-report-core")
@Deploy({"org.nuxeo.sample.sample-csv-report-core","org.nuxeo.ecm.platform.content.template"})
public class TestCSVReport {

    @Inject
    protected CoreSession session;

    @Inject
    protected AutomationService automationService;

    @Before 
    public void setUp() {
        DocumentModel ws1 = session.createDocumentModel("/default-domain/workspaces", "ws1", "Workspace");
        ws1 = session.createDocument(ws1);

        DocumentModel doc = session.createDocumentModel("/default-domain/workspaces/ws1", "default", "File");
        doc = session.createDocument(doc);
        session.save();
    }

    @Test
    public void shouldCallTheOperation() throws OperationException {
        OperationContext ctx = new OperationContext(session);
        automationService.run(ctx, CSVReport.ID);
    }

    @Test
    public void shouldCallWithParameters() throws OperationException {
        final String query = "select * from Document";
        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        params.put("nxqlQuery", query);
        automationService.run(ctx, CSVReport.ID, params);
    }
}
