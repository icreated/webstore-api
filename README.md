# &int; created
> Integration created
[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=7TYVAGLZ7XATQ&source=url)

# Idempiere WebStore API
> 

This server side plugin is created to show how API REST can be used in OSGI Idempiere Environment.

It's based on Jersey API Framework, Java-JWT for security purposes & Swagger integration:

![WebStore Schema](/assets/images/ic_webstore_schema.png?raw=true "Webstore Schema")

This API provides following services:

![Swagger services](/assets/images/ic_webstore_swagger.png?raw=true "Swagger services")

It's quite enough to build a real frontend commercial site with.
As an example you can check an Angular project built on it: [https://github.com/icreated/webstore](https://github.com/icreated/webstore)


## Installing / Getting started

To build this plugin you need to get sources in your project directory:

```shell
git clone https://github.com/icreated/webstore-api.git
```
**Important!**
Edit pom.xml to link native Idempiere libraries with parent project.
If you put sources directly in Idempiere sources folder, it will be

```xml
<relativePath>../org.idempiere.parent/pom.xml</relativePath>
```
like others Idempiere plugins, otherwise modify it.

Build it with Maven:

```shell
mvn install
```
It works? 
If it's ok, then you are using Idempiere 7.

Otherwise, make sure you have all Jersey dependencies.
Important to outline that Idempiere (version 6.2) already contains some libraries from Jersey.

You can download missing dependencies manually from https://mvnrepository.com/.
Check the version. In my case it was 2.22.1

Just in case here are my Jersey dependencies, I hope this helps you:

![Jersey Dependencies](/assets/images/ic_webstore_plugins.png?raw=true "Jersey Dependencies")


### Deploying / Publishing / Testing

Check if plugin is working by getting OpenAPI description file:

[http://localhost:8080/services/api-docs/openapi.xml](http://localhost:8080/services/api-docs/openapi.xml)

To check and test REST services install Swagger plugin from here:

[https://github.com/icreated/swagger](https://github.com/icreated/swagger)

> Make sure that Swagger plugin points to http://localhost:8080/services/api-docs/openapi.xml 


## Contributing

If you'd like to contribute, please fork the repository and use a feature
branch. 

Pull requests are warmly welcome


## Licensing

GNU General Public License
