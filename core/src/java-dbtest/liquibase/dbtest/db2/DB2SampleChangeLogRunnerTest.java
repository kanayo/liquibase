package liquibase.dbtest.db2;

import liquibase.dbtest.AbstractSimpleChangeLogRunnerTest;

import java.util.Properties;

@SuppressWarnings({"JUnitTestCaseWithNoTests"})
public class DB2SampleChangeLogRunnerTest extends AbstractSimpleChangeLogRunnerTest {

    public DB2SampleChangeLogRunnerTest() throws Exception {
        super("db2", "jdbc:db2://localhost:50000/liquibas");
    }

    protected Properties createProperties() {
        Properties properties = super.createProperties();
        properties.put("retrieveMessagesFromServerOnGetMessage", "true");
        return properties;
    }

}
