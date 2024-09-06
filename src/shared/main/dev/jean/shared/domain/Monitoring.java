package dev.jean.shared.domain;

import java.util.HashMap;

/**
 * This interface represents a monitoring.
 */
public interface Monitoring {
    /**
     * Increments a counter.
     * 
     * @param times The number of times to increment the counter.
     */
    void incrementCounter(int times);

    /**
     * Increment a metric gauge.
     *
     * @param times The number of times to increment the gauge.
     */
    void incrementGauge(int times);

    /**
     * Decrement a metric gauge.
     *
     * @param times The number of times to decrement the gauge.
     */
    void decrementGauge(int times);

    /**
     * Set a metric gauge.
     *
     * @param value The value to set the gauge to.
     */
    void setGauge(int value);

    /**
     * Observe a histogram.
     *
     * @param value  The value to observe.
     * @param labels The labels to observe.
     */
    void observeHistogram(int value, HashMap<String, String> labels);
}
