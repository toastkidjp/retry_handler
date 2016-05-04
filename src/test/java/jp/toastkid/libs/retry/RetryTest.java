/**
 *
 */
package jp.toastkid.libs.retry;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link Retry}'s test case.
 * @author Toast kid
 *
 */
public class RetryTest {

    /** test object. */
    private Retry retry;

    /**
     * @throws java.lang.Exception.
     */
    @Before
    public void setUp() throws Exception {
        retry = new Retry.Builder()
                .setAction((i) -> {System.out.println("retry.");})
                .setMaxRetry(2).build();
    }

    /**
     * test {@link jp.toastkid.libs.retry.Retry#attempt(jp.toastkid.libs.retry.Retry.Action, int)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInitMinus() {
        new Retry.Builder().setAction((i) -> {}).setMaxRetry(-1).build();
    }

    /**
     * test {@link jp.toastkid.libs.retry.Retry#attempt(jp.toastkid.libs.retry.Retry.Action, int)}.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testInitZero() {
        new Retry.Builder().setAction((i) -> {}).setMaxRetry(0).build();
    }

    /**
     * test {@link jp.toastkid.libs.retry.Retry#attempt(jp.toastkid.libs.retry.Retry.Action, int)}.
     */
    @Test
    public void testStaticRetry() {
        final long start = System.currentTimeMillis();
        Retry.attempt((i) -> {System.out.println(i + " static retry.");}, 2);
        Retry.attempt((i) -> {
            if (i == 1) {
                System.out.println(i + " retry.");
                return;
            }
            assertTrue(i < 2);
            throw new RuntimeException("Static retry." + i);
            }, 2, 1000L);
        assertTrue(1000L < System.currentTimeMillis() - start);
    }

    /**
     * test {@link jp.toastkid.libs.retry.Retry#action()}.
     */
    @Test
    public void testRetrySimple() {
        retry.attempt();
    }

    /**
     * test {@link jp.toastkid.libs.retry.Retry#action()}.
     */
    @Test
    public void testRetry() {
        new Retry.Builder().setAction((i) -> {
            if (i == 1) {
                System.out.println(i + " retry.");
                return;
            }
            assertTrue(i < 2);
            throw new RuntimeException("Please retry." + i);
        }).setMaxRetry(3).setSuppressWarning(false).build().attempt();
    }

}
