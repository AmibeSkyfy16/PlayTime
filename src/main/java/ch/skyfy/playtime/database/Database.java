package ch.skyfy.playtime.database;

import com.mongodb.DB;
import com.mongodb.MongoClient;

@SuppressWarnings("UnusedReturnValue")
public class Database {

    private static class DatabaseHolder {
        public static final Database INSTANCE = new Database();
    }

    public static Database getInstance() {
        return Database.DatabaseHolder.INSTANCE;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void initializeInstance() {
        getInstance();
    }

    public Database() {



//        MongoClient mongoClient = new MongoClient("localhost", 27017);
//        DB database = mongoClient.getDB("myMongoDb");
//
//        mongoClient.getDatabaseNames().forEach(System.out::println);
//
//        database.createCollection("customers", null);
//        database.getCollectionNames().forEach(System.out::println);
    }

}
