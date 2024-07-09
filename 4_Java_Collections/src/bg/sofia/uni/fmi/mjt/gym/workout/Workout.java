package bg.sofia.uni.fmi.mjt.gym.workout;

import java.util.SequencedCollection;

public record Workout(SequencedCollection<Exercise> exercises) {
    public boolean hasExercise(String exerciseName) {
        for (Exercise currentExercise : exercises) {
            if (currentExercise.name().equals(exerciseName)) {
                return true;
            }
        }
        return false;
    }
}
