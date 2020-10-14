package ImageHoster.controller;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.service.CommentService;
import ImageHoster.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommentService commentService;

    @RequestMapping("/images/{imageId}/{imageTitle}/comments")
    public String uploadComment(@PathVariable("imageId") Integer imageId,
                                @PathVariable("imageTitle") String imageTitle,
                                @RequestParam(value = "comment") String comment,
                                Model model, HttpSession session) {

        Comment newComment = new Comment();

        // Set all the fields for the comment object so it can saved in database and retrieved later
        User user = (User) session.getAttribute("loggeduser");
        Image image = imageService.getImage(imageId);;
        newComment.setUser(user);
        newComment.setImage(image);
        newComment.setDate(new Date());
        newComment.setText(comment);

        commentService.uploadComment(newComment);

        // redirect back to the same page to display the Image
        return "redirect:/images/" + imageId + "/" + imageTitle;
    }

}
