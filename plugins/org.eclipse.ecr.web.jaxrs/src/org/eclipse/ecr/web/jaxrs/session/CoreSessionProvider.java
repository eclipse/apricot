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
package org.eclipse.ecr.web.jaxrs.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.ecr.core.api.CoreSession;
import org.eclipse.ecr.core.api.repository.Repository;
import org.eclipse.ecr.core.api.repository.RepositoryManager;
import org.eclipse.ecr.runtime.api.Framework;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class CoreSessionProvider<REF extends SessionRef> {

    private static final Log log = LogFactory.getLog(CoreSessionProvider.class);

    protected Map<String, REF> sessions;


    protected CoreSessionProvider() {
        this.sessions = new HashMap<String, REF>();
    }

    /**
     * The HTTP request was consumed. Do any request level cleanup now.
     */
    protected abstract void onRequestDone(HttpServletRequest request);

    protected abstract REF createSessionRef(CoreSession session);

    public SessionRef[] getSessions() {
        return sessions.values().toArray(new SessionRef[sessions.size()]);
    }

    public SessionRef getSessionRef(HttpServletRequest request, String repoName) {
        REF ref = sessions.get(repoName);
        if (ref == null) {
            ref = createSessionRef(createSession(request, repoName));
            sessions.put(repoName, ref);
        }
        return ref;
    }

    public CoreSession getSession(HttpServletRequest request,
            String repoName) {
        return getSessionRef(request, repoName).get();
    }

    protected CoreSession createSession(HttpServletRequest request, String repoName) {
        try {
            return _createSession(request, repoName);
        } catch (Exception e) {
            throw new WebApplicationException(e, 500);
        }
    }

    protected CoreSession _createSession(HttpServletRequest request, String repoName) throws Exception {
        if (request.getUserPrincipal() == null) {
            throw new java.lang.IllegalStateException("Not authenticated user is trying to get a core session");
        }
        RepositoryManager rm = Framework.getService(RepositoryManager.class);
        Repository repo = null;
        if (repoName == null) {
            repo = rm.getDefaultRepository();
        } else {
            repo = rm.getRepository(repoName);
        }
        if (repo == null) {
            //TODO use custom exception
            throw new java.lang.IllegalStateException("Unable to get " + repoName
                    + " repository");
        }
        return repo.open();
    }

    public boolean hasSessions() {
        return !sessions.isEmpty();
    }

    protected void destroy() {
        for (SessionRef ref : getSessions()) {
            try {
                ref.destroy();
            } catch(Throwable t) {
                log.error("Failed to destroy core session", t);
            }
        }
        sessions = null;
    }

}
