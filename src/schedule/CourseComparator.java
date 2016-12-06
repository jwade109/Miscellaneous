package schedule;

import java.util.Comparator;

public class CourseComparator implements Comparator<Course>
{
    public int compare(Course c1, Course c2)
    {
        if (c1.isComplete() && !c2.isComplete())
        {
            return -1;
        }
        if (c2.isComplete() && !c1.isComplete())
        {
            return 1;
        }
        int dif = c1.department().compareTo(c2.department());
        if (dif == 0)
        {
            dif = c1.number() - c2.number();
        }
        if (dif == 0)
        {
            dif = c1.name().compareTo(c2.name());
        }
        if (dif == 0)
        {
            dif = c1.credits() - c2.credits();
        }
        return dif;
    }
}
