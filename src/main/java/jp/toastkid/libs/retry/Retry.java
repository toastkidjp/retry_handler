package jp.toastkid.libs.retry;

/**
 * Retry handler.
 *
 * <h3>Usage</h3>
 * <h4>Use instance</h4>
 * <pre>
 * new Retry.Builder().setAction((i) -> {
 *          if (i == 1) {
 *              System.out.println("retry.");
 *              return;
 *          }
 *          throw new RuntimeException("Please retry.");
 *      }).setMaxRetry(3).setIntervalMs(1000L).build().attempt();
 * </pre>
 *
 * <h4>Use static</h4>
 * <pre>
 * Retry.attempt((i) -> {System.out.println(i + " static retry.");}, 2);
 * </pre>
 * @author Toast kid
 *
 */
public class Retry {

    /**
     * use for implementing Command pattern,
     * @author Toast kid
     *
     */
    public interface Action {
        public void action(int retrying);
    }

    /** retain retrying action. */
    private final Action action;

    /** max retrying count. */
    private final int maxRetry;

    /** suppress print warning. default print stack-trace. */
    private final boolean suppressWarning;

    /** retry interval milliseconds. default 0ms. */
    private final long intervalMs;

    /**
     * call from only internal builder.
     * @param b Builder.
     */
    private Retry(final Builder b) {
        this.action          = b.action != null ? b.action : (i) -> {};
        this.maxRetry        = b.maxRetry;
        this.suppressWarning = b.suppressWarning;
        this.intervalMs      = b.intervalMs;
    }

    /**
     * instance builder.
     * @author Toast kid
     *
     */
    public static final class Builder {

        private Action action;

        private int maxRetry;

        private boolean suppressWarning;

        private long intervalMs;

        public Builder setAction(final Action action) {
            this.action = action;
            return this;
        }

        public Builder setMaxRetry(final int maxRetry) {
            if (maxRetry < 1) {
                throw new IllegalArgumentException("You should pass plus integere value.");
            }
            this.maxRetry = maxRetry;
            return this;
        }

        public Builder setSuppressWarning(final boolean suppressWarning) {
            this.suppressWarning = suppressWarning;
            return this;
        }

        /**
         * If you attempt to fetch web contents, you should set interval.
         * @param intervalMs
         * @return.
         */
        public Builder setIntervalMs(final long intervalMs) {
            this.intervalMs = intervalMs;
            return this;
        }

        public Retry build() {
            return new Retry(this);
        }
    }

    /**
     * instant retry.
     * @param action
     * @param maxRetry
     */
    public static void attempt(final Action action, final int maxRetry) {
        attempt(action, maxRetry, 0L);
    }

    /**
     * instant retry with interval ms.
     * @param action
     * @param maxRetry
     * @param intervalMs waiting ms every attemption.
     */
    public static void attempt(final Action action, final int maxRetry, final long intervalMs) {
        new Retry.Builder()
            .setAction(action)
            .setMaxRetry(maxRetry)
            .setIntervalMs(intervalMs)
            .build().attempt();
    }

    /**
     * do retrying.
     */
    public void attempt() {
        for (int i = 0; i < maxRetry; i++) {
            try {
                action.action(i);
                return;
            } catch (final Exception e) {
                if (!suppressWarning) {
                    e.printStackTrace();
                }
                sleep();
            }
        }
    }

    /**
     * sleep interval.
     * .
     */
    private void sleep() {
        try {
            Thread.sleep(intervalMs);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

}
