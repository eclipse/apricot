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

package org.eclipse.ecr.runtime.service.sample;

import org.eclipse.ecr.runtime.service.AdapterManager;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
public class Main {

    public static void main(String[] args) {
        AdapterManager mgr = AdapterManager.getInstance();
        mgr.registerAdapter(new Service2Adapter());

        Service1Impl s1 = new Service1Impl();
        Service2 s2 = s1.getAdapter(Service2.class);
        s1.m1();
        s2.m2();
    }

}
