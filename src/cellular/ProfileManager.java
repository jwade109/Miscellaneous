package cellular;

import java.util.Map;
import java.util.HashMap;

/**
 * Controls the creation and management of different profiles.
 * TODO
 * 
 * @author William McDermott
 * @version 2016.11.23
 */
public class ProfileManager
{
    /**
     * The map that contains the profiles.
     */
    private Map<String, Profile> profiles;
    
    /**
     * Creates a new ProfileManager for this interface.
     */
    public ProfileManager()
    {
        profiles = new HashMap<String, Profile>();
    }
    
    /**
     * Gets a profile given its name.
     * @return  The name of the profile.
     */
    public Profile getProfile(String name)
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
    public void initializeProfiles()
    {
        profiles.put("ant", new Profile(9, 8));
        profiles.put("life", new Profile(1, 4));
        profiles.put("test", new Profile(11, 8));
        profiles.put("wire", new Profile(3, 4));
    }
}
