package schedule;

import java.util.ArrayList;

/**
 * A wrapper class for Schedule. Since Schedule was beginning to become bloated,
 * what with all the counting and returning it needs to do, it was decided that
 * a separate object should quiery the Schedule to return specific information.
 * 
 * @author Wade Foster
 * @version 2016.12.6
 */
public class ScheduleQuerier
{

    public static void main(String[] args)
    {
        Schedule s = new AE_CS_M_Schedule();
        ScheduleQuerier q = new ScheduleQuerier(s);
        
        q.printMostEfficientSemester(18, 6);
        q.printMostEfficientSemester(18, 6);
        q.printMostEfficientSemester(18, 6);
        q.printMostEfficientSemester(18, 6);
        q.printMostEfficientSemester(18, 6);
        q.printMostEfficientSemester(18, 6);
        q.printMostEfficientSemester(18, 6);
    }

    /**
     * The Schedule stored and accessed by this Querier.
     */
    private Schedule s;

    /**
     * Constructs a new ScheduleQuerier.
     * 
     * @param schedule The Schedule to interpret.
     */
    public ScheduleQuerier(Schedule schedule)
    {
        s = schedule;
    }

    /**
     * Finds a given Course using a search String. If a Course is not found,
     * this method will return null.
     * 
     * @param search The search term to use.
     * @return A Course if it can be found. Null otherwise.
     */
    public Course search(String search)
    {
        if (search == null)
        {
            return null;
        }
        ArrayList<Course> courses = s.toCourseList();
        for (Course each : courses)
        {
            if (each.name().contains(search) || search.contains(each.name()))
            {
                return each;
            }
        }
        for (Course each : courses)
        {
            if (search.contains(Integer.toString(each.number())))
            {
                return each;
            }
        }
        for (Course each : courses)
        {
            if (search.contains(each.department().toString()))
            {
                return each;
            }
        }
        return null;
    }

    /**
     * Ranks a Course based on its importance in the Schedule. "Importance"
     * simply quantifies how many Courses must be taken after a particular
     * Course. For example, if Course A must come before B, C, and D, then A has
     * an importance of 3. Courses which are leaves on the Schedule have an
     * importance of 0.
     * 
     * @param parent
     * @return The importance of the Course.
     */
    public int importanceOf(Course parent)
    {
        return s.getCourseSublist(parent).size() - 1;
    }

    /**
     * Prints the most efficient next Semester.
     */
    public void printMostEfficientSemester(int maxCredits, int maxClasses)
    {
        for (Course c : getMostEfficientSemester(maxCredits, maxClasses))
        {
            c.setRegistered(false);
            c.makeComplete(true);
            System.out.println(c.toString());
        }
        System.out.println();
    }
    
    /**
     * Gets the most important Course in the Schedule.
     * 
     * @return The Course with the most descendents.
     */
    public Course getMostImportantCourse()
    {
        return getMostImportantCourse(s.toCourseList(), 10000000);
    }

    /**
     * Checks whether a Course can be taken. Logically, a Course can be taken
     * iff ALL of its pre and corequisites have been marked isCompleted() ==
     * true.
     * 
     * @param c The Course to be inspected.
     * @return Whether the Course can be taken.
     */
    public boolean canTake(Course c)
    {
        if (!s.contains(c) || c.isComplete())
        {
            return false;
        }
        for (Course pre : c.getPrereqs())
        {
            if (!pre.isComplete())
            {
                return false;
            }
        }
        for (Course co : c.getCoreqs())
        {
            if (!co.isRegistered() && !co.isComplete())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Compiles and returns a List of all Courses in the Schedule which can be
     * taken.
     * 
     * @return All Courses where canTake() returns true.
     */
    public ArrayList<Course> getRegisterableCourses()
    {
        ArrayList<Course> list = new ArrayList<Course>();
        for (Course c : s)
        {
            if (canTake(c) && !c.isRegistered())
            {
                list.add(c);
            }
        }
        return list;
    }
    
    /**
     * Gets a List of all Courses which are currently registered.
     * 
     * @return A List of registered Courses.
     */
    public ArrayList<Course> getRegisteredCourses()
    {
        ArrayList<Course> list = new ArrayList<Course>();
        for (Course c : s)
        {
            if (c.isRegistered())
            {
                list.add(c);
            }
        }
        return list;
    }

    /**
     * Returns the most important Course in a given list, given a maximum number
     * of credits.
     * 
     * @param courses A List of Courses.
     * @param maxCredits The maximum number of credits which the returned Course
     *        can be.
     * @return The most important Course.
     */
    private Course getMostImportantCourse(ArrayList<Course> courses,
            int maxCredits)
    {
        int currentMax = -1;
        Course max = null;
        for (Course c : courses)
        {
            if (importanceOf(c) > currentMax && c.credits() <= maxCredits)
            {
                currentMax = importanceOf(c);
                max = c;
            }
        }
        return max;
    }

    /**
     * Compiles a List of Courses which 1) are the most important Courses
     * possible to take in a single term, and 2) sum at most to the maximum
     * allowable number of credits.
     * 
     * @param maxCredits The maximum allowable number of credits.
     * @return A List of Courses representing the most efficient possible
     *         semester.
     */
    public ArrayList<Course> getMostEfficientSemester(int maxCredits, int maxClasses)
    {
        int creditsLeft = maxCredits;
        int classesLeft = maxClasses;
        ArrayList<Course> sem = new ArrayList<Course>();
        while (creditsLeft > 0 && classesLeft > 0)
        {
            ArrayList<Course> takeable = getRegisterableCourses();
            takeable.sort(new PriorityComparator(this));
            Course max = getMostImportantCourse(takeable, creditsLeft);
            if (max == null)
            {
                return sem;
            }
            sem.add(max);
            max.setRegistered(true);
            creditsLeft -= max.credits();
            classesLeft--;
        }
        return sem;
    }
}
