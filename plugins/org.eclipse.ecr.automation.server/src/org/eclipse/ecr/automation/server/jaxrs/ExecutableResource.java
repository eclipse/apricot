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
package org.eclipse.ecr.automation.server.jaxrs;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;

import org.eclipse.ecr.automation.AutomationService;
import org.eclipse.ecr.automation.core.util.BlobList;
import org.eclipse.ecr.automation.server.AutomationServer;
import org.eclipse.ecr.core.api.Blob;
import org.eclipse.ecr.core.api.CoreSession;
import org.eclipse.ecr.core.api.DocumentModel;
import org.eclipse.ecr.core.api.DocumentModelList;
import org.eclipse.ecr.core.api.DocumentRef;
import org.eclipse.ecr.web.jaxrs.session.SessionFactory;
import org.eclipse.ecr.runtime.api.Framework;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
public abstract class ExecutableResource {

    @Context
    protected HttpServletRequest request;

    protected AutomationService service;

    protected ExecutableResource(AutomationService service) {
        this.service = service;
    }

    public CoreSession getCoreSession() {
        return SessionFactory.getSession(request);
    }

    @POST
    public Object doPost(@Context HttpServletRequest request,
            ExecutionRequest xreq) {
        this.request = request;
        try {
            AutomationServer srv = Framework.getLocalService(AutomationServer.class);
            if (!srv.accept(getId(), isChain(), request)) {
                return ResponseHelper.notFound();
            }
            Object result = execute(xreq);
            if (result == null) {
                return null;
            }
            if ("true".equals(request.getHeader("X-NXVoidOperation"))) {
                return ResponseHelper.emptyContent(); // void response
            }
            if (result instanceof Blob) {
                return ResponseHelper.blob((Blob) result);
            } else if (result instanceof BlobList) {
                return ResponseHelper.blobs((BlobList) result);
            } else if (result instanceof DocumentRef) {
                return getCoreSession().getDocument((DocumentRef) result);
            } else if ((result instanceof DocumentModel)
                    || (result instanceof DocumentModelList)
                    || (result instanceof JsonAdapter)) {
                return result;
            } else { // try to adapt to JSON
                return new DefaultJsonAdapter(result);
            }
        } catch (Throwable e) {
            throw ExceptionHandler.newException("Failed to execute operation: "
                    + getId(), e);
        }
    }

    public abstract String getId();

    public abstract Object execute(ExecutionRequest req) throws Exception;

    public abstract boolean isChain();

}
