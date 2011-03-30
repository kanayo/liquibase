package org.liquibase.ide.common.change.action;

import liquibase.change.Change;
import liquibase.database.structure.DatabaseObject;
import liquibase.database.structure.Table;
import org.liquibase.ide.common.change.wizard.RefactorWizard;
import org.liquibase.ide.common.change.wizard.page.RefactorWizardPage;

public abstract class BaseTableRefactorAction extends BaseRefactorAction {

    public BaseTableRefactorAction(String title) {
        super(title);
    }

    public abstract RefactorWizard createRefactorWizard(Table dbObject);

    public final RefactorWizard createRefactorWizard(DatabaseObject dbObject) {
        return createRefactorWizard((Table) dbObject);
    }

    public Change[] createChanges(DatabaseObject selectedTable, RefactorWizardPage... pages) {
        return createChanges(((Table) selectedTable), pages);
    }

    protected abstract Change[] createChanges(Table selectedTable, RefactorWizardPage... pages);

    public boolean isApplicableTo(Class objectType) {
        return objectType.equals(Table.class);
    }
}
