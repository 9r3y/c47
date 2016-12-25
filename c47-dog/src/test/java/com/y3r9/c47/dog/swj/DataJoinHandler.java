package com.y3r9.c47.dog.swj;

import com.y3r9.c47.dog.swj.model.parallext.AbstractJoinHandler;

/**
 * The class DataJoinHandler.
 *
 * @version 1.0
 */
final class DataJoinHandler extends AbstractJoinHandler<DataResult, Void> {

    /** The backspace char. */
    private static final char CARRIAGE_RETURN_CHAR = '\r';

    /** The erase char. */
    private static final char ERASE_CHAR = ' ';

    /** The backspace char. */
    private static final char BACKSPACE_CHAR = '\b';

    /** The progress chars. */
    private static final char[] PROGRESS_CHARS = {'|', '/', '-', '\\'};

    /** The last progress index, guarded by lock. */
    private int lastProgressIndex = 0;

    /** The constant PROGRESS_FREQ. */
    private static final int PROGRESS_FREQ = 10;

    /**
     * Instantiates a new abstract join handler.
     *
     * @param name the name
     */
    protected DataJoinHandler(final String name) {
        super(name);
    }

    @Override
    public void consumeJoinResult(final DataResult resultData) {
//        System.out.println(resultData.getData().getWork());
//        updateProgress();
    }

    /**
     * Update progress.
     */
    public void updateProgress() {
        // Get the seconds, roughly equivalent to divided by 1000.
        long now = System.currentTimeMillis() >> PROGRESS_FREQ;

        int index = (int) (now % PROGRESS_CHARS.length);

        if (index != lastProgressIndex) {
            System.out.print(BACKSPACE_CHAR);
            System.out.print(PROGRESS_CHARS[index]);
            System.out.flush();
            lastProgressIndex = index;
        }
    }
}
