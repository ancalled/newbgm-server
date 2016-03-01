package kz.bgm.platform.controller;

import kz.bgm.platform.model.MusicRec;
import kz.bgm.platform.model.MusicRecResp;
import kz.bgm.platform.service.UploadAudioService;
import kz.bgm.platform.service.impl.MainServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private MainServiceImpl mainService;

    @Autowired
    private UploadAudioService uploadAudioService;

    private static final Logger log = Logger.getLogger(MainController.class);

    @RequestMapping(method = RequestMethod.GET)
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

    @RequestMapping(value = "/music-rec", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public @ResponseBody
    MusicRecResp musicRec(@RequestBody MusicRec req) {
        log.info("Got music-rec request:" + req.toString());
        mainService.createRecord(req);
        MusicRecResp resp = new MusicRecResp();
        resp.setCustomer(req.getCustomer());
        return resp;
    }

    public File convert(MultipartFile multipart) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir"), multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }
}
