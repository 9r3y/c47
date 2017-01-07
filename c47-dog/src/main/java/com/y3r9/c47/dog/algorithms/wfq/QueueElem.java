package com.y3r9.c47.dog.algorithms.wfq;

class QueueElem
{
    public int flowID;
    public double packetLen;
    public double virtualFinTime;

    public double remaining_Virt_len;
    public double last_update_time;
    public double remaining_TransTime;

    public QueueElem next;
}
