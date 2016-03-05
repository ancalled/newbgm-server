package kz.bgm.platform.controller;

import kz.bgm.platform.model.MusicRec;
import kz.bgm.platform.service.UploadAudioService;
import kz.bgm.platform.service.impl.MainServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private MainServiceImpl service;

    @Autowired
    private UploadAudioService uploadAudioService;

    private static final Logger log = Logger.getLogger(MainController.class);

    @RequestMapping(method = GET)
    public String showRecs(ModelMap model) {
        List<MusicRec> musicRecs = service.listMusicReq();
        model.addAttribute("musicRecs", musicRecs);
        return "index";
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {
        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("login");
        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "upload")
    public ModelAndView main() {
        ModelAndView model = new ModelAndView();
        model.setViewName("upload");
        return model;
    }

    @RequestMapping(value = "upload-audio", method = RequestMethod.POST)
    public String uploadFileHandler(ModelMap model, @RequestParam("audioTitle") String audioTitle,
                                    @RequestParam("artist") String artist,
                                    @RequestParam("album") String album,
                                    @RequestParam("releaseDate") String releaseDate,
                                    @RequestParam("file") MultipartFile multipartFile) {

        if (!multipartFile.isEmpty()) {
            try {
                File file = convert(multipartFile);
                String result = uploadAudioService.uploadAudio(audioTitle, artist, album, releaseDate, file);
                model.addAttribute("msg", "Uploaded result: " + result);
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        } else {
            model.addAttribute("error", "File is empty.");
        }
        return "upload";
    }

    public File convert(MultipartFile multipart) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir"), multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }
}
