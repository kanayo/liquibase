package org.liquibase.eclipse.common.change.wizard;

import java.sql.Connection;

import liquibase.change.Change;
import liquibase.change.DropColumnChange;

import org.eclipse.datatools.connectivity.sqm.core.rte.ICatalogObject;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.jface.wizard.IWizardPage;

public class DropColumnWizard extends BaseEclipseRefactorWizard {

	private Column column;
	
	public DropColumnWizard(Database database, Connection connection, Column column) {
		super(database, connection);
		this.column = column;
	}

	@Override
	protected Change[] createChanges() {
		DropColumnChange change = new DropColumnChange();
		change.setTableName(column.getTable().getName());
		change.setColumnName(column.getName());
		
		return new Change[] { change };
	}

	@Override
	protected IWizardPage[] createPages() {
		return new IWizardPage[0];
	}

	@Override
	protected void refresh() {
		((ICatalogObject)column.getTable()).refresh();		
	}
}
