/*
 * Copyright (c) 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Arnaud Kervern
 *     Florent Guillaume
 */
package org.eclipse.ecr.core.storage.sql.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.ecr.core.storage.sql.jdbc.db.Column;

/**
 * Helper to provide SQL migration calls while adding a column.
 *
 * @since 5.4.2
 */
public class TableUpgrader {

    protected static class TableUpgrade {
        public final String tableKey;

        public final String columnName;

        public final String sqlProcedure;

        public final String testProp;

        public TableUpgrade(String tableKey, String columnName,
                String sqlProcedure, String testProp) {
            this.tableKey = tableKey;
            this.columnName = columnName;
            this.sqlProcedure = sqlProcedure;
            this.testProp = testProp;
        }
    }

    protected List<TableUpgrade> tableUpgrades = new ArrayList<TableUpgrade>();

    private JDBCMapper mapper;

    private static final Log log = LogFactory.getLog(TableUpgrader.class);

    public TableUpgrader(JDBCMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Add a couple table/column associated with a sql procedure to be executed
     * when the column is added and a a test flag to force his execution.
     *
     * @param tableKey table name
     * @param columnName desired added column
     * @param sqlProcedure sql procedure name
     * @param testProp test flag name
     */
    public void add(String tableKey, String columnName, String sqlProcedure,
            String testProp) {
        tableUpgrades.add(new TableUpgrade(tableKey, columnName, sqlProcedure,
                testProp));
    }

    /**
     * Check if there is an added column that match with a upgrade process. If
     * one exists, it executes the associated sql in the category. If not,
     * nothing happend.
     *
     * @param tableKey table name
     * @param addedColumns list of added column
     * @throws SQLException Exception thrown by JDBC
     */
    public void upgrade(String tableKey, List<Column> addedColumns)
            throws SQLException {
        for (TableUpgrade upgrade : tableUpgrades) {
            if (!upgrade.tableKey.equals(tableKey)) {
                continue;
            }
            boolean doUpgrade;
            if (addedColumns == null) {
                // table created
                doUpgrade = mapper.testProps.containsKey(upgrade.testProp);
            } else {
                // columns added
                doUpgrade = false;
                for (Column col : addedColumns) {
                    if (col.getKey().equals(upgrade.columnName)) {
                        doUpgrade = true;
                        break;
                    }
                }
            }
            if (doUpgrade) {
                log.info("Upgrading table: " + tableKey);
                mapper.sqlInfo.executeSQLStatements(upgrade.sqlProcedure,
                        mapper);
            }
        }
    }
}
