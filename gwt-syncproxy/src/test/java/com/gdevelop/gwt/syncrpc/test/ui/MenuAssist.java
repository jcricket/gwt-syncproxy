package com.gdevelop.gwt.syncrpc.test.ui;

import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuAssist {
	public static HashMap<String, JMenuItem> MenuItemManager = new HashMap<String, JMenuItem>();

	/**
	 *
	 * @param name
	 *            the menu's name
	 * @param mneumonic
	 *            KeyEvent VK constant
	 * @return the new menu
	 */
	public static JMenu createMenu(String name, int mneumonic) {
		JMenu jm = new JMenu(name);
		jm.setMnemonic(mneumonic);
		return jm;
	}

	public static JMenuItem createMenuItem(String name, KeyStroke accelerator,
			ActionListener actionListener, String actionCommand,
			boolean enabled, JMenu parent) {
		JMenuItem jmi = new JMenuItem(name);
		jmi.setAccelerator(accelerator);
		jmi.setActionCommand(actionCommand);
		jmi.addActionListener(actionListener);
		jmi.setEnabled(enabled);
		parent.add(jmi);
		MenuItemManager.put(actionCommand, jmi);
		return jmi;
	}

	/**
	 *
	 * @param ke
	 *            KeyEvent VK constant
	 * @param ae
	 *            ActionEvent constant
	 * @return the keystroke to use
	 */
	public static KeyStroke getAccel(int ke, int ae) {
		return KeyStroke.getKeyStroke(ke, ae);
	}

	public static JMenuItem getJMenuItem(String actionCommand) {
		return MenuItemManager.get(actionCommand);
	}
}
