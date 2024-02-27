package com.ados;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static final String FILE_URL = "D:\\animal.txt";
    private static final String DB_URL = "jdbc:mysql://35.186.150.248:3308/abcxyz";
    private static final String USERNAME = "abcxyz";
    private static final String PASSWORD = "abcxyz";

    public static void main(String[] args) {

        Random rn = new Random();
        List<Animal> list = new ArrayList<>();

        for (int i = 1; i <= 1000; i++) {
            int rd = rn.nextInt(1000) + 1;
            if (rd % 2 == 0) {
                Cat cat = new Cat();
                cat.setId(String.valueOf(i));
                cat.setName("CAT");
                cat.setType("Cat " + i);
                cat.setNumOfFoots(4);

                list.add(cat);
            } else {
                Duck duck = new Duck();
                duck.setId(String.valueOf(i));
                duck.setName("DUCK");
                duck.setType("Duck " + i);
                duck.setNumOfFoots(2);

                list.add(duck);
            }
        }

        Thread thread1 = new Thread(() -> saveToDatabase(list));
        Thread thread2 = new Thread(() -> saveToFile(list));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Done");

    }

    private static void saveToDatabase(List<Animal> list) {
        System.out.println("Start saveToDatabase");
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO tbl_animals (id, name, type, num_of_foots) VALUES (?,?,?,?);");

            for (Animal a : list) {
                System.out.println(a.id);
                statement.setString(1, a.getId());
                statement.setString(2, a.getName());
                statement.setString(3, a.getType());
                statement.setInt(4, a.getNumOfFoots());
                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("saveToDatabase ERR: ");
            e.printStackTrace();
        }
    }

    private static void saveToFile(List<Animal> list) {
        System.out.println("Start saveToFile");
        try {
            FileWriter fw = new FileWriter(FILE_URL);
            for (Animal a : list) {
                fw.write(a.toString());
            }
            fw.close();
        } catch (Exception e) {
            System.out.println("saveToFile ERR: ");
            System.out.println(e);
        }
    }

    private static List<Animal> getFromDatabase() {
        List<Animal> list = new ArrayList<>();
        System.out.println("Start getFromDatabase");
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            connection.setAutoCommit(false);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from tbl_animals;");
            while (rs.next()) {
                Animal animal = new Animal() {
                    @Override
                    String speak() {
                        return null;
                    }
                };
                animal.setId(rs.getString(1));
                animal.setName(rs.getString(2));
                animal.setType(rs.getString(3));
                animal.setNumOfFoots(rs.getInt(4));

                list.add(animal);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            System.out.println("getFromDatabase ERR: ");
            e.printStackTrace();
            return list;
        }
    }

    private static List<Animal> getFromFile() {
        List<Animal> list = new ArrayList<>();
        System.out.println("Start getFromFile");
        FileReader fr = null;
        try {
            fr = new FileReader(FILE_URL);
            int i;
            while ((i = fr.read()) != -1) {
                System.out.print((char) i);
            }
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
