/*
 * Copyright (c) 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     bstefanescu
 */
package org.eclipse.ecr.core.storage.sql.coremodel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.ecr.core.api.impl.blob.StreamingBlob;
import org.eclipse.ecr.core.storage.sql.Binary;
import org.eclipse.ecr.testlib.NXRuntimeTestCase;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class TestRemotableBlob extends NXRuntimeTestCase {


    protected SQLBlob createSQLBlob() throws Exception {
        File file = File.createTempFile("nuxeo-test-", ".blob");
        file.deleteOnExit();
        Binary binary = new Binary(file, "abc");
        return new SQLBlob(binary, "test.txt", "text/plain", "UTF-8", "abc");
    }

    public void testSerialization() throws Exception {
        SQLBlob blob = createSQLBlob();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(blob);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bais);
        Object obj = in.readObject();

        assertTrue(obj instanceof StreamingBlob);
        StreamingBlob sblob = (StreamingBlob)obj;

        assertEquals(sblob.getFilename(), blob.getFilename());
        assertEquals(sblob.getDigest(), blob.getDigest());
        assertEquals(sblob.getEncoding(), blob.getEncoding());
        assertEquals(sblob.getLength(), blob.getLength());
        assertEquals(sblob.getMimeType(), blob.getMimeType());
    }

}
