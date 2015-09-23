package com.gdevelop.gwt.syncrpc.test.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

public class MenuManager<MenuType extends Enum<MenuType>, MenuItemType extends Enum<MenuItemType>>
		implements ActionListener {
	public interface MMHandler<MenuItemType extends Enum<MenuItemType>> {
		void menuItem(MenuItemType menuItem);
	}

	HashMap<MenuType, JMenu> menuMap;

	HashMap<MenuItemType, JMenuItem> menuItemMap;

	MMHandler<MenuItemType> handler;
	Class<MenuItemType> miClazz;

	public MenuManager(MMHandler<MenuItemType> handler,
			Class<MenuItemType> miClazz) {
		this.handler = handler;
		this.miClazz = miClazz;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.handler.menuItem(Enum.valueOf(this.miClazz,
				arg0.getActionCommand()));
	}

	public void addSeparator(MenuType menu) {
		this.menuMap.get(menu).add(new JSeparator());
	}

	/**
	 *
	 * @param name
	 *            the menu's name
	 * @param mneumonic
	 *            KeyEvent VK constant
	 * @return the new menu
	 */
	public JMenu createMenu(MenuType menu, String name, int mneumonic) {
		if (this.menuMap == null) {
			this.menuMap = new HashMap<>();
		}
		JMenu jm = new JMenu(name);
		jm.setMnemonic(mneumonic);
		this.menuMap.put(menu, jm);
		return jm;
	}

	public JMenuItem createMenuItem(MenuItemType menuItem, String name,
			int mnemonic, int accelModifier, MenuType menu) {
		return createMenuItem(menuItem, name, mnemonic, accelModifier, menu,
				true);
	}

	public JMenuItem createMenuItem(MenuItemType menuItem, String name,
			int mnemonic, int accelModifier, MenuType menu, boolean enabled) {
		JMenuItem jmi = new JMenuItem(name, mnemonic);
		if (mnemonic > 0) {
			jmi.setAccelerator(KeyStroke.getKeyStroke(mnemonic, accelModifier));
		}
		jmi.setActionCommand(menuItem.toString());
		jmi.addActionListener(this);
		jmi.setEnabled(enabled);
		this.menuMap.get(menu).add(jmi);
		if (this.menuItemMap == null) {
			this.menuItemMap = new HashMap<>();
		}
		this.menuItemMap.put(menuItem, jmi);
		return jmi;
	}

	public void disableItem(MenuItemType item) {
		JMenuItem menuItem = this.menuItemMap.get(item);
		menuItem.setEnabled(false);
	}

	public void enableItem(MenuItemType item) {
		JMenuItem menuItem = this.menuItemMap.get(item);
		menuItem.setEnabled(true);
	}
}