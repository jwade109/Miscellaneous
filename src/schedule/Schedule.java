package schedule;

import java.util.ArrayList;

public class Schedule implements ScheduleInterface
{
    /**
     * A List of all of the roots of this Schedule - i.e., all of the Courses
     * which have zero, or already satisfied, prerequisites.
     */
    private ArrayList<CNode> root;

    public Schedule()
    {
        clear();
    }

    /**
     * Adds a Course to the Schedule. Throws an RequisitesException if the
     * Course that's added lists a pre or corequisite which is not already in
     * the Schedule.
     * 
     * @param c The course to add.
     * @throws IllegalArgumentException
     * @throws DuplicateItemException
     * @throws RequisitesException
     */
    public void add(Course c)
    {
        // course is null, throw an exception
        if (c == null)
        {
            throw new IllegalArgumentException("Course cannot be null");
        }
        // course is already in the schedule, throw exception
        if (contains(c))
        {
            throw new DuplicateItemException("Cannot store duplicate courses");
        }
        // course has no dependencies, it can be added as a root
        if (c.dependencies() == 0)
        {
            root.add(new CNode(c));
        }
        else
        {
            // check if all dependencies are met
            ArrayList<Course> unsatisfied = new ArrayList<Course>();
            // check if all prereqs are already in the schedule
            for (Object pre : c.getPrereqs())
            {
                if (!contains((Course) pre))
                {
                    unsatisfied.add((Course) pre);
                }
            }
            // check if all coreqs are already in the schedule
            for (Object co : c.getCoreqs())
            {
                if (!contains((Course) co))
                {
                    unsatisfied.add((Course) co);
                }
            }
            // if they are... do something
            if (unsatisfied.isEmpty())
            {
                // recursive method?
                throw new UnsupportedOperationException("Not implemented yet");
            }
            // some required ancestors are not present, throw an exception
            else
            {
                StringBuilder out = new StringBuilder("Can't add - unmet requisites:\n");
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

    @Override
    public boolean remove(Course c)
    {
        // TODO Auto-generated method stub
        return false;
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
        for (CNode node : root)
        {
            if (c.equals(node.getCourse()))
            {
                return node.getCourse();
            }
            CNode found = findNode(node, c);
            if (found != null)
            {
                return found.getCourse();
            }
        }
        return null;
        
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
        for (CNode node : parent.getPredependents())
        {
            if (findNode(node, c) != null)
            {
                return findNode(node, c);
            }
        }
        for (CNode node : parent.getCodependents())
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
        int n = 0;
        for (CNode c : root)
        {
            n += size(c);
        }
        return n;
    }

    /**
     * A recursive helper method which determines the size of a subtree given
     * its topmost CNode. Note that the returned value includes the root itself,
     * such that a root with zero children will return 1.
     * 
     * @param root The top of the subtree.
     * @return The number of children of the root plus 1.
     */
    private int size(CNode parent)
    {
        int n = 1;
        for (CNode c : parent.getPredependents())
        {
            n += size(c);
        }
        for (CNode c : parent.getCodependents())
        {
            n += size(c);
        }
        return n;
    }

    /**
     * Removes all Courses from the Schedule.
     */
    public void clear()
    {
        root = new ArrayList<CNode>();
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
         * Sets the Course stored by this CNode.
         * 
         * @param c The Course to be stored.
         */
        public void setCourse(Course c)
        {
            course = c;
        }

        /**
         * Returns a list of all predependent courses of this CNode's course.
         * 
         * @return An ArrayList of CNodes.
         */
        public ArrayList<CNode> getPredependents()
        {
            return predependents;
        }

        /**
         * Returns a list of all codependent courses of this CNode's course.
         * 
         * @return An ArrayList of CNodes.
         */
        public ArrayList<CNode> getCodependents()
        {
            return codependents;
        }

        /**
         * Adds a CNode to this CNode's predependent descendents.
         * 
         * @param node The node to be added.
         */
        public void addPredependent(CNode node)
        {
            predependents.add(node);
        }

        /**
         * Adds a CNode to this CNode's codependent descendents.
         * 
         * @param node The node to be added.
         */
        public void addCodependent(CNode node)
        {
            codependents.add(node);
        }

        /**
         * Removes a CNode from this node's predependent descendents.
         * 
         * @param node The CNode to be removed.
         * @return True if successful, false if otherwise.
         */
        public boolean removePredependent(CNode node)
        {
            return predependents.remove(node);
        }

        /**
         * Removes a CNode from this node's codependent descendents.
         * 
         * @param node The CNode to be removed.
         * @return True if successful, false if otherwise.
         */
        public boolean removeCodependent(CNode node)
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
    }

}
