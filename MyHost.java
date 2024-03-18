/* Implement this class. */

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyHost extends Host {
    
    //

    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<Task>();
    //private Task runningTask = null;
    private boolean runningHost = false;
    private boolean startHost = false;
    private int workLeft = 0;
    private boolean isRunningTask = false;


    @Override
    public void run() {
        runningHost = true;
        int currentTime = 0;
        Task runningTask = null;

    while (runningHost) {
        if (!startHost) {
            if (taskQueue.isEmpty()) {
                continue;
            } else {
                startHost = true;
            }
        }

        // partea care se ocupa de gasirea lui runningTask pt secunda curenta identificand task.id
        int taskID = getNextRunningTaskID(runningTask, currentTime);

        if (runningTask != null && runningTask.getLeft() == 0) {
            runningTask.finish();
//System.out.println("Finish Task " + runningTask.getId() +" la current time " +  currentTime + " si finish " + runningTask.getFinish() );
            runningTask = null;
            isRunningTask = false;
        }

        // partea care aranjeaza coada
        if (runningTask == null) {
            if (taskID == -1) {
                // nu voi avea task de executat
            } else {
                // apelez procedura de aranjare coada
                runningTask = getNextRunningTask(taskID, runningTask);
                isRunningTask = true;
            }
        } else {
            if (taskID == -1) {
                // inseamna ca nu voi avea task
            } else {
                if (taskID != runningTask.getId()) {
                    // apelez procedura de aranjare coada
                    runningTask = getNextRunningTask(taskID, runningTask);
                    isRunningTask = true;
                }
            }
        }

        // partea care sta o secunda
//            if (runningTask != null) {
        if (runningTask != null) {
            isRunningTask = true;
//    System.out.println("Task ID " + runningTask.getId() + " asteapta 1 secunda, currenttime " + currentTime);
//    if (runningTask.getId() == 1) {

//        runningTask.finish();
//    System.out.println("Task ID " + runningTask.getId() + " currenttime " + currentTime+ " finish " + runningTask.getFinish());

//    }
        } else {
            isRunningTask = false;
        }


        if (runningTask != null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (runningTask != null) {
            runningTask.setLeft(runningTask.getLeft() - 1000);
            workLeft -= 1000;
        }

        if (runningTask != null || !taskQueue.isEmpty()) {
            currentTime++;
        }
        //            }


    }

    }

    private int getNextRunningTaskID(Task runningTask, int currentTime) {
        int taskID = -1;
        int taskPriority = -1;

        if (runningTask != null) {
            taskID = runningTask.getId();
            taskPriority = runningTask.getPriority();
        }
        if (taskQueue.isEmpty()) {
            // daca NU am coada NU pot sa ma gandesc daca schimb taskul care ruleaza
            if (runningTask == null) {
                // nu fac nimic
            } else {
                if (runningTask.getLeft() == 0) {
                    // am terminat taskul curent, nu mai am nimic de facut
                    taskID = -1;
                    taskPriority = -1;
                }
            }
        }
        else {
            // am coada si eventual runningTask != null
            // parcurg coada, nu modific nimic
            for (Task queueTask : taskQueue) {
                // test daca taskQueue poate incepe - daca nu voi pastra neschimbat taskID
                if (queueTask.getStart() > currentTime) {

// !!!!!!!!!!!!!!
                    continue;
                }
                if (runningTask != null ) {
                    if (runningTask.getLeft() == 0) {
                        if (taskID == runningTask.getId()) {
                            taskID = queueTask.getId();
                            taskPriority = queueTask.getPriority();
//                            if (currentTime == 7) {
//                                System.out.println("ct 5 exit 0 " + taskID);
//                            }
                            continue;
                        }
                        else {
                            // deja am luat din coada un task diferit de runningTask
                            if (taskPriority < queueTask.getPriority()
                                || (taskPriority == queueTask.getPriority() && taskID > queueTask.getId())) {
                                taskID = queueTask.getId();
                                taskPriority = queueTask.getPriority();
//                                if (currentTime == 7) {
//                                    System.out.println("ct 5 exit 000 " + taskID);
//                                }
                                continue;
                            }
                        }
                    }
                }
                if (taskID == -1) {
                    // nu am runningTask, il iau pe cel curent
                    taskID = queueTask.getId();
                    taskPriority = queueTask.getPriority();
//if (currentTime == 7) {
//    System.out.println("ct 5 exit 1 " + taskID);
//}
                    continue;

                }
                // test daca queueTask are prioritate mai mare decat taskID
                // este valabil cand am runningTask care este Preemptible
                if (runningTask == null ) {
                    if (taskPriority < queueTask.getPriority()
                            || (taskPriority == queueTask.getPriority() && taskID > queueTask.getId())) {
                        taskID = queueTask.getId();
                        taskPriority = queueTask.getPriority();
//                        if (currentTime == 7) {
//                            System.out.println("ct 5 exit 2 " + taskID);
//                        }
                    }
                }
                else {
//                    if (currentTime == 7) {
//                        System.out.println("intru pe else " + taskID);
//                    }
                    if (runningTask != null && runningTask.getLeft() > 0 && runningTask.isPreemptible()) {

//                        if (currentTime == 7) {
//                            System.out.println("intru pe !null " + taskID);
//                        }

                        if (taskID == runningTask.getId()) {
                            if (runningTask.getPriority() < queueTask.getPriority()) {
                                taskID = queueTask.getId();
                                taskPriority = queueTask.getPriority();
//                                if (currentTime == 7) {
//                                    System.out.println("ct 5 exit 3 " + taskID);
//                                }
                            }
                            else {
                                if (taskPriority < queueTask.getPriority()
                                        || (taskPriority == queueTask.getPriority() && taskID > queueTask.getId() )) {
                                    taskID = queueTask.getId();
                                    taskPriority = queueTask.getPriority();
//                                    if (currentTime == 7) {
//                                        System.out.println("ct 5 exit 4 " + taskID);
//                                    }
                                }
                            }
                        }
                    }
//                    else {
//                        if (runningTask.getLeft() == 0) {
//
//                        }
//                    }
                }

            }
        }

//        if (taskID > 0) {
//            System.out.println("Ies cu taskID " + taskID);
//        }
        return taskID;
    }

    private Task getNextRunningTask(int taskID, Task runningTask) {
        Task newRunningTask = null;
        if (taskID < 0) {
//            System.out.println("Atentie am intrat cu taskID = " + taskID);
            return newRunningTask;
        }
        BlockingQueue<Task> newTaskQueue = new LinkedBlockingQueue<>();
        while (!taskQueue.isEmpty()) {
            Task currentTask = taskQueue.poll();
            if (taskID == currentTask.getId()) {
                newRunningTask = currentTask;
            }
            else {
                newTaskQueue.add(currentTask);
            }
        }
        if (runningTask != null) {
            newTaskQueue.add(runningTask);
        }
        taskQueue = newTaskQueue;
        if (newRunningTask == null) {
//            System.out.println("Atentie am iesit cu null pt taskID = " + taskID);
        }
        return newRunningTask;
    }



    @Override
    public void addTask(Task task) {
        synchronized (taskQueue) {
            task.setLeft(task.getDuration());
            workLeft += task.getDuration();
            taskQueue.add(task);
        }
    }

    @Override
    public int getQueueSize() {
        int qSize = taskQueue.size();
        if (isRunningTask) {
            qSize++;
        }
        return qSize;
    }

    @Override
    public long getWorkLeft() {
        return workLeft;
    }

    @Override
    public void shutdown() {
        runningHost = false;
//        if (getQueueSize() == 0) {
//            continueRun = false;
//        }
    }
}
