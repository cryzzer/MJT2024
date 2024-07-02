package bg.sofia.uni.fmi.mjt.gym.member;

import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.*;

public class Member implements GymMember {
    private final String name;
    private final int age;
    private final String personalIdNumber;
    private final Gender gender;
    private final Address address;
    private final Map<DayOfWeek, Workout> trainingProgram;

    public Member(Address address, String name, int age, String personalIdNumber, Gender gender) {
        this.address = address;
        this.name = name;
        this.age = age;
        this.personalIdNumber = personalIdNumber;
        this.gender = gender;
        this.trainingProgram = new HashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public Map<DayOfWeek, Workout> getTrainingProgram() {
        return Collections.unmodifiableMap(trainingProgram);
    }

    @Override
    public void setWorkout(DayOfWeek day, Workout workout) {
        if (day == null) {
            throw new IllegalArgumentException(
                    "Day should not be null for user: %s"
                            .formatted(name));
        }
        if (workout == null) {
            throw new IllegalArgumentException(
                    "Workout should not be null for user: %s"
                            .formatted(name));
        }
        trainingProgram.put(day, workout);
    }

    @Override
    public Collection<DayOfWeek> getDaysFinishingWith(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("ExerciseName should not be null or empty");
        }

        List<DayOfWeek> foundDays = new ArrayList<>();
        for (Map.Entry<DayOfWeek, Workout> entry : trainingProgram.entrySet()) {
            Workout workoutForTheDay = entry.getValue();
            if (workoutForTheDay.exercises().getLast().name().equals(exerciseName)) {
                foundDays.add(entry.getKey());
            }
        }

        return foundDays;
    }

    @Override
    public void addExercise(DayOfWeek day, Exercise exercise) throws DayOffException {
        if (day == null || exercise == null) {
            throw new IllegalArgumentException("Day and exercise should not be null");
        }

        Workout workout = trainingProgram.get(day);
        if (workout == null) {
            throw new DayOffException("Cannot add exercise to day off");
        }

        workout.exercises().add(exercise);
    }

    @Override
    public void addExercises(DayOfWeek day, List<Exercise> exercises) throws DayOffException {
        if (day == null) {
            throw new IllegalArgumentException("Day should not be null");
        }
        if (exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException("List of exercises should not be null or empty");
        }

        var workout = trainingProgram.get(day);
        if (workout == null) {
            throw new DayOffException("There is no defined workout for this day.");
        }

        workout.exercises().addAll(exercises);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Member member = (Member) obj;
        return Objects.equals(personalIdNumber, member.personalIdNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personalIdNumber);
    }
}
