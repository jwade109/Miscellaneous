package schedule;

import junit.framework.TestCase;

public class ScheduleTest extends TestCase
{
    private Schedule s;
    private Course aero;
    private Course statics;
    private Course comp;
    
    public void setUp()
    {
        s = new Schedule();
        aero = new Course(Department.AOE, 2104, "Intro to Aerospace Engineering", 2);
        statics = new Course(Department.ESM, 2104, "Statics", 3);
        comp = new Course(Department.AOE, 2074, "Comp Methods", 2);
    }
    
    public void test()
    {
        s.add(aero);
        assertEquals(1, s.size());
        
        Exception e = null;
        try
        {
            s.add(aero);
        }
        catch (DuplicateItemException ex)
        {
            e = ex;
        }
        assertNotNull(e);
        
        s.clear();
        s.add(comp);
        aero.addPrereq(statics);
        aero.addPrereq(comp);
        aero.addCoreq(statics);
        aero.addCoreq(comp);
        
        e = null;
        try
        {
            s.add(aero);
        }
        catch (RequisiteException ex)
        {
            e = ex;
        }
        assertNotNull(e);
        String error = "Can't add - unmet requisites:\n[ESM\t2104\tStatics (3)], [ESM\t2104\tStatics (3)]";
        assertEquals(error, e.getMessage());
        
        e = null;
        try
        {
            s.add(null);
        }
        catch (IllegalArgumentException ex)
        {
            e = ex;
        }
        assertNotNull(e);
    }
}
