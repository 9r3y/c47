package com.y3r9.c47.dog.algorithms.wfq;// This version of the List class contains an insert method
// that inserts new ListElem objects at the tail of the list


public class List
{
    // *******************************************************************
    // insert(v):
    // *******************************************************************
    public static QueueElem insert(QueueElem head, QueueElem v)
    {
        if ( head == null )
        {  // Handle insert into empty list separately.
            v.next = null;
            return(v);
        }
        else
        {
            head.next = insert1( head.next, v );
            return(head);
        }
    }


    public static QueueElem insert1(QueueElem head, QueueElem v)
    {
        if ( head == null )
        {  // Handle insert into empty list separately.
            v.next = null;
            return(v);
        }
        else if ( v.virtualFinTime < head.virtualFinTime )
        {
            v.next = head;
            return(v);
        }
        else
        {
            head.next = insert( head.next, v );
            return(head);
        }
    }


    public static QueueElem delete(QueueElem head)
    {
        if ( head != null )
            return(head.next);
        else
            return(null);
    }

    public static void print(QueueElem head)
    {
        String x = "   >>> ";

        if ( head == null )
        {
            System.out.println("   -- empty");
            return;
        }

        while ( head != null )
        {
            System.out.println(x + "(ID= " + head.flowID
//			+ " , lastUpdT= " + head.last_update_time
                    + " , pktLen=" + head.packetLen
                    + " , RemTrT=" + head.remaining_TransTime
                    + " , RemVirLen=" + head.remaining_Virt_len
                    + " , virEndT=" + head.virtualFinTime
                    + ")");
            x = "    -- ";
            head = head.next;
        }
    }
}