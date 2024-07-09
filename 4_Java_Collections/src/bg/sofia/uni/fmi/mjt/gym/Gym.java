package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.*;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.*;

public class Gym implements GymAPI {
    private int capacity;
    private Address address;
    private SortedSet<GymMember> members;

    public Gym(int capacity, Address address) {
        this.capacity = capacity;
        this.address = address;
        this.members = new TreeSet<>(new MembersByPersonalIdComparator());
    }

    @Override
    public SortedSet<GymMember> getMembers() {
        return Collections.unmodifiableSortedSet(members);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByName() {
        return getUnmodifiableSortedMembers(new MembersByNameComparator());
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByProximityToGym() {
        return getUnmodifiableSortedMembers(new MembersByProximityToGymComparator(address));
    }

    @Override
    public void addMember(GymMember member) throws GymCapacityExceededException {
        if (member == null) {
            throw new IllegalArgumentException("Member should not be null");
        }

        if (this.members.size() >= capacity) {
            throw new GymCapacityExceededException("Cannot add member to existing members set, because capacity will be exceeded.");
        }

        this.members.add(member);
    }

    @Override
    public void addMembers(Collection<GymMember> members) throws GymCapacityExceededException {
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("Members should not be null or empty");
        }

        if (this.members.size() + members.size() > capacity) {
            throw new GymCapacityExceededException("Cannot add members to existing members set, because capacity will be exceeded.");
        }

        this.members.addAll(members);
    }

    @Override
    public boolean isMember(GymMember member) {
        if (member == null) {
            throw new IllegalArgumentException("Member should not be null");
        }

        return members.contains(member);
    }

    @Override
    public boolean isExerciseTrainedOnDay(String exerciseName, DayOfWeek day) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("exerciseName should not be null or empty");
        }

        if (day == null) {
            throw new IllegalArgumentException("Day should not be null");
        }

        for (GymMember member : members) {
            Map<DayOfWeek, Workout> memberProgram = member.getTrainingProgram();
            Workout workout = memberProgram.get(day);
            if (workout != null && workout.hasExercise(exerciseName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("exerciseName should not be null or empty");
        }

        // Result map
        Map<DayOfWeek, List<String>> membersDoingExercise = new HashMap<>();

        for (GymMember member : members) {
            //Getting the program for each member
            Map<DayOfWeek, Workout> memberProgram = member.getTrainingProgram();

            for (Map.Entry<DayOfWeek, Workout> workoutForDay : memberProgram.entrySet()) {
                // Getting the workout for the corresponding day for the member
                Workout workout = workoutForDay.getValue();

                //If the workout contains this exercise add it
                if (workout.hasExercise(exerciseName)) {
                    //If the array list is not initialized, initialize it
                    membersDoingExercise.putIfAbsent(workoutForDay.getKey(), new ArrayList<>());

                    // Get the existing list and add the member to it
                    List<String> names = membersDoingExercise.get(workoutForDay.getKey());
                    names.add(member.getName());
                }
            }
        }

        return Collections.unmodifiableMap(membersDoingExercise);
    }

    private SortedSet<GymMember> getUnmodifiableSortedMembers(Comparator<GymMember> comparator) {
        SortedSet<GymMember> sortedMembers = new TreeSet<>(comparator);
        sortedMembers.addAll(this.members);
        return Collections.unmodifiableSortedSet(sortedMembers);
    }
}
