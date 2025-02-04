package main.java.Todo;

public class Todo {
    private String content;
    private int priority; // 우선순위 sorting

    public Todo(String content, int priority){
        this.content = content;
        this.priority = priority;
    }

    public void SetTodo(String content, int priority) {
        this.content = content;
        this.priority = priority;
    }

    public String getContent(){
        return content;
    }

    public int getPriority() {
        return priority;
    }
}
