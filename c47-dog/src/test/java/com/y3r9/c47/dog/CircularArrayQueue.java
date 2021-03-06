package com.y3r9.c47.dog;

public class CircularArrayQueue {

    private static final int capacity = 5;
    private Object[] Q;
    private final int N; // capacity
    private int f = 0;
    private int r = 0;

    public CircularArrayQueue() {
        this(capacity);
    }

    public CircularArrayQueue(int capacity) {
        N = capacity;
        Q = new Object[N];
    }

    public int size() {
        if (r > f)
            return r - f;
        return N - f + r;
    }

    public boolean isEmpty() {
        return (r == f) ? true : false;
    }

    public boolean isFull() {
        int diff = r - f;
        if (diff == -1 || diff == (N - 1))
            return true;
        return false;
    }

    public void enqueue(Object obj) throws RuntimeException {
        if (isFull()) {
            throw new RuntimeException("Queue is Full.");
        } else {
            Q[r] = obj;
            r = (r + 1) % N;
        }
    }

    public Object dequeue() throws RuntimeException {
        Object item;
        if (isEmpty()) {
            throw new RuntimeException();
        } else {
            item = Q[f];
            Q[f] = null;
            f = (f + 1) % N;
        }
        return item;
    }

}