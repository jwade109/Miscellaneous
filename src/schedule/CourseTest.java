package schedule;

import java.util.ArrayList;
import junit.framework.TestCase;

public class CourseTest extends TestCase
{
    private Course aero;
    private Course comp;
    private Course data;
    private Course statics;
    private Course morality;
    private Course reality;

    private Course staticsClone;
    private Course dynamics;
    private Course brokenStatics;
    private Course moreStatics;
    private Course againStatics;
    private String notACourse;
    private Course nullCourse;

    private ArrayList<Course> courses;

    public void setUp()
    {
        aero = new Course(Department.AOE, 2104,
                "Intro to Aerospace Engineering", 2);
        comp = new Course(Department.AOE, 2074, "Computational Methods", 2);
        data = new Course(Department.CS, 2114,
                "Software Design and Data Structures", 3);
        statics = new Course(Department.ESM, 2104, "Statics", 3);
        morality = new Course(Department.PHIL, 1304, "Morality and Justice", 3);
        reality = new Course(Department.PHIL, 1204, "Knowledge and Reality", 3);

        staticsClone = new Course(Department.ESM, 2104, "Statics", 3);
        dynamics = new Course(Department.ESM, 2304, "Dynamics", 3);
        brokenStatics = new Course(Department.ESM, 2104, "Smatics", 3);
        moreStatics = new Course(Department.ESM, 2104, "Statics", 2);
        againStatics = new Course(Department.ME, 2104, "Statics", 3);
        notACourse = "Nope!";
        nullCourse = null;

        courses = new ArrayList<Course>();
        courses.add(aero);
        courses.add(comp);
        courses.add(statics);
        courses.add(data);
        courses.add(morality);
        courses.add(reality);
    }

    public void testGetters()
    {
        assertEquals(Department.AOE, aero.department());
        assertEquals(2074, comp.number());
        assertEquals(Department.CS, data.department());
        assertEquals("Statics", statics.name());
        assertEquals(1304, morality.number());
        assertEquals(3, reality.credits());
    }

    public void testToString()
    {
        assertEquals("[AOE\t2104\tIntro to Aerospace Engineering (2)]",
                aero.toString());
        assertEquals("[ESM\t2104\tStatics (3)]", statics.toString());

        aero.addCoreq(statics);
        aero.addPrereq(comp);
        StringBuilder test = new StringBuilder(
                "[AOE\t2104\tIntro to Aerospace Engineering (2)]");
        test.append("\n\tPrereqs: [AOE\t2074\tComputational Methods (2)]");
        test.append("\n\tCoreqs: [ESM\t2104\tStatics (3)]");
        assertEquals(test.toString(), aero.toString());
    }

    public void testEquals()
    {
        assertFalse(aero.equals(comp));
        assertTrue(statics.equals(statics));
        assertTrue(statics.equals(staticsClone));
        assertFalse(aero.equals(notACourse));
        assertFalse(comp.equals(nullCourse));
        assertFalse(statics.equals(brokenStatics));
        assertFalse(statics.equals(dynamics));
        assertFalse(statics.equals(moreStatics));
        assertFalse(statics.equals(againStatics));
    }

    public void testCompareTo()
    {
        courses.sort(new CourseComparator());
        assertEquals(aero, courses.get(1));
        assertEquals(morality, courses.get(5));
        assertEquals(statics, courses.get(2));
    }

    public void testPrereqs()
    {
        aero.addPrereq(statics);
        aero.addPrereq(comp);
        aero.addPrereq(reality);

        ArrayList<Course> p = aero.getPrereqs();
        assertTrue(comp.equals(p.get(0)));
        assertTrue(statics.equals(p.get(1)));
        assertTrue(reality.equals(p.get(2)));

        assertTrue(aero.removePrereq(statics));
        assertTrue(aero.removePrereq(reality));
        assertFalse(aero.removePrereq(aero));
        assertTrue(comp.equals(aero.getPrereqs().get(0)));
    }
    
    public void testCoreqs()
    {
        aero.addCoreq(statics);
        aero.addCoreq(comp);
        aero.addCoreq(reality);
        
        ArrayList<Course> c = aero.getCoreqs();
        assertTrue(comp.equals(c.get(0)));
        assertTrue(statics.equals(c.get(1)));
        assertTrue(reality.equals(c.get(2)));

        assertTrue(aero.removeCoreq(statics));
        assertTrue(aero.removeCoreq(reality));
        assertFalse(aero.removeCoreq(aero));
        assertTrue(comp.equals(aero.getCoreqs().get(0)));
    }
    
    public void testDependencies()
    {
        assertEquals(0, aero.dependencies());
        
        aero.addPrereq(statics);
        aero.addPrereq(comp);
        
        assertEquals(2, aero.dependencies());
        
        aero.addCoreq(statics);
        aero.addCoreq(comp);
        aero.addCoreq(reality);
        
        assertEquals(5, aero.dependencies());
    }
}