package com.example.chthp00108.myapplication;

import org.junit.Test;


import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    MyMaths maths=new MyMaths();
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        //assertEquals(1,maths.Add(5,6));
        assertEquals(19,maths.Add(5,6,8));

    }
}