MockHttpServer
==============

Devs/QAs who want to test a REST client which is under development face difficulties to write automation code for it because  
they cannot run their tests to verify if there test code is bug free.

MockHttpServer has the intention to solve this problem. They would be able to configure expected responses against  
requests and start a server at a preconfigured host url and port.And thereafter they can point their test code to this mocked http server and make sure they have a working automation code.

This tool can also be used by devs to test their prod rest client or it can be used to stub out external http services.

Instructions to build from source
=================================

* clone the git repository
* checkout tag version v1.0
* run `gradle clean build oneJar` from root directory. In build/libs there will be MockHttpServer-standalone.jar created.  
* run `java -jar MockHttpServer-standalone.jar --help` to see the usage instructions on command line

Prebuilt Jars
=============

Clone this github repository and the two prebuilt jars **mock-http-server-1.0.jar** (api) and  
**MockHttpServer-standalone.jar** (standalone) can be found under `<CLONED_DIRECTORY>/artifacts/v1`            


Usage Instructions from command line
====================================

`java -jar MockHttpServer-standalone.jar [OPTION...]`

Options:  
  -b, --basePath  <arg>   (base path to expectations root folder)    
  -p, --port  <arg>       (port to start the server (default = 8080))
  
Expectations
============

basePath is the path to the root folder where all expectations are kept

the directory structure should be
```
--expectations
  |--request
  |    |-someRequest1.request   
  |    |-someRequest2.request   
  |--response
  |    |-someResponse1.response
  |    |-someResponse2.response
  |-get.expectations
  |-post.expectations
  |-put.expectations
  |-delete.expectations
```
* expectations is the root folder and basePath should point to expectations(name of the root folder is arbitrary)
* get.expectations, post.expectations, put.expectations and delete.expectations are optional files. If present, they can  
specify multiple expectations(described below) for the corresponding verbs.
* request and response are optional directories which will be described later
* The format of an expectation in an expectations file is 

``` *<url>~|<request body>|@<content type>-><response status code>|<response body>|@<content type> ```
where only 'url' and 'response status code>' are the only mandatory parameters. But to specifiy a request body  
or response body, the corresponding content type is mandatory.

* expectations in a file can be specified in the format mentioned below. For example - in post.expectations we can specify

```
*/foo1~->204
*/foo2~|{"name":"request","age":"10"}|@application/json->200
*/foo3~->200|{"name":"response","age":"10"}|@application/json
*/foo4~|{"name":"request","age":"10"}|@application/json->200|{"name":"response","age":"10"}|@application/json
*/foo5~|hi|@text/plain->200|bye|@text/plain
```
* Sometimes, it's difficult to put the request or response body with the expectations(xmls can be case). There is a provision  
to extract the request or response body into a file and point the expectations to the corresponding files.

e.g. - ``` */foo4~|file(someRequest1)|@text/xml->200|file(someResponse2)|@text/xml ```

To resolve the above expectations, the framework will try to look for 'someRequest1.request' file in request folder  
and 'someResponse2.response' file in response folder under root directory.

It's not mandatory to specifiy both request and response in the same style. They can be mix and matched like  
``` */foo4~|file(someRequest1)|@text/xml->200|my response|@text/plain ```

* url specified in an expectation supports standard regex format

* There is a special preconfigured GET '/reload' which will reload the server with the latest expectations. Which means,  
if you add/modify/delete expectations then you don't need to restart the server. Just fire a GET '/reload' on the server  
and the expectations from the root folder will be reloaded.
