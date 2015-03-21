

# Source Code Layout #

As of version 0.4, the layout of the system has been changed over into multiple Eclipse projects in order to reduce code duplication across the
Java library and the Android library. As such, there are 3 additional Eclipse projects that exist in order to help with
testing the source code: a Google App Engine/GWT project for hosting the test services and testing the services in GWT, a
sample Android app for manual testing, and an Android testing app to conduct automated testing of the library on Android.
All Eclipse launchers are stored in the `.settings` folder of the respective projects. On project import, they should be
directly available to you, check the Run Configurations manually in case they don't show up right away.

Traces of the prior build/test system, which was non-functional, have been removed as of version 0.5.

Additionally, the SyncProxy system can be used as an excellent tool for Synchronous testing of GWT Services. Please see this
[Sebastian Gurin's Blog](http://cancerberonia.blogspot.com/2012/10/testing-gwt-service-classes.html) for details.

## Dependencies ##

All testing projects are setup to draw the testing services directly from GWT source so that it can be automatically tested
against the latest version as GWT changes whenever possible. The current Android and POJ tests are slightly modified replicates of the GWT tests
at the 2.7.0 version (minor modifications were made to remove/modify the `GWTTestCase` dependency). As such, you must have a checkout of the entire google-web-toolkit trunk checked out
to the path `google-web-toolkit` in your workspace. Make sure to checkout the proper branch release version (eg. 2.7.0) AND change all project
dependencies to the same GWT version. When updating App Engine dependencies, make sure to copy the new versions _servlet-api.jar_ into the SPALibrary projects "lib" folder (this is found in the AppEngine Dependency module listing).
Additionally, you will need gdata checked-out to `gdata` within
your workspace as well (a future version will look to remove/upgrade this dependency: See [Roadmap](Roadmap.md)). The Android library is dependent on the included ~~gwt-servlet.jar~~ web-app's _servlet-api.jar_ library. While only a fraction of the sources in the gwt-user are needed for the
project, there is a lot of internal referencing that can cause errors if not made available. This is compensated for by using a modified version of
a couple of the gwt-user source files to break the referencing chain.

Checking out the entire trunk with all necessary dependencies should give you a layout workspace similar to the following:

```
Workspace\
Workspace\gdata
Workspace\google-web-toolkit
Workspace\gwt-syncproxy
Workspace\SPAAppTest
Workspace\SPALibrary
Workspace\SPWebTest
```

All paths within the Eclipse projects are path dependent on the `WORKSPACE_LOC` variable, so make sure it is set properly to avoid
build path problems. The **google-web-toolkit** dependencies for are primarily for testing purposes as the general **gwt-syncproxy** library
will function with the _gwt-user.jar_ in place. It is also utilized for drawing source code into the Andorid library.
However, the **gdata** dependencies are utilized directly in the library, so those will be needed
in production as well. See the .project and .classpath files to note the appropriate dependencies. A future version will upgrade this system to
utilize Gradle (see [Roadmap](Roadmap.md)) and hopefully eliminate the strict dependency on Eclipse.

For simplicity, the SPWebTest project also makes use of a single user-defined library called AppEngineTesting. It will need to reference the following
jars which can likely be found in your `eclipse\plugins` path:

```
junit.jar - org.junit_4.10.0.v4_10_0_v20120426-0900 (or acceptable alternative)
appengine-testing.jar - com.google.appengine.eclipse.sdkbundle_1.9.17\appengine-java-sdk-1.9.17\lib\testing
appengine-api.jar - com.google.appengine.eclipse.sdkbundle_1.9.17\appengine-java-sdk-1.9.17\lib\impl
appengine-api-labs.jar - com.google.appengine.eclipse.sdkbundle_1.9.17\appengine-java-sdk-1.9.17\lib\impl
appengine-stubs.jar - com.google.appengine.eclipse.sdkbundle_1.9.17\appengine-java-sdk-1.9.17\lib\impl
```

### GWT, GAE, and Android ###

The current library (0.5) has been compiled/tested against GWT 2.7.0, GAE 1.9.17, and Android V21 (Lollipop). Testing against newer versions should be as simple
as updating the library references within each appropriate project and updating the checked-out source code for GWT and GData. However, there
may be some complications that arise with newer versions from GWT source as subtle changes in classes referenced may render the build paths
incomplete.

If you are utilizing the Android library, the account management system is only available on a Google APIs Android Virtual Device for testing.
It is recommended that you create and utilize an AVD with as much memory as your system will accept (Windows may max at about 768mb)
and a VM Heap of at least 64 to help with testing. Additionally, the Android library comes with the ability to send fake accounts while testing
against a local GAE dev server. Please see the [Android](Android.md) wiki doc for details.

Alternatively, as of version 0.5, you can use a hardware device to conduct testing against any specified URL for the server.

## Service Hosting ##

The **SPWebTest** project hosts an AppEngine project that serves several of the gwt-user test services for both the Java and Android client
to send tests to, generally testing serialization of different formats and types. In order to utilize the hosting server:

  1. GWT-Compile the **SPWebTest** project
  1. Launch the `SPAWebTest` launcher
  1. Open the local URL to startup the module (may take a few minutes to load the first time...a future upgrade will support SuperDevMode)

## Java Library ##

In order to use the Java-only Library and testing, you need only get two projects from the source, **gwt-syncproxy** and **SPWebTest**.
There are two sets of tests contained between these two projects: one to test the hosted services (just to make sure nothing
has changed), and one to test the library from a Java-stand alone. The gwt hosting tests can be run utilizing the `SPGWTTest-dev`
launcher and the Java stand alone tests can be launched using the `PojGSPTests` launcher.

## Android Library ##

The Android library requires all of the source hierarchy described above [SourceAndTesting#Dependencies](SourceAndTesting#Dependencies.md). Additionally, there is an
integrated project that will need to be imported into eclipse within the `SPAAppTest/tests/` path. This contains the source
for the **SPAAppTester** project, which conducts the Android JUnit testing using the **SPAAppTest** app as a base. The android tests
can be launched using the `SPATests` launcher. The actual app for manual testing can be launched using the `SPA_App` launcher.

# Creating Tests #

This portion of the document will review the process needed to add more tests throughout the system. All testing within the system is
currently done utilizing the JUnit structures, and there are 3 separate locations to add tests.

## Testing Against a Service ##

If your test will be run against a service, there are a number of steps to ensure success without compile/run-time issues.

  1. Create the service in the **SPWebTest** project
  1. Even if it is not utilized manually, make sure it is referenced in the `client/SPAWebTest` class so that it's service handle will be serialized for retrieval.
  1. Add the servlet handlers to the `web.xml`
  1. GWT-Compile the SPWebTest project
  1. Create a test within the `test/.../client/gwttests` path. Be sure to properly utilize the GWTTestCase finishTest() and delayTestFinish() methods for Asynchronous testing.
  1. Add this test to the `/gwttests/GwtTests` TestSuite class.

## Java Stand Alone Test ##

> You can utilize the same testing structure for the stand along test, but you will need to modify the source-copied to work outside the GWTTestCase
structure.

  1. Copy the test case into the **gwt-syncproxy** project `test/.../test/poj/` path.
  1. Convert the test from a GWTTestCase to a TestCase
  1. (Recommended not required) Create the SyncProxy service statically in the test case.
  1. Convert the finishTest()/delayTestFinish() to utilize a CountDownLatch. This can be easily done across a large test case with the following search/replacements
    * If you have a standard method start, like _service = GWT.create(...)_, Replace With _`final CountDownLatch signal = new CountDownLatch(1);`_
    * Search _`finishTest()`_ Replace with _`signal.countDown()`_
    * Search _`delayTestFinish(2000)`_ Replace with _`assertTrue("Failed to Complete", signal.await(2, TimeUnit.SECONDS))`_
  1. Resolve any pending issues
  1. Add this testcase to `PojGSPTests` TestSuite class

## Android AppTester Test - _See Below for Update_ ##

> As with the Java Stand Alone Testing, you can utilize the converted Java SA test for the Android Testing system. This requires some additional
> subtle modification since Android requires network access to be completed off the main thread. We do this with the AsyncTask.

  1. Copy the converted test case in the Java Stand Alone to the **SPAAppTester** `test/gwttests` path.
  1. Convert TestCase to `ActivityInstrumentationTestCase2<MainActivity>`
  1. Connect to the service in the Constructor using the following pattern:
    * Take note of the url path that is connected to. Android uses the 10.0.2.2 url to access the locally hosted webserver at 127.0.0.1.
```
  		final CountDownLatch signal = new CountDownLatch(1);
		final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... arg0) {
				service = (CollectionsTestServiceAsync) SyncProxy
						.newProxyInstance(CollectionsTestServiceAsync.class,
								"http://10.0.2.2:8888/spawebtest/",
								"collections", true);
				signal.countDown();
				return null;
			}
		};
		task.execute();
		if (!signal.await(20, TimeUnit.SECONDS)) {
			throw new RuntimeException("Failed to get service started");
		}
```
  1. Convert to the AsyncTask system utilizing the following Search/Replace systems as a guide. It will be very useful to utilize auto-format after these search/replacements
    * Search _`service.echo`_ (or your appropriate method call) Replace with _`final AsyncTask<Void, Void, Void> myTask = new AsyncTask<Void, Void, Void>() {@Override	protected Void doInBackground(Void... arg0){ service.echo`_
    * Search _`});`_ Replace with _`});return null;}};runTestOnUiThread(new Runnable(){@Override public void run(){myTask.execute();}});`_
    * Search _`throws InterruptedException`_ Replace with _`throws Throwable`_

> In order to make it easier to test between the emulator and a device, SPALibrary as of version 0.4.4 utilizes a String resource **R.string.test\_server\_url**. Specifically,
> each test now inherits from a new TestCase **AndroidGWTTestCase** which provides access to the method _`getModuleBaseURL()`_ which looks at this resource. In this manner,
> by changing the value in the strings.xml file, you can switch between the emulator's loopback interface (10.0.2.2) and any url you may wish to specify. For device testing locally,
> you will likely use a local IP address such as 192.168.1.XXX. Make sure that your device and the computer hosting the App Engine Dev Server are on the same network.

## Android AppTester Test - New from Version 0.5 ##

> Similar to the above with slight modifications to build from AndroidGWTTestCase.
> Create a template for quick use with the following:

```
 	setTask(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				${line_selection}
				return null;
			}
		});
```

> Then for each existing test, highlight the code within the test, 

&lt;alt&gt;



&lt;shift&gt;

-Z, and your template should come up (if close to top of alphabet, then 1).
> and this will automatically surround the given code with AsyncTasked Code for use in Android Testing. The rest of the async needs and waiting are handled by AndroidGWTTestCase.
> Remember that the call to `delayTestFinishForRpc()` must come before the call to set the Task.

# Testing Expectations - 0.5 #

The following times are based on my system, so you can expect changes in processing times accordingly (6-core AMD@2.8Ghz, 8GB, Win 7-64) and
Eclipse (-Xms1g, -Xmx2g). Android testing takes about 600s (Heavy in UnicodeEscaping, ValueTypes, and Collections).
Plain old Java (POJ) testing takes about 110s (Heavy in Collections and ValueTypes). GWT testing takes 83s (45s within the UnicodeEscapingTest).

When running either the Java Standalone Tests `PojGSPTests` or the Android Tests `SPATests`, you must make sure the web service has been
GWT compiled and is running `SPAWebTest`

## Current Testing Failures 0.5 ##

| TestCase | Project | Issue | Resolution/Reason |
|:---------|:--------|:------|:------------------|
| CollectionsTest | Android | Failure in EnumMap tests. EnumMap serializer does not work for Android version of this class (different that in current JRE). | **Not Yet Resolved** |
| ExceptionTest | Java & Android | Failure on Server side because the UmbrellaException class doesn't implement the IsSerializable interface | A manual override of this class with the IsSerializable interface makes the test pass. |
| FinalFieldsTest | GWT | IncompatibleRemoteServiceException for FinalFieldsNode | **Not Yet Resolved** |
| RemoteServiceServletTests | Java & Android | Methods utilizing Request or RequestBuilder fail as they are not yet supported by SyncProxy | **Will be supported in a future version** |
| RunTimeSerializationTests | Java & Android | SAA | SAA |
| ValueTypesTest | Java & Android | The _over64KB tests fail with insufficient time or data match failure_| **Not Yet Resolved** |
| XsrfProtectionTest | Java _& Android_ | Failures due to lack of Xsrf support in SyncProxy. However, they unexpectedly pass in Android tests. | **Will be supported in a future version** |

### Testing Failures 0.4 ###

> Within the current set of tests implemented, there are a couple of tests which are failing, so please see below a for a list of failures and the issue.
> There are also a number of tests in the original test system which were not re-implemented (either for time or other reason, explained below).

| TestCase | Project | Issue | Resolution/Reason |
|:---------|:--------|:------|:------------------|
| AsyncEnumsTest & EnumsTest | Java & Android | Failure on Server side because the Enums don't implement the IsSerializable interface | A manual override of those enum classes with the IsSerializable interface makes the tests pass. |
| CollectionsTest#testArraysAsList | Java & Android | Fails with a NoClassDefFoundError for GWTBridge | **Not Yet Resolved** |
| EnumsTest#testNull | Android | Server-side throws IncompatibleRemoteServiceException for accessing echo(Object) | **Not Yet Resolved** |
| ExceptionTest | Java & Android | Failure on Server side because the UmbrellaException class doesn't implement the IsSerializable interface | A manual override of this class with the IsSerializable interface makes the test pass. |
| HttpsTest | Java & Android | SSL Plain Text Error | **Not Yet Resolved**|
| ~~RecursiveClassTest~~ | ALL | **Not reimplemented** | No original implementation |
| SessionTest | ALL | **Not reimplemented** | No value noted for reimplementing this scenario in unit testing |
| ~~TypeCheckedObjectsTes~~ | ~~Android~~ |~~All throw a SerializationException on the server side: Invalid type signature for ...client.rpc.TypeUncheckedGenericClass~~|~~Resolved - Forgot the CustomFieldSerializers in the Source~~|
| UnicodeEscapingTest#ServerToClientNonBMP | GWT|GAE | Throws a series of StringIndexOutOfBoundsException's | **Not Yet Resolved**|
| XsrfProtectionTest | ALL | **Not Fully Implemented** | Many of the original tests were not implemented |