package net.dogemines.framework.data.database;

import java.util.Map;

public enum SQLDataType {
    //string
    CHAR(String.class, true),
    VARCHAR(String.class, true),
    TEXT(String.class),

    //numbers
    INT(int.class),
    FLOAT(float.class),
    DOUBLE(double.class),

    DEFAULT(null);

    private final Class<?> type;
    private final boolean hasSizeParam;

    public Class<?> getType() {
        return type;
    }
    public boolean hasSizeParam() {
        return hasSizeParam;
    }

    SQLDataType(Class<?> type, boolean hasSizeParam) {
        this.type = type;
        this.hasSizeParam = hasSizeParam;
    }
    SQLDataType(Class<?> type) {
        this(type, false);
    }

    public static final Map<Class<?>, SQLDataType> DEFAULT_FOR_CLASS = Map.of(
            String.class, TEXT,
            int.class, INT,
            float.class, FLOAT,
            double.class, DOUBLE
    );
}
