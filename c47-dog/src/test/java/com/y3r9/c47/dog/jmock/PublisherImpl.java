package com.y3r9.c47.dog.jmock;

import java.util.ArrayList;
import java.util.List;

/**
 * The Publisher class
 *
 * @version 1.0
 */
public class PublisherImpl implements Publisher {

    private List<Subscriber> subscribers = new ArrayList<>();

    private int size = 2;

    public PublisherImpl(int size) {
        this.size = size;
    }

    public void addSubscriber(Subscriber subscriber) {
        if (subscribers.size() == size - 1) {
            throw new IllegalArgumentException("exceed");
        } else {
            subscribers.add(subscriber);
        }

    }
}
