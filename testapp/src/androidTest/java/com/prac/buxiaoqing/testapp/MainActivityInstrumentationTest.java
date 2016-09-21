package com.prac.buxiaoqing.testapp;

/**
 * author：buxiaoqing on 9/21/16 10:25
 * Just do IT(没有梦想,何必远方)
 *
 * Espresso测试
 *
 */

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.text.TextUtils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityInstrumentationTest {

    public static final String PRE_FIXED = "Hello,";
    private static final String STRING_TO_BE_TYPED = "Peter";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test(timeout = 5000)
    public void sayHello() {
//        onView(withId(R.id.editText)).perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard()); //line 1
//
//        onView(withText("Say hello!")).perform(click()); //line 2
//
//        String expectedText = PRE_FIXED + STRING_TO_BE_TYPED + "!";
//        onView(withId(R.id.textView)).check(matches(withText(expectedText))); //line 3
        //onView  只能查找那些已经先是在屏幕上的View,对于那些ListView 或者GridView 上的还没有显示到屏幕上的数据,就用onData来查找。

        //AdapterView   查找数据
        //onData(allOf(is(instanceOf(MainActivity.SearchItem.class)),teacherSearchItemWithName("item:10"))).inAdapterView(withId(R.id.listview));

//        onView(allOf(withText("halo!"), hasSibling(withText("Say hello!"))))
//                .perform(click());

        onData(teacherSearchItemWithName("item:20"))
                .onChildView(withId(R.id.tv_size))
                .perform(click());


    }

    /**
     * 查找指定关键字的搜索条件
     * @param name 需要搜索的关键字
     */
    public static Matcher<Object> teacherSearchItemWithName(final String name) {
        return new BoundedMatcher<Object, MainActivity.SearchItem>(MainActivity.SearchItem.class) {
            @Override
            protected boolean matchesSafely(MainActivity.SearchItem item) {
                return item != null
                        && !TextUtils.isEmpty(item.getKeyword())
                        && item.getKeyword().equals(name);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("SearchItem has Name: " + name);
            }
        };
    }

    @After
    public void finish() {

    }

//
//    @AfterClass
//    public void after() {
//
//    }
//
//
//    @BeforeClass
//    public void before() {
//
//    }


}