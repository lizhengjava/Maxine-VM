/*
 * Copyright (c) 2007 Sun Microsystems, Inc.  All rights reserved.
 *
 * Sun Microsystems, Inc. has intellectual property rights relating to technology embodied in the product
 * that is described in this document. In particular, and without limitation, these intellectual property
 * rights may include one or more of the U.S. patents listed at http://www.sun.com/patents and one or
 * more additional patents or pending patent applications in the U.S. and in other countries.
 *
 * U.S. Government Rights - Commercial software. Government users are subject to the Sun
 * Microsystems, Inc. standard license agreement and applicable provisions of the FAR and its
 * supplements.
 *
 * Use is subject to license terms. Sun, Sun Microsystems, the Sun logo, Java and Solaris are trademarks or
 * registered trademarks of Sun Microsystems, Inc. in the U.S. and other countries. All SPARC trademarks
 * are used under license and are trademarks or registered trademarks of SPARC International, Inc. in the
 * U.S. and other countries.
 *
 * UNIX is a registered trademark in the U.S. and other countries, exclusively licensed through X/Open
 * Company, Ltd.
 */
package com.sun.max.vm.cps.hotpath;

import java.util.concurrent.*;

import com.sun.max.profile.Metrics.*;
import com.sun.max.program.*;
import com.sun.max.program.option.*;
import com.sun.max.vm.*;
import com.sun.max.vm.cps.collect.*;
import com.sun.max.vm.cps.hotpath.compiler.*;
import com.sun.max.vm.cps.tir.*;
import com.sun.max.vm.runtime.*;

public class AsynchronousProfiler implements Runnable {

    public static enum CounterMetric {
        INTERPRETED_BYTECODES, TRUNKS, BRANCHES;
        public static long[] counters = new long[CounterMetric.values().length];
        public static void increment(CounterMetric metric) {
            counters[metric.ordinal()]++;
        }
        private static void print() {
            for (CounterMetric metric : CounterMetric.values()) {
                printKeyValueCount(metric.name(), counters[metric.ordinal()]);
            }
        }
    }

    public static OptionSet optionSet = new OptionSet();
    public static Option<Boolean> profile = optionSet.newBooleanOption("P", false, "(P)rofiles hotpath execution.");

    private static LinkedBlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>();

    public static void init() {
        if (MaxineVM.isHosted()) {
            FatalError.unexpected(AsynchronousProfiler.class.getName() + " should only be started at runtime");
        }
        Thread profilerThread = new Thread(new AsynchronousProfiler());
        profilerThread.setPriority(Thread.NORM_PRIORITY);
        profilerThread.setDaemon(true);
        profilerThread.start();
    }

    public void run() {
        try {
            while (true) {
                eventQueue.take().process();
            }
        } catch (InterruptedException e) {
            ProgramError.unexpected();
        }
    }

    public static void print() {
        try {
            // Consume all events before printing final results.
            while (eventQueue.size() > 0) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) { }

        TreeEvent.print();
        ExecuteEvent.print();
        CounterMetric.print();
    }

    private abstract static class Event {
        public abstract void process();
    }

    private abstract static class TreeEvent extends Event {
        private static final LinkedIdentityHashSet<TirTree> trees = new LinkedIdentityHashSet<TirTree>();
        private final TirTree tree;
        public TreeEvent(TirTree tree) {
            this.tree = tree;
            trees.add(tree);
        }

        public static void print() {
            Console.printThinDivider("Trees");
            for (TirTree tree : trees) {
                printKey("Tree", NameMap.nameOf(tree));
                printKey("Anchor", tree.anchor().toString());
                printKey("Iterations", tree.profile().iterations);
                printKey("Executions", tree.profile().executions);
                Console.printThinDivider();
            }
            Console.printThinDivider();
        }
    }

    private static void printKey(String key, Object value) {
        Console.println(key + " : " + value);
    }

    public static void printKeyValueCount(String key, long value) {
        float bytes = 0;
        String unit = "";
        if (value < 1000) {
            bytes = value;
        } else if (value < 1000 * 1000) {
            bytes = value / 1024f;
            unit = "k";
        } else {
            bytes = value / (1000f * 1000f);
            unit = "m";
        }
        printKey(key, String.format("%.2f %s", bytes, unit));
    }

    public static void writeKeyValueBytes(String key, long value) {
        float bytes = 0;
        String unit = "";
        if (value < 1024) {
            bytes = value;
            unit = "B";
        } else if (value < 1024 * 1024) {
            bytes = value / 1024f;
            unit = "KB";
        } else {
            bytes = value / (1024f * 1024f);
            unit = "MB";
        }
        printKey(key, String.format("%.2f %s", bytes, unit));
    }

    private static class ExecuteEvent extends TreeEvent {
        private static final Counter count = new Counter();

        private final Bailout bailout;

        public ExecuteEvent(TirTree tree) {
            super(tree);
            bailout = null;
        }

        public static void print() {
            Console.printThinDivider("Executions");
            printKey("execution", count.getCount());
            Console.printThinDivider();
        }

        public ExecuteEvent(TirTree tree, Bailout bailout) {
            super(tree);
            this.bailout = bailout;
        }

        @Override
        public void process() {
            if (bailout == null) {
                count.increment();
            }
        }
    }

    private static boolean isProfiling() {
        return profile.getValue();
    }

    private static void enqueue(Event event) {
        eventQueue.add(event);
    }

    public static void eventExecute(TirTree tree) {
        if (isProfiling()) {
            enqueue(new ExecuteEvent(tree));
        }
    }

    public static void eventBailout(TirTree tree, Bailout bailout) {
        if (isProfiling()) {
            enqueue(new ExecuteEvent(tree, bailout));
        }
    }

    public static void event(CounterMetric metric) {
        CounterMetric.increment(metric);
    }
}