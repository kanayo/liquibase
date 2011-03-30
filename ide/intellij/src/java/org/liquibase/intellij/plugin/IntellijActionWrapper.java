package org.liquibase.intellij.plugin;

import dbhelp.db.Catalog;
import dbhelp.db.Schema;
import dbhelp.db.model.AbstractDBObject;
import dbhelp.db.model.DBTreeModel;
import dbhelp.db.model.IDBNode;
import dbhelp.db.ui.DBTree;
import dbhelp.plugin.action.portable.PortableAction;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.structure.*;
import liquibase.exception.LiquibaseException;
import liquibase.exception.JDBCException;
import org.liquibase.ide.common.IdeFacade;
import org.liquibase.ide.common.action.BaseDatabaseAction;
import org.liquibase.ide.common.action.MigratorAction;
import org.liquibase.ide.common.change.action.BaseRefactorAction;
import org.liquibase.intellij.plugin.change.wizard.IntellijRefactorWizard;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.text.ParseException;
import java.util.Enumeration;

public class IntellijActionWrapper extends PortableAction {
    private BaseDatabaseAction action;
    private IdeFacade ideFacade;


    public IntellijActionWrapper(BaseDatabaseAction action, Class objectType) {
        super(action.getTitle());
        this.action = action;
        setEnabled(action.isApplicableTo(objectType));
        ideFacade = LiquibaseProjectComponent.getInstance().getIdeFacade();
    }

    public void actionPerformed(ActionEvent actionEvent) {
       Database selectedDatabase = null;
       try {
          selectedDatabase = getSelectedDatabase();
          DatabaseObject selectedDatabaseObject = getSelectedDatabaseObject();
           if (action instanceof BaseRefactorAction) {
                IntellijRefactorWizard wizard = new IntellijRefactorWizard(((BaseRefactorAction) action).createRefactorWizard(selectedDatabaseObject), selectedDatabaseObject, selectedDatabase, ((BaseRefactorAction) action));
                wizard.setup();
                wizard.pack();
                wizard.show();
            } else if (action instanceof MigratorAction) {
                ((MigratorAction) action).actionPerform(selectedDatabase, ideFacade);
            }
          if (! selectedDatabase.getAutoCommitMode()) {
             selectedDatabase.commit();
          }
        } catch (LiquibaseException e) {
            ideFacade.showError("Error Executing Change", e);
        } finally {
          if (selectedDatabase != null) {
             try {
                selectedDatabase.close();
             } catch (JDBCException e) {
                //ignore, we did our best.
             }
          }
       }
       if (action.needsRefresh()) {
           refresh();
       }
    }

    private void refresh() {
        LiquibaseProjectComponent liquibaseProjectComponent = LiquibaseProjectComponent.getInstance();
        DBTree tree = liquibaseProjectComponent.getDbTree();
        if (tree == null) {
            liquibaseProjectComponent.getIdeFacade().showMessage("Operation Complete", "Could not auto-refresh the database display\n\nPlease refresh it manually");
            return;
        }

        TreePath pathToRefresh = tree.getSelectionPath().getParentPath();
        Enumeration<TreePath> expandedPaths = tree.getExpandedDescendants(new TreePath(tree.getModel().getRoot()));
        Object c = pathToRefresh.getLastPathComponent();
        if (c instanceof IDBNode) {
            IDBNode dbObject = (IDBNode) c;
            dbObject.refresh();
            DBTreeModel m = (DBTreeModel) tree.getModel();
            m.fireTreeNodeChange(new TreeModelEvent(this, pathToRefresh));
            while (!(dbObject instanceof AbstractDBObject.ProfileNode) && null != dbObject) {
                dbObject = dbObject.getParent();
            }
            if (dbObject != null) {
                AbstractDBObject.ProfileNode dbNode = (AbstractDBObject.ProfileNode) dbObject;
                try {
                    dbNode.getDatabase().getResolver().refresh();
                } catch (Exception e1) {
                    liquibaseProjectComponent.getIdeFacade().showError(e1);
                }
            }
        }

        while (expandedPaths != null && expandedPaths.hasMoreElements()) {
            TreePath path = expandedPaths.nextElement();
            tree.expandPath(path);
            TreeExpansionListener[] expansionListeners = tree.getListeners(TreeExpansionListener.class);
            for (TreeExpansionListener listener : expansionListeners) {
                listener.treeExpanded(new TreeExpansionEvent(this, path));
            }
        }

    }

    public DatabaseObject getSelectedDatabaseObject() {
        try {
            Object selectedObject = getUserData();
            if (selectedObject instanceof DBTree) {
                TreePath selectionPath = ((DBTree) getUserData()).getSelectionModel().getLeadSelectionPath();

                selectedObject = selectionPath.getPathComponent(1);
            }

            if (selectedObject instanceof dbhelp.db.Database) {
                return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(((dbhelp.db.Database) selectedObject).getConnection());
            } else if (selectedObject instanceof Catalog) {
                return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(((Catalog) selectedObject).getDatabase().getConnection());
            } else if (selectedObject instanceof dbhelp.db.Column) {
                dbhelp.db.Column selectedColumn = (dbhelp.db.Column) selectedObject;
                dbhelp.db.Table selectedTable = selectedColumn.getTable();

                Table table = createTableObject(selectedTable);

                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(selectedTable.getDatabase().getConnection());

                return createColumnObject(selectedColumn, database, table);
            } else if (selectedObject instanceof Schema) {
                return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(((Schema) selectedObject).getDatabase().getConnection());
            } else if (selectedObject instanceof dbhelp.db.Table) {
                dbhelp.db.Table selectedTable = (dbhelp.db.Table) selectedObject;
                if (selectedTable.getTableType().equals("VIEW")) {
                    return createViewObject(selectedTable);
                }
                return createTableObject(selectedTable);
            } else if (selectedObject instanceof dbhelp.db.ForeignKey) {
                dbhelp.db.ForeignKey selectedFK = (dbhelp.db.ForeignKey) selectedObject;
                return createForeignKeyObject(selectedFK);
            } else if (selectedObject instanceof dbhelp.db.PrimaryKey) {
                dbhelp.db.PrimaryKey selectedPK = (dbhelp.db.PrimaryKey) selectedObject;
                return createPrimaryKeyObject(selectedPK);
            } else if (selectedObject instanceof dbhelp.db.Index) {
                dbhelp.db.Index selectedIndex = (dbhelp.db.Index) selectedObject;
                return createIndexObject(selectedIndex);
            } else if (selectedObject instanceof AbstractDBObject.ProfileNode) {
                return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(((AbstractDBObject.ProfileNode) selectedObject).getDatabase().getConnection());
            } else {
                throw new RuntimeException("Unknown object type: " + selectedObject.getClass().getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Column createColumnObject
            (dbhelp.db.Column selectedColumn,
             Database database,
             Table table) throws ParseException {
        Column column = new Column();
        column.setName(selectedColumn.getName());
        column.setAutoIncrement(selectedColumn.isAutoIncrement());
        column.setColumnSize(selectedColumn.getColumnSize());
        column.setDataType(selectedColumn.getType());
        column.setDecimalDigits(selectedColumn.getDecimalDigits());
        column.setDefaultValue(database.convertDatabaseValueToJavaObject(selectedColumn.getColumnDef(), selectedColumn.getType(), selectedColumn.getColumnSize(), selectedColumn.getDecimalDigits()));
        column.setNullable(selectedColumn.getNullable() == ResultSetMetaData.columnNullable || selectedColumn.getNullable() == ResultSetMetaData.columnNullableUnknown);
        column.setPrimaryKey(selectedColumn.isPrimaryKey());
        column.setTypeName(selectedColumn.getTypeName());
        column.setTable(table);
        return column;
    }

    private Table createTableObject(dbhelp.db.Table selectedTable) {
        Table table = new Table(selectedTable.getName());
        table.setDatabase(getSelectedDatabase());
        return table;
    }

    private View createViewObject(dbhelp.db.Table selectedTable) {
        View view = new View(selectedTable.getName());
        view.setDatabase(getSelectedDatabase());
        return view;
    }

    private ForeignKey createForeignKeyObject(dbhelp.db.ForeignKey selectedFK) {
        ForeignKey fk = new ForeignKey();
        fk.setName(selectedFK.getFkName());

        Table pkTable = new Table(selectedFK.getPkTableName());
        pkTable.setDatabase(getSelectedDatabase());
        fk.setPrimaryKeyTable(pkTable);
        fk.setPrimaryKeyColumns(selectedFK.getPkColumnName());

        Table fkTable = new Table(selectedFK.getFkTableName());
        fkTable.setDatabase(getSelectedDatabase());
        fk.setForeignKeyTable(fkTable);
        fk.setForeignKeyColumns(selectedFK.getFkColumnName());

        fk.setDeferrable(selectedFK.getDeferrability() == 1);
        return fk;
    }

    private Index createIndexObject(dbhelp.db.Index selectedIndex) {
        Index index = new Index();
        index.setName(selectedIndex.getIndexName());

        Table table = new Table(selectedIndex.getName());
        table.setDatabase(getSelectedDatabase());
        index.setTable(table);

        index.getColumns().add(selectedIndex.getColumnName());
        index.setFilterCondition(selectedIndex.getFilterCondition());

        return index;
    }

    private PrimaryKey createPrimaryKeyObject(dbhelp.db.PrimaryKey selectedPK) {
        PrimaryKey pk = new PrimaryKey();
        pk.setName(selectedPK.getPkName());

        Table table = new Table(selectedPK.getTableName());
        table.setDatabase(getSelectedDatabase());
        pk.setTable(table);

        pk.addColumnName(1, selectedPK.getColumnName());

        return pk;
    }

    public Database getSelectedDatabase() {
        DBTree tree = LiquibaseProjectComponent.getInstance().getDbTree();
        Object pathComponent = tree.getSelectionPath().getPathComponent(1);
        try {
            Connection connection = ((AbstractDBObject) pathComponent).getDatabase().getConnection();
            return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Column getSelectedColumn() {
        Object selectedObject = getUserData();
        if (selectedObject instanceof DBTree) {
            TreePath selectionPath = ((DBTree) getUserData()).getSelectionModel().getLeadSelectionPath();

            selectedObject = selectionPath.getPathComponent(1);
        }

        if (selectedObject instanceof Column) {
            return (Column) selectedObject;
        }

        return null;
    }

    public Table getSelectedTable() {
        Object selectedObject = getUserData();
        if (selectedObject instanceof DBTree) {
            TreePath selectionPath = ((DBTree) getUserData()).getSelectionModel().getLeadSelectionPath();

            selectedObject = selectionPath.getLastPathComponent();
        }

        if (selectedObject instanceof AbstractDBObject.TableNode) {
            return createTableObject(((AbstractDBObject.TableNode) selectedObject).getTable());
        } else if (selectedObject instanceof Column) {
            return ((Column) selectedObject).getTable();
        } else if (selectedObject instanceof Table) {
            return ((Table) selectedObject);
        }

        return null;
    }

    public DatabaseConnection getSelectedConnection
            () {
        try {
            return getSelectedDatabase().getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getSelectedObject
            () {
        Object userData = getUserData();
        if (userData instanceof JTree) {
            TreePath selectionPath = ((DBTree) userData).getSelectionModel().getLeadSelectionPath();
            return selectionPath.getLastPathComponent();
        }

        return userData;
    }
}
