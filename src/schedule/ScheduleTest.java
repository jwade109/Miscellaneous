package schedule;

import java.util.ArrayList;

import junit.framework.TestCase;

public class ScheduleTest extends TestCase
{
    private Schedule s;

    private Course aero;
    private Course statics;
    private Course comp;
    private Course philOne;
    private Course philTwo;
    private Course software;

    private Course aircraft;

    public void setUp()
    {
        s = new Schedule();

        aero = new Course(Department.AOE, 2104,
                "Intro to Aerospace Engineering", 2);
        statics = new Course(Department.ESM, 2104, "Statics", 3);
        comp = new Course(Department.AOE, 2074, "Comp Methods", 2);
        philOne = new Course(Department.PHIL, 1204, "Knowledge and Reality", 3);
        philTwo = new Course(Department.PHIL, 1304, "Morality and Justice", 3);
        software = new Course(Department.CS, 2114,
                "Software Design and Data Structures", 3);

        aircraft = new Course(Department.AOE, 3104, "Aircraft Performance", 3);
        aircraft.addPrereq(aero);
        aircraft.addCoreq(comp);
    }

    public void testAddProper()
    {
        s.add(aero);
        s.add(comp);
        s.add(aircraft);
        assertEquals(3, s.size());
        assertEquals(aircraft, s.find(aircraft));
        s.clear();
        assertEquals(0, s.size());
    }

    public void testAddDuplicate()
    {
        s.add(aero);

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

    }

    public void testAddRequisite()
    {
        Exception e = null;
        try
        {
            s.add(aircraft);
        }
        catch (RequisiteException ex)
        {
            e = ex;
        }
        assertNotNull(e);
        String error = "Can't add - unmet requisites:\n"
                + "[AOE\t2104\tIntro to Aerospace Engineering (2)], "
                + "[AOE\t2074\tComp Methods (2)]";
        assertEquals(error, e.getMessage());
    }

    public void testAddNull()
    {
        Exception e = null;
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
    
    public void testRemove()
    {
        s.add(aero);
        s.add(statics);
        s.add(comp);
        s.add(aircraft);
        assertEquals(4, s.size());
        assertTrue(s.remove(aircraft));
        assertTrue(s.remove(aero));
        assertEquals(2, s.size());
    }
    
    public void testRemoveNull()
    {
        Exception e = null;
        try
        {
            s.remove(null);
        }
        catch (IllegalArgumentException ex)
        {
            e = ex;
        }
        assertNotNull(e);
    }
    
    public void testRemoveNotInSchedule()
    {
        assertFalse(s.remove(aero));
    }
    
    public void testRemoveDependency()
    {
        s.add(aero);
        s.add(comp);
        s.add(aircraft);
        Exception e = null;
        try
        {
            s.remove(aero);
        }
        catch (RequisiteException ex)
        {
            e = ex;
        }
        assertNotNull(e);
    }

    public void testSize()
    {
        s.add(aero);
        s.add(statics);
        s.add(comp);
        assertEquals(3, s.size());
        s.clear();
        assertEquals(0, s.size());
    }

    public void testFind()
    {
        s.add(comp);
        s.add(aero);
        s.add(aircraft);
        assertEquals(aero, s.find(aero));
        assertEquals(comp, s.find(comp));
        assertNull(s.find(statics));
        assertEquals(aircraft, s.find(aircraft));
    }

    public void testToCourseList()
    {
        s.add(aero);
        s.add(comp);
        s.add(statics);
        s.add(philOne);
        s.add(philTwo);
        s.add(software);
        s.add(aircraft);
        ArrayList<Course> list = s.toCourseList();
        assertEquals(7, list.size());
        assertEquals(7, s.size());
    }
    
    public void testGetDependents()
    {
        s.add(aero);
        s.add(comp);
        s.add(aircraft);
        assertEquals(aircraft, s.getDependents(aero).get(0));
        assertEquals(aircraft, s.getDependents(comp).get(0));
    }
}
