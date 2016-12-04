package schedule;

/**
 * An ADT which contains just Course objects for the time being. Basically, it
 * stores Objects which have some notion of prerequisites and corequisites. It
 * takes the form of a Tree of Nodes with Arbitrary numbers of children. Nodes
 * that have zero children, the leaves, represent Courses for which all
 * requirements are satisfied.
 * 
 * @author Wade Foster
 * @version 2016.12.4
 */
public interface ScheduleInterface
{
    /**
     * Adds a Course to the Schedule. Throws an RequisitesException if the
     * Course that's added lists a pre or corequisite which is not already in
     * the Schedule.
     * 
     * @param c The course to add.
     */
    public void add(Course c);

    /**
     * Removes a Course from the Schedule. Throws a RequisitesException if the
     * Course to remove is a requisite for another Course in the schedule (i.e.
     * if the Course is not a parent node for any other Courses on the tree.)
     * 
     * @param c The Course to remove.
     * @return Whether the Course was removed, true or false.
     * @throws RequisitesException if the Course is not a leaf.
     */
    public boolean remove(Course c);

    /**
     * Checks if the Schedule contains a given Course.
     * 
     * @return Whether the Course is in the Schedule.
     */
    public boolean contains(Course c);
    
    /**
     * Finds a Course in the Schedule. Returns null if the Course cannot be
     * found.
     * 
     * @param c The Course to find.
     * @return The Course if found, null if not.
     */
    public Course find(Course c);

    /**
     * Gets the number of Courses in the Schedule.
     * 
     * @return The size of the Schedule.
     */
    public int size();
}
