package schedule;

public class AE_CS_M_Schedule extends Schedule
{
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
    private Course manufacturing;

    // 7th semester
    private Course boundary;
    private Course scDesign1;
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

    public AE_CS_M_Schedule()
    {
        super();
        
        deforms = new Course(Department.ESM, 2204,
                "Mechanics of Deformable Bodies", 3);
        add(deforms);

        dynamics = new Course(Department.ESM, 2304, "Dynamics", 3);
        add(dynamics);

        materials = new Course(Department.AOE, 3094, "Materials for AOE", 3);
        materials.addCoreq(deforms);
        add(materials);

        aircraft = new Course(Department.AOE, 3104, "Aircraft Performance", 3);
        aircraft.addCoreq(dynamics);
        add(aircraft);

        discrete = new Course(Department.MATH, 2534, "Intro to Discrete Math",
                3);
        add(discrete);

        compOrg1 = new Course(Department.CS, 2505,
                "Intro to Computer Organization I", 3);
        compOrg1.addCoreq(discrete);
        add(compOrg1);

        aeroHydro = new Course(Department.AOE, 3014, "Aero/Hydrodynamics", 3);
        aeroHydro.addPrereq(aircraft);
        add(aeroHydro);

        thinWalls = new Course(Department.AOE, 3024, "Thin-Walled Structures",
                3);
        thinWalls.addPrereq(deforms);
        add(thinWalls);

        vibration = new Course(Department.AOE, 3034,
                "Vehicle Vibration and Control", 3);
        vibration.addPrereq(dynamics);
        add(vibration);

        astromechanics = new Course(Department.AOE, 4134, "Astromechanics", 3);
        astromechanics.addPrereq(dynamics);
        add(astromechanics);

        thermo = new Course(Department.ME, 3134,
                "Fundamentals of Thermodynamics", 3);
        add(thermo);

        compOrg2 = new Course(Department.CS, 2506,
                "Intro to Computer Organization II", 3);
        compOrg2.addPrereq(compOrg1);
        add(compOrg2);

        exMethods = new Course(Department.AOE, 3054, "Experimental Methods", 3);
        exMethods.addPrereq(aeroHydro);
        exMethods.addPrereq(thinWalls);
        exMethods.addPrereq(vibration);
        add(exMethods);

        compressible = new Course(Department.AOE, 3114,
                "Compressible Aerodynamics", 3);
        compressible.addPrereq(aeroHydro);
        compressible.addPrereq(thermo);
        add(compressible);

        structures = new Course(Department.AOE, 3124, "Aerospace Structures",
                3);
        structures.addPrereq(thinWalls);
        add(structures);

        spacecraftDyn = new Course(Department.AOE, 4140,
                "Spacecraft Dynamics and Control", 3);
        spacecraftDyn.addPrereq(vibration);
        spacecraftDyn.addPrereq(astromechanics);
        add(spacecraftDyn);

        opMethods = new Course(Department.MATH, 4564, "Operational Methods", 3);
        add(opMethods);

        manufacturing = new Course(Department.ISE, 2214,
                "Manufacturing Processes Lab", 1);
        add(manufacturing);

        boundary = new Course(Department.AOE, 3044, "Boundary Layer Theory", 3);
        boundary.addPrereq(aeroHydro);
        boundary.addPrereq(opMethods);
        boundary.addPrereq(thermo);
        add(boundary);

        scDesign1 = new Course(Department.AOE, 4165, "Spacecraft Design I", 3);
        scDesign1.addPrereq(exMethods);
        scDesign1.addPrereq(compressible);
        scDesign1.addPrereq(structures);
        scDesign1.addPrereq(spacecraftDyn);
        add(scDesign1);

        aeroEngrLab = new Course(Department.AOE, 4154, "Aero Engr Lab", 1);
        aeroEngrLab.addPrereq(exMethods);
        aeroEngrLab.addPrereq(compressible);
        aeroEngrLab.addPrereq(structures);
        aeroEngrLab.addPrereq(spacecraftDyn);
        add(aeroEngrLab);

        propulsion = new Course(Department.AOE, 4234, "Aero Propulsion Systems",
                3);
        propulsion.addPrereq(compressible);
        propulsion.addPrereq(thermo);
        add(propulsion);

        complexVector = new Course(Department.MATH, 4574,
                "Complex and Vector Analysis", 3);
        complexVector.addPrereq(opMethods);
        add(complexVector);

        CADcontrols = new Course(Department.AOE, 4004, "CAD Controls", 3);
        CADcontrols.addPrereq(vibration);
        add(CADcontrols);

        scDesign2 = new Course(Department.AOE, 4166, "Spacecraft Design II", 3);
        scDesign2.addPrereq(scDesign1);
        add(scDesign2);

        nuMethods = new Course(Department.CS, 3414, "Numerical Methods", 3);
        add(nuMethods);

        compSystems = new Course(Department.CS, 3214, "Computer Systems", 3);
        compSystems.addPrereq(compOrg2);
        add(compSystems);

        algorithms = new Course(Department.CS, 3114,
                "Data Structures and Algorithms", 3);
        algorithms.addPrereq(compOrg1);
        add(algorithms);

        missionPlan = new Course(Department.AOE, 4414,
                "CAD Space Mission Planning", 3);
        missionPlan.addPrereq(astromechanics);
        add(missionPlan);
    }
}
