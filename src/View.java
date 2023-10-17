import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class View {
    private String fullName;
    private String username;
    private String password;
    Database database = new Database();
    Connection connection = database.getConnection();
    Scanner input = new Scanner(System.in);
    Model model = new Model();

    private Map<String, Integer> userSessions = new HashMap<>();

    public void validateUser() {
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("Enter username-->:");
            username = input.nextLine();
            model.setUsername(username);

            System.out.println("Enter password-->:");
            password = input.nextLine();
            model.setPassword(password);

            try {
                String checkUser = "SELECT * FROM user WHERE username=? AND password=?";
                PreparedStatement preparedStatement = connection.prepareStatement(checkUser);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int userId = resultSet.getInt("userId");
                    userSessions.put(username, userId);
                    loggedIn = true;
                    System.out.println("Logged In successfully. Welcome " + username);
                } else {
                    System.out.println("Invalid details. Try again.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Invalid details. Try again.");
            }
        }
    }
    // Method to get the user's session information (userId) from the user's username
    public int getUserIdFromSession(String username) {
        return userSessions.getOrDefault(username, -1);
    }

    // Method to end the user's session
    public void endUserSession(String username) {
        userSessions.remove(username);
    }

    public void registerUser(){
        boolean registered=false;
        while (!registered){
            System.out.println("Enter fullName-->:");
            fullName = input.nextLine();
            model.setFullName(fullName);

            System.out.println("Enter username-->:");
            username = input.nextLine();
            model.setUsername(username);

            System.out.println("Enter password-->:");
            password = input.nextLine();
            model.setPassword(password);
            try{
                String checkExist="SELECT *FROM user WHERE username=?";
                PreparedStatement preparedStatement=connection.prepareStatement(checkExist);
                preparedStatement.setString(1,username);
                ResultSet resultSet=preparedStatement.executeQuery();
                if (resultSet.next()){
                    registered=false;
                    System.out.println("Username already exists. Registration failed.");
                }else {
                    String insertquery="INSERT INTO user(fullName,username,password)VALUES(?,?,?)";
                    preparedStatement=connection.prepareStatement(insertquery);
                    preparedStatement.setString(1,fullName);
                    preparedStatement.setString(2,username);
                    preparedStatement.setString(3,password);

                    int rowAffected=preparedStatement.executeUpdate();

                    if (rowAffected>0){
                        registered=true;
                        System.out.println("Registered Successfully");
                    }else {
                        System.out.println("Try again please");
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
                System.out.println("Error occurred.Try again");
            }
     }
    }

    public static void main(String[] args) {
        int userId = -1;
        View view = new View();
        Controller controller=new Controller();
        Scanner input = new Scanner(System.in);
        int select;
        System.out.println("WELCOME TO M-BANKING");
        System.out.println("SELECT MENU BELOW");
        System.out.println("1. Login\n2. Register\n3. Exit");
        System.out.println("Enter your Choice here-->:");
        select = input.nextInt();
        if (select == 1) {
            view.validateUser();
            userId = view.getUserIdFromSession(view.username);
            System.out.println("SELECT THE NEXT ACTION BELOW");
            System.out.println("1.Deposit\n2.Withdraw\n3.Check Balance");
            System.out.println("Enter your choice here---->:");
            select=input.nextInt();
            if(select==1){
                controller.deposit(view.input,userId);
            } else if (select==2) {
                controller.withdraw();
            }else {
                controller.checkBalance();
            }

        } else if(select==2){
            view.registerUser();
            System.out.println("SELECT THE NEXT ACTION BELOW");
            System.out.println("1.Login\n2.Exit");
            System.out.println("Enter your choice here---->:");
            select=input.nextInt();
            if(select==1){
                System.out.println("LOGIN BELOW");
                view.validateUser();
            }else {
                System.out.println("YOU ARE EXITING THE SYSTEM");
                System.exit(0);
            }
        }
        else {
            System.exit(0);
        }
    }
}
