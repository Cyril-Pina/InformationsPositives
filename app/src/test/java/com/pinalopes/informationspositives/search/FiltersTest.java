package com.pinalopes.informationspositives.search;

import com.pinalopes.informationspositives.search.model.CategoryFilter;
import com.pinalopes.informationspositives.search.model.Filters;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class FiltersTest {

    private static final String name = "Sport";
    private static final String pictureRes = "img_sport";

    private Filters filters;

    @Before
    public void initCategoryFilter() {
        filters = new Filters();
    }

    @Test
    public void getFiltersTest() {
        assertNull(filters.getBeginningDate());
        assertNull(filters.getEndingDate());
        assertEquals(7, filters.getCategories().length);
        assertFalse(filters.getCategories()[0]);
        assertFalse(filters.getCategories()[1]);
        assertFalse(filters.getCategories()[2]);
        assertFalse(filters.getCategories()[3]);
        assertFalse(filters.getCategories()[4]);
        assertFalse(filters.getCategories()[5]);
        assertFalse(filters.getCategories()[6]);
    }

    @Test
    public void setFiltersTest() {
        filters.setBeginningDate("17/12/2006");
        filters.setEndingDate("17/12/2008");
        filters.getCategories()[0] = true;
        filters.getCategories()[1] = true;
        filters.getCategories()[2] = true;
        filters.getCategories()[3] = true;
        filters.getCategories()[4] = true;
        filters.getCategories()[5] = true;
        filters.getCategories()[6] = true;

        assertEquals("17/12/2006", filters.getBeginningDate());
        assertEquals("17/12/2008", filters.getEndingDate());
        assertTrue(filters.getCategories()[0]);
        assertTrue(filters.getCategories()[1]);
        assertTrue(filters.getCategories()[2]);
        assertTrue(filters.getCategories()[3]);
        assertTrue(filters.getCategories()[4]);
        assertTrue(filters.getCategories()[5]);
        assertTrue(filters.getCategories()[6]);

        filters.setCategoriesValue(new Boolean[] { false, false, false, false, false, false, false });
        assertFalse(filters.getCategories()[0]);
        assertFalse(filters.getCategories()[1]);
        assertFalse(filters.getCategories()[2]);
        assertFalse(filters.getCategories()[3]);
        assertFalse(filters.getCategories()[4]);
        assertFalse(filters.getCategories()[5]);
        assertFalse(filters.getCategories()[6]);
    }
}
