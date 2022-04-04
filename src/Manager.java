import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

public class Manager {
    private HashMap<Integer, Task> tasksById;
    private HashMap<Integer, Subtask> subtasksByID;
    private HashMap<Integer, Epic> epicsById;
    private int nextId = 0;
    private String newStatus = "New";
    private String doneStatus = "Done";
    private String inProgressStatus = "In progress";

    public Manager() {
        this.tasksById = new HashMap<>();
        this.subtasksByID = new HashMap<>();
        this.epicsById = new HashMap<>();
    }

    //Добавление новой задачи
    public Task addTask(Task newTask) {
        int taskId = ++nextId;
        newTask.setId(taskId);
        tasksById.put(taskId, newTask);
        return newTask;
    }

    //Обновление задачи
    public Task updateTask(Task taskToUpdate) {
        int taskId = taskToUpdate.getId();
        Task foundTask = tasksById.get(taskId);
        if (foundTask == null) {
            return null;
        }
        tasksById.put(taskId, taskToUpdate);
        return taskToUpdate;
    }

    //Получение задачи по ID
    public Task getTask(int taskId) {
        return tasksById.get(taskId);
    }

    //Получение списка всех задач
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksById.values());
    }

    //Удаление задачи по ID
    public Task deleteTaskById(int id) {
        return tasksById.remove(id);
    }

    //Удаление всех задач
    public void deleteAllTasks() {
        tasksById.clear();
    }

    //Добавление подзадачи
    public Subtask addSubtask(Subtask newSubtask) {
        Epic epic = getEpic(newSubtask.getEpicID());
        if (epic == null) {
            return null;
        }
        int taskId = ++nextId;
        newSubtask.setId(taskId);
        subtasksByID.put(taskId, newSubtask);
        epic.addSubtask(newSubtask);
        updateEpic(epic);
        return newSubtask;
    }

    //Обновление подзадачи
    public Subtask updateSubtask(Subtask taskUpdate) {
        int taskId = taskUpdate.getId();
        Subtask subtask = subtasksByID.get(taskId);
        if (subtask == null) {
            return null;
        }
        if (!Objects.equals(taskUpdate.getEpicID(), subtask.getEpicID())) {
            Epic removeSubtaskEpic = getEpic(subtask.getEpicID());
            Epic addSubtaskEpic = getEpic(taskUpdate.getEpicID());
            removeSubtaskEpic.deleteSubtaskList(subtask);
            addSubtaskEpic.addSubtask(taskUpdate);
            updateEpic(removeSubtaskEpic);
            updateEpic(addSubtaskEpic);
        }
        subtasksByID.put(taskId, taskUpdate);
        Epic epic = getEpic(taskUpdate.getEpicID());
        updateEpic(epic);
        return taskUpdate;
    }

    //Получение подзадачи по ID
    public Subtask getSubtask(int taskId) {
        return subtasksByID.get(taskId);
    }

    //Получение списка всех подзадач
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasksByID.values());
    }

    //Удаление подзадачи по ID
    public Subtask deleteSubtaskById(int id) {
        Subtask subtask = getSubtask(id);
        Epic epic = getEpic(subtask.getEpicID());
        if (epic == null) {
            System.out.println("Эпик не найден!");
            return null;
        }
        epic.deleteSubtaskList(subtask);
        updateEpic(epic);
        return subtasksByID.remove(id);
    }

    public void deleteAllSubtasks() {
        ArrayList<Epic> allEpics = getAllEpics();
        for (Epic epic : allEpics) {
            ArrayList<Subtask> subtasks = epic.getSubtasks();
            for (Subtask subtask : subtasks) {
                deleteSubtaskById(subtask.getId());
            }
        }
    }

    //Создание нового эпика
    public Epic addEpic(Epic newEpic) {
        int epicId = ++nextId;
        newEpic.setId(epicId);
        epicsById.put(epicId, newEpic);
        return newEpic;
    }

    //Обновление эпика
    public Epic updateEpic(Epic epicUpdate) {
        int epicId = epicUpdate.getId();
        Epic foundTask = epicsById.get(epicId);
        if (foundTask == null) {
            return null;
        }
        String calculatedStatus = calculateEpicStatus(epicUpdate);
        epicUpdate.setStatus(calculatedStatus);
        epicsById.put(epicId, epicUpdate);
        return epicUpdate;
    }

    //Получение эпика по ID
    public Epic getEpic(int epicId) {
        return epicsById.get(epicId);
    }

    //Получение подзадач эпика по ID эпика
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = getEpic(epicId);
        if (epic == null) {
            return null;
        }
        return epic.getSubtasks();
    }

    //Получение списка всех эпиков
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicsById.values());
    }

    //Удаление эпика по ID
    public Epic deleteEpicById(int id) {
        ArrayList<Subtask> epicSubtasks = getEpicSubtasks(id);
        for (Subtask subtask : epicSubtasks) {
            deleteSubtaskById(subtask.getId());
        }
        return epicsById.remove(id);
    }

    //Удаление всех эпиков
    public void deleteAllEpics() {
        ArrayList<Epic> allEpics = getAllEpics();
        for (Epic epic : allEpics) {
            deleteEpicById(epic.getId());
        }
    }

    //Вычисление статуса эпика
    private String calculateEpicStatus(Epic epic) {
        ArrayList<Subtask> allSubtasks = epic.getSubtasks();
        if (allSubtasks.isEmpty()) {
            return newStatus;
        }
        boolean subtasksHaveStatusNew = true;
        boolean subtasksHaveStatusDone = true;
        for (Subtask subtask : allSubtasks) {
            if (!Objects.equals(subtask.getStatus(), newStatus)) {
                subtasksHaveStatusNew = false;
            }
            if (!Objects.equals(subtask.getStatus(), doneStatus)) {
                subtasksHaveStatusDone = false;
            }
        }
        if (subtasksHaveStatusNew) {
            return newStatus;
        }
        if (subtasksHaveStatusDone) {
            return doneStatus;
        }

        // во всех остальных случаях статус должен быть IN_PROGRESS
        return inProgressStatus;
    }
}

