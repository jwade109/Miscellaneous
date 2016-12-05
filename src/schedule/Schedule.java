package schedule;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Schedule is a data structure which stores Course objects as members of a
 * directed graph. This graph has two types of vertices, which represent
 * prerequisite courses and corequisite courses. Courses at the top of the tree
 * represent those with no unsatisfied prerequisites - i.e., Courses which can
 * be taken immediately. This structure is used to generate an optimal
 * class-taking order.
 * 
 * @author Wade Foster
 * @version 2016.12.4
 */
public class Schedule implements Iterable<Course>
{
    /**
     * A List of all of the roots of this Schedule - i.e., all of the Courses
     * which have zero, or already satisfied, prerequisites.
     */
    private CNode root;

    /**
     * Constructs a new Schedule object.
     */
    public Schedule()
    {
        clear();
    }

    /**
     * Adds a Course to the Schedule. Throws a RequisiteException if the Course
     * that's added lists a pre or corequisite which is not already in the
     * Schedule.
     * 
     * @param c The course to add.
     * @throws IllegalArgumentException
     * @throws DuplicateItemException
     * @throws RequisiteException
     */
    public void add(Course newCourse)
    {
        // course is null, throw an exception
        if (newCourse == null)
        {
            throw new IllegalArgumentException("Course cannot be null");
        }
        // course is already in the schedule, throw exception
        if (contains(newCourse))
        {
            throw new DuplicateItemException("Cannot store duplicate courses");
        }
        // course has no dependencies, it can be added as a root
        if (newCourse.dependencies() == 0)
        {
            root.addPre(new CNode(newCourse));
        }
        else
        {
            // check if all dependencies are met
            ArrayList<Course> unsatisfied = new ArrayList<Course>();
            // check if all prereqs are already in the schedule
            for (Course pre : newCourse.getPrereqs())
            {
                if (!contains(pre))
                {
                    unsatisfied.add((pre));
                }
            }
            // check if all coreqs are already in the schedule
            for (Course co : newCourse.getCoreqs())
            {
                if (!contains(co))
                {
                    unsatisfied.add(co);
                }
            }
            // if they are... do something
            if (unsatisfied.isEmpty())
            {
                CNode newNode = new CNode(newCourse);
                for (Course pre : newCourse.getPrereqs())
                {
                    CNode preNode = findNode(root, pre);
                    preNode.addPre(newNode);
                }
                for (Course co : newCourse.getCoreqs())
                {
                    CNode coNode = findNode(root, co);
                    coNode.addCo(newNode);
                }
            }
            // some required ancestors are not present, throw an exception
            else
            {
                StringBuilder out = new StringBuilder(
                        "Can't add - unmet requisites:\n");
                for (Course uns : unsatisfied)
                {
                    out.append(uns.toString());
                    out.append(", ");
                }
                out.delete(out.length() - 2, out.length());
                throw new RequisiteException(out.toString());
            }
        }
    }

    /**
     * Removes a Course from the Schedule. Throws a RequisiteException if the
     * Course is a requisite for another Course - that is, if the Course is not
     * a leaf for the Schedule.
     * 
     * @return True if removed successfully, false otherwise.
     * @throws RequisiteException
     */
    public boolean remove(Course c)
    {
        if (c == null)
        {
            throw new IllegalArgumentException("Can't remove a null Course");
        }
        CNode toRemove = findNode(root, c);
        if (toRemove == null)
        {
            return false;
        }
        if (!toRemove.isLeaf())
        {
            throw new RequisiteException(
                    "Removal would create unsatisfied dependency");
        }
        if (toRemove.isRoot())
        {
            root.removePre(toRemove);
            return true;
        }
        for (Course pre : c.getPrereqs())
        {
            CNode parent = findNode(root, pre);
            parent.removePre(toRemove);
        }
        for (Course co : c.getCoreqs())
        {
            CNode parent = findNode(root, co);
            parent.removeCo(toRemove);
        }
        return true;
    }

    /**
     * Checks if the Schedule contains a given Course.
     * 
     * @return True if the Schedule contains the Course, false otherwise.
     */
    public boolean contains(Course c)
    {
        return find(c) != null;
    }

    /**
     * Finds a Course in the Schedule. Returns null if the Course cannot be
     * found.
     * 
     * @param c The Course to find.
     * @return The Course if found, null if not.
     */
    public Course find(Course c)
    {
        CNode foundNode = findNode(root, c);
        if (foundNode == null)
        {
            return null;
        }
        return foundNode.getCourse();
    }

    /**
     * Gets a list of all Courses which must come after, or during, this Course.
     * In other words, returns all Courses for which this Course is a pre or co
     * requisite. This is a dynamic return, since Courses can be added as this
     * Course's dependent.
     * 
     * @param parent The Course to inspect.
     * @return All Courses which depend upon parent.
     */
    public ArrayList<Course> getDependents(Course parent)
    {
        ArrayList<Course> list = new ArrayList<Course>();
        list = toCourseList(findNode(root, parent), list);
        list.sort(new CourseComparator());
        list.remove(parent);
        return list;
    }

    /**
     * A private helper method which recursively finds the CNode containing a
     * given Course, given root node.
     * 
     * @return The Node containing the given Course if it can be found. Null
     *         otherwise.
     */
    private CNode findNode(CNode parent, Course c)
    {
        if (c.equals(parent.getCourse()))
        {
            return parent;
        }
        for (CNode node : parent.getPre())
        {
            if (findNode(node, c) != null)
            {
                return findNode(node, c);
            }
        }
        for (CNode node : parent.getCo())
        {
            if (findNode(node, c) != null)
            {
                return findNode(node, c);
            }
        }
        return null;
    }

    /**
     * Gets the number of Courses in this Schedule.
     * 
     * @return The number of Courses.
     */
    public int size()
    {
        return toCourseList().size();
    }

    public ArrayList<Course> toCourseList()
    {
        ArrayList<Course> list = new ArrayList<Course>();
        list = toCourseList(root, list);
        list.sort(new CourseComparator());
        return list;
    }

    private ArrayList<Course> toCourseList(CNode root, ArrayList<Course> list)
    {
        Course rootCourse = root.getCourse();
        if (rootCourse != null && !list.contains(rootCourse))
        {
            list.add(root.getCourse());
        }
        if (root.isLeaf())
        {
            return list;
        }
        for (CNode pre : root.getPre())
        {
            list = toCourseList(pre, list);
        }
        for (CNode co : root.getCo())
        {
            list = toCourseList(co, list);
        }
        return list;
    }

    /**
     * Removes all Courses from the Schedule.
     */
    public void clear()
    {
        root = new CNode(null);
    }

    /**
     * The CNode class acts as a wrapper class which contains references
     * chronologically forwards, whereas Courses store their information
     * chronologically backwards. To be clear, CNodes contain a certain Course,
     * and references to all CNodes which contain Courses for which that certain
     * Course is a pre or corequisite.
     * 
     * This implementation allows Courses with zero requisites to act as roots
     * for the Tree.
     * 
     * @author Wade Foster
     * @version 2016.12.4
     */
    private class CNode
    {
        /**
         * The Course contained by this CNode.
         */
        private Course course;

        /**
         * A List of all CNodes containing courses for which the Course
         * contained by this CNode is a prerequisite.
         */
        private ArrayList<CNode> predependents;

        /**
         * A list of all CNodes containing courses for which the Course
         * contained by this CNode is a corequisite.
         */
        private ArrayList<CNode> codependents;

        public CNode(Course c)
        {
            course = c;
            predependents = new ArrayList<CNode>();
            codependents = new ArrayList<CNode>();
        }

        /**
         * Gets the Course contained by this CNode.
         * 
         * @return the Course in this node.
         */
        public Course getCourse()
        {
            return course;
        }

        /**
         * Returns a list of all predependent courses of this CNode's course.
         * 
         * @return An ArrayList of CNodes.
         */
        public ArrayList<CNode> getPre()
        {
            return predependents;
        }

        /**
         * Returns a list of all codependent courses of this CNode's course.
         * 
         * @return An ArrayList of CNodes.
         */
        public ArrayList<CNode> getCo()
        {
            return codependents;
        }

        /**
         * Adds a CNode to this CNode's predependent descendents.
         * 
         * @param node The node to be added.
         */
        public void addPre(CNode node)
        {
            predependents.add(node);
        }

        /**
         * Adds a CNode to this CNode's codependent descendents.
         * 
         * @param node The node to be added.
         */
        public void addCo(CNode node)
        {
            codependents.add(node);
        }

        /**
         * Removes a CNode from this node's predependent descendents.
         * 
         * @param node The CNode to be removed.
         * @return True if successful, false if otherwise.
         */
        public boolean removePre(CNode node)
        {
            return predependents.remove(node);
        }

        /**
         * Removes a CNode from this node's codependent descendents.
         * 
         * @param node The CNode to be removed.
         * @return True if successful, false if otherwise.
         */
        public boolean removeCo(CNode node)
        {
            return codependents.remove(node);
        }

        /**
         * Gets the number of descendents of this CNode.
         * 
         * @return the number of pre and codependent descendents.
         */
        public int dependents()
        {
            return predependents.size() + codependents.size();
        }

        /**
         * Checks whether this CNode is a "root" of the Schedule. CNodes are
         * considered roots the Course they contain lists no pre or
         * corequisites.
         * 
         * @return Whether this CNode is a root.
         */
        public boolean isRoot()
        {
            return course.dependencies() == 0;
        }

        /**
         * Checks whether this CNode is a "leaf" of the Schedule. CNodes are
         * considered leaves the Course they contain is not a co or prerequisite
         * of any other course on the schedule.
         * 
         * @return Whether this CNode is a leaf.
         */
        public boolean isLeaf()
        {
            return dependents() == 0;
        }
    }

    public Iterator<Course> iterator()
    {
        return toCourseList().iterator();
    }
}
