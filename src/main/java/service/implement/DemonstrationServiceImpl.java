package service.implement;

import enums.Role;
import main.MainApp;
import model.Groups;
import model.Messages;
import model.User;
import service.DemonstrationService;
import service.GroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DemonstrationServiceImpl implements DemonstrationService {
public static Scanner scanner;
public static GroupService groupService = new GroupsServiceImpl();
static List<User> userList = new ArrayList<>();


    @Override
    public void showUserMenu() {
        scanner = new Scanner(System.in);
        /**
         * Boshqa foydalanuvchilarga message jo’natish
         * O’zi egasi bo’lgan guruhga yoki o’zi a’zo bo’lgan guruhga message jo’natish .
         * Guruh yaratish va mavjud guruhlarga a’zo bolish uchun shu guruhnign egasidan rozilik so’rovini jo’natish
         * O’zguruhiga qoshilmoqchi bolganlar so’rovini tasdiqlash yoki rad etish
         * O’z parolini o’zgartirish.
         */


        System.out.println("====== User Menu ======");
        System.out.println("1.Send message");
        System.out.println("2.Send message to group");
        System.out.println("3.Group services");
        System.out.println("4.Change password");
        System.out.println("0.Sign Out");

        System.out.print("Menu: ");
        int choice = scanner.nextInt();
        switch (choice){

            case 0:
                MainApp.currentUser.setSignedIn(false);
                break;

            case 1:
                scanner = new Scanner(System.in);

                System.out.println("1. Send");
                System.out.println("2. Inbox");
                System.out.println("3. Outbox");
                System.out.println("0. Sign Out");

                System.out.print("Select the operation => ");
                int choice1 = scanner.nextInt();
                switch (choice1) {
                    case 1:
                        send();
                        break;
                    case 2:
                        inbox();
                        break;

                    case 3:
                        outbox();
                        break;
                    case 0:
                        return;
                }
                break;

            case 2:

                break;

            case 3:
                chanelMessageMenu();
                break;

            case 4:
                while (true) {
                    try {
                        System.out.print("Enter new password: ");
                        String password = scanner.next();
                        System.out.print("Confirm new password: ");
                        String confirmPassword = scanner.next();
                        if (password.equals(confirmPassword)) {
                            MainApp.currentUser.setPassword(password);
                            System.out.println("Password has been successfully changed.");
                            break;
                        } else {
                            System.out.println("Passwords didn't match. Please, try again!");
                        }
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
                break;



        }





    }

    private void chanelMessageMenu() {
        boolean returnBack = false;
        while (true){
            System.out.println("---------------------------");
            System.out.println("1.Add Group ");
            System.out.println("2.Edit Group");
            System.out.println("3.Delete Group");
            System.out.println("4.Add users");
            System.out.println("5.Group list");
            System.out.println("0.Exit");
            System.out.println("---------------------------");
            System.out.print("Select the operation: => ");
            int choice = scanner.nextInt();
            switch (choice){
                case 0:
                    returnBack = true;
                    break;

                case 1:
                    scanner = new Scanner(System.in);
                    Long groupId = MainApp.groups.size() + 1L;
                    System.out.println("Enter the new name group:");
                    String newGroup = scanner.next();
                    Groups groups = new Groups(
                            groupId,
                            newGroup,
                            Role.ADMIN,
                            MainApp.currentUser,
                            Role.USER,
                            userList
                    );
                    groupService.addGroup(groups);
                    System.out.println("The group has been created");
                    break;

                case 2:
                    String editGroups = scanner.next();
                    Groups groupToUpdate = groupService.findByName(editGroups);
                    System.out.println(groupToUpdate);

                        System.out.print("Enter new groupname: ");
                        String newGroupName = scanner.next();
                        groupToUpdate.setName(newGroupName);
                        groupService.editGroups(groupToUpdate);
                        System.out.println("Successfully updated.");
                        break;
                case 3:
                    for (Groups group : MainApp.groups) {
                        System.out.println(group.getId() + ". " + group.getName());
                    }
                    System.out.print("Enter group id: ");
                    int groupIdForDeletion = scanner.nextInt();
                    boolean deleteStatus = groupService.deleteGroups((long)groupIdForDeletion);
                    if(deleteStatus){
                        System.out.println("Group deleted successfully.");
                    }
                    else {
                        System.out.println("There was error in groud deletion.");
                    }
                    break;
                case 4:
                    for (Groups group : MainApp.groups) {
                        System.out.println(group.getId() + ". " + group.getName() + " || Owner: " + group.getOwner());
                        long choicex = scanner.nextLong();
                        if(group.getId().equals(choicex)){
                            System.out.println("Add user: (+)\t\t\t\tCancel (-)");
                            String charachter = scanner.next();
                            switch (charachter){
                                case "+":
                                    System.out.print("Enter the user email:");
                                    String email = scanner.next();
                                    userList.add(findByEmail(email));
                                    System.out.println(email + " has been added to the group");
                                    break;
                                case "-":
                                    break;
                            }
                        }else{
                            System.out.println("There is no such group!");
                        }
                        break;
                    }

                    break;

                case 5:
                    for (Groups group : MainApp.groups) {
                        System.out.println(group.getId() + ". " + group.getName());
                    }
                    System.out.println("======== Users Menu =======");
                    for (Groups group : MainApp.groups) {
                        List<User> users = group.getUsers();
                        for (User user : users) {
                            System.out.println(user);
                        }
                    }
                    System.out.println("===========================");
                    break;

            }if(returnBack){
                break;
            }
        }
        showUserMenu();

    }

    private static void outbox() {
        for (Messages message : MainApp.messages) {
            if(message != null){
                if(message.getSender().equals(MainApp.currentUser)){
                    System.out.println("------------------------------");
                    System.out.println("Receiver: " + message.getReceiver().getEmail());
                    System.out.println("Title: " + message.getTitle());
                    System.out.println("Message: " + message.getBody());
                }

            }
        }
    }

    private static void inbox() {

        for (Messages message : MainApp.messages) {
            if(message != null){
                if(message.getReceiver().equals(MainApp.currentUser)){
                    System.out.println("---------------------------------------");
                    System.out.println("Sender: " + message.getSender().getEmail());
                    System.out.println("Title: " + message.getTitle());
                    System.out.println("Message: " + message.getBody());
                }
            }
        }

    }

    private static void send() {

        System.out.println("---------->Email Addresses<----------");
        for (User user : MainApp.users) {
            if (user != null) {
                if (user.equals(MainApp.currentUser)) {
                    System.out.println(user.getEmail());
                }
            }
        }


        System.out.println("-------------------------------------");
        System.out.print("To: ");
        String receiverEmailAddresses = scanner.next();
        User receiver = null;

        for (User user : MainApp.users) {
            if(user != null){
                if(user.getEmail().equals(receiverEmailAddresses)){
                    receiver = user;
                }
            }
        }

        System.out.print("Subject: ");
        scanner = new Scanner(System.in);
        String subject = scanner.nextLine();
        System.out.print("Message: ");
        scanner = new Scanner(System.in);
        String message = scanner.nextLine();

        Messages messages = new Messages(
                subject,
                message,
                MainApp.currentUser,
                receiver,
                true
        );
        MainApp.messages.add(messages);
        System.out.println(receiverEmailAddresses + " successfully sent messages!\n");
    }
    @Override
    public void showAdminMenu() {


        while (true) {
            scanner = new Scanner(System.in);
            System.out.println("--------------------");
            adminMenu();
            System.out.println("--------------------");
            System.out.print("Select the option: => ");
            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    System.out.println(String.format("%1$-5s", "Id") +
                            String.format("%1$-15s", "First name") +
                            String.format("%1$-15s", "Last name") +
                            String.format("%1$-25s", "Email") +
                            String.format("%1$-15s", "Password") +
                            String.format("%1$-15s", "Role") +
                            String.format("%1$-15s", "Status")
                    );
                    for (User user : MainApp.users) {
                        if(user.getRole().equals(Role.USER)) {
                            System.out.println(user);
                        }
                    }
                    break;

                case 2:
                    while (true) {
                        try {
                            scanner = new Scanner(System.in);
                            System.out.print("Enter email address:");
                            String email = scanner.next();
                            User user = findByEmail(email);
                            if (user != null) {
                                user.deactivate();
                                System.out.println(user.getFirstname() + " has been blocked!");
                            } else {
                                System.out.println("User with " + email + " not found!");
                            }
                            break;
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
                case 3:
                    while (true) {
                        try {
                            scanner = new Scanner(System.in);
                            System.out.print("Enter email address:");
                            String email = scanner.next();
                            User user = findByEmail(email);
                            if (user != null) {
                                user.activate();
                                System.out.println(user.getFirstname() + " has been activated!");
                            } else {
                                System.out.println("User with " + email + " not found!");
                            }
                            break;
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
                case 4:
                    while (true) {
                        try {
                            scanner = new Scanner(System.in);
                            System.out.print("Enter email address:");
                            String email = scanner.next();
                            User user = findByEmail(email);
                            if (user != null) {
                                MainApp.users.remove(user);
                                System.out.println(user.getFirstname() + " has been deleted!");
                            } else {
                                System.out.println("User with " + email + " not found!");
                            }
                            break;
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;

                case 5:
                    while (true) {
                        try {
                            System.out.print("Enter new password: ");
                            String password = scanner.next();
                            System.out.print("Confirm new password: ");
                            String confirmPassword = scanner.next();
                            if (password.equals(confirmPassword)) {
                                MainApp.currentUser.setPassword(password);
                                System.out.println("Password has been successfully changed.");
                                break;
                            } else {
                                System.out.println("Passwords didn't match. Please, try again!");
                            }
                        }catch (Exception exception){
                            exception.printStackTrace();
                        }
                    }
            }

        }


    }

    private User findByEmail(String email) {
        for (User user : MainApp.users) {
            if(user.getEmail().equals(email)){
                return user;
            }
        }
        return null;
    }

    private void adminMenu() {

        System.out.println("1. User list ");
        System.out.println("2. Block the user ");
        System.out.println("3. Activate blocked users ");
        System.out.println("4. User delete ");
        System.out.println("5. Change password ");

    }
}
