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
package org.eclipse.ecr.web.jaxrs.tx;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.ecr.web.jaxrs.HttpFilter;
import org.eclipse.ecr.runtime.transaction.TransactionHelper;

/**
 * Filter using the {@link SimpleLoginModule} to authenticate a request.
 *
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class TransactionFilter extends HttpFilter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void run(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        boolean txStarted = false;
        if (!TransactionHelper.isTransactionActive())  {
            if (TransactionHelper.startTransaction()) {
                txStarted = true;
            }
        }
        try {
            chain.doFilter(request, response);
        } finally {
            if (txStarted) {
                TransactionHelper.commitOrRollbackTransaction();
            }
        }
    }

    @Override
    public void destroy() {
    }

}
