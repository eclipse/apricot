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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.ecr.runtime.api.Framework;

/**
 * @author bstefanescu
 *
 */
public abstract class AbstractResource {

	protected String path;
	
	protected String url;
	
	protected WebContext ctx;
	
	public AbstractResource(WebContext ctx) {
		initContext(ctx);
	}
	
	protected void addBreadcrumb(String name) {
		String path = getPath();
		if (path.length() == 0) {
			path = "/";
		}
		ctx.getBreadcrumb().add(path, name);
	}

	protected void addBreadcrumb(String url, String name) {
		ctx.getBreadcrumb().add(url, name);
	}

	protected void initContext(WebContext ctx) {
		this.ctx = ctx;
		UriInfo uriInfo = ctx.getUriInfo();
		if (uriInfo != null) {
			initPath();
		}
	}
	
	protected void initPath() {
		String resourcePath = ctx.uriInfo.getMatchedURIs().get(0);
		this.path = ctx.getPathResolver().getPath(resourcePath);
		this.url = PathResolver.normalizeUrl(PathResolver.appendPath(ctx.getPathResolver().getBaseUrl(), this.path));
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
	public WebContext getContext() {
		return ctx;
	}

	public View getView(String viewPath) {
		return new TemplateView(this, viewPath);
	}

	public View getView(String viewPath, Map<String, Object> args) {
		return new TemplateView(this, viewPath, args);
	}

	public void initBindings(Map<String, Object> args) {
		args.put("Application", ctx.getApplication());		
		args.put("This", this);
		args.put("Root", ctx.getRoot());
		args.put("Context", ctx);
		args.put("Runtime", Framework.getRuntime());
		args.put("Request", ctx.getRequest());
		args.put("appUrl", ctx.getRootUrl());
		args.put("baseUrl", ctx.getRoot().getUrl());
		args.put("basePath", ctx.getRoot().getPath());
		args.put("skinPath", ctx.getSkinPath());
		args.putAll(ctx.getProperties());
	}
	
	public final String getPath() {
		return path;
	}

	public WebApplication getApplication() {
		return ctx.getApplication();
	}
	
	
	/**
	 * Redirect to the given absolutePath and an optional queryString array.
	 * The queryString array contains in order the key, value elements to use.
	 * If the last query value is a key then "key=" will be generated
	 * @param absPath
	 * @param queryParams
	 * @return
	 * @throws URISyntaxException
	 */
	public Response redirect(String absPath, String ... queryParams) throws URISyntaxException, UnsupportedEncodingException {
		if (queryParams != null && queryParams.length > 0) {
			StringBuilder buf = new StringBuilder().append(absPath).append('?');
			for (int i=0; i<queryParams.length; i++) {
				buf.append(URLEncoder.encode(queryParams[i++], "UTF-8")).append('=');
				if (i<queryParams.length) {
					buf.append(URLEncoder.encode(queryParams[i++], "UTF-8")).append('&');	
				}				
			}
			buf.setLength(buf.length()-1);
			absPath = buf.toString();
		}
		URI uri = new URI(ctx.getPathResolver().getUrlForAbsolutePath(absPath));
		return redirect(uri);
	}
	
	public Response redirect(URI uri) {
		return Response.seeOther(uri).build();
	}

	/**
	 * Get the parent path at the given level.
	 * A level of 0 means the current object path (is similar to {@link #getPath()}).
	 * A level of 1 means the direct parent path and so on.
	 * @param level
	 * @return
	 */
	public String getParentPath(int level) {
		return level <=0 ? getPath() : new org.eclipse.ecr.common.utils.Path(getPath()).removeLastSegments(level).toString();
	}

	/**
	 * Shortcut for <code>getParentPath(1)</code>
	 * @return
	 */
	public String getParentPath() {
		return getParentPath(1);
	}

	/**
	 * Resolve a relative path from the current path (the one returned by {@link #getPath()})
	 * @param relativePath
	 * @return
	 */
	public String resolvePath(String relativePath) {
		return new org.eclipse.ecr.common.utils.Path(getPath()).append(relativePath).toString();
	}

}
