package kz.bgm.platform.controller;

import kz.bgm.platform.model.MusicRec;
import utils.Utils;
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

import javax.servlet.http.HttpServletRequest;
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
    public
    @ResponseBody
    MusicRecResp musicRec(HttpServletRequest request) {
        MusicRecResp resp = new MusicRecResp();
        try {
            String bodyString = Utils.toString(request.getInputStream());
            log.info("Got music-rec request:" + bodyString);
            MusicRec musicReq = (MusicRec) Utils.fromJsonWithDate(bodyString, MusicRec.class);
            Utils.checkNotNull(musicReq, "POST data is empty");
            Utils.checkParameterNotNull(musicReq.getCustomer(), "customer");
            Utils.checkParameterNotNull(musicReq.getMusic(), "music");
            mainService.createRecord(musicReq);
            resp.setCustomer(musicReq.getCustomer());
        } catch (Exception ex) {
            resp.setError(ex.getMessage());
        }
        return resp;
    }

    public File convert(MultipartFile multipart) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir"), multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }
}
