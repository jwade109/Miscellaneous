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
     * The current size of the Schedule.
     */
    private int size;

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
        // course has no requisites, it can be added as a root
        if (newCourse.countReqs() == 0)
        {
            root.addPre(new CNode(newCourse));
            size++;
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
                size++;
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
     * @param c The Course to remove.
     * @return True if removed successfully, false otherwise.
     */
    public boolean remove(Course c)
    {
        if (c == null)
        {
            return false;
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
            size--;
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
        size--;
        return true;
    }

    /**
     * Checks if the Schedule contains a given Course.
     * 
     * @param c The Course to search for.
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
     * Checks if a Course is complete or not. Returns false if the Course is not
     * in the Schedule.
     * 
     * @param c The Course to inspect.
     * @return Whether the Course is complete or not.
     */
    public boolean isComplete(Course c)
    {
        if (c == null || !contains(c))
        {
            return false;
        }
        return findNode(root, c).getCourse().isComplete();
    }

    /**
     * Makes a Course complete or not complete.
     * 
     * @param c The Course to alter.
     * @param state Whether the Course is complete or not.
     */
    public void makeComplete(Course c, boolean state)
    {
        if (c == null || !contains(c))
        {
            findNode(root, c).getCourse().makeComplete(state);
        }
    }

    /**
     * Gets whether a Course is currently registered.
     * 
     * @param c The Course to inspect.
     * @return Whether the Course is registered.
     */
    public boolean isRegistered(Course c)
    {
        if (!contains(c))
        {
            return false;
        }
        return findNode(root, c).getCourse().isRegistered();
    }
    
    /**
     * Sets the registration of a Course in this Schedule.
     * 
     * @param c The Course to alter.
     * @param state Whether the Course is registered.
     */
    public void setRegistered(Course c, boolean state)
    {
        if (contains(c))
        {
            findNode(root, c).getCourse().setRegistered(state);
        }
    }
    
    /**
     * A private helper method which recursively finds the CNode containing a
     * given Course, given root node.
     * 
     * @param parent A CNode which is expected to be the target CNode's
     *        ancestor.
     * @param c The target Course.
     * @return The Node containing the given Course if it can be found. Null
     *         otherwise.
     */
    private CNode findNode(CNode parent, Course c)
    {
        if (c == null)
        {
            return null;
        }
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
        return size;
    }

    /**
     * Gets all the children of a Course in a sorted List.
     * 
     * @param parent The Course to be inspected.
     * @return a sorted List of Courses which are children of the parent Course.
     */
    public ArrayList<Course> getChildren(Course parent)
    {
        if (parent == null)
        {
            return null;
        }
        ArrayList<CNode> childNodes = findNode(root, parent).getChildren();
        ArrayList<Course> childCourses = new ArrayList<Course>();
        for (CNode node : childNodes)
        {
            childCourses.add(node.getCourse());
        }
        childCourses.sort(new CourseComparator());
        return childCourses;
    }

    /**
     * Gets the number of children of this Course.
     * 
     * @param c The Course to inspected.
     * @return The number of Courses directly under this Course.
     */
    public int countChildren(Course parent)
    {
        return findNode(root, parent).countChildren();
    }

    /**
     * Returns a sorted List of all Courses in this Schedule. This List will
     * contain each Course no more than once.
     * 
     * @return A List of Courses in this Schedule.
     */
    public ArrayList<Course> toCourseList()
    {
        ArrayList<Course> list = getCourseList(root, new ArrayList<Course>());
        list.sort(new CourseComparator());
        return list;
    }

    /**
     * Gets a sorted list containing a Course and all Courses in the subschedule
     * defined by that Course. In other words, returns a Course and all Courses
     * which depend on that Course.
     * 
     * @param parent The Course to define the top of the subschedule.
     * @return A List containing a parent Course all Courses underneath.
     */
    public ArrayList<Course> getCourseSublist(Course parent)
    {
        ArrayList<Course> list = getCourseList(findNode(root, parent),
                new ArrayList<Course>());
        list.sort(new CourseComparator());
        return list;
    }

    /**
     * A private helper method which returns a List of Courses. The List
     * contains the Course in the root CNode, as well as all of root's children.
     * This amounts to compiling a list of all Courses which are dependent on
     * the parent Course, as well as the parent itself. The list is not sorted.
     * 
     * @param parent The parent CNode of the subtree to compile a list for.
     * @param list A list which is compiled and passed to this method
     *        recursively.
     * @return A List of all Courses in the subtree defined by CNode parent.
     */
    private ArrayList<Course> getCourseList(CNode parent,
            ArrayList<Course> list)
    {
        Course rootCourse = parent.getCourse();
        if (rootCourse != null && !list.contains(rootCourse))
        {
            list.add(parent.getCourse());
        }
        if (parent.isLeaf())
        {
            return list;
        }
        for (CNode pre : parent.getPre())
        {
            list = getCourseList(pre, list);
        }
        for (CNode co : parent.getCo())
        {
            list = getCourseList(co, list);
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
     * Gets a String representation of the Schedule which displays the hierarchy
     * of Courses.
     * 
     * @return A String representation of the Schedule.
     */
    public String toString()
    {
        return traverse(root, 0);
    }

    /**
     * Returns a String representing the hierarchy of dependencies of the
     * specified Course.
     * 
     * @param c The Course to inspect.
     * @return A String printout of the tree with the c as its root.
     */
    public String path(Course c)
    {
        if (c == null)
        {
            return null;
        }
        return traverse(findNode(root, c), 1);
    }

    /**
     * Traverses the root CNode and every child of root, and returns a String
     * representation of the hierarchy.
     * 
     * @param parent The parent CNode.
     * @param depth An int representing the current depth of the recursive call.
     * @return A String representation of root and all of its children.
     */
    private String traverse(CNode parent, int depth)
    {
        StringBuilder out = new StringBuilder();
        if (parent != root)
        {
            out.append(parent.getCourse().name());
            if (parent.getCourse().isComplete())
            {
                out.append(" *");
            }
            out.append("\n");
        }
        for (CNode each : parent.getChildren())
        {
            for (int i = 0; i < depth; i++)
            {
                out.append("| ");
            }
            out.append(traverse(each, depth + 1));
        }
        return out.toString();
    }

    /**
     * Gets an Iterator for this Schedule, which iterates over a sorted
     * courselist.
     * 
     * @return an Iterator over this Schedule's courselist.
     */
    public Iterator<Course> iterator()
    {
        return toCourseList().iterator();
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

        /**
         * Constructs a new CNode containing a Course.
         * 
         * @param c The Course to be contained in this node.
         */
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
         * Returns a list of all predependent children of this CNode's course.
         * 
         * @return An ArrayList of CNodes.
         */
        public ArrayList<CNode> getPre()
        {
            return predependents;
        }

        /**
         * Returns a list of all codependent children of this CNode's course.
         * 
         * @return An ArrayList of CNodes.
         */
        public ArrayList<CNode> getCo()
        {
            return codependents;
        }

        /**
         * Gets a list of all children of this CNode.
         * 
         * @return A List of CNodes.
         */
        public ArrayList<CNode> getChildren()
        {
            ArrayList<CNode> list = new ArrayList<CNode>();
            list.addAll(predependents);
            list.addAll(codependents);
            return list;
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
        public int countChildren()
        {
            return predependents.size() + codependents.size();
        }

        /**
         * Checks whether this CNode is a "root" of the Schedule. CNodes are
         * considered roots the Course they contain lists no pre or
         * corequisites.
         * 
         * @return Whether this CNode is an orphan.
         */
        public boolean isRoot()
        {
            return course.countReqs() == 0;
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
            return countChildren() == 0;
        }
    }
}
