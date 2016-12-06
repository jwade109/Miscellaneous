package schedule;

import junit.framework.TestCase;

public class ScheduleTest extends TestCase
{
    private Schedule s;
    private ScheduleQuerier q;

    public void setUp()
    {
        s = new AE_CS_M_Schedule();
        q = new ScheduleQuerier(s);
    }

    /**
     * Tests add() and remove() in normal cases.
     */
    public void testRemoveAdd()
    {
        assertTrue(s.remove(q.search("Manufacturing")));
        assertFalse(s.remove(q.search("Manufacturing")));
        assertTrue(s.remove(q.search("Materials")));
        assertFalse(s.remove(q.search("Materials")));
        assertTrue(s.remove(q.search("Space Mission")));
        assertFalse(s.remove(q.search("Space Mission")));
        
        assertFalse(s.remove(new Course(Department.OTHER, 4, "", 2)));
        assertFalse(s.remove(new Course(Department.CS, 8888, "Cheese", 3)));
    }

    /**
     * Tests add() in cases of adding duplicate Courses.
     */
    public void testAddDuplicateException()
    {
        Exception e = null;
        try
        {
            s.add(q.search("Discrete"));
        }
        catch (DuplicateItemException ex)
        {
            e = ex;
        }
        assertNotNull(e);
    }
    
    /**
     * Tests add() in the case of insufficient requisites.
     */
    public void testAddRequisiteException()
    {
        Course pretest = new Course(Department.ESM, 9, "PreReqTest", 3);
        Course cotest = new Course(Department.ESM, 9, "CoReqTest", 3);
        Course bothtest = new Course(Department.ESM, 9, "BothReqTest", 3);
        pretest.addPrereq(q.search("Manufacturing"));
        cotest.addCoreq(q.search("Manufacturing"));
        bothtest.addPrereq(q.search("Manufacturing"));
        bothtest.addCoreq(q.search("Numerical"));
        s.remove(q.search("Manufacturing"));
        s.remove(q.search("Numerical"));
        
        Exception e = null;
        try
        {
            s.add(pretest);
        }
        catch (RequisiteException ex)
        {
            e = ex;
        }
        assertNotNull(e);
        e = null;
        try
        {
            s.add(cotest);
        }
        catch (RequisiteException ex)
        {
            e = ex;
        }
        assertNotNull(e);
        e = null;
        try
        {
            s.add(bothtest);
        }
        catch (RequisiteException ex)
        {
            e = ex;
        }
        assertNotNull(e);
    }

    /**
     * Tests add() in cases of adding a null Course. 
     */
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

    /**
     * Tests remove() in cases of removing a Course upon which others are dependent.
     */
    public void testRemoveRequisiteException()
    {
        Exception e = null;
        try
        {
            s.remove(q.search("Discrete"));
        }
        catch (RequisiteException ex)
        {
            e = ex;
        }
        assertNotNull(e);
    }

    /**
     * Tests find() for all cases.
     */
    public void testFind()
    {
        assertEquals(q.search("Thermo"), s.find(q.search("Thermo")));
        assertEquals(q.search(null), s.find(q.search(null)));
    }
    
    /**
     * Tests size() for all cases.
     */
    public void testSize()
    {
        assertEquals(29, s.size());
        s.remove(q.search("Numer"));
        s.remove(q.search("Materials"));
        assertEquals(27, s.size());
        s.remove(null);
        assertEquals(27, s.size());
        s.add(new Course(Department.OTHER, 4, "", 3));
        assertEquals(28, s.size());
    }
    
    /**
     * Tests getChildren() for all cases.
     */
    public void testCountChildren()
    {
        assertEquals(0, s.countChildren(q.search("4414")));
        assertEquals(2, s.countChildren(q.search("Operational")));
    }

    /**
     * Tests toCourseList() for all cases.
     */
    public void testToCourseList()
    {
        assertEquals(q.search("Hydro"), s.toCourseList().get(0));
        assertEquals(q.search("Thin"), s.toCourseList().get(1));
        assertEquals(q.search("ISE"), s.toCourseList().get(28));
        
        s.remove(q.search("ISE"));

        assertEquals(q.search("Hydro"), s.toCourseList().get(0));
        assertEquals(q.search("Thin"), s.toCourseList().get(1));
        assertEquals(q.search("CS 3414"), s.toCourseList().get(27));
    }
    
    /**
     * Tests getChildren() and countChildren for all cases.
     */
    public void testGetCountChildren()
    {
        assertEquals(3, s.getChildren(q.search("ESM 2304")).size());
        assertEquals(3, s.countChildren(q.search("ESM 2304")));
        assertEquals(q.search("Vehicle Vibration"), s.getChildren(q.search("ESM 2304")).get(0));
        assertEquals(0, s.countChildren(q.search("Numerical")));
        assertEquals(q.search("AOE 4165"), s.getChildren(q.search("Aerospace Structures")).get(1));
        assertNull(s.getChildren(null));
    }
    
    /**
     * Tests getCourseSublist() for all cases.
     */
    public void testGetCourseSublist()
    {
        assertEquals(3, s.getCourseSublist(q.search("Operational")).size());
        assertEquals(15, s.getCourseSublist(q.search("ESM 2304")).size());
        assertEquals(q.search("Exper"), s.getCourseSublist(q.search("ESM 2304")).get(3));
        assertEquals(q.search("CS 2506"), s.getCourseSublist(q.search("CS 2505")).get(1));
    }
    
    /**
     * Tests toString().
     */
    public void testToString()
    {
        assertNotNull(s.toString());
        // System.out.println(s.toString());
    }

    /**
     * Tests path() for all cases.
     */
    public void testPath()
    {
        assertNotNull(s.path(q.search("Discrete")));
        assertNull(s.path(null));
        System.out.println(s.path(q.search("Discrete")));
    }
    
    /**
     * Tests Schedule's iterator().
     */
    public void testIterator()
    {
        for (Course c : s)
        {
            assertNotNull(c.name());
        }
    }

}
