package schedule;

import java.util.Comparator;

public class PriorityComparator implements Comparator<Course>
{
    private Schedule s;
    
    public PriorityComparator(Schedule s)
    {
        this.s = s;
    }
    
    public int compare(Course c1, Course c2)
    {
        return s.children(c2) - s.children(c1);
    }
}