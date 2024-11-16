package backend.example.backend.Service;

import backend.example.backend.Entity.Shift;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ShiftConflictDetector {

    private Map<Shift, List<Shift>> conflictGraph = new HashMap<>();

    public void buildConflictGraph(List<Shift> shifts) {
        for (int i = 0; i < shifts.size(); i++) {
            for (int j = i + 1; j < shifts.size(); j++) {
                if (isConflict(shifts.get(i), shifts.get(j))) {
                    conflictGraph.computeIfAbsent(shifts.get(i), k -> new ArrayList<>()).add(shifts.get(j));
                    conflictGraph.computeIfAbsent(shifts.get(j), k -> new ArrayList<>()).add(shifts.get(i));
                }
            }
        }
    }

    private boolean isConflict(Shift s1, Shift s2) {
        return s1.getStartTime().isBefore(s2.getEndTime()) && s2.getStartTime().isBefore(s1.getEndTime());
    }

    public boolean hasConflict(Shift shift) {
        return conflictGraph.containsKey(shift) && !conflictGraph.get(shift).isEmpty();
    }

    public List<Shift> getConflictingShifts(Shift shift) {
        return conflictGraph.getOrDefault(shift, Collections.emptyList());
    }
}
