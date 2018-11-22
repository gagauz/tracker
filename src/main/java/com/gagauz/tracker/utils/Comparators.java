package com.gagauz.tracker.utils;

import java.util.Comparator;

import com.gagauz.tracker.db.model.User;

public class Comparators {
    public static final Comparator<User> USER_BY_NAME_COMPARATOR = new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {
            if (null == o1) {
                return 1;
            }
            if (null == o2) {
                return -1;
            }
            return o1.getName().compareTo(o2.getName());
        }
    };
}
