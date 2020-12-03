package com.pinalopes.informationspositives.search;

import com.pinalopes.informationspositives.search.model.CategoryFilter;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class CategoryFilterTest {

    private static final String name = "Sport";
    private static final String pictureRes = "img_sport";

    private CategoryFilter categoryFilter;

    @Before
    public void initCategoryFilter() {
        categoryFilter = new CategoryFilter(name, pictureRes);
    }

    @Test
    public void categoryFilterTest() {
        assertEquals(name, categoryFilter.getName());
        assertEquals(pictureRes, categoryFilter.getPictureRes());
        assertFalse(categoryFilter.isSelected());
    }

    @Test
    public void changeSelectionState() {
        assertFalse(categoryFilter.isSelected());
        categoryFilter.changeSelectionState();
        assertTrue(categoryFilter.isSelected());
    }
}
