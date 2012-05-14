PathResolver mappings (to work with proxies)

1. No proxy
GET http://localhost:8080/ecr/app/{path}
rootUrl   = http://localhost:8080/ecr/app
basePath  = /ecr
baseUrl   = http://localhost:8080
shift     = 0
=> path(path) => {basePath}/{path}
=> url(path)  => {baseUrl}/{basePath}/{path}

2. Proxy: move all app paths to root
GET http://www.cannonic-host.com/{path} => http://localhost:8080/ecr/app/{path}
#X-ECR-BaseUrl: http://www.cannonic-host.com/..
#X-ECR-RootUrl: http://www.cannonic-host.com
rootUrl   = http://www.cannonic-host.com
basePath  = 
baseUrl   = http://www.cannonic-host.com
shift     = 1

3. Proxy: move only a section of the site to root in a subdomain. 
   The rest of the app is available through RootUrl (the cannonic address) 
GET http://{domain}.host.com/{path} => http://localhost:8080/ecr/app/a/{domain}/{path}
#X-ECR-BaseUrl: http://{domain}.host.com/../../..
#X-ECR-RootUrl: http://www.cannonic-host.com
rootUrl   = http://www.cannonic-host.com
basePath  =
baseUrl   = http://{domain}.host.com
shift     = 3

In all cases skins are exposed through RootUrl/skin 