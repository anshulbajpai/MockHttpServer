MockHttpServer
==============

QAs who want to test a REST client which is under development face difficulties to write automation code for it because they cannot run their tests to verify if there test code is bug free.

MockHttpServer has the intention to solve this problem for QAs. They would be able to configure expected responses against requests and start a server at a preconfigured host url and port.

And thereafter they can point their test code to this mocked http server and make sure they have a working automation code.

