package schedule;

import java.util.ArrayList;

public class Course
{
    private Department department;
    private int courseNo;
    private String name;
    private int credits;
    private ArrayList<Course> prereqs;
    private ArrayList<Course> coreqs;
    private boolean registered;
    private boolean completed;

    /**
     * Constructs a new Course object.
     * 
     * @param dep The course's department, like CS or MATH.
     * @param no The course's identifier, like 2114.
     * @param na The course's name, like Software Design and Data Structures.
     * @param cr The number of credit hours this course is worth.
     */
    public Course(Department dep, int no, String na, int cr)
    {
        department = dep;
        courseNo = no;
        name = na;
        credits = cr;
        prereqs = new ArrayList<Course>();
        coreqs = new ArrayList<Course>();
        registered = false;
        completed = false;
    }

    /**
     * Gets this Course's department.
     * 
     * @return Department.
     */
    public Department department()
    {
        return department;
    }

    /**
     * Gets this Course's identifier number.
     * 
     * @return Course number.
     */
    public int number()
    {
        return courseNo;
    }

    /**
     * Gets this course's name.
     * 
     * @return Name.
     */
    public String name()
    {
        return name;
    }

    /**
     * Gets the number of credits this course is worth.
     * 
     * @return Credits.
     */
    public int credits()
    {
        return credits;
    }
    
    /**
     * Sets whether this Course is completed or not.
     * 
     * @param state True if completed, false if not.
     */
    public void makeComplete(boolean state)
    {
        completed = state;
    }
    
    /**
     * Gets whether this Course is completed or not.
     * 
     * @return True of completed, false otherwise.
     */
    public boolean isComplete()
    {
        return completed;
    }
    
    /**
     * Sets the registration status of this Course.
     * 
     * @param True if registered, false if not.
     */
    public void setRegistered(boolean state)
    {
        registered = state;
    }
    
    /**
     * Gets whether this Course is registered into a Semester.
     * 
     * @return state True if registered, false otherwise.
     */
    public boolean isRegistered()
    {
        return registered;
    }

    /**
     * Adds a prerequisite to this Course. Prerequisites are courses that must
     * be taken BEFORE (not concurrently with) this course.
     * 
     * @param pre Course to be taken before this one.
     */
    public void addPrereq(Course pre)
    {
        prereqs.add(pre);
    }

    /**
     * Gets all the prerequisite Courses for this Course, sorted.
     * 
     * @return an ArrayList of Courses.
     */
    public ArrayList<Course> getPrereqs()
    {
        prereqs.sort(new CourseComparator());
        return prereqs;
    }

    /**
     * Removes a prerequisite Course from this Course.
     * 
     * @param pre The Course to be removed.
     * @return Whether the removal was successful, true or false.
     */
    public boolean removePrereq(Course pre)
    {
        return prereqs.remove(pre);
    }

    /**
     * Adds a corequisite to this Course. Corequisites are courses that must be
     * taken BEFORE OR CONCURRENTLY WITH this course.
     * 
     * @param co Course to be taken before or during this one.
     */
    public void addCoreq(Course co)
    {
        coreqs.add(co);
    }

    /**
     * Gets all the corequisite Courses for this Course, sorted.
     * 
     * @return an ArrayList of Courses.
     */
    public ArrayList<Course> getCoreqs()
    {
        coreqs.sort(new CourseComparator());
        return coreqs;
    }

    /**
     * Removes a corequisite Course from this Course.
     * 
     * @param co The Course to be removed.
     * @return Whether the removal was successful, true or false.
     */
    public boolean removeCoreq(Course co)
    {
        return coreqs.remove(co);
    }

    /**
     * Gets all requisites (pre and corequisites) of this Course.
     * 
     * @return A sorted List of Courses.
     */
    public ArrayList<Course> getReqs()
    {
        ArrayList<Course> list = new ArrayList<Course>();
        list.addAll(prereqs);
        list.addAll(coreqs);
        list.sort(new CourseComparator());
        return list;
    }
    
    /**
     * Gets the number of dependencies (the number of prerequisites and
     * corequisites) of this Course.
     * 
     * @return The number of dependencies.
     */
    public int countReqs()
    {
        return getReqs().size();
    }

    /**
     * Gets a String representation of this Course, including prerequisites.
     * 
     * @return a String.
     */
    public String toFullString()
    {
        StringBuilder out = new StringBuilder(toString());
        if (prereqs.size() > 0)
        {
            out.append("\nPrereqs: ");
            for (Course c : prereqs)
            {
                out.append(c.toShortString());
                out.append(", ");
            }
            out.delete(out.length() - 2, out.length());
        }
        if (coreqs.size() > 0)
        {
            out.append("\nCoreqs: ");
            for (Course c : coreqs)
            {
                out.append(c.toShortString());
                out.append(", ");
            }
            out.delete(out.length() - 2, out.length());
        }
        return out.toString();
    }

    /**
     * Gets a String representation of the Course.
     * 
     * @return A String.
     */
    public String toString()
    {
        StringBuilder out = new StringBuilder("[");
        out.append(department);
        out.append("\t");
        out.append(courseNo);
        out.append("\t");
        out.append(credits);
        out.append("\t");
        out.append(name);
        if (isRegistered())
        {
            out.append(" +");
        }
        if (isComplete())
        {
            out.append(" *");
        }
        out.append("]");
        return out.toString();
    }
    
    /**
     * Gets a truncated String representation.
     * 
     * @return A String.
     */
    public String toShortString()
    {
        StringBuilder out = new StringBuilder("[");
        out.append(department);
        out.append(" ");
        out.append(courseNo);
        if (isComplete())
        {
            out.append(" *");
        }
        out.append("]");
        return out.toString();
    }

    /**
     * Checks if another Object is equal to this one. Courses are equal iff
     * their Departments, Numbers, Names, and Credits are identical.
     * 
     * @param other The Object to check for equality.
     * @return Whether this Course and the argument are equal.
     */
    public boolean equals(Object other)
    {
        if (other == null || other.getClass() != this.getClass())
        {
            return false;
        }
        if (other == this)
        {
            return true;
        }

        Course otherCourse = (Course) other;

        return department.equals(otherCourse.department)
                && courseNo == otherCourse.number()
                && name.equals(otherCourse.name())
                && credits == otherCourse.credits();
    }
}
