import java.sql.Connection;
import java.util.Scanner;

public class App {
    public App() {
    }

    public static void main(String[] args) throws Exception {
        SQLreg sqlReg = new SQLreg();
        Connection conn = sqlReg.getConnection();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Login or Register? (1/2)");
        int val = scanner.nextInt();

        if (val == 1) {
            UserLogin userLogin = new UserLogin(conn);
            String username = "Aryan";
            String password = "test";
            boolean userExists = userLogin.checkUser(username, password);
            if (userExists) {
                System.out.println("User exists and credentials are correct.");
            } else {
                System.out.println("User does not exist or credentials are incorrect.");
            }
        } else if (val == 2) {
            UserRegistration userRegistration = new UserRegistration(conn);


            String username = "Aryan";
            String password = "test";
            userRegistration.addUser(username, password);
        }

    }
}
