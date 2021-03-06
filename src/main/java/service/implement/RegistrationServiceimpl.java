package service.implement;

import enums.Role;
import main.MainApp;
import model.User;
import service.RegistrationService;


import java.util.Scanner;
import java.util.Set;

public class RegistrationServiceimpl implements RegistrationService {
public static Scanner scanner;
    @Override
    public boolean signIn() {
        scanner = new Scanner(System.in);
        System.out.println("---------------------");
        System.out.print("Email: ");
        String email = scanner.next();

        System.out.print("Password: ");
        String password = scanner.next();
        System.out.println("----------------------");

        for (User user : MainApp.users) {
            if(user.getEmail().equals(email) && user.getPassword().equals(password)){

                MainApp.currentUser= user;
                MainApp.currentUser.setSignedIn(true);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean signUp() {
        User user = new User();
        scanner = new Scanner(System.in);
        System.out.println("------------------------------");
        System.out.print("Firstname: ");
        String firstname = scanner.next();

        System.out.print("Lastname: ");
        String lastname = scanner.next();

        String email;

        while (true) {
            System.out.println("Email Address (alex@gmail.com): ");
            email = scanner.next();
            boolean validateEmail = user.validateEmail(email);
            if (validateEmail) {
                user.setEmail(email);
                break;
            } else {
                System.out.println("Email Invalid");
            }
        }

        System.out.print("Password: ");
        String password = scanner.next();

        while (true){

            System.out.print("ReTypePassword:");
            String reTypePassword = scanner.next();

            if(password.equals(reTypePassword)){
                break;
            }
        }
        System.out.println("-------------------------------\n");

        System.out.println("Choose the role: ");
        int indexOfRole = 1;
        for (Role value : Role.values()) {
            System.out.println(indexOfRole + ". " + value);
            indexOfRole++;
        }

        int roleIndex = scanner.nextInt();
        Role role = Role.values()[roleIndex - 1];

        Set<User> users = MainApp.users;
        int lastId = users.size() + 1;
        User newUser = new User(
                (long) lastId,
                firstname,
                lastname,
                email,
                password,
                true,
                role,
                false
        );
        MainApp.users.add(newUser);
        return true;








    }

    @Override
    public boolean exit() {
        return true;
    }
}
