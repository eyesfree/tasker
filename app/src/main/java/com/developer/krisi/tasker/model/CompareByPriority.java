package com.developer.krisi.tasker.model;

import java.util.Comparator;

public class CompareByPriority implements Comparator<Task> {
        // Used for sorting in ascending order of priority
        public int compare(Task a, Task b)
        {
            return a.getPriority() - b.getPriority();
        }
}
