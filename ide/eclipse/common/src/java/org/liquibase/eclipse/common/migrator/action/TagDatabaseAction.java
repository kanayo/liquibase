package org.liquibase.eclipse.common.migrator.action;

import liquibase.Liquibase;
import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.connectivity.sqm.core.rte.jdbc.JDBCDatabase;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.liquibase.eclipse.common.LiquibasePreferences;
import org.liquibase.eclipse.common.action.BaseDatabaseAction;
import org.liquibase.eclipse.common.migrator.dialog.TagDatabaseDialog;

public class TagDatabaseAction extends BaseDatabaseAction {

    public void run(IAction action) {
        TagDatabaseDialog dialog = new TagDatabaseDialog(null);
        if (Dialog.OK != dialog.open() || dialog.getTagName() == null) {
            return;
        }

        try {
            Liquibase liquibase = getLiquibase(LiquibasePreferences.getRootChangeLog(), getSelectedConnection(getSelection()));
            liquibase.tag(dialog.getTagName());

            ((JDBCDatabase) getSelectedDatabase(getSelection())).refresh();
        } catch (Exception e) {
            IStatus status = new OperationStatus(IStatus.ERROR, LiquibasePreferences.PLUGIN_ID, 2, "Error Updating Database: " + e.getMessage(), e);
            ErrorDialog.openError(null, "Update Error", "Error Updating Database", status);
        }

    }
}
