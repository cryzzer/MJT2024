package bg.sofia.uni.fmi.mjt.gym.workout;

import java.util.Objects;

public record Exercise(String name, int sets, int repetitions) {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Exercise exercise = (Exercise) obj;
        return Objects.equals(name, exercise.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
