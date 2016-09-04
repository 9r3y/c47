package com.y3r9.c47.dog.swj;

import java.lang.management.*;

class  DeadLockDetect
{
    public void findDeadLocks()
    {
        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
        long[] ids = tmx.findDeadlockedThreads();
        if (ids != null )
        {
            ThreadInfo[] infos = tmx.getThreadInfo(ids,true,true);
            System.out.println("Following Threads are deadlocked");
            for (ThreadInfo info : infos)
            {
                System.out.println(info);
            }
        }
    }
}
