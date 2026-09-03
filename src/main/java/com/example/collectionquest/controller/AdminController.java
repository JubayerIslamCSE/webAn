import com.example.collectionquest.model.Anime;
import com.example.collectionquest.service.AnimeService;
import com.example.collectionquest.service.UserService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AnimeService animeService;
    private final UserService userService;

    public AdminController(AnimeService animeService, UserService userService) {
        this.animeService = animeService;
        this.userService = userService;
    }

    // ADMIN HOME — list all anime
    @GetMapping
    public String adminHome(Model model) {
        model.addAttribute("animes", animeService.getAll());
        return "admin/admin-list";
    }

    // USERS LIST
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        return "admin/admin-users";
    }

    // SHOW ADD FORM
    @GetMapping("/anime/new")
    public String showAddForm(Model model) {
        model.addAttribute("anime", new Anime());
        return "admin/admin-form";
    }

    // SHOW EDIT FORM
    @GetMapping("/anime/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("anime", animeService.getById(id));
        return "admin/admin-form";
    }

    // SAVE — handles image upload
    @PostMapping("/anime/save")
    public String saveAnime(@ModelAttribute Anime anime,
                            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        if (!imageFile.isEmpty()) {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", System.getenv("CLOUDINARY_CLOUD_NAME"),
            "api_key",    System.getenv("CLOUDINARY_API_KEY"),
            "api_secret", System.getenv("CLOUDINARY_API_SECRET")
        ));
    
        Map result = cloudinary.uploader().upload(
            imageFile.getBytes(), ObjectUtils.emptyMap()
        );
    
        String imageUrl = (String) result.get("secure_url");
        anime.setImageUrl(imageUrl);
    }

        animeService.save(anime);
        return "redirect:/admin";
    }

    // DELETE
    @GetMapping("/anime/delete/{id}")
    public String deleteAnime(@PathVariable Long id) {
        animeService.delete(id);
        return "redirect:/admin";
    }
}
