package com.developer.krisi.tasker;

import com.developer.krisi.tasker.model.DateConverter;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Small - unit tests
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        assertEquals(formatter.format(converted), formatter.format(rightNow));
    }
}