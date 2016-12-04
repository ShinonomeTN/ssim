package net.catten.ssim;

import net.catten.lnc.kingo.jw.caterpillar.KingoJWCaterpillar;
import net.catten.ssim.events.ProgressPrinter;
import net.catten.ssim.helper.TableChecker;
import net.catten.ssim.helper.TableMaker;
import net.catten.ssim.services.DataImporter;
import net.catten.ssim.helper.DatabaseConnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
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

        Properties tableProperty = new Properties();
        tableProperty.load(Main.class.getResourceAsStream("/tables.properties"));
        logger.info("Table properties loaded.");

        //Handling Database connection
        DatabaseConnector databaseConnector = new DatabaseConnector(dbProperties);
        Connection connection = databaseConnector.getConnection();
        logger.info("Database connected.");

        //Start capturing pages
        Properties loginProperties = new Properties();
        loginProperties.load(new FileInputStream(new File("login.properties")));
        KingoJWCaterpillar caterpillar = new KingoJWCaterpillar(loginProperties);
        caterpillar.setTickReceiver(progressPrinter);

        DataImporter dataImporter;

        String schoolTerm = args[0];
        String tablePrefix = schoolTerm + "_";

        //Check tables and create not existed table
        TableMaker tableMaker = new TableMaker(connection, tableProperty, tablePrefix);
        TableChecker tableChecker = new TableChecker(connection, new ArrayList<>(tableProperty.stringPropertyNames()), tablePrefix);
        List<String> notExistTables = tableChecker.checkNotExist();
        logger.info("Check tables successful");
        tableMaker.make(notExistTables);

        if (args.length == 1) {
            Map<Integer, String> resultMap = caterpillar.getTermSubjectToMemory(Integer.parseInt(schoolTerm));

            dataImporter = new DataImporter(connection, resultMap, loginProperties.getProperty("UTF-8"), tablePrefix);
        } else {
            File targetDir = new File(args[1]);

            if (args.length <= 2 || !args[2].equals("--continue"))
                caterpillar.getTermSubjectToFiles(Integer.parseInt(schoolTerm), targetDir);

            dataImporter = new DataImporter(connection, targetDir, loginProperties.getProperty("charset"), ".html", tablePrefix);
        }

        //Import to database
        dataImporter.setTickReceiver(progressPrinter);
        dataImporter.startJobs();

        connection.close();
    }
}
