package com.gdevelop.gwt.syncrpc.test.ui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

public class OpenActionListener implements ActionListener {
	public enum Type {
		WEB, MAIL;
	}

	private final URI uri;
	private final Type type;

	public OpenActionListener(URI uri, Type type) {
		this.uri = uri;
		this.type = type;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		open();
	}

	private void open() {
		if (Desktop.isDesktopSupported()) {
			try {
				switch (this.type) {
				case WEB:
					Desktop.getDesktop().browse(this.uri);
					break;
				case MAIL:
					Desktop.getDesktop().mail(this.uri);
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("System Desktop is not supported.");
		}
	}
}
