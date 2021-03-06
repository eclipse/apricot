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

import javax.ws.rs.GET;

import org.eclipse.ecr.automation.AutomationService;
import org.eclipse.ecr.automation.OperationContext;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
public class ChainResource extends ExecutableResource {

    protected final String chainId;

    public ChainResource(AutomationService service, String chainId) {
        super(service);
        this.chainId = chainId;
    }

    @GET
    public Object doGet() { // TODO
        return null;
    }

    @Override
    public Object execute(ExecutionRequest xreq) throws Exception {
        OperationContext ctx = xreq.createContext(request, getCoreSession());
        // Copy params in the Chain context
        ctx.putAll(xreq.getParams());
        return service.run(ctx, chainId);
    }

    @Override
    public String getId() {
        return chainId;
    }

    @Override
    public boolean isChain() {
        return true;
    }
}
