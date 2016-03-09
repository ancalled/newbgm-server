package kz.bgm.platform.controller;

import kz.bgm.platform.model.AcrConfig;
import kz.bgm.platform.model.MusicRec;
import kz.bgm.platform.model.MusicRecResp;
import kz.bgm.platform.service.impl.MainServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import utils.Utils;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/customer")
public class CustomerApi {

    @Autowired
    private MainServiceImpl service;

    @Autowired
    private AcrConfig acrConfig;

    private static final Logger log = Logger.getLogger(MainController.class);

    @RequestMapping(value = "/acr-data", method = RequestMethod.GET)
    public @ResponseBody AcrConfig musicRec() {
        return acrConfig;
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
            service.createRecord(musicReq);
            resp.setCustomer(musicReq.getCustomer());
        } catch (Exception ex) {
            resp.setError(ex.getMessage());
        }
        return resp;
    }
}
