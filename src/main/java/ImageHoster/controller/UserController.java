package ImageHoster.controller;

import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.model.UserProfile;
import ImageHoster.service.ImageService;
import ImageHoster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    //This controller method is called when the request pattern is of type 'users/registration'
    //Adds User type object to a model and returns 'users/registration.html' file
    @RequestMapping("users/registration")
    public String registration(Model model) {
        User user = createUser();
        model.addAttribute("User", user);

        return "users/registration";
    }

    //This controller method is called when the request pattern is of type 'users/registration' and also the incoming request is of POST type
    //This method calls the business logic and after the user record is persisted in the database, directs to login page
    @RequestMapping(value = "users/registration", method = RequestMethod.POST)
    public String registerUser(User user, Model model) {
        String passwordTypeError = "Password must contain atleast 1 alphabet, 1 number & 1 special character";

        //Check if the password matches the required Criteria
        if (checkPassword(user) == false) {
            //Password entered doesnt match the criteria, set the model attribute to display
            //the error that the password is not meeting requirements and render the same page
            model.addAttribute("passwordTypeError", passwordTypeError);

            User newUser = createUser();
            model.addAttribute("User", newUser);

            return "users/registration";
        }
        //Valid password, register the user
        userService.registerUser(user);
        return "users/login";
    }

    //This controller method is called when the request pattern is of type 'users/login'
    @RequestMapping("users/login")
    public String login() {
        return "users/login";
    }

    //This controller method is called when the request pattern is of type 'users/login' and also the incoming request is of POST type
    //The return type of the business logic is changed to User type instead of boolean type. The login() method in the business logic checks whether the user with entered username and password exists in the database and returns the User type object if user with entered username and password exists in the database, else returns null
    //If user with entered username and password exists in the database, add the logged in user in the Http Session and direct to user homepage displaying all the images in the application
    //If user with entered username and password does not exist in the database, redirect to the same login page
    @RequestMapping(value = "users/login", method = RequestMethod.POST)
    public String loginUser(User user, HttpSession session) {
        User existingUser = userService.login(user);
        if (existingUser != null) {
            session.setAttribute("loggeduser", existingUser);
            return "redirect:/images";
        } else {
            return "users/login";
        }
    }

    //This controller method is called when the request pattern is of type 'users/logout' and also the incoming request is of POST type
    //The method receives the Http Session and the Model type object
    //session is invalidated
    //All the images are fetched from the database and added to the model with 'images' as the key
    //'index.html' file is returned showing the landing page of the application and displaying all the images in the application
    @RequestMapping(value = "users/logout", method = RequestMethod.POST)
    public String logout(Model model, HttpSession session) {
        session.invalidate();

        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "index";
    }

    //Function to create a new user to avoid duplicate code
    //This method declares User type and UserProfile type object
    //Sets the user profile with UserProfile type object
    public User createUser () {
        User newUser = new User();
        UserProfile profile = new UserProfile();
        newUser.setProfile(profile);

        return newUser;
    }

    //Function to check password. The same implemented in UserService but we use the local copy
    //This also helps in getting the JUNIT tests pass
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
