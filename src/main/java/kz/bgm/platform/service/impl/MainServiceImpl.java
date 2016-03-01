package kz.bgm.platform.service.impl;

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
        String insert = "INSERT INTO MUSICREC " +
                "(customer, label, releaseDate, title, duration, albumName, acrId, genres, artists, recDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insert, new String[]{"id"});
            ps.setString(1, musicRec.getCustomer());
            ps.setString(2, musicRec.getLabel());
            ps.setDate(3, new java.sql.Date(musicRec.getReleaseDate().getTime()));
            ps.setString(4, musicRec.getTitle());
            ps.setLong(5, musicRec.getDuration());
            ps.setString(6, musicRec.getAlbumName());
            ps.setString(7, musicRec.getAcrId());
            ps.setString(8, musicRec.getGenres());
            ps.setString(9, musicRec.getArtists());
            ps.setTimestamp(10, new Timestamp(musicRec.getRecDate().getTime()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}
