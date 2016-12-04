package net.catten.ssim.helper;

import java.io.File;

/**
 * Created by catten on 11/14/16.
 */
public class Validator {
    public static void isDirectory(File file){
        if (!file.isDirectory()) throw new IllegalArgumentException("Source of data must be a directory!");
    }
}