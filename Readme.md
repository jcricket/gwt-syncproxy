## Updates

Work is currently in progress to support Android to GAE login support using OAuth2 Credentials, specifically the Cross Client Identification. See the (in progress) [OAuth 2.0 App Engine Authentication](https://github.com/jcricket/gwt-syncproxy/wiki/OAuth-2.0-App-Engine-Authentication) wiki for details.

# GWT-SyncProxy (GSP)

[Google-Web-Toolkit (GWT)](http://www.gwtproject.org/) uses asynchronous RPC's between client (e.g Browser) and RemoteService servlet. Written in Java, the client-side code is converted to JS when compiled. The client side uses dynamically created Proxy's to handle method calls and serialization back and forth with the server. As such, there is no library within the GWT project to make these method calls from Java that is not translated. GWT-SyncProxy fills that gap.

GWT-SyncProxy provides asynchronous and synchronous RPC between a Java client and RemoteService servlet. By using SyncProxy, we can invoke the GWT RemoteService methods from pure Java (no JSNI) code.

The GWT SyncProxy Android library provides the same system for calling RPC's from Android in the same way the SyncProxy library allows calls to be made from pure Java. There are a few objects that can't yet be serialized properly, but aside from those the library allows GWT style access to GWT RPC servlets. Android API 14(Ice Cream Sandwich) and higher is required to utilize the Google App Engine Login Utilities.

Both libraries also provide Utility methods for providing and using login credentials against App Engine hosted GWT services.

## Status and Contribution

This project is currently (0.5) working with GWT 2.7.0 as a base, and has some support back to GWT 2.2 with previous versions of SyncProxy.

This project is under new management (effective Feb 2014). If you would like to contribute to this project, please open a new issue (Contrib-Request) to get added on to this project.

## Usage Example

GWT SyncProxy allows you to make both Synchronous and Asynchronous calls from a Java or Android application. While most will still prefer the Async style utilized in GWT for the majority of RPC calls, there is a specific scenario where the synchronized method comes in handy. This is for testing GWT services in unit tests. Please see [Sebastian Gurin's Blog](http://cancerberonia.blogspot.com/2012/10/testing-gwt-service-classes.html) for details and examples. Below is a quick example of what SyncProxy allows you to do from a Java or Android application (Android is slightly more complex as it needs to be run off the main thread: See Android wiki).
```java
SyncProxy.setBaseUrl("http://myapp.appspot.com/module");
// Normal Async Call Handling
GreetingServiceAsync greetingServiceAsync = SyncProxy.create(GreetingService.class);
greetingServiceAsync.greet("Hello", new AsyncCallback<String>(){
    void onFailure(Throwable throwable){
        // TODO Handle exception
    }
    void onSuccess(String value){
        // Handle return value
    } 
}
// Synchronous calls
GreetingService greetingService = SyncProxy.createSync(GreetingService.class);
String value = greetingService.greet("Hello");
```

## Developers

Are you using SyncProxy in your Project? Let us know so we can generate a list of projects utilizing the SyncProxy Project. Let us know which Library (Java/Android) and a website if you have it!

## Changes

The latest ReleaseNOtes are available up to version 0.4. The Roadmap is a work in progress and currently targeting version 0.6. Check the CommonIssues wiki for a list of problems that may occur and how to resolve them before submitting a ticket. You may also post up to StackOverflow with the 'gwt-syncproxy' tag and we'll help you there.
