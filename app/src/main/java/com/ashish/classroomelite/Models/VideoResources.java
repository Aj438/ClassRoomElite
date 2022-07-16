package com.ashish.classroomelite.Models;

import java.util.ArrayList;

public class VideoResources {
    private ArrayList<String> videoList;

        public ArrayList<String> getVideoList() {
                return videoList;
        }

        public void setVideoList(ArrayList<String> videoList) {
                this.videoList = videoList;
        }

        public VideoResources() {
        }

        public VideoResources(ArrayList<String> videoList) {
                this.videoList = videoList;
        }
}
