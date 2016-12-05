package schedule;

import java.util.ArrayList;

public class AE_CS_M_Schedule
{
    private Schedule s;

    // 3rd semester
    private Course statics;
    private Course compMethods;
    private Course introAero;
    private Course knowledge;
    private Course morality;
    private Course softwareDesign;

    // 4th semester
    private Course deforms;
    private Course dynamics;
    private Course materials;
    private Course aircraft;
    private Course compOrg1;
    private Course discrete;

    // 5th semester
    private Course aeroHydro;
    private Course thinWalls;
    private Course vibration;
    private Course astromechanics;
    private Course thermo;
    private Course compOrg2;

    // 6th semester
    private Course exMethods;
    private Course compressible;
    private Course structures;
    private Course spacecraftDyn;
    private Course opMethods;
    // private Course manufacturing;

    // 7th semester
    private Course boundary;
    private Course scDesign;
    private Course aeroEngrLab;
    private Course propulsion;
    private Course complexVector;
    private Course CADcontrols;

    // 8th semester
    private Course scDesign2;
    private Course nuMethods;
    private Course compSystems;
    private Course algorithms;
    private Course missionPlan;

    public static void main(String[] args)
    {
        Schedule s = new AE_CS_M_Schedule().getSchedule();
        ArrayList<Course> courseList = s.toCourseList();
        int i = 0;
        for (Course c : courseList)
        {
            System.out.println(i + " " + c.toFullString());
            i++;
        }
        System.out.println(s.size() + " - Size");
    }
    
    public AE_CS_M_Schedule()
    {
        s = new Schedule();

        statics = new Course(Department.ESM, 2104, "Statics", 3);
        s.add(statics);

        compMethods = new Course(Department.AOE, 2074, "Computational Methods",
                2);
        s.add(compMethods);

        introAero = new Course(Department.AOE, 2104,
                "Intro to Aerospace Engineering", 2);
        introAero.addCoreq(compMethods);
        s.add(introAero);

        knowledge = new Course(Department.PHIL, 1204, "Knowledge and Reality",
                3);
        s.add(knowledge);

        morality = new Course(Department.PHIL, 1304, "Morality and Justice", 3);
        s.add(morality);

        softwareDesign = new Course(Department.CS, 2114,
                "Software Design and Data Structures", 3);
        s.add(softwareDesign);

        deforms = new Course(Department.ESM, 2204,
                "Mechanics of Deformable Bodies", 3);
        deforms.addPrereq(statics);
        s.add(deforms);
        
        dynamics = new Course(Department.ESM, 2304, "Dynamics", 3);
        dynamics.addPrereq(statics);
        s.add(dynamics);
        
        materials = new Course(Department.AOE, 3094, "Materials for AOE", 3);
        materials.addCoreq(deforms);
        s.add(materials);
        
        aircraft = new Course(Department.AOE, 3104, "Aircraft Performance", 3);
        aircraft.addPrereq(statics);
        aircraft.addPrereq(introAero);
        aircraft.addCoreq(compMethods);
        aircraft.addCoreq(dynamics);
        s.add(aircraft);
        
        discrete = new Course(Department.MATH, 2534, "Intro to Discrete Math", 3);
        s.add(discrete);
        
        compOrg1 = new Course(Department.CS, 2505, "Intro to Computer Organization", 3);
        compOrg1.addCoreq(discrete);
        compOrg1.addPrereq(softwareDesign);
        s.add(compOrg1);
        
        
    }

    public Schedule getSchedule()
    {
        return s;
    }

}
