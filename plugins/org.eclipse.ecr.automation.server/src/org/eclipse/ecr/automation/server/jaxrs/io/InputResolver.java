/*
 * Copyright (c) 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     matic
 */
package org.eclipse.ecr.automation.server.jaxrs.io;

/**
 * @author matic
 *
 */
public interface InputResolver<T> {

    String getType();

    T getInput(String content);
}
