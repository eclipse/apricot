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
 *
 * $Id$
 */

package org.eclipse.ecr.core.api.model;


/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class PropertyRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -4188122987347256698L;

    public PropertyRuntimeException(String message) {
        super(message);
    }

    public PropertyRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
