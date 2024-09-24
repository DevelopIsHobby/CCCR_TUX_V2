/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.dialect;

import org.hibernate.JDBCException;
import org.hibernate.PessimisticLockException;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.LimitHelper;
import org.hibernate.engine.spi.RowSelection;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.LockTimeoutException;
import org.hibernate.exception.spi.SQLExceptionConversionDelegate;
import org.hibernate.exception.spi.TemplatedViolatedConstraintNameExtracter;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.JdbcExceptionHelper;
import org.hibernate.type.StandardBasicTypes;
import org.jboss.logging.Logger;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MachbaseDialect extends Dialect {
    private static final CoreMessageLogger LOG = Logger.getMessageLogger(
            CoreMessageLogger.class,
            MachbaseDialect.class.getName()
    );

    private static final AbstractLimitHandler LIMIT_HANDLER = new AbstractLimitHandler() {
        @Override
        public String processSql(String sql, RowSelection selection) {
            final boolean hasOffset = LimitHelper.hasFirstRow( selection );
            return sql + (hasOffset ? " limit ?, ?" : " limit ?");
        }

        @Override
        public boolean supportsLimit() {
            return true;
        }

        @Override
        public boolean bindLimitParametersInReverseOrder() {
            return true;
        }
    };

    private static final Pattern SQL_STATEMENT_TYPE_PATTERN = Pattern.compile("^(?:\\/\\*.*?\\*\\/)?\\s*(select|insert|delete)\\s+.*?", Pattern.CASE_INSENSITIVE);

    public MachbaseDialect() {
        super();
        this.registerCharacterTypeMappings();
        this.registerNumericTypeMappings();
        this.registerDateTimeTypeMappings();
        this.registerLargeObjectTypeMappings();
        this.registerReverseHibernateTypeMappings();
        this.registerFunctions();
        this.registerDefaultProperties();
    }

    protected void registerCharacterTypeMappings() {
        this.registerColumnType(Types.VARCHAR, 4000L, "varchar($l)");
    }

    protected void registerNumericTypeMappings() {
        this.registerColumnType(Types.SMALLINT, "short");
        this.registerColumnType(2201,           "ushort");
        this.registerColumnType(Types.INTEGER,  "integer");
        this.registerColumnType(2202,           "uinteger");
        this.registerColumnType(Types.BIGINT,   "long");
        this.registerColumnType(2203,           "ulong");
        this.registerColumnType(Types.FLOAT,	"float");
        this.registerColumnType(Types.DOUBLE,   "double");
    }

    protected void registerDateTimeTypeMappings() {
        this.registerColumnType(Types.TIMESTAMP, "datetime");
    }

    protected void registerLargeObjectTypeMappings() {
        this.registerColumnType(2104,         "ipv4");
        this.registerColumnType(2106,         "ipv6");
        this.registerColumnType(2100,         "text");
        this.registerColumnType(Types.BLOB,   "blob");
        this.registerColumnType(Types.CLOB,   "clob");
        this.registerColumnType(Types.BINARY, "binary");
    }

    protected void registerReverseHibernateTypeMappings() {
    }

    protected void registerFunctions() {
        this.registerFunction("abs",             new StandardSQLFunction("abs"));
        this.registerFunction("add_time",        new StandardSQLFunction("add_time"));
        this.registerFunction("avg",             new StandardSQLFunction("avg", StandardBasicTypes.DOUBLE));
        this.registerFunction("bitand",          new StandardSQLFunction("bitand"));
        this.registerFunction("date_trunc",      new StandardSQLFunction("date_trunc"));
        this.registerFunction("dayofweek",       new StandardSQLFunction("dayofweek", StandardBasicTypes.INTEGER));
        this.registerFunction("decode",          new StandardSQLFunction("decode", StandardBasicTypes.STRING));
        this.registerFunction("first",           new StandardSQLFunction("first"));
        this.registerFunction("last",            new StandardSQLFunction("last"));
        this.registerFunction("from_unixtime",   new StandardSQLFunction("from_unixtime"));
        this.registerFunction("from_timestamp",  new StandardSQLFunction("from_timestamp"));
        this.registerFunction("group_concat",    new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
        this.registerFunction("instr",           new StandardSQLFunction("instr", StandardBasicTypes.INTEGER));
        this.registerFunction("least",           new StandardSQLFunction("least"));
        this.registerFunction("greatest",        new StandardSQLFunction("greatest"));
        this.registerFunction("length",          new StandardSQLFunction("length", StandardBasicTypes.INTEGER));
        this.registerFunction("lower",           new StandardSQLFunction("lower", StandardBasicTypes.STRING));
        this.registerFunction("lpad",            new StandardSQLFunction("lpad", StandardBasicTypes.STRING));
        this.registerFunction("rpad",            new StandardSQLFunction("rpad", StandardBasicTypes.STRING));
        this.registerFunction("ltrim",           new StandardSQLFunction("ltrim", StandardBasicTypes.STRING));
        this.registerFunction("rtrim",           new StandardSQLFunction("rtrim", StandardBasicTypes.STRING));
        this.registerFunction("max",             new StandardSQLFunction("max"));
        this.registerFunction("min",             new StandardSQLFunction("min"));
        this.registerFunction("nvl",             new StandardSQLFunction("nvl"));
        this.registerFunction("round",           new StandardSQLFunction("round"));
        this.registerFunction("rownum",          new NoArgSQLFunction("rownum", StandardBasicTypes.INTEGER, false));
        this.registerFunction("seriesnum",       new NoArgSQLFunction("seriesnum", StandardBasicTypes.LONG, false));
        this.registerFunction("stddev",          new StandardSQLFunction("stddev", StandardBasicTypes.DOUBLE));
        this.registerFunction("stddev_pop",      new StandardSQLFunction("stddev_pop", StandardBasicTypes.DOUBLE));
        this.registerFunction("substr",          new StandardSQLFunction("substr", StandardBasicTypes.STRING));
        this.registerFunction("substring_index", new StandardSQLFunction("substring_index", StandardBasicTypes.STRING));
        this.registerFunction("sum",             new StandardSQLFunction("sum"));
        this.registerFunction("sysdate",         new NoArgSQLFunction("sysdate", StandardBasicTypes.TIMESTAMP, false));
        this.registerFunction("now",             new NoArgSQLFunction("sysdate", StandardBasicTypes.TIMESTAMP, false));
        this.registerFunction("to_char",         new StandardSQLFunction("to_char", StandardBasicTypes.STRING));
        this.registerFunction("to_date",         new StandardSQLFunction("to_date"));
        this.registerFunction("to_date_safe",    new StandardSQLFunction("to_date_safe", StandardBasicTypes.TIMESTAMP));
        this.registerFunction("to_hex",          new StandardSQLFunction("to_hex"));
        this.registerFunction("to_ipv4",         new StandardSQLFunction("to_ipv4"));
        this.registerFunction("to_ipv4_safe",    new StandardSQLFunction("to_ipv4_safe"));
        this.registerFunction("to_ipv6",         new StandardSQLFunction("to_ipv6"));
        this.registerFunction("to_ipv6_safe",    new StandardSQLFunction("to_ipv6_safe"));
        this.registerFunction("to_number",       new StandardSQLFunction("to_number"));
        this.registerFunction("to_number_safe",  new StandardSQLFunction("to_number_safe"));
        this.registerFunction("to_timestamp",    new StandardSQLFunction("to_timestamp", StandardBasicTypes.TIMESTAMP));
        this.registerFunction("trunc",           new StandardSQLFunction("trunc", StandardBasicTypes.DOUBLE));
        this.registerFunction("ts_change_count", new StandardSQLFunction("ts_change_count", StandardBasicTypes.INTEGER));
        this.registerFunction("unix_timestamp",  new StandardSQLFunction("unix_timestamp", StandardBasicTypes.TIMESTAMP));
        this.registerFunction("upper",           new StandardSQLFunction("upper", StandardBasicTypes.STRING));
        this.registerFunction("variance",        new StandardSQLFunction("variance", StandardBasicTypes.DOUBLE));
        this.registerFunction("var_pop",         new StandardSQLFunction("var_pop", StandardBasicTypes.DOUBLE));
        this.registerFunction("year",            new StandardSQLFunction("year", StandardBasicTypes.INTEGER));
        this.registerFunction("month",           new StandardSQLFunction("month", StandardBasicTypes.INTEGER));
        this.registerFunction("day",             new StandardSQLFunction("day", StandardBasicTypes.INTEGER));
        this.registerFunction("isnan",           new StandardSQLFunction("isnan", StandardBasicTypes.BOOLEAN));
        this.registerFunction("isinf",           new StandardSQLFunction("isinf", StandardBasicTypes.BOOLEAN));
    }

    protected void registerDefaultProperties() {
//		this.getDefaultProperties().setProperty("hibernate.jdbc.use_streams_for_binary", "true");
//		this.getDefaultProperties().setProperty("hibernate.jdbc.batch_size", "15");
//		this.getDefaultProperties().setProperty("hibernate.jdbc.use_get_generated_keys", "false");
//		this.getDefaultProperties().setProperty("hibernate.jdbc.batch_versioned_data", "false");
    }

    @Override
    public String getAddColumnString() {
        return "add column";
    }

    @Override
    public String getForUpdateString() {
        return " ";
    }

    @Override
    public LimitHandler getLimitHandler() {
        return LIMIT_HANDLER;
    }

    @Override
    public ViolatedConstraintNameExtracter getViolatedConstraintNameExtracter() {
        return EXTRACTER;
    }

    private static final ViolatedConstraintNameExtracter EXTRACTER = new TemplatedViolatedConstraintNameExtracter() {
        /**
         * Extract the name of the violated constraint from the given SQLException.
         *
         * @param sqle The exception that was the result of the constraint violation.
         * @return The extracted constraint name.
         */
        @Override
        protected String doExtractConstraintName(SQLException sqle) throws NumberFormatException {
            String constraintName = null;
            // 23000: Check constraint violation: {0}
            // 23001: Unique index or primary key violation: {0}
            if ( sqle.getSQLState().startsWith( "23" ) ) {
                final String message = sqle.getMessage();
                final int idx = message.indexOf( "violation: " );
                if ( idx > 0 ) {
                    constraintName = message.substring( idx + "violation: ".length() );
                }
            }
            return constraintName;
        }
    };

    @Override
    public SQLExceptionConversionDelegate buildSQLExceptionConversionDelegate() {
        SQLExceptionConversionDelegate delegate = super.buildSQLExceptionConversionDelegate();
        if (delegate == null) {
            delegate = new SQLExceptionConversionDelegate() {
                @Override
                public JDBCException convert(SQLException sqlException, String message, String sql) {
                    final int errorCode = JdbcExceptionHelper.extractErrorCode( sqlException );

                    if ( errorCode == 8005 || errorCode == 8043 ) {
                        // 8005 - connection, 8043 - DDL
                        return new LockTimeoutException(message, sqlException, sql);
                    }

                    if ( errorCode == 153 ) {
                        // 153 - Failed to lock mutex
                        return new PessimisticLockException(message, sqlException, sql);
                    }

                    if ( errorCode == 2042 || errorCode == 2284 ) {
                        // 2042 - Expression cannot have a NULL value,
                        // 2284 - Internal NULL value exists in the condition expression
                        final String constraintName = getViolatedConstraintNameExtracter().extractConstraintName( sqlException );
                        return new ConstraintViolationException( message, sqlException, sql, constraintName );
                    }

                    return null;
                }
            };
        }
        return delegate;
    }

    @Override
    public boolean supportsLockTimeouts() {
        return false;
    }

    @Override
    public boolean supportsCaseInsensitiveLike() {
        return true;
    }

    @Override
    public boolean canCreateSchema() {
        return false;
    }

    @Override
    public String getDropForeignKeyString() {
        return " ";
    }

    @Override
    public String getAddForeignKeyConstraintString(String constraintName,
                                                   String foreignKeyDefinition) {
        return " ";
    }

    @Override
    public String getAddForeignKeyConstraintString(String constraintName,
                                                   String[] foreignKey,
                                                   String referencedTable,
                                                   String[] primaryKey,
                                                   boolean referencesPrimaryKey) {
        return " ";
    }

    @Override
    public String getAddPrimaryKeyConstraintString(String constraintName) {
        return " ";
    }

    @Override
    public boolean supportsColumnCheck() {
        return false;
    }

    @Override
    public boolean supportsTableCheck() {
        return false;
    }

    @Override
    public boolean supportsCascadeDelete() {
        return false;
    }

    @Override
    public boolean supportsEmptyInList() {
        return false;
    }

    @Override
    public boolean areStringComparisonsCaseInsensitive() {
        return true;
    }

    @Override
    public boolean useInputStreamToInsertBlob() {
        return false;
    }

    @Override
    public boolean replaceResultVariableInOrderByClauseWithPosition() {
        return true;
    }

    @Override
    public boolean supportsSubselectAsInPredicateLHS() {
        return false;
    }

    @Override
    public boolean supportsUnboundedLobLocatorMaterialization() {
        return false;
    }

    @Override
    public boolean supportsTupleDistinctCounts() {
        return false;
    }

    @Override
    public boolean supportsUnique() {
        return false;
    }

    @Override
    public boolean supportsUniqueConstraintInCreateAlterTable() {
        return false;
    }

    @Override
    public boolean supportsTupleCounts() {
        return false;
    }

    @Override
    public boolean supportsNamedParameters(DatabaseMetaData databaseMetaData) throws SQLException {
        return false;
    }

    @Override
    public boolean supportsNationalizedTypes() {
        return false;
    }

    // Overridden informational metadata ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean supportsLobValueChangePropogation() {
        return false;
    }

    @Override
    public boolean supportsTuplesInSubqueries() {
        return false;
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new IdentityColumnSupportImpl();
    }

    @Override
    public String getQueryHintString(String query, String hints) {
        String statementType = statementType(query);

        final int pos = query.indexOf( statementType );
        if ( pos > -1 ) {
            final StringBuilder buffer = new StringBuilder( query.length() + hints.length() + 8 );
            if ( pos > 0 ) {
                buffer.append( query.substring( 0, pos ) );
            }
            buffer
                    .append( statementType )
                    .append( " /*+ " )
                    .append( hints )
                    .append( " */" )
                    .append( query.substring( pos + statementType.length() ) );
            query = buffer.toString();
        }

        return query;
    }

    protected String statementType(String sql) {
        Matcher matcher = SQL_STATEMENT_TYPE_PATTERN.matcher( sql );

        if(matcher.matches() && matcher.groupCount() == 1) {
            return matcher.group(1);
        }

        throw new IllegalArgumentException( "Can't determine SQL statement type for statement: " + sql );
    }
}
