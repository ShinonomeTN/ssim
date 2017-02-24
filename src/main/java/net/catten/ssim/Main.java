package net.catten.ssim;

import net.catten.ssim.events.ProgressPrinter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by catten on 11/13/16.
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, InterruptedException {
        Logger logger = Logger.getLogger(Main.class.getClass().toString());
        ProgressPrinter progressPrinter = new ProgressPrinter(40);

        //Property preparing
        Properties dbProperties = new Properties();
        dbProperties.load(new FileInputStream(new File("connection.properties")));
        logger.info("Database connection properties loaded.");

    }
}
