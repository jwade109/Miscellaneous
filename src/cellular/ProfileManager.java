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
}
