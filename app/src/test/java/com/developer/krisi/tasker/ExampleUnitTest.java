package com.developer.krisi.tasker;

import com.developer.krisi.tasker.model.DateConverter;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testDateConverter() {
        Calendar today = Calendar.getInstance();
        Date rightNow = today.getTime();
        String timestamp = DateConverter.dateToTimestamp(rightNow);
        Date converted = DateConverter.fromTimestamp(timestamp);
        assertEquals(converted, rightNow);
    }
}