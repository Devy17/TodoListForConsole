package main.java.Todo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in); // 입력을 위한 스캐너 선언
        List<Todo> Todos = new ArrayList<Todo>(0);
        boolean isWorking = true; // 종료를 위한 boolean

        // json 파일 읽기
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile;
        try {
            jsonFile = new File("res/TodoSave.txt");
            if (!jsonFile.exists()) {
                jsonFile.createNewFile();
            }

            JsonNode rootNode = objectMapper.readTree(jsonFile);

            for (JsonNode element : rootNode) {
                String jContent = element.get("content").asText();
                int jPriority = element.get("priority").asInt();

                Todos.add(new Todo(jContent, jPriority));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // main loop 시작
        System.out.println("Hi! Let's make To-do List!");
        while(isWorking){
            System.out.println("Choose the number!");
            System.out.println(
                    "1. Create To-do\n" +
                    "2. Delete To-do\n" +
                    "3. Edit To-do\n"   +
                    "4. Show To-do\n"   +
                    "5. Close To-do List"
            );

            int command = sc.nextInt();
            sc.nextLine(); // 버퍼 제거

            System.out.println();
            switch (command) {
                case 1: // Create
                    CreateTodo(sc, Todos); break;
                case 2: // Delete
                    DeleteTodo(Todos, sc); break;
                case 3: // Edit
                    EditTodo(Todos, sc); break;
                case 4: // Show
                    ShowTodos(Todos, sc); break;
                case 5: // Close
                    isWorking = false;
                    System.out.println("Close the To-do List! bye");
                    break;
                default: // Wrong command
                    System.out.println("You Select the wrong number. Try again!");
                    continue;
            }
            System.out.println();
        }

        // 현재 상태 json으로 파싱
        String todosJson = objectMapper.writeValueAsString(Todos);
        try {
            if (!jsonFile.exists()) {
                jsonFile.createNewFile();
            }

            FileWriter fw = new FileWriter(jsonFile);
            fw.write(todosJson);

            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        sc.close();
    }

    private static void CreateTodo(Scanner sc, List<Todo> Todos) {
        System.out.println("Let's Create To-do");
        System.out.println("What is your To-do?");
        String todoContent = sc.nextLine();

        int todoPriority;
        do {
            System.out.println("What is that To-do's priority? (1 ~ 3)");
            todoPriority = sc.nextInt();
            sc.nextLine();
        } while(todoPriority < 1 || todoPriority > 3);

        Todos.add(new Todo(todoContent, todoPriority)); // Todo 생성
        System.out.println("Successfully Save!");
    }

    private static void DeleteTodo(List<Todo> Todos, Scanner sc) {
        System.out.println("Select the Todo number that you want to delete.\n");
        printTodos(Todos);

        int removeIdx = sc.nextInt();
        Todos.remove(removeIdx - 1);
        System.out.println("Successfully Delete!");
    }

    private static void EditTodo(List<Todo> Todos, Scanner sc) {
        System.out.println("Select the Todo number that you want to edit.\n");
        printTodos(Todos);

        int editIdx = sc.nextInt();
        sc.nextLine();

        System.out.println("Write down your new To-do.");
        String newContent = sc.nextLine();
        int newPriority;
        do {
            System.out.println("Write down your new To-do's priority. (1 ~ 3)");
            newPriority = sc.nextInt();
            sc.nextLine();
        } while(newPriority < 1 || newPriority > 3);

        Todos.get(editIdx - 1).SetTodo(newContent, newPriority);

        // Save
        System.out.println("Successfully Save!");
    }

    private static void ShowTodos(List<Todo> Todos, Scanner sc) {
        printTodos(Todos);

        System.out.println("\nIf you want to return to the menu screen, press q button!");
        String quitCode = "q";
        while(true) {
            if (sc.hasNext() && sc.next().equals(quitCode)) {
                System.out.println("\nReturn to the menu screen!");
                break;
            }
        }
    }

    private static void printTodos(List<Todo> Todos) {
        SortingForPriority(Todos);

        System.out.println("==========");
        for(int i = 1; i <= Todos.size(); i++) {
            System.out.println(i + ". " + Todos.get(i-1).getContent() + " --- priority : " + Todos.get(i-1).getPriority());
        }
        System.out.println("==========");
    }

    private static void SortingForPriority(List<Todo> Todos) {
        for(int i = 0; i < Todos.size(); i++) {
            Todo todo = Todos.get(i);
            if(todo.getPriority() == 1) {
                Todos.remove(todo);
                Todos.add(0, todo);
            } else if(todo.getPriority() == 3) {
                Todos.remove(todo);
                Todos.add(Todos.size(),todo);
                i--;
            }
        }
    }
}
