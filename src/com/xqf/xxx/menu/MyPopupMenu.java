package com.xqf.xxx.menu;

import javax.swing.*;

/**
 * Created by XQF on 2016/12/27.
 */
public class MyPopupMenu extends JFrame {
    private JPopupMenu popupMenu;

    public MyPopupMenu() {
        popupMenu=getMenu();
    }

    private JPopupMenu getMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("剪切");
        JMenuItem menuItem2 = new JMenuItem("复制");
        JMenuItem menuItem3 = new JMenuItem("粘贴");
        JMenu menuSub = new JMenu("编辑");
        JMenuItem menuSubItem1 = new JMenuItem("只读");
        JMenuItem menuSubItem2 = new JMenuItem("可写");
        JMenu menuSubSub = new JMenu("字体");
        menuSub.add(menuSubItem1);
        menuSub.add(menuSubItem2);
        menuSub.add(menuSubSub);
        JMenuItem menuSubSubItem1 = new JMenuItem("加粗");
        JMenuItem menuSubSubItem2 = new JMenuItem("倾斜");
        menuSubSub.add(menuSubSubItem1);
        menuSubSub.add(menuSubSubItem2);

        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        popupMenu.add(menuItem3);
        popupMenu.add(menuSub);
        return popupMenu;
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }
}
