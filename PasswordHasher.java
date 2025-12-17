import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    
    /**
     * Hashes the password using BCrypt.
     * @param plainText the raw password from the user
     * @return a 60-character hashed string
     */
    public static String hash(String plainText) {
        // 12 is the work factor. Higher is more secure but slower.
        return BCrypt.hashpw(plainText, BCrypt.gensalt(12));
    }

    /**
     * Checks if the raw password matches the stored hash.
     * @param plainText the raw password entered during login
     * @param hashed the hash retrieved from the database
     * @return true if they match
     */
    public static boolean check(String plainText, String hashed) {
        try {
            return BCrypt.checkpw(plainText, hashed);
        } catch (Exception e) {
            return false;
        }
    }
}