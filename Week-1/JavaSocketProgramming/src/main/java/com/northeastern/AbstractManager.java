package main.java.com.northeastern;

import main.java.com.northeastern.Utils.Utils;

/**
 * Abstract class to initialize the utility
 * class. Can be used to store any other
 * abstracted information.
 */
public abstract class AbstractManager {
    //Instance of Utils.
    protected static Utils utils;

    protected AbstractManager() {
        utils = Utils.getInstance();
    }
}
