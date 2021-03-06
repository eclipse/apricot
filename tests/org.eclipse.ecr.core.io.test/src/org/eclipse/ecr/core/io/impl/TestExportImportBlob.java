/*
 * Copyright (c) 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 *
 * $Id: TestExportedDocument.java 29029 2008-01-14 18:38:14Z ldoguin $
 */

package org.eclipse.ecr.core.io.impl;

import org.eclipse.ecr.core.api.Blob;
import org.eclipse.ecr.core.api.DocumentModel;
import org.eclipse.ecr.core.api.DocumentModelList;
import org.eclipse.ecr.core.api.impl.blob.StringBlob;
import org.eclipse.ecr.core.io.DocumentWriter;
import org.eclipse.ecr.core.io.ExportedDocument;
import org.eclipse.ecr.core.io.impl.plugins.DocumentModelWriter;
import org.eclipse.ecr.core.storage.sql.testlib.SQLRepositoryTestCase;

public class TestExportImportBlob extends SQLRepositoryTestCase {

    DocumentModel rootDocument;

    DocumentModel workspace;

    DocumentModel docToExport;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        deployBundle("org.eclipse.ecr.core.api");

        openSession();
    }

    private void createDocs() throws Exception {
        rootDocument = session.getRootDocument();
        workspace = session.createDocumentModel(rootDocument.getPathAsString(),
                "ws1", "Workspace");
        workspace.setProperty("dublincore", "title", "test WS");
        workspace = session.createDocument(workspace);

        docToExport = session.createDocumentModel(workspace.getPathAsString(),
                "file", "File");
        docToExport.setProperty("dublincore", "title", "MyDoc");

        Blob blob = new StringBlob("SomeDummyContent");
        blob.setFilename("dummyBlob.txt");
        blob.setMimeType("text/plain");
        docToExport.setProperty("file", "content", blob);

        docToExport = session.createDocument(docToExport);

        session.save();
    }

    public void testBlobFilenamePresent() throws Exception {
        createDocs();

        ExportedDocument exportedDoc = new ExportedDocumentImpl(docToExport,
                true);
        assertEquals("File", exportedDoc.getType());

        session.removeDocument(docToExport.getRef());
        session.save();
        assertEquals(0, session.getChildren(workspace.getRef()).size());

        DocumentWriter writer = new DocumentModelWriter(session,
                rootDocument.getPathAsString());
        writer.write(exportedDoc);

        DocumentModelList children = session.getChildren(workspace.getRef());
        assertEquals(1, children.size());
        DocumentModel importedDocument = children.get(0);
        Blob blob = (Blob) importedDocument.getProperty("file", "content");
        assertEquals("dummyBlob.txt", blob.getFilename());
    }

}
