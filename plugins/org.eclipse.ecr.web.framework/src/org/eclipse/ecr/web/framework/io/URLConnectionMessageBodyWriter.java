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
package org.eclipse.ecr.web.framework.io;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URLConnection;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.ecr.common.utils.FileUtils;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 * 
 */
@Provider
public class URLConnectionMessageBodyWriter implements MessageBodyWriter<URLConnection> {

    private static final Log log = LogFactory.getLog(URLConnectionMessageBodyWriter.class);

    // @ResourceContext private HttpServletRequest request;

    @Override
    public void writeTo(URLConnection conn, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException {
        try {
        	java.io.InputStream in = conn.getInputStream();
        	try {
        		FileUtils.copy(in, entityStream);
        	} finally {
        		try {
        			entityStream.flush();
        		} finally {
        			in.close();
        		}
        	}
        } catch (Throwable e) {
            log.error("Failed to get resource: " + conn.getURL(), e);
            throw new IOException("Failed to get resource: " + conn.getURL(), e);
        }
    }

    @Override
    public long getSize(URLConnection conn, Class<?> arg1, Type arg2, Annotation[] arg3,
            MediaType arg4) {
        return conn.getContentLength();
    }

    @Override
    public boolean isWriteable(Class<?> arg0, Type type, Annotation[] arg2,
            MediaType arg3) {
        return URLConnection.class.isAssignableFrom(arg0);
    }

}
