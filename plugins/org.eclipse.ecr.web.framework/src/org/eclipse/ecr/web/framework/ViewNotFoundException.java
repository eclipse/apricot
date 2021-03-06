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
 */
package org.eclipse.ecr.web.framework;

import javax.ws.rs.WebApplicationException;

/**
 * @author bstefanescu
 *
 */
public class ViewNotFoundException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public ViewNotFoundException() {
		super (404);
	}
}
