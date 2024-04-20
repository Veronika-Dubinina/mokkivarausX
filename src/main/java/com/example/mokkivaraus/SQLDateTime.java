package com.example.mokkivaraus;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

class SQLDateTime extends Timestamp {
    public SQLDateTime(long time) {
        super(time);
    }
    public SQLDateTime(Timestamp tst) {
        this(tst.getTime());
    }

    @Override
    public String toString() {
        // formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return this.toLocalDateTime().format(formatter);
    }
}
