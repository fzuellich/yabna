package app.yabna.utils;

import java.security.NoSuchAlgorithmException;

/**
 * Helper class used when operating with the filesystem. Provides functions like
 * hashing a string to use it as an filename.
 */
public class FileSystemHelper {

    /**
     * Creates a new md5 hash to be used as filename. todo nice error handling
     *
     * @param input input for the hashing algorithm.
     * @return filename
     */
    public static String createFileNameFromURI(String input) throws NoSuchAlgorithmException {
        String result = input.replace(':', '0');
        result = result.replace('.', '_');
        result = result.replace('/', '0');
        return result;
    }
}
