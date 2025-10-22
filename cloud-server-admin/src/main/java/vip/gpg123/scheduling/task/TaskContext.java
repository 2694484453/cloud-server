package vip.gpg123.scheduling.task;

public class TaskContext {
    private static final ThreadLocal<Long> currentTaskId = new ThreadLocal<>();

    public static void setCurrentTaskId(Long taskId) {
        currentTaskId.set(taskId);
    }

    public static Long getCurrentTaskId() {
        return currentTaskId.get();
    }

    public static void clear() {
        currentTaskId.remove();
    }
}
