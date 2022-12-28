package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        System.out.println();
        userService.createUsersTable();
        System.out.println();
        userService.saveUser("Victor", "Gordon", (byte) 22);
        userService.saveUser("Oscar", "Allen", (byte) 37);
        userService.saveUser("Lawrence", "Carter", (byte) 14);
        userService.saveUser("Stephen", "Frank", (byte) 69);
        System.out.println();
        userService.getAllUsers().forEach(System.out::println);
        System.out.println();
        userService.cleanUsersTable();
        System.out.println();
        userService.dropUsersTable();
    }
}
