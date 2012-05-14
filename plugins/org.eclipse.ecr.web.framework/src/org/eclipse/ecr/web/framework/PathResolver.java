/*
 * Copyright (c) 2006-2011 Nuxeo SA (http://nuxeo.com/) and others.
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

import javax.servlet.http.HttpServletRequest;

/**
 * @author bstefanescu
 *
 */
public class PathResolver {
	
	protected String rootUrl;
	protected String skinUrl;
	protected String baseUrl;
	protected String basePath;
	protected int shift;
	
	protected PathResolver() {
		
	}

	public PathResolver(String rootUrl, String skinPath, String baseUrl, String basePath, int shift) {
		this.rootUrl = normalizeUrl(rootUrl);
		this.baseUrl = normalizeUrl(baseUrl);
		this.basePath = normalizePath(basePath);
		this.skinUrl = appendPath(rootUrl, normalizePath(skinPath));
		this.shift = shift;
	}	

	/**
	 * The server URL - can be used to make absolute request in canonical form
	 * @return
	 */
	public String getRootUrl() {
		return rootUrl;
	}
	
	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}
	
	/**
	 * @return the skinUrl
	 */
	public String getSkinUrl() {
		return skinUrl;
	}
	
	public String getUrlForAbsolutePath(String absPath) {
		return appendPath(baseUrl, absPath);
	}
	
	/**
	 * Get the absolute path of a sub-resource. Ex: "/ecr/root/resource".
	 * The path argument represent the JAX-RS path of the resource to resolve. Ex: "root/resource"
	 * @param path
	 * @return
	 */
	public final String getPath(String path) {
		String p;
		if (basePath == "") {
			p = shift > 0 ? shiftPath(path, shift) : path;
		} else {
			p = shift > 0 ? appendPath(basePath, shiftPath(path, shift)) : appendPath(basePath, path);
		}
		return normalizePath(p);
	}
	
	public final String getUrl(String path) {
		return appendPath(baseUrl, getPath(path));
	}

	@Override
	public String toString() {
		return "ROOT_URL: "+rootUrl+";\nSKIN_URL: "+skinUrl+";\nBASE_URL: "+baseUrl+";\nBASE_PATH: "+basePath+";\nshift: "+shift;
	}
	
	/** Path mapping helpers */
	
	public final static String normalizeUrl(String url) {
		return url.endsWith("/") ? url.substring(0, url.length()-1) : url; 
	}
	
	/**
	 * Normalize the given path:
	 * if path is null or "" or "/" returns ""
	 * else remove trailing slash and append leading slash if none
	 * @param path
	 * @return
	 */
	public final static String normalizePath(String path) {
		if (path == null || path.length() == 0 || "/".equals(path)) {
			return "";
		}
		if (path.endsWith("/")) {
			path = path.substring(0, path.length()-1);
			return path.startsWith("/") ? path : "/".concat(path);
			//return path.startsWith("/") ? path.substring(0, path.length()-1) : :  
		} else if (!path.startsWith("/")) {
			return "/".concat(path);
		} else { // already normalized
			return path;
		}
	}
	
	/**
	 * Append path to basePath.
	 * @param basePath
	 * @param path
	 * @return
	 */
	public final static String appendPath(String basePath, String path) {
		if (basePath == null || basePath.length() == 0) {
			return path;
		}
		if (basePath.endsWith("/")) {
			if (path.startsWith("/")) {
				return basePath.concat(path.substring(1));
			} else {				
				return basePath.concat(path);
			}
		} else {
			if (path.startsWith("/")) {
				return basePath.concat(path);
			} else {
				return new StringBuilder().append(basePath).append('/').append(path).toString();
			}
		}
	}
	
	/**
	 * Shift the given path with 'shift' segments count. (shifting is done on left).
	 * Example: Using a shift of 2: /c/domain/q => /q
	 * 
	 * @param path
	 * @param shift
	 * @return
	 */
	public final static String shiftPath(String path, int shift) {
		int s = path.startsWith("/") ? 1 : 0;
		for (int i = 0; i<shift; i++) {
			int e = path.indexOf('/', s);
			if (e == -1) {
				return "/";
			}
			s = e+1;
		}
		return s > 1 ? path.substring(s-1) : path;		
	}
		
	
	public static final String getDefaultBaseUrl(HttpServletRequest req, String defBasePath) {
		String scheme = req.getScheme();
		int port = req.getServerPort();
		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(req.getServerName());
		if ((scheme.equals ("http") && port != 80)
				|| (scheme.equals ("https") && port != 443)) {
			url.append (':');
			url.append (req.getServerPort ());
		}
		if (defBasePath != null) {
			url.append(normalizePath(defBasePath));
		}
		return url.toString();
	}

	public static PathResolver createDefault(HttpServletRequest request, 
			String rootResourcePath, String skinPath) {
		PathResolver b = new PathResolver();
	    b.baseUrl = getDefaultBaseUrl(request, null);
		b.basePath = normalizePath(appendPath(
				normalizePath(request.getContextPath()), 
				normalizePath(request.getServletPath())));
		b.shift = 0;
		b.rootUrl = normalizeUrl(appendPath(appendPath(b.baseUrl, b.basePath), rootResourcePath));
		b.skinUrl = appendPath(b.rootUrl, skinPath);
		return b;
	}
	
	public static PathResolver create(HttpServletRequest request, 
			String rootResourcePath, String skinPath, 
			String rootUrlHeader, String baseUrlHeader) {
		String proxyUrl = request.getHeader(baseUrlHeader);
		int p;
		if (proxyUrl == null || (p = proxyUrl.indexOf("://")) == -1) {
		    // no proxy or invalid proxy header -> guess paths from request
			return createDefault(request, rootResourcePath, skinPath);
		} else {
			PathResolver b = new PathResolver();
			proxyUrl = normalizeUrl(proxyUrl);			
			// compute shift and basePath
			p = proxyUrl.indexOf('/', p+3);
			if (p > -1) {
				b.baseUrl = proxyUrl.substring(0, p);
				// compute shift and remove  trailing /..
				int i = proxyUrl.indexOf("..", p+1); // ..
				if (i > -1) {
					b.shift = (proxyUrl.length() - i + 1) / 3;
					b.basePath = proxyUrl.substring(p, i-1);
				} else {
					b.shift = 0;
					b.basePath = proxyUrl.substring(p);
				}
			} else {
				b.baseUrl = proxyUrl;
				b.basePath = "";
				b.shift = 0;
			}			
			String rootUrl = request.getHeader(rootUrlHeader);
			if (rootUrl == null) {
				b.rootUrl = b.baseUrl;
			} else {
				b.rootUrl = normalizeUrl(rootUrl);
			}
			b.skinUrl = appendPath(b.rootUrl, skinPath);
			return b;
		}
	}

}
