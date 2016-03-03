package kz.bgm.platform.service.impl;

import kz.bgm.platform.model.Music;
import kz.bgm.platform.model.MusicRec;
import kz.bgm.platform.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

@Service
public class MainServiceImpl implements MainService {

    protected JdbcTemplate db;

    @Autowired
    public void setScheduleDataSource(@Qualifier("bgmDS") DataSource dataSource) {
        this.db = new JdbcTemplate(dataSource);
    }

    public long createRecord(MusicRec musicRec) {
        String insert = "INSERT INTO musicrec (customer, acrid, title, label, duration, releaseDate, " +
                "album, genres, artists, playOffset, isrcCode, upcCode, recDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            Music music = musicRec.getMusic();
            PreparedStatement ps = connection.prepareStatement(insert, new String[]{"id"});
            ps.setString(1, musicRec.getCustomer());
            ps.setString(2, music.getAcrid());
            ps.setString(3, music.getTitle());
            ps.setString(4, music.getLabel());
            ps.setLong(5, music.getDuration());
            ps.setDate(6, new java.sql.Date(music.getReleaseDate().getTime()));
            ps.setString(7, music.getAlbum());
            ps.setString(8, String.join(", ", music.getGenres()));
            ps.setString(9, String.join(", ", music.getArtists()));
            ps.setLong(10, music.getPlayOffset());
            ps.setString(11, music.getIsrcCode());
            ps.setString(12, music.getUpcCode());
            ps.setTimestamp(13, new Timestamp(musicRec.getRecDate().getTime()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}
