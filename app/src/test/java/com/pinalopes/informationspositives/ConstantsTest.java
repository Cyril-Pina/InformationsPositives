package com.pinalopes.informationspositives;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstantsTest {

    @Test
    public void constantsFieldsTest() {
        assertEquals("drawable", Constants.DRAWABLE);
        assertEquals("com.pinalopes.informationspositives", Constants.PACKAGE_NAME);
        assertEquals("filters", Constants.FILTERS);
    }
}