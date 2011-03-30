package liquibase.dbtest.pgsql;

import liquibase.dbtest.AbstractSimpleChangeLogRunnerTest;

@SuppressWarnings({"JUnitTestCaseWithNoTests"})
public class PostgreSQLSampleChangeLogRunnerTest extends AbstractSimpleChangeLogRunnerTest {

    public PostgreSQLSampleChangeLogRunnerTest() throws Exception {
        super("pgsql", "jdbc:postgresql://localhost/liquibase");
    }
}
