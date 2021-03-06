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
 * $Id: LifeCycleException.java 19491 2007-05-27 13:51:18Z sfermigier $
 */

package org.eclipse.ecr.core.lifecycle;

/**
 * Life cycle exception.
 *
 * @author <a href="mailto:ja@nuxeo.com">Julien Anguenot</a>
 */
public class LifeCycleException extends Exception {

    private static final long serialVersionUID = 1L;

    public LifeCycleException(String message) {
        super(message);
    }

    public LifeCycleException(String message, Throwable cause) {
        super(message, cause);
    }

}
