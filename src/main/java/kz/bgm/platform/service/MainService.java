package kz.bgm.platform.service;

import kz.bgm.platform.model.MusicRec;

import java.util.List;

public interface MainService {

    List<MusicRec> listMusicReq();

    long createRecord(MusicRec musicRec);

}
