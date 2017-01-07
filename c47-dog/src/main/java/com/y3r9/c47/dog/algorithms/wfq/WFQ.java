package com.y3r9.c47.dog.algorithms.wfq;

import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;


public class WFQ
{

    public static double compute_VT_Rate(ArrayList<QueueElem>[] Q,
            double[] w)
    {
        double r = 0.0;

        for (int i = 0; i < Q.length; i++)
            if ( Q[i].size() > 0 )
                r += w[i];

        if ( r > 0 )
            return(1/r);
        else
            return(0.0);
    }

    public static void printArrivals(double [] arrTime, int[] flowID,
            double[] packetLen)
    {
        for (int i = 0; i < arrTime.length; i++)
            System.out.println(" T = " + arrTime[i]
                    + ", flow " + (flowID[i]+1)
                    + ", length " + packetLen[i]);
    }

    public static void printFFS_State(ArrayList<QueueElem>[] Q)
    {
//    System.out.println("----------------------------------------");
//    System.out.println("FFS State:");

        int flag = 1;

        for (int i = 0; i < Q.length; i++)
        {
            if ( Q[i].size() > 0 )
            {  flag = 0;

                System.out.println("   Packet from flow " + i
                        + " has remaining VIRTUAL length = "
                        + Q[i].get(0).remaining_Virt_len);

//          System.out.println("   Q[" + i + "].get(0).remaining_Virt_len = "
//                      + Q[i].get(0).remaining_Virt_len);
            }
        }

        if ( flag == 1 )
            System.out.println("   -- empty");

//    System.out.println("----------------------------------------");
    }


    public static void main(String[] args) throws IOException
    {

        int Ordering = 1;

        Scanner in = new Scanner(System.in);
        int i, j, k;

        int nFlows;		// # Flows
        double[] w;		// w[i] = weight of flow i
        ArrayList<QueueElem> Q[];	// Queue[i] = packet queue of flow i

        int nArrs;		// # Arrivals
        double [] arrTime;	// arrTime[i] = arrival time of packet i
        int[] flowID;		// flowID[i] = ID of the flow of packet i
        double[] packetLen;	// packetLen[i] = Norm. length of packet i


        double new_t = 0.0;
        double new_vt = 0.0;

        System.out.print("# flows = ");
        nFlows = in.nextInt();

        w = new double[nFlows];
        Q = new ArrayList[nFlows];

      /* ----------------------------------------
	 Read in weight:
	   w[i] = weight of flow i
	   Q[i] = information on flow i
         --------------------------------------- */

        for (i = 0; i < nFlows; i++)
        {
            System.out.print("Weight of flow " + i + " = ");
            w[i] = in.nextDouble();
            Q[i] = new ArrayList<QueueElem>();
        }

      /* ------------------------------------------------
	 Read in arrivals
	    arrTime[i]    = arrival time of i-th arrival
	    flowID[i]     = flow ID of i-th arrival
	    packetLen[i]  = length of i-th arrival
         ------------------------------------------------- */
        System.out.print("# Arrivals = ");
        nArrs = in.nextInt();

        arrTime = new double[nArrs];
        flowID = new int[nArrs];
        packetLen = new double[nArrs];

        for (i = 0; i < nArrs; i++)
        {
            System.out.print("Arrival time " + i + " = ");
            arrTime[i] = in.nextDouble();

            System.out.print("Flow ID " + i + " = ");
            flowID[i] = in.nextInt();
            flowID[i]--;

            System.out.print("Packet len " + i + " = ");
            packetLen[i] = in.nextDouble();
        }


        printArrivals(arrTime, flowID, packetLen);

        System.out.println("\n\nBegin WFQ computation");
      /* ***********************************************
	 WFQ Begins here
         *********************************************** */

        double t = 0.0;		// = Now, real time clock
        double vt = 0.0;		// = Now, virtual time clock
        double vt_rate = 0.0;	// = speed of virtual time clock
        QueueElem PacketQ = null;

        // ----------------------------------------
        // Variables for packet Arrival
        // ----------------------------------------
        int nextArrIndex = 0;
        double nextArrTime = arrTime[0];
        int nextFlowID = -1;
        double nextPacketLen = -1.0;
        QueueElem q;

        // ----------------------------
        // Next packet departure
        // ----------------------------
        double nextDepartureTime = 0.0;


        // ----------------------------------------
        // Variables for Flow's virtual clocks
        // ----------------------------------------
        double[] VT_flow = new double[nFlows]; // Max flow TS
        double   next_FFS_service_end_time;
        double last_update_time = 0.0;		     // Last real time rem len updated

        for (i = 0; i < nFlows; i++)
            VT_flow[i] = 0.0;

        next_FFS_service_end_time = -1.0;

        // ------------------------
        // Help variables
        // ------------------------


      /* ---------------------------------------------------------------
	 Main loop
         --------------------------------------------------------------- */
        while (true)
        {
            System.out.println("\n\n\n");
            System.out.println("==========================================");
            System.out.println("last_update_time = " + last_update_time);
            System.out.println("(1)  Current packet Queue:");
            List.print(PacketQ);
            System.out.println("(2)  Fluid Flow Server State:");
            printFFS_State(Q);
            System.out.println("------------------------------");

            System.out.println("BEGIN processing");
            System.out.println("t = " + t + " , vt = " + vt);

/**********************************************************************/

            // ----------------------------------------------------------
            // Determine next arrival time
            // ----------------------------------------------------------
            if ( nextArrIndex < nArrs )
            {
                nextArrTime = arrTime[nextArrIndex];
            }
            else
            {
                nextArrTime = 9999999.0;
            }

            // ----------------------------------------------------------
            // Determine next departure time
            // ----------------------------------------------------------
            if ( PacketQ != null )
            {
                nextDepartureTime = t + PacketQ.remaining_TransTime;
            }
            else
            {
                nextDepartureTime = 9999999.0;
            }


            // ----------------------------------------------------------
            // Determine next FFS finish time
            // ----------------------------------------------------------

            vt_rate = compute_VT_Rate(Q, w);
            // System.out.println("vt_rate = "  + vt_rate );

            next_FFS_service_end_time = 9999999.0;  // Cheap trick

            for ( i = 0; i < nFlows; i++ )
            {
                double h;
                double transmit_rate;

                if ( Q[i].size() > 0 )
                {
                    h = Q[i].get(0).remaining_Virt_len;

//	       System.out.println("Flow " + i
//				+ " Rem. virt len = " + h);

                    h = h / w[i];

//	       System.out.println("Flow " + i
//				+ " Rem. virtual transmit time = " + h);

                    if ( h < next_FFS_service_end_time )
                        next_FFS_service_end_time = h;
                }
            }
            next_FFS_service_end_time += t;

//	 System.out.println("+++ Next FFS service end time = "
//			+ next_FFS_service_end_time);

/**********************************************************************/

            // ******************************************************************
            // Print timing information
            // ******************************************************************
            System.out.println("+++ Next packet arrival time   = " + nextArrTime
                    + "   (flow ID = " + nextFlowID
                    + ", packet len = " + nextPacketLen + ")");

            System.out.println("+++ Next packet departure time = "
                    + nextDepartureTime);

            System.out.println("+++ Next FFS service end time  = "
                    + next_FFS_service_end_time);

            System.out.println();
            System.out.println("------------------------------------------");
            System.out.println("----- Find out Next Event in WFQ ---------");

/**********************************************************************/

            // **************************************
            // Test first !!!
            // **************************************

            boolean Arrival, Departure, FFS_fin;

            Arrival = Departure = FFS_fin = false;

            if ( nextArrTime < 10000 &&
                    (nextArrTime <= nextDepartureTime) &&
                    (nextArrTime <= next_FFS_service_end_time) &&
                    nextArrIndex < nArrs )
            {
                System.out.println("Next event: packet arrival");
                Arrival = true;

                new_t = nextArrTime;
            }

            if ( nextDepartureTime < 10000 &&
                    (nextDepartureTime<= nextArrTime) &&
                    (nextDepartureTime <= next_FFS_service_end_time) )
            {
                System.out.println("Next event: packet departure (real)");
                Departure = true;

                new_t = nextDepartureTime;
            }

            if ( next_FFS_service_end_time < 10000 &&
                    (next_FFS_service_end_time <= nextArrTime) &&
                    (next_FFS_service_end_time <= nextDepartureTime) )
            {
                System.out.println("Next event: FFS finishes a packet (virtual packet departure)");
                FFS_fin = true;

                new_t = next_FFS_service_end_time;
            }

            System.out.println("------------------------------------------");

            vt_rate = compute_VT_Rate(Q, w);

            if ( vt_rate > 0 )
                new_vt = vt + vt_rate * (new_t - t);


//  System.out.println("@@@@@@@@@@@@@@@@ DEBUG: new_t = " + new_t);
//  System.out.println("@@@@@@@@@@@@@@@@ DEBUG: new_vt = " + new_vt);

/**********************************************************************/
// PROCESS !!!
/**********************************************************************/

//  System.out.println(" + + + + PROCESSING !!! + + + + + +");

            // *******************
            // Virtual progress
            // *******************

//	 System.out.println("********************************");
//	 System.out.println("Processing VIRTUAL progress\n");
//	 System.out.println("********************************");
//	 System.out.println("vt_rate= " + vt_rate);

            for (i = 0; i < nFlows; i++)
            {
                if ( Q[i].size() > 0 )
                {
                    QueueElem x = Q[i].get(0);
                    x.remaining_Virt_len -= w[i] * vt_rate *
                            (new_t - x.last_update_time);
                    x.last_update_time = new_t;

//	       System.out.println("Flow " + i + " remaining_Virt_len = "
//					+ x.remaining_Virt_len);

//	       System.out.println("Flow " + i + " remaining_Virt_len/w = "
//					+ x.remaining_Virt_len/w[i]);
                }
            }

            // *******************
            // Real progress
            // *******************
//	 System.out.println("********************************");
//	 System.out.println("Processing REAL progress\n");
//	 System.out.println("********************************");

            if ( PacketQ != null )
            {
                PacketQ.remaining_TransTime -= (new_t - last_update_time);

//	    System.out.println("!!!! Packet Q, remaining trans time = "
//					+ PacketQ.remaining_TransTime);
            }

            // ********************************
            // Remember the current time and compute curret VT rate
            // ********************************
            last_update_time = new_t;

            // **************************************
            // Check if next event = packet arrival
            // **************************************
            if ( Arrival )
            {
                System.out.println("***** Processing packet arrival");

                // Packet arrival

//	    System.out.println("nextArrIndex = " + nextArrIndex);
//	    System.out.println("nextArrTime = " + nextArrTime);

                nextFlowID = flowID[nextArrIndex];
                nextPacketLen = packetLen[nextArrIndex];

                q = new QueueElem();

                q.flowID = nextFlowID;
                q.packetLen = nextPacketLen;

                q.remaining_TransTime = q.packetLen;
                q.remaining_Virt_len = q.packetLen;
                q.last_update_time = new_t;

                q.virtualFinTime = ( (VT_flow[nextFlowID] > new_vt) ?
                        VT_flow[nextFlowID] : new_vt ) +
                        q.remaining_Virt_len/w[nextFlowID];
                VT_flow[nextFlowID] = q.virtualFinTime;

                Q[nextFlowID].add(q);


                // *********************************
                // Insert in Packet Q
                // *********************************
                PacketQ = List.insert(PacketQ, q);


                nextArrIndex++;		// go to Next arrival
            }

            // **************************************
            // Check if next event = packet departure
            // **************************************
            if ( Departure )
            {
                System.out.println("***** Processing packet departure");

                System.out.println(">>>>>> Output order " + Ordering
                        + " --> flow ID = " + (PacketQ.flowID +1)
                        + " (packet len = " + PacketQ.packetLen + ")");
                Ordering++;

                PacketQ = List.delete(PacketQ);

                if ( PacketQ != null )
                {
                    PacketQ.remaining_TransTime = PacketQ.packetLen;
                    PacketQ.last_update_time = new_t;
                }

//	    System.out.println("--- PacketQ after processing packet departure");
//	    if ( PacketQ != null )
//	    {
//	       List.print(PacketQ);
//	    }
            }


            // **************************************
            // Check if next event = FFS fin packet
            // **************************************
            if ( FFS_fin )
            {
                System.out.println("***** Processing FFS Finish");


                for ( i = 0; i < nFlows; i++ )
                {

                    if ( Q[i].size() > 0 )
                    {
                        if ( Q[i].get(0).remaining_Virt_len < 0.00001 )
                        {
                            Q[i].remove(0);

                            if ( Q[i].size() > 0 )
                            {
                                Q[i].get(0).remaining_Virt_len =
                                        Q[i].get(0).packetLen;
                            }
                        }
                    }
                }


            }



	 /* --------------------------------------------

		t ------------> new_t
		    vt_rate
	    -------------------------------------------- */
            vt_rate = compute_VT_Rate(Q, w);

            if ( vt_rate > 0 )
                new_vt = vt + vt_rate * (new_t - t);

            t = new_t;
            vt = new_vt;

            if ( nextArrTime > 10000 &&
                    nextDepartureTime > 10000 &&
                    next_FFS_service_end_time > 10000 )
            {
                System.out.println("t = " + t + " ----- END");
                break;
            }

            System.out.println();
            System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            System.out.println("END processing: result");
            System.out.println("t = " + t);
            System.out.println("vt = " + vt);


            System.out.println("(1)  Current packet Queue:");
            List.print(PacketQ);
            System.out.println("(2)  Fluid Flow Server State:");
            printFFS_State(Q);
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

            System.out.println("\n\n");
        }
    }
}
