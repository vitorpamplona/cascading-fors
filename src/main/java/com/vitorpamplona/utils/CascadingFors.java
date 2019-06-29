package com.vitorpamplona.utils;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Computes a cascade of For loops in the sequence it was input and includes a progress function
 * to calculate the progress of the entire computation.
 */
public class CascadingFors {
    List<For> loopInto;

    public CascadingFors(List<For> loopInto) {
        this.loopInto = loopInto;
    }

    public CascadingFors(For... loopInto) {
        this.loopInto = Arrays.asList(loopInto);
    }

    // Uses recursion to cascade fors.
    private void run(List<For> remaining, Runnable r) {
        if (remaining.size() == 0) {
            r.run();
        } else {
            For current = remaining.remove(0);
            current.each(() -> { run(remaining, r); });
            remaining.add(0, current);
        }
    }

    /**
     * Run the Runnable at every combination of parameters of the cascading for structure.
     * @param r Code to be run.
     */
    public void run(Runnable r) {
        // Copies the list to allow removing without changing the state of the object
        // Uses a LinkedList to have the remove(int) method.
        run(new LinkedList<For>(loopInto), r);
    }

    /**
     * Calculates total number of steps are going to be computed.
     * @param cascadingFors list of Fors to consider (recursive)
     * @return number of steps already computed
     */
    public double totalNumberOfSteps(List<For> cascadingFors) {
        double totalSteps = 1;
        for (For f : cascadingFors) {
            totalSteps *= f.numberOfStepsCache;
        }
        return totalSteps;
    }

    /**
     * Calculates the number of computed steps already performed
     * in a recurrent fashion for the list of cascading fors
     * @param cascadingFors list of Fors to consider (recursive)
     * @return number of steps already computed
     */
    public double computedSteps(List<For> cascadingFors) {
        double ret = 0;

        if (cascadingFors.size() == 0) {
            return 0;
        } else {
            For current = cascadingFors.remove(0);
            ret = computedSteps(cascadingFors) + current.stepsComputed() * totalNumberOfSteps(cascadingFors);
            cascadingFors.add(0, current);
        }

        return ret;
    }

    /**
     * Calculate the progress of the entire for cascade at the end of the current loop (+1)
     * @return % of completness
     */
    public double progress() {
        return (computedSteps(new LinkedList<For>(loopInto)) + 1)/totalNumberOfSteps(loopInto);
    }

    /**
     * Progress in the 0% format
     * @return  % of completness
     */
    public String fmtProgress() {
        return NumberFormat.getPercentInstance().format(progress());
    }

    /**
     * Class to represent a For Loop.
     */
    public static class For {
        public double min;
        public double max;
        public double step;
        public double value;
        private double numberOfStepsCache;

        /**
         * The same as: for (int i=min; i<=max; i+=step)
         */
        public For(double min, double max, double step) {
            this.min = min;
            this.max = max;
            this.step = step;
            this.numberOfStepsCache = ((max - min) / this.step) + 1;
        }

        public double stepsComputed() {
            return (value - min) / step;
        }

        /**
         * Return % completed at the end of the current loop  (+1)
         * @return
         */
        public double progress() {
            return (stepsComputed() + 1) / numberOfStepsCache;
        }

        /**
         * Progress in the 0% format
         * @return  % of completness
         */
        public String fmtProgress() {
            return NumberFormat.getPercentInstance().format(progress());
        }

        public void each(Runnable toRun) {
            for (value = min; value <= max; value += step) {
                toRun.run();
            }
        }
    }


    /**
     * this is the same as
     *
     * for (int i=0; i<10; i++) {
     *     for (int j=0; j<10; j++) {
     *         System.out.println(i + " " + j + " " + x%);
     *     }
     * }
     * @param args
     */
    public static void main(String[] args) {
        For f1 = new For(0, 10, 1);
        For f2 = new For(0, 10, 1);

        CascadingFors fs = new CascadingFors(f1, f2);
        fs.run(() -> {
            System.out.println(f1.value + " " + f2.value + " " + fs.fmtProgress());
        });
    }
}