package com.gdevelop.gwt.syncrpc.test.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public abstract class BasicFrame extends JFrame {

	class WindowEventHandler extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent evt) {
			onWindowClose();
		}
	}

	private static final long serialVersionUID = 1L;
	private JLabel statusLabel;
	private JPanel contentPane;
	JPanel statusPanel;

	HashMap<String, JProgressBar> barMap;

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	public BasicFrame createAndShowGUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1200, 800);
		// demo.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setJMenuBar(this.createMenuBar());
		this.setContentPane(this.createContentPane());
		// Display the window.
		// demo.pack();
		this.setVisible(true);

		this.setStatusPanel();

		return this;
	}

	public Container createContentPane() {
		this.addWindowListener(new WindowEventHandler());
		// Create the content-pane-to-be.
		this.contentPane = new JPanel();
		this.contentPane.setOpaque(true);
		// JLabel label = new JLabel("Test");
		// this.contentPane.add(label);
		this.contentPane.setLayout(new BorderLayout());
		return this.contentPane;
	}

	protected abstract JMenuBar createMenuBar();

	protected abstract String getAppTitle();

	public JProgressBar getProgressBar(String name) {
		if (this.barMap == null) {
			this.barMap = new HashMap<>();
		}
		if (this.barMap.get(name) != null) {
			return this.barMap.get(name);
		}
		JProgressBar newBar = new JProgressBar();
		this.statusPanel.add(newBar);
		this.barMap.put(name, newBar);
		return newBar;
	}

	protected abstract String getSummary();

	public abstract void init();

	public abstract void onWindowClose();

	public void setStatus(String status) {
		this.statusLabel.setText(status);
	}

	private void setStatusPanel() {
		// create the status bar panel and shove it down the bottom of the frame
		this.statusPanel = new JPanel();
		this.statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.add(this.statusPanel, BorderLayout.SOUTH);
		this.statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));
		this.statusPanel.setLayout(new BoxLayout(this.statusPanel,
				BoxLayout.X_AXIS));
		this.statusLabel = new JLabel("");
		this.statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		this.statusPanel.add(this.statusLabel);
	}

	protected void showAbout() {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.add(new JLabel(getAppTitle()));
		pane.add(new JLabel(
				"<html><br />"
						+ getSummary()
						+ "<br />By using this program, the user takes all responisiblity for the integrity of the files produced <br />and agrees not to hold Blue Esoteric Web Development, LLC responsible for any and <br />all damages or corruption that may occur. Please see included EULA for further details.<br />&nbsp;</html>"));
		pane.add(new JLabel("Blue Esoteric Web Devleopment, LLC \u00a9 2014"));
		JButton email = new JButton();
		email.setText("<HTML>Email: <FONT color=\"#000099\"><U>Developer@BlueEsoteric.com</U></FONT></HTML>");
		email.setHorizontalAlignment(SwingConstants.LEFT);
		email.setBorderPainted(false);
		email.setOpaque(false);
		email.setBackground(Color.WHITE);
		URI emailUri = null;
		try {
			emailUri = new URI("mailto:developer@BlueEsoteric.com");
			email.setToolTipText(emailUri.toString());
			email.addActionListener(new OpenActionListener(emailUri,
					OpenActionListener.Type.MAIL));
			// pane.add(new JLabel("Contact: Developer@BlueEsoteric.com"));
			pane.add(email);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JButton site = new JButton();
		site.setText("<HTML>Website: <FONT color=\"#000099\"><U>http://www.BlueEsoteric.com/</U></FONT></HTML>");
		site.setHorizontalAlignment(SwingConstants.LEFT);
		site.setBorderPainted(false);
		site.setOpaque(false);
		site.setBackground(Color.WHITE);
		URI siteUri = null;
		try {
			siteUri = new URI("http://www.blueesoteric.com");
			site.setToolTipText(siteUri.toString());
			site.addActionListener(new OpenActionListener(siteUri,
					OpenActionListener.Type.WEB));
			// pane.add(new JLabel("Website: http://www.BlueEsoteric.com/"));
			pane.add(site);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(this, pane);
	}
}