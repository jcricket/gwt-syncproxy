#deprecated

## Prior to Version 0.5 ##

### Multiple sessions ###
Form version 0.3, SyncPoryy use standard Java java.net.CookieManager to manage client session.
```
CookieManager empSession = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
CookieManager mgrSession = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
GreetingService empRpcService =
  SyncProxy.newProxyInstance(GreetingService.class,
        "http://example.com/helloApp", "greet", empSession);
GreetingService mgrRpcService =
  SyncProxy.newProxyInstance(GreetingService.class,
        "http://example.com/helloApp", "greet", mgrSession);
```

### Invoke GWT RPC services deployed on AppEngine ###
For security-enabled RPC services deployed on AppEngine, we have to login before calling RPC methods.

```
CookieManager userSession = LoginUtils.loginAppEngine("https://example.appspot.com",
    "http://example.appspot.com/helloApp/greet",
    "emailaddress@gmail.com", "password");
```

Then invoke the RPC method:

```
private static GreetingService rpcService =
  SyncProxy.newProxyInstance(GreetingService.class,
        "http://example.appspot.com/helloApp", "greet", userSession);

String result = rpcService.greetServer("SyncProxy");
```