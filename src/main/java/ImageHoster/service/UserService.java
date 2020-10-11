package ImageHoster.service;

import ImageHoster.model.User;
import ImageHoster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //Call the registerUser() method in the UserRepository class to persist the user record in the database
    public void registerUser(User newUser) {
        userRepository.registerUser(newUser);
    }

    //Since we did not have any user in the database, therefore the user with username 'upgrad' and password 'password' was hard-coded
    //This method returned true if the username was 'upgrad' and password is 'password'
    //But now let us change the implementation of this method
    //This method receives the User type object
    //Calls the checkUser() method in the Repository passing the username and password which checks the username and password in the database
    //The Repository returns User type object if user with entered username and password exists in the database
    //Else returns null
    public User login(User user) {
        User existingUser = userRepository.checkUser(user.getUsername(), user.getPassword());
        if (existingUser != null) {
            return existingUser;
        } else {
            return null;
        }
    }

    public boolean checkPassword(User user) {
        //Must contain at least 1 alphabet (a-z or A-Z), 1 number (0-9) and
        // 1 special character (any character other than a-z, A-Z and 0-9)

        String password = user.getPassword();
        int digitC = 0, charC = 0, miscC = 0;
        char ch;

        for (int i = 0; i < password.length(); i++) {
            ch = password.charAt(i);
            if (Character.isLetter(ch)) {
                charC++;
            } else if (Character.isDigit(ch)) {
                digitC++;
            } else {
                miscC++;
            }
        }
        if ((charC >= 1) && (digitC >= 1) && (miscC >= 1)) {
            //Criteria met, return true
            return true;
        } else {
            return false;
        }
    }

}
