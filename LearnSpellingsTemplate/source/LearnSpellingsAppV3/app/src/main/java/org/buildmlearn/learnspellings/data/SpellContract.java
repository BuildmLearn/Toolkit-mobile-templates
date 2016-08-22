package org.buildmlearn.learnspellings.data;

import android.provider.BaseColumns;

/**
 * @brief Contains database contracts for spell template's app.
 *
 * Created by Anupam (opticod) on 1/6/16.
 */

class SpellContract {

    public static final class Spellings implements BaseColumns {

        public static final String TABLE_NAME = "spellings";

        public static final String WORD = "word";
        public static final String MEANING = "meaning";
        public static final String ANSWERED = "answered";
        public static final String ATTEMPTED = "attempted";
    }
}
