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
     * Gets the number of dependencies (the number of prerequisites and
     * corequisites) of this Course.
     * 
     * @return The number of dependencies.
     */
    public int dependencies()
    {
        return prereqs.size() + coreqs.size();
    }

    /**
     * Gets a String representation of this Course.
     * 
     * @return a String.
     */
    public String toFullString()
    {
        StringBuilder out = new StringBuilder(toStringHelper(this));
        if (prereqs.size() > 0)
        {
            out.append("\n\tPrereqs: ");
            for (Course c : prereqs)
            {
                out.append(toStringHelper(c));
                out.append(", ");
            }
            out.delete(out.length() - 2, out.length());
        }
        if (coreqs.size() > 0)
        {
            out.append("\n\tCoreqs: ");
            for (Course c : coreqs)
            {
                out.append(toStringHelper(c));
                out.append(", ");
            }
            out.delete(out.length() - 2, out.length());
        }
        return out.toString();
    }

    private String toStringHelper(Course c)
    {
        StringBuilder out = new StringBuilder("[");
        out.append(c.department());
        out.append("\t");
        out.append(c.number());
        out.append("\t");
        out.append(c.name());
        out.append(" (");
        out.append(c.credits());
        out.append(")]");
        return out.toString();
    }
    
    public String toString()
    {
        return toStringHelper(this);
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
