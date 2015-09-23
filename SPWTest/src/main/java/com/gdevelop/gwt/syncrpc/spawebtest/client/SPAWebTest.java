/*
 * Copyright 2013 Blue Esoteric Web Development, LLC
 * (http://www.blueesoteric.com/)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gdevelop.gwt.syncrpc.spawebtest.client;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AnnotatedRpcTokenTestService;
import com.google.gwt.user.client.rpc.AnnotatedRpcTokenTestServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.CollectionsTestService;
import com.google.gwt.user.client.rpc.CollectionsTestServiceAsync;
import com.google.gwt.user.client.rpc.CoreJavaTestService;
import com.google.gwt.user.client.rpc.CoreJavaTestServiceAsync;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestService;
import com.google.gwt.user.client.rpc.CustomFieldSerializerTestServiceAsync;
import com.google.gwt.user.client.rpc.EnumsTestService;
import com.google.gwt.user.client.rpc.EnumsTestService.Basic;
import com.google.gwt.user.client.rpc.EnumsTestServiceAsync;
import com.google.gwt.user.client.rpc.ExceptionsTestService;
import com.google.gwt.user.client.rpc.ExceptionsTestServiceAsync;
import com.google.gwt.user.client.rpc.FinalFieldsTestService;
import com.google.gwt.user.client.rpc.FinalFieldsTestService.FinalFieldsNode;
import com.google.gwt.user.client.rpc.FinalFieldsTestServiceAsync;
import com.google.gwt.user.client.rpc.InheritanceTestService;
import com.google.gwt.user.client.rpc.InheritanceTestServiceAsync;
import com.google.gwt.user.client.rpc.LoggingRPCTestService;
import com.google.gwt.user.client.rpc.LoggingRPCTestServiceAsync;
import com.google.gwt.user.client.rpc.MixedSerializableEchoService;
import com.google.gwt.user.client.rpc.MixedSerializableEchoServiceAsync;
import com.google.gwt.user.client.rpc.ObjectGraphTestService;
import com.google.gwt.user.client.rpc.ObjectGraphTestServiceAsync;
import com.google.gwt.user.client.rpc.RecursiveClassTestService;
import com.google.gwt.user.client.rpc.RecursiveClassTestServiceAsync;
import com.google.gwt.user.client.rpc.RemoteServiceServletTestService;
import com.google.gwt.user.client.rpc.RemoteServiceServletTestServiceAsync;
import com.google.gwt.user.client.rpc.RpcTokenTestService;
import com.google.gwt.user.client.rpc.RpcTokenTestServiceAsync;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.TestSetFactory;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeArrayList;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeEnum;
import com.google.gwt.user.client.rpc.TestSetFactory.MarkerTypeEnumMapValue;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestService;
import com.google.gwt.user.client.rpc.TypeCheckedObjectsTestServiceAsync;
import com.google.gwt.user.client.rpc.UnicodeEscapingService;
import com.google.gwt.user.client.rpc.UnicodeEscapingService.InvalidCharacterException;
import com.google.gwt.user.client.rpc.UnicodeEscapingServiceAsync;
import com.google.gwt.user.client.rpc.UnicodeEscapingTest;
import com.google.gwt.user.client.rpc.ValueTypesTestService;
import com.google.gwt.user.client.rpc.ValueTypesTestServiceAsync;
import com.google.gwt.user.client.rpc.XsrfTestService;
import com.google.gwt.user.client.rpc.XsrfTestServiceAsync;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Preethum
 * @since 0.4
 */
public class SPAWebTest implements EntryPoint {
	private static LogRecord createLogRecord() {
		LogRecord result = new LogRecord(Level.INFO, "Test Log Record");

		// Only set serialized fields.

		result.setLoggerName("Test Logger Name");
		result.setMillis(1234567);

		Throwable thrown = new Throwable("Test LogRecord Throwable 1");
		thrown.initCause(new Throwable("Test LogRecord Throwable cause"));
		result.setThrown(thrown);

		return result;
	}

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";
	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	public final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	public final ProfileServiceAsync profileService = GWT.create(ProfileService.class);
	private final CookieServiceAsync cookieService = GWT.create(CookieService.class);
	public final LargePayloadServiceAsync payloadService = GWT.create(LargePayloadService.class);
	public final CollectionsTestServiceAsync collectionsService = GWT.create(CollectionsTestService.class);
	public final CoreJavaTestServiceAsync coreJavaService = GWT.create(CoreJavaTestService.class);
	public final CustomFieldSerializerTestServiceAsync customFieldSerService = GWT
			.create(CustomFieldSerializerTestService.class);
	public final EnumsTestServiceAsync enumTestService = GWT.create(EnumsTestService.class);
	public final ExceptionsTestServiceAsync customExceptionService = GWT.create(ExceptionsTestService.class);
	public final FinalFieldsTestServiceAsync finalFieldSerService = GWT.create(FinalFieldsTestService.class);
	public final LoggingRPCTestServiceAsync loggingRPCService = GWT.create(LoggingRPCTestService.class);
	public final InheritanceTestServiceAsync inheritanceService = GWT.create(InheritanceTestService.class);
	public final ObjectGraphTestServiceAsync objectGraphService = GWT.create(ObjectGraphTestService.class);
	public final RecursiveClassTestServiceAsync recursiveClassService = GWT.create(RecursiveClassTestService.class);
	public final RemoteServiceServletTestServiceAsync remoteServiceServletService = GWT
			.create(RemoteServiceServletTestService.class);
	public final AnnotatedRpcTokenTestServiceAsync annotatedRpcTokenService = GWT
			.create(AnnotatedRpcTokenTestService.class);
	public final RpcTokenTestServiceAsync rpcTokenService = GWT.create(RpcTokenTestService.class);
	public final MixedSerializableEchoServiceAsync mixedEchoService = GWT.create(MixedSerializableEchoService.class);
	public final TypeCheckedObjectsTestServiceAsync typeCheckedService = GWT
			.create(TypeCheckedObjectsTestService.class);
	public final UnicodeEscapingServiceAsync unicodeService = GWT.create(UnicodeEscapingService.class);
	public final ValueTypesTestServiceAsync valuesService = GWT.create(ValueTypesTestService.class);
	public final XsrfTestServiceAsync xsrfTestService = GWT.create(XsrfTestService.class);

	// public final JUnitHostAsync junitService = GWT.create(JUnitHost.class);

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {

		try {
			UnicodeEscapingTest.verifyStringContainingCharacterRange(0, 0, new String());
		} catch (InvalidCharacterException e) {

		}
		// new GWTTestCase() {
		//
		// @Override
		// public String getModuleName() {
		// return null;
		// }
		// };
		ServiceDefTarget serTarget = (ServiceDefTarget) this.unicodeService;
		serTarget.setServiceEntryPoint("/spawebtest/unicodeEscape");
		this.unicodeService.getStringContainingCharacterRange(0, 1, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(String result) {

			}

		});
		// TestSetValidator.isValidComplexCyclicGraph(TSFAccessor
		// .createComplexCyclicGraph());
		this.coreJavaService.echoMathContext(new MathContext(5, RoundingMode.CEILING),
				new AsyncCallback<MathContext>() {

					@Override
					public void onFailure(Throwable caught) {
						throw new RuntimeException(caught);
					}

					@Override
					public void onSuccess(MathContext result) {

					}

				});
		this.collectionsService.echo(TestSetFactory.createArrayList(),
				new AsyncCallback<ArrayList<MarkerTypeArrayList>>() {
					@Override
					public void onFailure(Throwable caught) {
						throw new RuntimeException(caught);
					}

					@Override
					public void onSuccess(ArrayList<MarkerTypeArrayList> result) {

					}
				});
		final EnumMap<MarkerTypeEnum, MarkerTypeEnumMapValue> expected = TestSetFactory.createEmptyEnumMap();
		this.collectionsService.echoEmptyEnumMap(expected,
				new AsyncCallback<EnumMap<MarkerTypeEnum, MarkerTypeEnumMapValue>>() {
					@Override
					public void onFailure(Throwable caught) {
						throw new RuntimeException(caught);
					}

					@Override
					public void onSuccess(EnumMap<MarkerTypeEnum, MarkerTypeEnumMapValue> result) {

					}
				});
		((ServiceDefTarget) this.finalFieldSerService).setServiceEntryPoint(GWT.getModuleBaseURL() + "finalfields");
		this.finalFieldSerService.transferObject(new FinalFieldsNode(4, "C", 9),
				new AsyncCallback<FinalFieldsTestService.FinalFieldsNode>() {

					@Override
					public void onFailure(Throwable caught) {
						// /throw new RuntimeException(caught);
					}

					@Override
					public void onSuccess(FinalFieldsNode result) {
					}
				});
		ServiceDefTarget target = (ServiceDefTarget) this.enumTestService;
		target.setServiceEntryPoint(GWT.getModuleBaseURL() + "enums");
		this.enumTestService.echo(Basic.A, new AsyncCallback<EnumsTestService.Basic>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Basic result) {
			}
		});

		this.loggingRPCService.echoLogRecord(createLogRecord(), new AsyncCallback<LogRecord>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(LogRecord result) {

			}
		});
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			@Override
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a
			 * response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				// if (!FieldVerifier.isValidName(textToServer)) {
				// errorLabel.setText("Please enter at least four characters");
				// return;
				// }

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");

				// Greet Server for General T1 Object
				// greetingService.greetServer(textToServer, new
				// AsyncCallback<T1>() {
				// @Override
				// public void onFailure(Throwable caught) {
				// // Show the RPC error message to the user
				// dialogBox.setText("Remote Procedure Call - Failure");
				// serverResponseLabel.addStyleName("serverResponseLabelError");
				// serverResponseLabel.setHTML(SERVER_ERROR);
				// dialogBox.center();
				// closeButton.setFocus(true);
				// }
				//
				// @Override
				// public void onSuccess(T1 result) {
				// dialogBox.setText("Remote Procedure Call");
				// serverResponseLabel.removeStyleName("serverResponseLabelError");
				// serverResponseLabel.setHTML(result.getText());
				// dialogBox.center();
				// closeButton.setFocus(true);
				// }
				// });

				// Greet Server for ArrayList<String>
				SPAWebTest.this.greetingService.greetServerArr(textToServer, new AsyncCallback<ArrayList<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						dialogBox.setText("Remote Procedure Call - Failure");
						serverResponseLabel.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(SERVER_ERROR);
						dialogBox.center();
						closeButton.setFocus(true);
					}

					@Override
					public void onSuccess(ArrayList<String> result) {
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel.removeStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(result.get(0));
						dialogBox.center();
						closeButton.setFocus(true);
					}
				});

				// Cookies.setCookie("Test", "Value");
				// SPAWebTest.this.cookieService
				// .testCookie(new AsyncCallback<ArrayList<String>>() {
				//
				// @Override
				// public void onFailure(Throwable caught) {
				// throw new RuntimeException(caught);
				// }
				//
				// @Override
				// public void onSuccess(ArrayList<String> result) {
				// dialogBox.setText("Cookie Test RPC");
				// serverResponseLabel.setHTML(result.toString());
				// dialogBox.center();
				// }
				// });
				// SPAWebTest.this.cookieService.generateCookiesOnServer(new
				// AsyncCallback<Void>() {
				//
				// @Override
				// public void onFailure(Throwable caught) {
				// throw new RuntimeException(caught);
				// }
				//
				// @Override
				// public void onSuccess(Void result) {
				// dialogBox.setText("Cookie RPC");
				// serverResponseLabel.setHTML(Cookies.getCookieNames().toString());
				// dialogBox.center();
				// }
				// });

			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}
