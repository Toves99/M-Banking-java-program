import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public  class Controller {
    private double amount;
    private double total;
    Scanner input=new Scanner(System.in);
    Model model=new Model();
    Database database = new Database();
    Connection connection = database.getConnection();

    public void deposit(Scanner input, int userId){
        System.out.println("MAKE YOUR DEPOSIT BELOW");
        System.out.println("Enter Amount to Deposit--->:");
        amount = input.nextDouble();
        model.setAmount(amount);
        try{
            String depositQuery = "INSERT INTO deposit(amount, userId) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(depositQuery);
            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, userId);
            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected > 0) {
                // Calculate the total amount for the user
                String totalQuery = "SELECT SUM(amount) FROM deposit WHERE userId = ?";
                PreparedStatement totalStatement = connection.prepareStatement(totalQuery);
                totalStatement.setInt(1, userId);
                ResultSet totalResult = totalStatement.executeQuery();

                if (totalResult.next()) {
                    double totalAmount = totalResult.getDouble(1);
                    System.out.println("YOU DEPOSITED KSH:" + amount + " SUCCESSFULLY");
                    System.out.println("YOUR TOTAL BALANCE IS: " + totalAmount + " KSH");
                } else {
                    System.out.println("Unable to retrieve total balance.");
                }
            } else {
                System.out.println("TRANSACTION WAS NOT SUCCESSFUL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred");
        }
    }

    public void withdraw(){

    }
    public void checkBalance(){

    }
}