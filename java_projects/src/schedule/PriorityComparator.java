package schedule;

import java.util.Comparator;

public class PriorityComparator implements Comparator<Course>
{
    private ScheduleQuerier q;
    
    public PriorityComparator(ScheduleQuerier q)
    {
        this.q = q;
    }
    
    public int compare(Course c1, Course c2)
    {
        int dif = q.importanceOf(c2) - q.importanceOf(c1);
        if (dif == 0)
        {
            dif = new CourseComparator().compare(c1, c2);
        }
        return dif;
    }
}