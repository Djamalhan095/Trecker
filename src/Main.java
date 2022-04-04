public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Уборка", "Убраться", "New");
        Task task2 = new Task("Стирка", "Постирать", "New");
        System.out.println(manager.addTask(task1));
        System.out.println(manager.addTask(task2));

        Epic epic1 = new Epic("Тренировка", "Потренироваться", "New");
        Epic epic2 = new Epic("Отдых", "Отдохнуть", "New");
        Subtask subtaskEpic1 = new Subtask("Пробежка", "Бег", "Done", 3);
        Subtask subtaskEpic2first = new Subtask("Кино", "Просмотр кино", "Done", 4);
        Subtask subtaskEpic2second = new Subtask("Сон", "Поспать", "New", 4);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubtask(subtaskEpic1);
        manager.addSubtask(subtaskEpic2first);
        manager.addSubtask(subtaskEpic2second);
        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getEpicSubtasks(3));
        System.out.println(manager.getEpicSubtasks(4));
        manager.deleteTaskById(1);
        manager.deleteEpicById(4);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());

    }
}
