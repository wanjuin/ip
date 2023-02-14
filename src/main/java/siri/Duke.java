package siri;

import siri.exception.AddTaskIndexOutOfBounds;
import siri.exception.MarkerArrayIndexOutOfBoundsException;
import siri.exception.UnknownCommandException;
import siri.task.Deadline;
import siri.task.Event;
import siri.task.Task;
import siri.task.ToDo;

import java.lang.reflect.Array;
import java.util.Scanner;
import java.util.ArrayList;
public class Duke {

    private static ArrayList<Task> tasks = new ArrayList<>();
    //private static Task[] tasks = new Task[100];
    private static int indexOfTask = 0;

    public static void drawLine() {
        System.out.println("=====================================================================================");
    }

    public static void createSiriChatBox() {
        System.out.println("Siri:\n");
    }

    public static void createUserChatBox() {
        System.out.println("user: \n");
    }


    public static void greet() {
        //greet and ask name
        drawLine();
        createSiriChatBox();
        System.out.println("Hey, I'm Siri\n" + "What can I do for you?");
        drawLine();
    }

    public static void sayGoodbye() {
        //say goodbye with name
        drawLine();
        createSiriChatBox();
        System.out.println(">o< Goodbye! Hope to see you again soon! >o<");
        drawLine();
    }


    public static void printTaskList() {
        System.out.println("Below is your task list");
        for (int i = 0; i < indexOfTask; ++i) {
            System.out.println((i + 1) + ". " + tasks.get(i).toString());
        }
    }

    public static void markTask(String marker, String taskNumberString) {
        int taskNumber = Integer.parseInt(taskNumberString);

        if (marker.equals("mark")) {
            //mark task as done
            tasks.get(taskNumber - 1).setDone(true);
            System.out.println("Nice! I've marked this task as done: ");
            System.out.println(tasks.get(taskNumber - 1).toString());
        } else {
            //mark task as undone
            tasks.get(taskNumber - 1).setDone(false);
            System.out.println("Ok! I've marked this task as not done yet: ");
            System.out.println(tasks.get(taskNumber - 1).toString());
        }

        //Null Pointer Exception (taskNumber > indexOfTask)
    }

    //Task Description = taskD
    public static void addTask(String taskCommand, String taskD) {
        switch (taskCommand) {
        case "todo":
            tasks.add(new ToDo(taskD.trim()));
            break;
        case "deadline":
            String[] deadlineTaskD = taskD.split("/by ", 2);
            tasks.add(new Deadline(deadlineTaskD[0].trim(), deadlineTaskD[1].trim()));
            break;
        case "event":
            String[] eventName = taskD.split("/from ", 2);
            String[] eventDate = eventName[1].split("/to ", 2);
            tasks.add(new Event(eventName[0].trim(), eventDate[0].trim(), eventDate[1].trim()));
            break;
        }
    }

    public static void deleteTask(String taskNumberString){
        int taskNumber = Integer.parseInt(taskNumberString);
        printDeletedTask(taskNumber);
        tasks.remove(taskNumber-1);
        indexOfTask--;

        //IndexOutOfBoundsException
    }

    public static void printNewTask() {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + tasks.get(indexOfTask).toString());
        System.out.println("Now you have " + (indexOfTask + 1) + " tasks in the list.");
    }

    public static void printDeletedTask(int taskNumber) {
        System.out.println("Noted! I've deleted this task:");
        System.out.println("  " + tasks.get(taskNumber-1).toString());
        System.out.println("Now you have " + (indexOfTask-1) + " tasks in the list.");
    }
    public static void readUserInput(String input) throws MarkerArrayIndexOutOfBoundsException, AddTaskIndexOutOfBounds, UnknownCommandException {

        String[] command = input.split(" ", 2);

        switch (command[0]) {
        case "list":
            printTaskList();
            break;
        case "mark":
        case "unmark":
            if (command.length == 1) {
                throw new MarkerArrayIndexOutOfBoundsException();
            }
            markTask(command[0], command[1]);
            break;
        case "delete":
            deleteTask(command[1]);
            break;
        case "todo":
        case "deadline":
        case "event":
            if (command.length == 1) {
                throw new AddTaskIndexOutOfBounds(command[0]);
            }
            addTask(command[0], command[1]);
            printNewTask();
            indexOfTask++;
            break;
        default:
            throw new UnknownCommandException();
        }
    }

    public static void main(String[] args) {

        String logo = "  ______     __     __  _____     __\n"
                + " | _____|   |__|   |  |/ ____|   |__|\n"
                + " | |____     __    |   /          __\n"
                + " |_____ |   |  |   |  |          |  |\n"
                + " _____| |   |  |   |  |          |  |\n"
                + " |______|   |__|   |__|          |__|\n";

        System.out.println("Hello from \n" + logo);
        greet();
        createUserChatBox();
        Scanner in = new Scanner(System.in);
        String input = in.nextLine().trim();


        while (!input.equals("bye")) {
            drawLine();
            createSiriChatBox();
            try {
                readUserInput(input);
            } catch (UnknownCommandException e) {
                System.out.println("T^T OPPS!!! I'm sorry, but I don't know what that means");
            } catch (AddTaskIndexOutOfBounds e) {
                e.printError();
            } catch (MarkerArrayIndexOutOfBoundsException e) {
                System.out.println("Please enter the task number that you would like to mark / unmark, in the following format:");
                System.out.println("For example if you want to mark / unmark task 2 as done / undone: mark 2 / unmark 2");
            } catch (NumberFormatException e) {
                System.out.println("Please mark / unmark each task one by one, in the following format: ");
                System.out.println("For example if you want to mark / unmark task 2 as done / undone: mark 2 / unmark 2");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Please add the tasks in the following format: \n");
                System.out.println("Todo task format: todo task_name");
                System.out.println("Deadline task format: deadline deadline_name /by deadline_date");
                System.out.println("Event task format: event event_name /from event_from_timing /to event_to_timing");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Please only mark / unmark / delete task that is available in your task list.");
                System.out.println("You have up to " + indexOfTask + " number of tasks.");
            }

            drawLine();
            createUserChatBox();
            input = in.nextLine().trim();
        }
        sayGoodbye();
    }
}