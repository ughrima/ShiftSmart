package backend.example.backend.Service;

import java.util.*;
import backend.example.backend.Entity.*;
import org.springframework.stereotype.Component;

@Component
public class ConflictGraph {
    // Adjacency list representation of the graph
    private final Map<Shift, List<Shift>> adjacencyList = new HashMap<>();

    // Add an edge between two conflicting shifts
    public void addEdge(Shift shift1, Shift shift2) {
        adjacencyList.computeIfAbsent(shift1, k -> new ArrayList<>()).add(shift2);
        adjacencyList.computeIfAbsent(shift2, k -> new ArrayList<>()).add(shift1);
    }

    // Get all conflicts for a specific shift
    public List<Shift> getConflicts(Shift shift) {
        return adjacencyList.getOrDefault(shift, new ArrayList<>());
    }

    // Get all conflicts in the graph
    public Map<Shift, List<Shift>> getAllConflicts() {
        return adjacencyList;
    }

    // Remove a conflict (optional, for resolution)
    public void removeEdge(Shift shift1, Shift shift2) {
        adjacencyList.getOrDefault(shift1, new ArrayList<>()).remove(shift2);
        adjacencyList.getOrDefault(shift2, new ArrayList<>()).remove(shift1);
    }
}
