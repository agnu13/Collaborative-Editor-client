package ui.nodes.projectTree;

import ui.nodes.projectTree.DocTreeItem;
import authenticate.entities.User;
import java.util.HashMap;
import java.util.HashSet;
import javafx.event.EventHandler;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import projectManager.Collections;
import ui.controllers.IDEController;
import ui.nodes.projectTree.NodeTreeItem.TreeItemType;

public class ProjectTree implements Closer {

    private TreeView treeView;
    private TreeItem dummyNode = new TreeItem("Root");
    private User user;
    private TabPane tabPane;
    private HashMap<Collections, ProjectTreeItem> map = new HashMap<>();
    private HashSet<Collections> openedProjects = new HashSet<>();
    private IDEController ideController;

    public ProjectTree(User user, TreeView treeView, TabPane tabPane, IDEController ideController) {
        this.treeView = treeView;
        this.user = user;
        this.tabPane = tabPane;
        this.ideController = ideController;
        
        treeView.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    if (treeView.getSelectionModel().getSelectedIndex() == -1) {
                        return;
                    }
                    NodeTreeItem selected = (NodeTreeItem) treeView.getSelectionModel().getSelectedItems().get(0);

                    if (selected.getTreeItemType() == TreeItemType.DOC) {
                        ((DocTreeItem) selected).openFile();
                    }
                }
            }
        });
        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    if (treeView.getSelectionModel().getSelectedIndex() == -1) {
                        return;
                    }
                    NodeTreeItem selected = (NodeTreeItem) treeView.getSelectionModel().getSelectedItems().get(0);

                    if (selected.getTreeItemType() == TreeItemType.DOC) {
                        ((DocTreeItem) selected).openFile();
                    }
                } else if (event.isSecondaryButtonDown()) {

                }
            }
        });
        treeView.setRoot(dummyNode);
        treeView.setShowRoot(false);
        treeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> p) {
                return new TreeCellImpl();
            }
        });
        treeView.setVisible(true);
    }

    public void loadProject(Collections project) {
        if (map.containsKey(project)) {
            return;
        }
        
        treeView.setVisible(true);
        
        ProjectTreeItem projectRoot = new ProjectTreeItem(project, user, this, tabPane, ideController);
        map.put(project, projectRoot);
        openedProjects.add(project);

        projectRoot.setExpanded(true);

        dummyNode.getChildren().add(projectRoot);
    }

    @Override
    public void closeProject(Collections project) {
        openedProjects.remove(project);
        treeView.getRoot().getChildren().remove(map.get(project));
        map.remove(project);
        if (treeView.getRoot().getChildren().isEmpty()) {
            treeView.setVisible(false);
        }
    }

}

interface Closer {

    void closeProject(Collections project);
}
