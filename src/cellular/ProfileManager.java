package cellular;

import java.util.Map;
import java.util.HashMap;

/**
 * Controls the creation and management of different automata profiles.
 * This class serves to unify all of the automatas known to this instance
 * of the cellular package. Upon request from somewhere else, this class
 * can be asked for any of the properties of any pre-loaded automata.
 * 
 * In the future, this class would ideally be able to load from the Engine
 * classes in a specified folder, and resources associated with them, to create
 * profiles.
 * 
 * @author William McDermott
 * @version 2016.11.24
 */
public class ProfileManager
{
    /**
     * The map that contains the profiles.
     */
    private static Map<String, Profile> profiles;


    /**
     * Creates a new ProfileManager for this interface.
     */
    public ProfileManager()
    {
        profiles = new HashMap<String, Profile>();
        this.initializeProfiles();
    }


    /**
     * Gets a profile given its name.
     * 
     * @return The name of the profile.
     */
    public static Profile getProfile(String name)
    {
        if (profiles.containsKey(name))
        {
            return profiles.get(name);
        }
        return null;
    }


    /**
     * Initializes the list of standard automata and their profiles.
     */
    private void initializeProfiles()
    {
        profiles.put("ant", new Profile(9, 8, new AntEngine()));
        profiles.put("life", new Profile(1, 4, new LifeEngine()));
        profiles.put("test", new Profile(11, 8, new TestEngine()));
        profiles.put("wire", new Profile(3, 4, new WireEngine()));
        this.intializeUserProfiles();
    }


    /**
     * Searches for other automata in the resources folder, and new Engine class
     * files, to allow users to add automata. This isn't so bad with the updated
     * FilingCabinet class, but it could still be hairy.
     */
    private void intializeUserProfiles()
    {
        // TODO
    }
}
