/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {

    private int lastUsedHost;
    private boolean firstUsage;
    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
        firstUsage = true;
    }

    @Override
    public void addTask(Task task) {



 //       synchronized (this)
 //       {
            int a = 0;
            //System.out.println("task " + task.getId());
            if (this.algorithm.equals(SchedulingAlgorithm.ROUND_ROBIN)) {
                int hostToUse;
                if (firstUsage) {
                    hostToUse = 0;
                    firstUsage = false;
                } else {
                    hostToUse = (lastUsedHost + 1) % hosts.size();
                }

                hosts.get(hostToUse).addTask(task);

                lastUsedHost = hostToUse;
            }

            if (this.algorithm.equals(SchedulingAlgorithm.SHORTEST_QUEUE)) {
                int hostToUse;
                if (firstUsage) {
                    hostToUse = 0;
                    firstUsage = false;
                } else {
                    hostToUse = -1;
                    long myQueueSize = 0;
                    for (int i = 0; i < hosts.size(); i++) {
                        if (hostToUse == -1) {
                            hostToUse = i;
                            myQueueSize = hosts.get(i).getQueueSize();
//System.out.println("Am pus hostToUse " + i + " cu work " + myWorkLeft + " task " + task.getId() );
                        }
                        else {
                              if (myQueueSize > hosts.get(i).getQueueSize()) {
                                  hostToUse = i;
                                  myQueueSize = hosts.get(i).getQueueSize();
//System.out.println("Am pus hostToUse " + i + " cu work " + myWorkLeft + " task " + task.getId() );
                              }
                        }
                    }
                }
//System.out.println("Am trimis la hostToUse " + hostToUse + " task " + task.getId() );
                hosts.get(hostToUse).addTask(task);
//System.out.println("Acum am la hostToUse " + hostToUse + " coada " + hosts.get(hostToUse).getQueueSize() );

            }
            if (this.algorithm.equals(SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT)) {
                if (task.getType().equals(TaskType.SHORT)) {
                    hosts.get(0).addTask(task);
                }
                if (task.getType().equals(TaskType.MEDIUM)) {
                    hosts.get(1).addTask(task);
                }
                if (task.getType().equals(TaskType.LONG)) {
                    hosts.get(2).addTask(task);
                }
            }

            if (this.algorithm.equals(SchedulingAlgorithm.LEAST_WORK_LEFT)) {
                int hostToUse;
                if (firstUsage) {
                    hostToUse = 0;
                    firstUsage = false;
                } else {
                    hostToUse = -1;
                    long myWorkLeft = 0;
                    for (int i = 0; i < hosts.size(); i++) {
                        if (hostToUse == -1) {
                            hostToUse = i;
                            myWorkLeft = hosts.get(i).getWorkLeft();
//System.out.println("Am pus hostToUse " + i + " cu work " + myWorkLeft + " task " + task.getId() );
                        }
                        else {
                            // daca nu merge incearca cu round
                            if (myWorkLeft > hosts.get(i).getWorkLeft()) {
                                hostToUse = i;
                                myWorkLeft = hosts.get(i).getWorkLeft();
//System.out.println("Am pus hostToUse " + i + " cu work " + myWorkLeft + " task " + task.getId() );
                            }
                        }
                    }
                }
//System.out.println("Am trimis la hostToUse " + hostToUse + " task " + task.getId() );
                hosts.get(hostToUse).addTask(task);
            }


        }
//    }

}
