package backend.example.backend.Service;

import backend.example.backend.Entity.Employee;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class IntervalTree {

    private class Node {
        LocalDateTime start, end;
        List<Employee> employees;
        Node left, right;

        Node(LocalDateTime start, LocalDateTime end) {
            this.start = start;
            this.end = end;
            this.employees = new ArrayList<>();
        }
    }

    private Node root;

    public void insert(Employee emp, LocalDateTime start, LocalDateTime end) {
        root = insert(root, emp, start, end);
    }

    private Node insert(Node node, Employee emp, LocalDateTime start, LocalDateTime end) {
        if (node == null) {
            node = new Node(start, end);
            node.employees.add(emp);
            return node;
        }

        if (end.isBefore(node.start)) {
            node.left = insert(node.left, emp, start, end);
        } else if (start.isAfter(node.end)) {
            node.right = insert(node.right, emp, start, end);
        } else {
            node.employees.add(emp);
        }

        return node;
    }

    // Query employees who are available during the shift
    public List<Employee> query(LocalDateTime shiftStart, LocalDateTime shiftEnd) {
        return query(root, shiftStart, shiftEnd);
    }

    private List<Employee> query(Node node, LocalDateTime start, LocalDateTime end) {
        if (node == null) return new ArrayList<>();

        if (end.isBefore(node.start)) {
            return query(node.left, start, end);
        } else if (start.isAfter(node.end)) {
            return query(node.right, start, end);
        }

        List<Employee> result = new ArrayList<>(node.employees);
        result.addAll(query(node.left, start, end));
        result.addAll(query(node.right, start, end));
        return result;
    }
}
