package com.material.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public abstract class InventoryConnectionPool {
    protected DataSource ds;

    public InventoryConnectionPool() {
        try {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/MESDB");
        } catch (Exception e) {
            throw new RuntimeException("JNDI DataSource lookup failed", e);
        }
    }
}
