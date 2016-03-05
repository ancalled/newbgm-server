package kz.bgm.platform.service;

import kz.bgm.platform.model.MusicRec;
import kz.bgm.platform.model.User;

import java.util.List;

public interface MainService {

    User getUser(String username);

    List<MusicRec> listMusicReq();

    long createRecord(MusicRec musicRec);

}
