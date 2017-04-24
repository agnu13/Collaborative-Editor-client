/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.nodes.projectTree;

import ui.nodes.projectTree.NodeTreeItem;
import javafx.scene.control.TreeCell;

/**
 *
 * @author Ashish
 */
public final class TreeCellImpl extends TreeCell<String> {

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(getItem() == null ? "" : getItem());
            setGraphic(getTreeItem().getGraphic());
            setContextMenu(((NodeTreeItem) getTreeItem()).getContextMenu());
        }
    }
}
