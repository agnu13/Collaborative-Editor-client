/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.nodes.projectTree;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

public abstract class NodeTreeItem extends TreeItem {
    
    protected TreeItemType treeItemType = TreeItemType.DOC;
    
    public NodeTreeItem(String str) {
        super(str);
    }

    public TreeItemType getTreeItemType() {
        return treeItemType;
    }
    
    public enum TreeItemType {
        DOC, PROJECT, NODE;
    }
    
    public abstract ContextMenu getContextMenu();
}
