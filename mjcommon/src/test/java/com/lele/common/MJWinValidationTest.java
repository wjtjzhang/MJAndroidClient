package com.lele.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lele.entity.Cart;
import com.lele.entity.CartType;

public class MJWinValidationTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test2CartsWin1() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test2CartsWin2() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.S, 1));
        carts.add(new Cart(CartType.S, 1));

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test2CartsNotWin() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));

        assertFalse(MJWinValidation.isWin(carts));
    }

    @Test
    public void test5CartsWin1() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test5CartsWin2() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test5CartsWin3() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test5CartsWin4() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test5CartsWin5() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 3));

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test5CartsWin6() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.W, 4));

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test5CartsNotWin7() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.S, 2));
        carts.add(new Cart(CartType.S, 3));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));

        assertFalse(MJWinValidation.isWin(carts));
    }

    @Test
    public void test8CartsWin1() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.S, 1));
        carts.add(new Cart(CartType.S, 2));
        carts.add(new Cart(CartType.S, 3));
        carts.add(new Cart(CartType.S, 3));
        carts.add(new Cart(CartType.S, 3));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test8CartsWin2() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.S, 1));
        carts.add(new Cart(CartType.S, 2));
        carts.add(new Cart(CartType.S, 3));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test8CartsWin3() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.S, 1));
        carts.add(new Cart(CartType.S, 2));
        carts.add(new Cart(CartType.S, 3));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test8CartsWin4() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test8CartsWin5() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test8CartsWin6() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test8CartsWin7() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test8CartsWin8() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test8CartsWin9() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test8CartsNotWin10() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.sort(new InCartComparator());

        assertFalse(MJWinValidation.isWin(carts));
    }

    @Test
    public void test8CartsWin11() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test11CartsWin1() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 5));
        carts.add(new Cart(CartType.S, 5));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test11CartsWin2() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.S, 7));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 9));
        carts.add(new Cart(CartType.S, 5));
        carts.add(new Cart(CartType.S, 5));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test11CartsWin3() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.S, 7));
        carts.add(new Cart(CartType.S, 7));
        carts.add(new Cart(CartType.S, 5));
        carts.add(new Cart(CartType.S, 6));
        carts.add(new Cart(CartType.S, 7));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test11CartsWin4() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.W, 5));
        carts.add(new Cart(CartType.W, 6));
        carts.add(new Cart(CartType.T, 1));
        carts.add(new Cart(CartType.T, 1));
        carts.add(new Cart(CartType.S, 5));
        carts.add(new Cart(CartType.S, 6));
        carts.add(new Cart(CartType.S, 7));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test11CartsWin5() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.S, 1));
        carts.add(new Cart(CartType.S, 2));
        carts.add(new Cart(CartType.S, 3));
        carts.add(new Cart(CartType.S, 9));
        carts.add(new Cart(CartType.S, 9));
        carts.add(new Cart(CartType.S, 9));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test11CartsWin6() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 9));
        carts.add(new Cart(CartType.S, 9));
        carts.add(new Cart(CartType.S, 9));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test14CartsWin1() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.S, 7));
        carts.add(new Cart(CartType.S, 7));
        carts.add(new Cart(CartType.S, 7));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 9));
        carts.add(new Cart(CartType.S, 9));
        carts.add(new Cart(CartType.S, 9));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test14CartsWin2() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.S, 7));
        carts.add(new Cart(CartType.S, 7));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 9));
        carts.add(new Cart(CartType.S, 9));
        carts.add(new Cart(CartType.S, 9));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test14CartsWin3() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.S, 7));
        carts.add(new Cart(CartType.S, 7));
        carts.add(new Cart(CartType.S, 7));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 9));
        carts.add(new Cart(CartType.S, 9));
        carts.add(new Cart(CartType.S, 9));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test14CartsWin4() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.S, 6));
        carts.add(new Cart(CartType.S, 7));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 9));
        carts.add(new Cart(CartType.S, 9));
        carts.add(new Cart(CartType.S, 9));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test14CartsWin5() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.S, 1));
        carts.add(new Cart(CartType.S, 2));
        carts.add(new Cart(CartType.S, 3));
        carts.add(new Cart(CartType.S, 3));
        carts.add(new Cart(CartType.S, 3));
        carts.add(new Cart(CartType.S, 4));
        carts.add(new Cart(CartType.S, 5));
        carts.add(new Cart(CartType.S, 6));
        carts.add(new Cart(CartType.S, 7));
        carts.add(new Cart(CartType.S, 8));
        carts.add(new Cart(CartType.S, 9));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }

    @Test
    public void test14CartsWin6() {
        List<Cart> carts = new ArrayList<Cart>();
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 1));
        carts.add(new Cart(CartType.W, 2));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 3));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.W, 4));
        carts.add(new Cart(CartType.W, 4));
        carts.sort(new InCartComparator());

        assertTrue(MJWinValidation.isWin(carts));
    }
}
