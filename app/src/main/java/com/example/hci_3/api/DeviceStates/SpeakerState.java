package com.example.hci_3.api.DeviceStates;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class SpeakerState implements DeviceState {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("volume")
    @Expose
    private Integer volume;

    @SerializedName("genre")
    @Expose
    private String genre;

    @SerializedName("song")
    @Expose
    private Song song;

    public SpeakerState(String status, Integer volume, String genre, Song song) {
        this.status = status;
        this.volume = volume;
        this.genre = genre;
        this.song = song;
    }

    public SpeakerState(String status, Integer volume, String genre) {
        this.status = status;
        this.volume = volume;
        this.genre = genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    @NonNull
    @Override
    public String toString() {
        return "SpeakerState{" +
                "status='" + status + '\'' +
                ", volume=" + volume +
                ", genre='" + genre + '\'' +
                ", song=" + song +
                '}';
    }

    @Override
    public Map<String, String> compareToNewerVersion(DeviceState state) {
        Map<String, String> ans = new HashMap<>();

        if(! (state instanceof SpeakerState))
            return ans; //TODO: null or empty map

        SpeakerState sState = (SpeakerState) state;

        if( ! getStatus().equals(sState.getStatus()))
            ans.put("status",sState.getStatus());

        if( ! getGenre().equals(sState.getGenre()))
            ans.put("genre",sState.getGenre());

        if( ! getVolume().equals(sState.getVolume()))
            ans.put("volume",sState.getVolume().toString());

        for(Map.Entry<String,String> entry : getSong().compareToNewerVersion(sState.getSong()).entrySet()) {
            ans.put("song." + entry.getKey(), entry.getValue());
        }

        return ans;
    }

    public static class Song {
        @SerializedName("title")
        @Expose
        private String title;

        @SerializedName("artist")
        @Expose
        private String artist;

        @SerializedName("album")
        @Expose
        private String album;

        @SerializedName("duration")
        @Expose
        private String duration;

        @SerializedName("progress")
        @Expose
        private String progress;

        public Song(String title, String artist, String album, String duration, String progress) {
            this.title = title;
            this.artist = artist;
            this.album = album;
            this.duration = duration;
            this.progress = progress;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        @NonNull
        @Override
        public String toString() {
            return "Song{" +
                    "title='" + title + '\'' +
                    ", artist='" + artist + '\'' +
                    ", album='" + album + '\'' +
                    ", duration='" + duration + '\'' +
                    ", progress='" + progress + '\'' +
                    '}';
        }

        public Map<String, String> compareToNewerVersion(SpeakerState.Song song) {

            Map<String, String> ans = new HashMap<>();

            if( ! getAlbum().equals(song.getAlbum()))
                ans.put("album",song.getAlbum());

            if( ! getArtist().equals(song.getArtist()))
                ans.put("artist",song.getArtist());

            if( ! getDuration().equals(song.getDuration()))
                ans.put("duration",song.getDuration());

            if( ! getProgress().equals(song.getProgress()))
                ans.put("progress",song.getProgress());

            if( ! getTitle().equals(song.getTitle()))
                ans.put("title",song.getTitle());

            return ans;
        }
    }
}
